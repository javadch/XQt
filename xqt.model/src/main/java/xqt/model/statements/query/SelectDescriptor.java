/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.LinkedHashMap;
import java.util.Map;
import xqt.model.ClauseDescriptor;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.exceptions.LanguageException;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.execution.ExecutionInfo;
import xqt.model.statements.StatementDescriptor;
import xqt.model.statements.StatementVisitor;

/**
 *
 * @author Javad Chamanara
 * @project SciQuest
 */
public class SelectDescriptor extends StatementDescriptor{
    // key is the name of the phrase e.g. SOURCE, TARGET, FILTER, etc, 
    // may be its better to create an enum for it: use SelectClauseType enum. The clauses have their types set based on the SelectClauseType enum. t should be enough ...
    //
    Map<String, ClauseDescriptor> clauses = new LinkedHashMap<>();

    public Map<String, ClauseDescriptor> getClauses() {
        return clauses;
    }

    public SetQualifierClause getSetQualifierClause() { 
        return (SetQualifierClause)clauses.get(SelectClauseType.Qualifier.toString());
    }

    public ProjectionClause getProjectionClause() { 
        return (ProjectionClause)clauses.get(SelectClauseType.Projection.toString());
    }

    public SourceClause getSourceClause() { 
        return (SourceClause)clauses.get(SelectClauseType.Source.toString());
    }
    
    public TargetClause getTargetClause() { 
        return (TargetClause)clauses.get(SelectClauseType.Target.toString());
    }

    public AnchorClause getAnchorClause() { 
        return (AnchorClause)clauses.get(SelectClauseType.Anchor.toString());
    }

    public FilterClause getFilterClause() { 
        return (FilterClause)clauses.get(SelectClauseType.Filter.toString());
    }

    public OrderClause getOrderClause() { 
        return (OrderClause)clauses.get(SelectClauseType.Order.toString());
    }

    public LimitClause getLimitClause() { 
        return (LimitClause)clauses.get(SelectClauseType.Limit.toString());
    }

    public GroupClause getGroupClause() { 
        return (GroupClause)clauses.get(SelectClauseType.Group.toString());
    }

    public void addClause(ClauseDescriptor clause) throws LanguageException{
        if(clause == null)
            //throw new LanguageException("Fatal error a null clause is provided to the select statement");
            return;
        if(this.clauses.containsKey(clause.getType()) || this.clauses.containsValue(clause))
            throw LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Duplicate clause of type %s detected.")
                    .setContextInfo1(clause.getType())
                    .setLineNumber(clause.getParserContext().getStart().getLine())
                    .setColumnNumber(clause.getParserContext().getStop().getCharPositionInLine())
                    .build()
                    ;
        this.clauses.put(clause.getType(), clause);
        clause.setOrderInParent(clauses.size()+1);
    }

    @Override
    public ExecutionInfo accept(StatementVisitor visitor) {
        Resultset result = visitor.visit(this);
        ExecutionInfo exInfo = new ExecutionInfo();
        exInfo.setStatement(this);
        exInfo.setExecuted(true);
        if(result != null && this.getTargetClause() != null && this.getTargetClause().getVariableName()!= null){
            Variable var = new Variable();
            var.setExecutionInfo(exInfo);
            var.setName(this.getTargetClause().getVariableName());
            var.setResult(result);
            exInfo.setVariable(var);
        }
        this.executionInfo = exInfo;
        return exInfo;
    }
}
