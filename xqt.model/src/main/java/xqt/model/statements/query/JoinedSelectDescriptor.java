/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.statements.query;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import com.vaiona.commons.logging.LoggerHelper;

import xqt.model.ClauseDescriptor;
import xqt.model.adapters.DataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.containers.VariableContainer;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageException;
import xqt.model.execution.ExecutionInfo;
import xqt.model.statements.StatementDescriptor;
import xqt.model.statements.StatementVisitor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class JoinedSelectDescriptor extends SelectDescriptor {
    private SelectDescriptor leftStatement;
    private SelectDescriptor rightStatement;

    // the original SelectDescriptor acts as the composition statement and may change during the rewriting process.
    public JoinedSelectDescriptor (SelectDescriptor original){
    	orderInParent = original.getOrderInParent();
        id = original.getId();
        type = original.getType();
        parserContext = original.getParserContext();
        languageExceptions = original.getLanguageExceptions();
        extra = original.getExtra();

        properties = original.getProperties();
        //elementType;
    	
        executionInfo = original.getExecutionInfo();
        dependsUpon = original.getDependsUpon();
        dependsUpon2 = original.getDependsUpon2();
    	
        clauses = original.getClauses();
        requiredCapabilities = original.getRequiredCapabilities();
        compensationStatement = original.getComplementingStatement();
    	
    }
    
    public SelectDescriptor getLeftSide(){
    	return leftStatement;
    }
    
    public SelectDescriptor getRightSide(){
    	return rightStatement;
    }
    
//    @Override
//    public SourceClause getSourceClause() {
//    	// If the query is re-written, should return the new source, otherwise the source of its original statement
//    	return composition.getSourceClause();
//    }
    
    @Override
    public void accept(StatementVisitor visitor) { //execute the left, right and main
     	// execute the left side, if exists, and put its result set into the memory, under the previously generated variable name
    	if(this.getLeftSide() != null)
    		this.getLeftSide().accept(visitor);   	
    	// execute the left side, if exists, and put its result set into the memory, under the previously generated variable name
    	if(this.getRightSide() != null)
    		this.getRightSide().accept(visitor);;
    	
        Resultset result = visitor.visit(this);
        ExecutionInfo exInfo = this.getExecutionInfo();
        exInfo.setExecuted(true);
        if(result != null && this.getTargetClause() != null && this.getTargetClause().getContainer().getId() /*.getVariableName() */!= null){
            Variable var = new Variable();
            var.setExecutionInfo(exInfo);
            var.setName(this.getTargetClause().getContainer().getId() /*.getVariableName() */);
            var.setResult(result);
            exInfo.setVariable(var);
        }
    }

    @Override
    public void prepare(StatementVisitor visitor) { // prepare left, right and main
        //visitor.prepare(this);
        if(this.hasError())
            return;
        ExecutionInfo eix = new ExecutionInfo();
        this.setExecutionInfo(eix);
        eix.setExecuted(false);
        
        DataAdapter adapter = visitor.getAdapterSelector().choose(this.getSourceClause().getContainer(), this);// create the adapter based on its registration info and the statement's binding info
        if(adapter != null){ // both sides of the join are supported by a single adapter.
            eix.setAdapter(adapter);
            
            if(!adapter.hasRequiredCapabilities(this)){ // the adapter does not support some query features.
                buildComplementingStatement(this, visitor);
            }
            // check if the original query's target clause is a persistent data source, and the original adapter supports writing to data containers
            // create another complementing query over the first complementing query to delegate the write to the original adapter
        } else { // the join query has heterogeneous sides not supported by either of the sides.
        	// decompose the join to a rewritten compositional join plus two stand-alone queries each for one side.
        	decomposeHeteroJoinQuery();
        	
        	// prepare the left and right side rewritten queries.
        	leftStatement.prepare(visitor);
        	rightStatement.prepare(visitor);
        	
        	// use the perspectives of the rewritten side queries to set the perspectives needed by the JOIN.
        	JoinedContainer joinedContainer = (JoinedContainer)this.getSourceClause().getContainer();
        	((VariableContainer)joinedContainer.getLeftContainer()).setPerspective(leftStatement.getProjectionClause().getPerspective());
        	((VariableContainer)joinedContainer.getRightContainer()).setPerspective(rightStatement.getProjectionClause().getPerspective());
        	
        	// choose an adapter for the rewritten query, the composition query
            adapter = visitor.getAdapterSelector().choose(this.getSourceClause().getContainer(), this);
            eix.setAdapter(adapter);                   	
        }
        
        this.getExecutionInfo().getAdapter().prepare(this, visitor.getAuxiliaryData()); // creates the source files but does not compile them 
        if(hasError()) // Validate the statement after lazy items are constructed
            return;
        // if there is any complementing query, it also should be prepared.
        // It is not the join clause, but may be some other features, such as ordering, etc. are not supported.
        // case of a join that writes to a third adapter, should be tested.
        if(getComplementingStatement() != null){ 
            SelectDescriptor comp = getComplementingStatement();
            comp.getExecutionInfo().getAdapter().prepare(comp, null);      
            if(comp.getComplementingStatement() != null){
                comp.getComplementingStatement().getExecutionInfo().getAdapter().prepare(comp.getComplementingStatement(), comp);
            }
        }                

        LoggerHelper.logDebug(MessageFormat.format("Statement {0} passed the preparation phase.", this.getId()));     
    }

	private void decomposeHeteroJoinQuery() {
		JoinedContainer joinedContainer = (JoinedContainer)this.getSourceClause().getContainer();
		
		leftStatement = buildSideQuery(joinedContainer.getLeftContainer(), true);
		rightStatement = buildSideQuery(joinedContainer.getRightContainer(), false);
		
		String leftVariableName = leftStatement.getTargetClause().getContainer().getId();
		String rightvariableName = rightStatement.getTargetClause().getContainer().getId();
		
		VariableContainer leftSource = new VariableContainer(leftVariableName);
		VariableContainer rightSource = new VariableContainer(rightvariableName);
		joinedContainer.setLeftContainer(leftSource);
		joinedContainer.setRightContainer(rightSource);
	}
	
	private SelectDescriptor buildSideQuery(DataContainer container, Boolean isLeftSide){
		SelectDescriptor side = new SelectDescriptor();
        side.setId(this.getId() + (isLeftSide? "_Left": "_Right"));
        //side.setDependsUpon(this);
        //this.setComplementingStatement(side);
        
        // It is needed to create a perspective for the side queries using the master query's 
        //side.addClause(select.getProjectionClause()); //the comp. query uses the main's projection
        ProjectionFeature   projection       = new ProjectionFeature();
        PerspectiveDescriptor perspective = this.getProjectionClause().getPerspective().createUnitPerspective();
        if(container instanceof SingleContainer && ((SingleContainer)container).getPerspective() != null){
        	perspective = ((SingleContainer)container).getPerspective();
        }
        else if(container instanceof VariableContainer && ((VariableContainer)container).getPerspective() != null){
        	perspective = ((VariableContainer)container).getPerspective();
        }
        projection.setPerspective(perspective);
        projection.setPresent(false);
        side.addClause(projection);
        
        SourceClause source = new SourceClause();
        source.setContainer(container);
        side.addClause(source);

        String variableName = "TempVar_" + this.getId() +  "_" + (isLeftSide? "Left_": "Right_") + System.currentTimeMillis();
        TargetClause target = new TargetClause();
        target.setContainer(new VariableContainer(variableName));
        
        side.addClause(target);
        
        side.addClause(new SetQualifierFeature());
        side.addClause(new SelectionFeature());
        side.addClause(new OrderFeature());
        side.addClause(new GroupFeature());
        side.addClause(new LimitFeature());
        side.addClause(new AnchorFeature());

        
        return side;
	}
}
