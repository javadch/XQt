package xqt.model.adapters;

import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.containers.VariableContainer;
import xqt.model.statements.query.SelectDescriptor;

public interface AdapterSelector {
	public DataAdapter choose(DataContainer container, SelectDescriptor select);
	
	public DataAdapter choose(JoinedContainer container);
	
	public DataAdapter choose(SingleContainer container);
	
	public DataAdapter choose(VariableContainer container, SelectDescriptor select);
}
