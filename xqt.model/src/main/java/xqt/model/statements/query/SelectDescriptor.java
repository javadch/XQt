/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;
import xqt.model.ClauseDescriptor;
import xqt.model.ProcessModel;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.execution.ExecutionInfo;
import xqt.model.expressions.BinaryExpression;
import xqt.model.expressions.Expression;
import xqt.model.expressions.FunctionExpression;
import xqt.model.expressions.MemberExpression;
import xqt.model.expressions.UnaryExpression;
import xqt.model.expressions.ValueExpression;
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
    private Map<String, ClauseDescriptor> clauses = new LinkedHashMap<>();
    private List<String> requiredCapabilities = new ArrayList<>();
    private SelectDescriptor compensationStatement = null;

    public SelectDescriptor getComplementingStatement() {
        return compensationStatement;
    }

    public void setCompensationStatement(SelectDescriptor compensationStatement) {
        this.compensationStatement = compensationStatement;
    }

    public List<String> getRequiredCapabilities() {
        return requiredCapabilities;
    }

    public void setRequiredCapabilities(List<String> requiredCapabilities) {
        this.requiredCapabilities = requiredCapabilities;
    }
    
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

    public void addClause(ClauseDescriptor clause){
        if(clause == null)
            //throw new LanguageException("Fatal error a null clause is provided to the select statement");
            return;
        if(this.clauses.containsKey(clause.getType()) || this.clauses.containsValue(clause))
            this.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Duplicate clause of type %s detected.")
                    .setContextInfo1(clause.getType())
                    .setLineNumber(clause.getParserContext().getStart().getLine())
                    .setColumnNumber(clause.getParserContext().getStop().getCharPositionInLine())
                    .build()
            );
        this.clauses.put(clause.getType(), clause);
        clause.setOrderInParent(clauses.size()+1);
    }

    @Override
    public void accept(StatementVisitor visitor) {
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
    public void prepare(StatementVisitor visitor) {
        visitor.prepare(this);
        
    }
    
    @Override
    public void pass2(StatementVisitor visitor) {
        visitor.prepare(this);
    }

    @Override
    public void checkDependencies(List<StatementDescriptor> stmts) {        
        for(StatementDescriptor stmt: stmts){
            if(stmt instanceof SelectDescriptor){
                SelectDescriptor stmtCasted = (SelectDescriptor)stmt;
                if(this.getSourceClause().getContainer().getDataContainerType() == stmtCasted.getTargetClause().getContainer().getDataContainerType()
                  && this.getSourceClause().getContainer().getId().equalsIgnoreCase(stmtCasted.getTargetClause().getContainer().getId())
                        ){
                    this.setDependsUpon(stmtCasted);
                    // there should not be more than one dependecies!
                    break;
                }
            }
        }
    }
    
    public static SelectDescriptor describeSelect(Object ctx, String id) {
        SelectDescriptor selectDesc = new SelectDescriptor();
        selectDesc.setId(id); // I need a way to identify the sattements. they may have exactly same scripts
        selectDesc.setParserContext((ParserRuleContext)ctx);
        return selectDesc;
    }
    
    public void validate() {
        MemberExpression faultyAttribute;
        if(getProjectionClause().isPresent()){
            ProjectionClause projection = getProjectionClause();
            // -> check whether Ids defined in the filter clause are defined as an attribute in the associated perspective
            if(getFilterClause().isPresent()){
                faultyAttribute = validateAttributesInExpression(getFilterClause().getPredicate(), projection.getPerspective());
                if(faultyAttribute != null){
                    getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                               .setMessageTemplate("The WHERE clause is using attribute '%s' but it is not defined in the associated perspective '%s'.")
                               .setContextInfo1(faultyAttribute.getId())
                               .setContextInfo2(projection.getPerspective().getId())
                               .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                               .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                               .build()
                    );
                }
            }
            // -> check whether Ids defined in the anchor clause are defined as an attribute in the associated perspective
            if(getAnchorClause().isPresent()){
                faultyAttribute = validateAttributesInExpression(getAnchorClause().getStartAnchor(), projection.getPerspective());
                if(faultyAttribute != null){
                    getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                               .setMessageTemplate("The ANCHOR START clause is using attribute '%s' but it is not defined in the associated perspective '%s'")
                               .setContextInfo1(faultyAttribute.getId())
                               .setContextInfo2(projection.getPerspective().getId())
                               .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                               .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                               .build()
                    );
                }
                faultyAttribute = validateAttributesInExpression(getAnchorClause().getStopAnchor(), projection.getPerspective());
                if(faultyAttribute != null){
                    getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                               .setMessageTemplate("The ANCHOR STOP clause is using attribute '%s' but it is not defined in the associated perspective '%s'")
                               .setContextInfo1(faultyAttribute.getId())
                               .setContextInfo2(projection.getPerspective().getId())
                               .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                               .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                               .build()
                    );
                }                
            }
            // -> check whether Ids defined in the ordering clause are defined as an attribute in the associated perspective
            if(getOrderClause().isPresent()){
                for(OrderEntry orderItem : getOrderClause().getOrderItems().values()){
                    if(!projection.getPerspective().getAttributes().containsKey(orderItem.getSortKey())){
                        getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                               .setMessageTemplate("The ORDER BY clause is using attribute '%s' but it is not defined in the associated perspective '%s'")
                                .setContextInfo1(orderItem.getSortKey())
                                .setContextInfo2(projection.getPerspective().getId())
                                .setLineNumber(orderItem.getParserContext().getStart().getLine())
                                .setColumnNumber(orderItem.getParserContext().getStop().getCharPositionInLine())
                               .build()
                        );
                    }
                }
            }
    
            // -> check whether Ids defined in the grouping clause are defined as an attribute in the associated perspective
            if(getGroupClause().isPresent()){
                for(GroupEntry groupItem : getGroupClause().getGroupIds().values()){
                    if(!projection.getPerspective().getAttributes().containsKey(groupItem.getId())){
                        getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("The group by clause refers to attribute %s "
                                     + ", which is not defined in the associated perspective ")
                                .setContextInfo1(groupItem.getId())
                                .setContextInfo2(projection.getPerspective().getId())
                                .setLineNumber(groupItem.getParserContext().getStart().getLine())
                                .setColumnNumber(groupItem.getParserContext().getStart().getCharPositionInLine())
                               .build()
                        );
                    }
                }
            }
        }
    }
    
    private static MemberExpression validateAttributesInExpression(Expression expression, PerspectiveDescriptor perspective) {
        if(expression == null){
            return null;
        }
        if(expression.getClass().equals(BinaryExpression.class)){
            BinaryExpression exp = (BinaryExpression)expression;
            MemberExpression left = validateAttributesInExpression(exp.getLeft(), perspective);
            if(left != null)
                return left;
            MemberExpression right = validateAttributesInExpression(exp.getRight(), perspective);
            if(right != null)
                return right;
            return null; 
            
        } else if(expression.getClass().equals(FunctionExpression.class)){
            FunctionExpression exp = (FunctionExpression)expression;
            for (Expression p: exp.getParameters()) {
                MemberExpression pa = validateAttributesInExpression(p, perspective);
                if(pa != null)
                    return pa;
            }
            return null;
            
        } else if(expression.getClass().equals(MemberExpression.class)){
            MemberExpression exp = (MemberExpression)expression;
            if(perspective.getAttributes().containsKey(exp.getId())){
                return null;
            }
            return exp;
            
            
        } else if(expression.getClass().equals(UnaryExpression.class)){
            UnaryExpression exp = (UnaryExpression)expression;
            MemberExpression operand = validateAttributesInExpression(exp.getOperand(), perspective);
            if(operand != null)
                return operand;
            return null; 
            
        } else if(expression.getClass().equals(ValueExpression.class)){
            return null;
            
        }
        return null;
    }    
}
