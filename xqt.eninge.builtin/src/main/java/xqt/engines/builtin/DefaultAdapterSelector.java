package xqt.engines.builtin;

import xqt.adapters.builtin.DefaultDataAdapter;
import xqt.engine.QueryEngine;
import xqt.model.adapters.AdapterInfo;
import xqt.model.adapters.AdapterInfoContainer;
import xqt.model.adapters.AdapterSelector;
import xqt.model.adapters.DataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.containers.VariableContainer;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.statements.query.SelectDescriptor;

public class DefaultAdapterSelector implements AdapterSelector{
	private QueryEngine engine;
    private AdapterInfoContainer adapterInfoContainer = null;

	public DefaultAdapterSelector(QueryEngine engine){
		this.engine = engine;
        try {
			adapterInfoContainer = AdapterInfoContainer.getInstance(engine.getConfigPaths());
		} catch (Exception ex) {
			engine.getProcessModel().getLanguageExceptions().add(
	                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Could not load the adapter configuration file. " + ex.getMessage())
                    .setLineNumber(-1)
                    .setColumnNumber(-1)
                    .build()
                );
		}
	}
	
	public DataAdapter choose(DataContainer container, SelectDescriptor select){
        switch (container.getDataContainerType()) {
	        case Variable: {	        	
            	return choose((VariableContainer)container, select);
	        }
            case Single:{
            	return choose((SingleContainer)container);
            }
            case Joined:{
            	return choose((JoinedContainer)container);
            }
            default:
            	return null;
        }
		
	}
	
	public DataAdapter choose(JoinedContainer container){
        DataAdapter adapter = null;
        String adapterType = "Default";
		try {
            // both sides are variable, so the default variable is returned
            if(container.getLeftContainer().getDataContainerType() == DataContainer.DataContainerType.Variable){
                adapter = new DefaultDataAdapter();  
                adapter.setup(null);
                adapter.setAdapterInfo(adapterInfoContainer.getDefultAdapter());
                return adapter;
            }
            // check whether left and right adapters are identical
            String leftAdapterCode = ((SingleContainer)container.getLeftContainer()).getBinding().getConnection().getAdapterName();
            String rightAdapterCode = ((SingleContainer)container.getRightContainer()).getBinding().getConnection().getAdapterName();
            if(!leftAdapterCode.equalsIgnoreCase(rightAdapterCode)) { 
            	// left and right sides of the join use different adapters
            	return null;
            }else { // adapters are identical, check the dialects
                String leftAdapterDialect = getConnectionDialect((SingleContainer)container.getLeftContainer());                    
                String rightAdapterDialect = getConnectionDialect((SingleContainer)container.getRightContainer());
                if(!leftAdapterDialect.equalsIgnoreCase(rightAdapterDialect)) {
                	// its a single adapter, but different dialects, so its not possible to perform the join as one query
                	return null;
                } else{ 
                	// The query can be done using one adapter/dialect
                    adapterType = leftAdapterCode;
                    AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(leftAdapterCode);
                    String adapterDialect = getConnectionDialect((SingleContainer)container.getLeftContainer());                               
                    adapter = adapterInfo.load(adapterDialect, engine.getClassLoader(), engine.getConfigPaths());
                    adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
                    adapter.setAdapterInfo(adapterInfo);
                    if(adapter.isSupported("select.source.joined")){
                    	return adapter;
                    } 
                }
            }            
        }
        catch (Exception ex) {
            container.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Could not load an adapter for '" + adapterType + "'. " + ex.getMessage())
                    //.setContextInfo1(select.getId())
                    .setLineNumber(container.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
            );                        
        }
		return null;// Its a heterogeneous join that should be decomposed first.
	}
	
	public DataAdapter choose(SingleContainer container){
        DataAdapter adapter = null;
        String adapterType = "";
        try {
            adapterType = container.getBinding().getConnection().getAdapterName();
            AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(adapterType); // Handle not found exception
            String adapterDialect = getConnectionDialect(container);
            adapter = adapterInfo.load(adapterDialect, engine.getClassLoader(), engine.getConfigPaths());
            adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
            adapter.setAdapterInfo(adapterInfo);
            return adapter;
        }
        catch (Exception ex) {
            container.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Could not load an adapter for '" + adapterType + "'. " + ex.getMessage())
                    //.setContextInfo1(select.getId())
                    .setLineNumber(container.getParserContext().getStart().getLine())
                    .setColumnNumber(-1)
                    .build()
            );                        
        }
        return adapter;
	}

	public DataAdapter choose(VariableContainer container, SelectDescriptor select){
        DataAdapter adapter = null;
		DataContainer target = select.getTargetClause().getContainer(); 
        switch (target.getDataContainerType()) {
	        case Single: // in this case a memory variable should be written to a persistent container, hence the associated adapter should take the statement
	        {
	           String adapterType = ((SingleContainer)select.getTargetClause().getContainer()).getBinding().getConnection().getAdapterName();
	           String adapterDialect = getConnectionDialect((SingleContainer)select.getTargetClause().getContainer());
	           try {
	               AdapterInfo adapterInfo = adapterInfoContainer.getAdapterInfo(adapterType); // handle not found exception
	               adapter = adapterInfo.load(adapterDialect, engine.getClassLoader(), engine.getConfigPaths());
	               adapter.setup(null); // pass the configuration information. they are in the connection object associated to the select
	               adapter.setAdapterInfo(adapterInfo);
	               return adapter;
	           }
	           catch (Exception ex) {
	               select.getLanguageExceptions().add(
	                   LanguageExceptionBuilder.builder()
	                       .setMessageTemplate("Could not load the adapter for '" + adapterType + "'. " + ex.getMessage())
	                       .setLineNumber(select.getSourceClause().getParserContext().getStart().getLine())
	                       .setColumnNumber(-1)
	                       .build()
	               );                        
	           }
	        }
	        default:
	           adapter = new DefaultDataAdapter();  
	           adapter.setup(null);
	           adapter.setAdapterInfo(adapterInfoContainer.getDefultAdapter());
	           return adapter; // when caching is enabled, remove this line
	    }
	}
	
    public String getConnectionDialect(SingleContainer container){
        return container.getBinding().getConnection().getParameterValue("dialect", "default").getValue();
    }

}
