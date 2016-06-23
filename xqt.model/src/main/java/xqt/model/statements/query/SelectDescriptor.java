/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xqt.model.statements.query;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.ParserRuleContext;

import com.vaiona.commons.logging.LoggerHelper;

import xqt.model.ClauseDescriptor;
import xqt.model.adapters.DataAdapter;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.VariableContainer;
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
    protected Map<String, ClauseDescriptor> clauses = new LinkedHashMap<>();
    protected List<String> requiredCapabilities = new ArrayList<>();
    protected SelectDescriptor compensationStatement = null;

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
        //visitor.prepare(this);
        if(this.hasError())
            return;
        ExecutionInfo eix = new ExecutionInfo();
        this.setExecutionInfo(eix);
        eix.setExecuted(false);
        
        DataAdapter adapter = visitor.getAdapterSelector().choose(this.getSourceClause().getContainer(), this);// create the adapter based on its registration info and the statement's binding info
        eix.setAdapter(adapter);
        
        if(!adapter.hasRequiredCapabilities(this)){
            buildComplementingStatement(this, visitor);
        }
        // check if the original query's target clause is a persistent data source, and the original adapter supports writing to data containers
        // create another complementing query over the first complementing query to delegate the write to the original adapter
        getExecutionInfo().getAdapter().prepare(this, visitor.getAuxiliaryData()); // creates the source files but does not compile them 
        if(hasError()) // check after lazy construction and validations
            return;
        if(getComplementingStatement() != null){
            SelectDescriptor comp = getComplementingStatement();
            comp.getExecutionInfo().getAdapter().prepare(comp, null);      
            if(comp.getComplementingStatement() != null){
                comp.getComplementingStatement().getExecutionInfo().getAdapter().prepare(comp.getComplementingStatement(), comp);
            }
        }                

        LoggerHelper.logDebug(MessageFormat.format("Statement {0} passed the preparation phase.", this.getId()));                        
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
                // the source is a single container
                else if(this.getSourceClause().getContainer().getDataContainerType() == stmtCasted.getTargetClause().getContainer().getDataContainerType()
                  && this.getSourceClause().getContainer().getId().equalsIgnoreCase(stmtCasted.getTargetClause().getContainer().getId())
                        ){
                    this.setDependsUpon(stmtCasted);
                    // there should not be more than one dependencies!
                    break;
                }
            }
        }
    }
    
    public static SelectDescriptor describeSelect(Object ctx, String id) {
        SelectDescriptor selectDesc = new SelectDescriptor();
        selectDesc.setId(id); // I need a way to identify the statements. they may have exactly same scripts
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

    protected SelectDescriptor buildComplementingStatement(SelectDescriptor select, StatementVisitor visitor) {
        // check which capabilities are missing and check whether they are supported by the completing adapter?
        // check the dependencies between the missing capabilities
        // build a completing query
        // adopt the main query to the changes.
        boolean specialCase = false;
        if(select.getSourceClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Variable
                && select.getTargetClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Single){
            specialCase = true;
        }        
        
        SelectDescriptor comp = new SelectDescriptor();
        comp.setId(select.getId() + "_CMPL1");
        comp.setDependsUpon(select);
        select.setComplementingStatement(comp);
        
        comp.addClause(new SetQualifierFeature());
        // commented on 15.06.15 because I think a canonic perspective made form the select's perspective would do the job better
        //comp.addClause(select.getProjectionClause()); //the comp. query uses the main's projection
        ProjectionFeature   projection       = new ProjectionFeature();
        projection.setPerspective(select.getProjectionClause().getPerspective().createCanonicPerspective());
        projection.setPresent(true);
        comp.addClause(projection);
        
        // create a source of type variable and name it as "Tempvar"+select.id+ time.ticks
        String variableName = "TempVar_" + select.getId() +  "_" + System.currentTimeMillis();
        SourceClause source = new SourceClause();
        source.setContainer(new VariableContainer(variableName));
        
        comp.addClause(source);
        comp.addClause(select.getTargetClause());
        //replace the main's target clause with the temp var, so that the main query puts the result in the tempvar.
        // the temp var should be deleted after the query is executed.
        
        // the target clause will be compensated anyway! this includes the case when only the target clause 
        // is not supported.
        // special care is needed for the target clauses that persist resultsets into an external media! the default adapter may not know how to perform it.
        select.getClauses().remove(select.getTargetClause().getType());
        TargetClause target = new TargetClause();
        target.setContainer(new VariableContainer(variableName));
        select.addClause(target);

        ExecutionInfo executionInfo = new ExecutionInfo();
        comp.setExecutionInfo(executionInfo);
        executionInfo.setExecuted(false);
        DataAdapter adapter = visitor.getAdapterSelector().choose(comp.getSourceClause().getContainer(), comp); 
        comp.getExecutionInfo().setAdapter(adapter);
        
        if(specialCase){
            // also check for joins with both side variable
            // the comp adapter takes the adapter of the main, because it will run the write operation into the container
            
            // the main adapter now is totally working on memory, so needs the fallback adapter
            // its should also be possible to interchange the adapters between the main and the comp!
            DataAdapter adapter2 = visitor.getAdapterSelector().choose(select.getSourceClause().getContainer(), select); 
            select.getExecutionInfo().setAdapter(adapter2);
            
            // when the code reaches here, it means that the main query is now totally running in memory using the fallback adapter.
            // the fallback adapter is supposed to support all the clauses (except writing to adapter specific containers). 
            // by changing the main's adapter to the fallback, it will run without issues
            // the comp query simply accepts the main's output and writes it to the container, using the original adapter that was
            // attached to the main statement.
            return comp;
        }
        
    
        if(select.getAnchorClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.anchor")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.anchor")){                
                comp.addClause(select.getAnchorClause());
                select.getClauses().remove(select.getAnchorClause().getType());
                select.addClause(new AnchorFeature());  // added an empty/neutral clause              
            }
        } else { // add default clauses to the compensation query
            comp.addClause(new AnchorFeature());
        }

        if(select.getFilterClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.filter")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.filter")){
                comp.addClause(select.getFilterClause());
                select.getClauses().remove(select.getFilterClause().getType());
                select.addClause(new SelectionFeature());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new SelectionFeature());
        }
        
        if(select.getOrderClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.orderby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.orderby")){
                comp.addClause(select.getOrderClause());
                select.getClauses().remove(select.getOrderClause().getType());
                select.addClause(new OrderFeature());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new OrderFeature());
        }

        if(select.getGroupClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.groupby")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.groupby")){
                comp.addClause(select.getGroupClause());
                select.getClauses().remove(select.getGroupClause().getType());
                select.addClause(new GroupFeature());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new GroupFeature());
        }
                
        if(select.getLimitClause().isPresent() && !select.getExecutionInfo().getAdapter().isSupported("select.limit")){
            if(comp.getExecutionInfo().getAdapter().isSupported("select.limit")){
                comp.addClause(select.getLimitClause());
                select.getClauses().remove(select.getLimitClause().getType());
                select.addClause(new LimitFeature());  // added an empty/neutral clause              
            }
        } else {
            comp.addClause(new LimitFeature());
        }
        
        return comp;
    }

}
