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
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
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

    public void setComplementingStatement(SelectDescriptor compensationStatement) {
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

    public SetQualifierFeature getSetQualifierClause() { 
        return (SetQualifierFeature)clauses.get(SelectQueryClauseType.Qualifier.toString());
    }

    public ProjectionFeature getProjectionClause() { 
        return (ProjectionFeature)clauses.get(SelectQueryClauseType.Projection.toString());
    }

    public SourceClause getSourceClause() { 
        return (SourceClause)clauses.get(SelectQueryClauseType.Source.toString());
    }
    
    public TargetClause getTargetClause() { 
        return (TargetClause)clauses.get(SelectQueryClauseType.Target.toString());
    }

    public AnchorFeature getAnchorClause() { 
        return (AnchorFeature)clauses.get(SelectQueryClauseType.Anchor.toString());
    }

    public SelectionFeature getFilterClause() { 
        return (SelectionFeature)clauses.get(SelectQueryClauseType.Filter.toString());
    }

    public OrderFeature getOrderClause() { 
        return (OrderFeature)clauses.get(SelectQueryClauseType.Order.toString());
    }

    public LimitFeature getLimitClause() { 
        return (LimitFeature)clauses.get(SelectQueryClauseType.Limit.toString());
    }

    public GroupFeature getGroupClause() { 
        return (GroupFeature)clauses.get(SelectQueryClauseType.Group.toString());
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
    
//    @Override
//    public void pass2(StatementVisitor visitor) {
//        visitor.prepare(this);
//    }

    @Override
    public void checkDependencies(List<StatementDescriptor> stmts) {        
        for(StatementDescriptor stmt: stmts){
            if(stmt instanceof SelectDescriptor){
                SelectDescriptor stmtCasted = (SelectDescriptor)stmt;
                if(this.getSourceClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Joined){
                    JoinedContainer join = ((JoinedContainer)this.getSourceClause().getContainer());
                    if(join.getLeftContainer().getDataContainerType() == stmtCasted.getTargetClause().getContainer().getDataContainerType()
                      && join.getLeftContainer().getId().equalsIgnoreCase(stmtCasted.getTargetClause().getContainer().getId())
                            ){
                        this.setDependsUpon(stmtCasted);
                        //break; // do not break, as the other dependency comes from another statement.
                    }
                    if(join.getRightContainer().getDataContainerType() == stmtCasted.getTargetClause().getContainer().getDataContainerType()
                      && join.getRightContainer().getId().equalsIgnoreCase(stmtCasted.getTargetClause().getContainer().getId())
                            ){
                        this.setDependsUpon2(stmtCasted);
                    }                    
                }
                // the source is a single conainer
                else if(this.getSourceClause().getContainer().getDataContainerType() == stmtCasted.getTargetClause().getContainer().getDataContainerType()
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
            ProjectionFeature projection = getProjectionClause();
            // check whether the join keys are valid and present in the associated perspective
            if(getSourceClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Joined){
                JoinedContainer join = (JoinedContainer) getSourceClause().getContainer();
                // it would be good to separately check the attributes with the left and right perspectives of the join, too!
                faultyAttribute = validateAttributesInExpression(join.getLeftKey(), projection.getPerspective(), true);
                if(faultyAttribute != null){
                    getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                               .setMessageTemplate("The Left Join Key is reffering to attribute '%s' but it is not defined in the associated perspective '%s'.")
                               .setContextInfo1(faultyAttribute.getId())
                               .setContextInfo2(projection.getPerspective().getId())
                               .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                               .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                               .build()
                    );
                }
                faultyAttribute = validateAttributesInExpression(join.getRightKey(), projection.getPerspective(), true);
                if(faultyAttribute != null){
                    getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                               .setMessageTemplate("The Right Join Key is reffering to attribute '%s' but it is not defined in the associated perspective '%s'.")
                               .setContextInfo1(faultyAttribute.getId())
                               .setContextInfo2(projection.getPerspective().getId())
                               .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                               .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                               .build()
                    );
                }
            }
            // -> check whether Ids defined in the filter clause are defined as an attribute in the associated perspective
            if(getFilterClause().isPresent()){
                faultyAttribute = validateAttributesInExpression(getFilterClause().getPredicate(), projection.getPerspective(), true);
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
                faultyAttribute = validateAttributesInExpression(getAnchorClause().getStartAnchor(), projection.getPerspective(), true);
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
                faultyAttribute = validateAttributesInExpression(getAnchorClause().getStopAnchor(), projection.getPerspective(), true);
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
                    faultyAttribute = validateAttributesInExpression(orderItem.getSortKey(), projection.getPerspective(), true);
                    if(faultyAttribute != null){
                        getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                               .setMessageTemplate("The ORDER BY clause is using attribute '%s' but it is not defined in the associated perspective '%s'")
                                .setContextInfo1(faultyAttribute.getId())
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
                    faultyAttribute = validateAttributesInExpression(groupItem.getKey(), projection.getPerspective(), true);
                    if(faultyAttribute != null){
                        getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("The group by clause refers to attribute %s "
                                     + ", which is not defined in the associated perspective ")
                                .setContextInfo1(faultyAttribute.getId())
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
    
    // returns the invalid member expression if found.
    private static MemberExpression validateAttributesInExpression(Expression expression, PerspectiveDescriptor perspective, boolean repair) {
        if(expression == null){
            return null;
        }
        if(expression instanceof BinaryExpression){
            BinaryExpression exp = (BinaryExpression)expression;
            MemberExpression left = validateAttributesInExpression(exp.getLeft(), perspective, repair);
            if(left != null)
                return left;
            MemberExpression right = validateAttributesInExpression(exp.getRight(), perspective, repair);
            if(right != null)
                return right;
            return null; 
            
        } else if(expression instanceof FunctionExpression){
            FunctionExpression exp = (FunctionExpression)expression;
            for (Expression p: exp.getParameters()) {
                MemberExpression pa = validateAttributesInExpression(p, perspective, repair);
                if(pa != null)
                    return pa;
            }
            return null;
            
        } else if(expression instanceof MemberExpression){
            MemberExpression exp = (MemberExpression)expression;
            if(repair){
                // component 0: is the literal "L" or "R". component 1: is the actual attribute name
                if(exp.getMemberType() == MemberExpression.MemberType.Compound 
                    && perspective.getAttributes().containsKey((exp.getComponents().get(0) + "_" + exp.getComponents().get(1)).toLowerCase())){
                    // changing the id of the expression to match the perspective attribute.
                    // the id of the member does not follow x.y.z pattern anymore
                    exp.setId(perspective.getAttributes().get((exp.getComponents().get(0) + "_" + exp.getComponents().get(1)).toLowerCase()).getId());
                    return null;
                }
            } 
            String expTempId = exp.getId().toLowerCase();
            if(perspective.getAttributes().containsKey(expTempId)){
                return null;
            } else if(repair) {
                // craete a canonic auxiliary statement and add it to the perespective
                PerspectiveAttributeDescriptor missing = PerspectiveAttributeDescriptor.createCanonic(expTempId, exp.getReturnType(), true);
                missing.setParserContext(exp.getParserContext());
                perspective.addAttribute(missing);
                return null;
            }
            return exp;
            
            
        } else if(expression instanceof UnaryExpression){
            UnaryExpression exp = (UnaryExpression)expression;
            MemberExpression operand = validateAttributesInExpression(exp.getOperand(), perspective, repair);
            if(operand != null)
                return operand;
            return null; 
            
        } else if(expression.getClass().equals(ValueExpression.class)){
            return null;            
        }
        return null;
    }    

}
