/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.lang.parsing;

import com.vaiona.commons.types.TypeSystem;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.misc.NotNull;
import xqt.lang.annotation.BindingAnnotator;
import xqt.lang.annotation.ConnectionAnnotator;
import xqt.lang.annotation.PerspectiveAnnotator;
import xqt.lang.grammar.XQtBaseVisitor;
import xqt.lang.grammar.XQtParser;
import xqt.lang.grammar.XQtParser.FunctionContext;
import xqt.model.ElementDescriptor;
import xqt.model.ProcessModel;
import xqt.model.configurations.BindingDescriptor;
import xqt.model.configurations.ConnectionDescriptor;
import xqt.model.configurations.ConnectionParameterDescriptor;
import xqt.model.containers.DataContainer;
import xqt.model.containers.JoinedContainer;
import xqt.model.containers.PlotContainer;
import xqt.model.containers.SingleContainer;
import xqt.model.containers.VariableContainer;
import xqt.model.data.PostponedValidationRecord;
import xqt.model.declarations.DeclarationDescriptor;
import xqt.model.declarations.PerspectiveAttributeDescriptor;
import xqt.model.declarations.PerspectiveDescriptor;
import xqt.model.exceptions.LanguageException;
import xqt.model.exceptions.LanguageExceptionBuilder;
import xqt.model.expressions.BinaryExpression;
import xqt.model.expressions.Expression;
import xqt.model.expressions.ExpressionType;
import xqt.model.expressions.FunctionExpression;
import xqt.model.expressions.InvalidExpression;
import xqt.model.expressions.MemberExpression;
import xqt.model.expressions.UnaryExpression;
import xqt.model.expressions.ValueExpression;
import xqt.model.functions.FunctionInfo;
import xqt.model.functions.FunctionInfoContainer;
import xqt.model.functions.FunctionParameterInfo;
import xqt.model.statements.query.AnchorFeature;
import xqt.model.statements.query.SelectionFeature;
import xqt.model.statements.query.GroupFeature;
import xqt.model.statements.query.GroupEntry;
import xqt.model.statements.query.JoinedSelectDescriptor;
import xqt.model.statements.query.LimitFeature;
import xqt.model.statements.query.NullOrdering;
import xqt.model.statements.query.OrderFeature;
import xqt.model.statements.query.OrderEntry;
import xqt.model.statements.query.ProjectionFeature;
import xqt.model.statements.query.SelectDescriptor;
import xqt.model.statements.query.SetQualifierFeature;
import xqt.model.statements.query.SetQualifierType;
import xqt.model.statements.query.SortOrder;
import xqt.model.statements.query.SourceClause;
import xqt.model.statements.query.TargetClause;

public class GrammarVisitor extends XQtBaseVisitor<Object> {
        
    protected ProcessModel processModel;
    protected Stack<ElementDescriptor> stack = new Stack<>();
    protected HashMap<String, PerspectiveDescriptor> variablesUsedAsTarget = new HashMap<>();
    protected String configPaths = ".";
    
    public GrammarVisitor(String configPaths){
        processModel = new ProcessModel();
    }
   
    public ProcessModel getProcessModel(){
        return(processModel);
    }

    /** 
     * visit each and every rule starting from the root and create a ProcessModel object which holds abstract
     * representation of all the process elements i.e. perspectives, connections, statements, bindings, etc
     * @param ctx
     * @return 
     */
    @Override
    public Object visitCreateProcessModel(XQtParser.CreateProcessModelContext ctx) {
        //VarDefModel vdm = new VarDefModel();
        //processModel.addStatementModel(vdm);
        
        return visitChildren(ctx);
    }

    @Override
    public Object visitPerspective(@NotNull XQtParser.PerspectiveContext ctx) { 
        PerspectiveDescriptor perspective = PerspectiveAnnotator.describePerspective(ctx, processModel);
        // check for duplicate names
        stack.push(perspective); // it would be better if there were no need for data communication :-(
        for(XQtParser.AttributeContext attCtx: ctx.attribute()){
            PerspectiveAttributeDescriptor att = (PerspectiveAttributeDescriptor)visitAttribute(attCtx);
            perspective.addAttribute(att);
            perspective.getLanguageExceptions().addAll(att.getLanguageExceptions());
        }
        perspective.setOrderInParent(processModel.totalElementCount());
        processModel.addDeclaration(perspective); //its better to return to visit process model and add the perspective there
        stack.pop();
        return perspective;
    }
    
    @Override 
    public Object visitAttribute(@NotNull XQtParser.AttributeContext ctx) { 
        PerspectiveDescriptor perspective = (PerspectiveDescriptor)stack.peek();
        PerspectiveAttributeDescriptor attribute = PerspectiveAnnotator.describePerspectiveAttribute(ctx, perspective.getId());
        if(ctx.fwd != null){ //these items will be moved to expression visitor/ descriptor methods
            Expression fwd = (Expression)visit(ctx.fwd);
            fwd.setReturnType(attribute.getDataType());
            attribute.setForwardExpression(fwd);
            //attribute.getForwardExpression().setBody(ctx.fwd.getText()); // to be removed
        }
        if(ctx.rvs != null){
            Expression fwd = (Expression)visit(ctx.rvs);
            attribute.setReverseExpression(fwd);
            //attribute.getReverseExpression().setBody(ctx.rvs.getText()); // to be removed
        }
        return attribute;
    }
    
    @Override 
    public Object visitSmartId(@NotNull XQtParser.SmartIdContext ctx) { 
        return visitChildren(ctx); 
    }
    
    @Override
    public Object visitConnection(@NotNull XQtParser.ConnectionContext ctx) { 
        ConnectionDescriptor connection = ConnectionAnnotator.describeConnection(ctx, processModel);
        stack.push(connection); // it would be better if there were no need for data communication :-(
        for(XQtParser.Parameter_defContext paramCtx: ctx.parameter_def()){
            ConnectionParameterDescriptor param = (ConnectionParameterDescriptor)visitParameter_def(paramCtx);
            connection.addParameter(param);
            connection.getLanguageExceptions().addAll(param.getLanguageExceptions());
        }
        connection.setOrderInParent(processModel.totalElementCount());
        processModel.addConfiguration(connection); //its better to return to visit process model and add the perspective there
        stack.pop();
        return connection;
    }
    
    @Override
    public Object visitParameter_def(@NotNull XQtParser.Parameter_defContext ctx) { 
        ConnectionDescriptor connection = (ConnectionDescriptor)stack.peek();
        ConnectionParameterDescriptor parameter = ConnectionAnnotator.describeConnectionParameter(ctx, connection.getId());

        //stack.push(attribute);
        //visitChildren(ctx);
        //stack.pop();

        return parameter;
    }
    
    @Override
    public Object visitBinding(@NotNull XQtParser.BindingContext ctx) { 
        BindingDescriptor binding = BindingAnnotator.describeBinding(ctx, processModel);
        stack.push(binding); // it would be better if there were no need for data communication :-(
        for(XQtParser.Binding_scope_defContext scopeCtx: ctx.binding_scope_def()){
            String scope = (String)visitBinding_scope_def(scopeCtx);
            binding.addScope(scope, scopeCtx);
        }
        // visit VersionSelector
        binding.setOrderInParent(processModel.totalElementCount());
        processModel.addConfiguration(binding); //its better to return to visit processmodel and add the perspective there
        stack.pop();
        return binding;
    }

    @Override
    public Object visitBinding_scope_def(@NotNull XQtParser.Binding_scope_defContext ctx) { 
        BindingDescriptor binding = (BindingDescriptor)stack.peek();
        String scope = "";
        try{
            scope = BindingAnnotator.describeBindingSource(ctx, binding.getId());
        } catch(LanguageException ex){
            binding.getLanguageExceptions().add(ex);
        }
        //stack.push(attribute);
        //visitChildren(ctx);
        //stack.pop();
        return scope;
    }

    @Override
    public Object visitSelectStatement(@NotNull XQtParser.SelectStatementContext ctx) {
        SelectDescriptor select = SelectDescriptor.describeSelect(ctx, String.valueOf(processModel.getStatements().size()+1));
        stack.push(select); // it would be better if there were no need for data communication :-(
        // process clauses, add new objects instead of null if the corresponding visitor returns null
        SetQualifierFeature setQuantifier    = new SetQualifierFeature();
        if(ctx.setQualifierClause() != null){
            setQuantifier = (SetQualifierFeature)visitSetQualifierClause(ctx.setQualifierClause());
            setQuantifier.setPresent(true);
            select.getRequiredCapabilities().add("select.qualifier");
        }
        ProjectionFeature   projection       = new ProjectionFeature();
        if(ctx.projectionClause()!= null) {
            projection = (ProjectionFeature)visitProjectionClause(ctx.projectionClause());
            if(projection.getPerspective().getPerspectiveType() == PerspectiveDescriptor.PerspectiveType.Inline){
                projection.getPerspective().setId("Inline_Perspective_" + select.getId());                
            }
            projection.setPresent(true);
            select.getRequiredCapabilities().add("select.projection.perspective");
            select.getRequiredCapabilities().add("select.projection.perspective." + projection.getPerspective().getPerspectiveType().toString().toLowerCase());
        }
        SourceClause source = (SourceClause)visitSourceSelectionClause(ctx.sourceSelectionClause());
        select.getRequiredCapabilities().add("select.source." + source.getContainer().getDataContainerType().toString().toLowerCase());

        TargetClause target = new TargetClause();
        if( ctx.targetSelectionClause() != null){
            target = (TargetClause)visitTargetSelectionClause(ctx.targetSelectionClause());
            target.setPresent(true);
            select.getRequiredCapabilities().add("select.target." + target.getContainer().getDataContainerType().toString().toLowerCase());
        }
        
        AnchorFeature anchor = new AnchorFeature();
        if(ctx.anchorClause() != null){
            anchor = (AnchorFeature)visitAnchorClause(ctx.anchorClause());
            anchor.setPresent(true);
            select.getRequiredCapabilities().add("select.anchor");
        }
//        if(source.getContainer().getDataContainerType() == DataContainer.DataContainerType.Joined)
//            parsingContext = "Joined_source";
        SelectionFeature       filter           = new SelectionFeature();
        if(ctx.filterClause() != null){
            filter = (SelectionFeature)visitFilterClause(ctx.filterClause());
            filter.setPresent(true);
            select.getRequiredCapabilities().add("select.filter");
        }
//        parsingContext = "";
        OrderFeature        order            = new OrderFeature();
        if(ctx.orderClause() != null){
            order = (OrderFeature)visitOrderClause(ctx.orderClause());
            order.setPresent(true);
            select.getRequiredCapabilities().add("select.orderby");
        }
        
        LimitFeature        limit            = new LimitFeature();
        if(ctx.limitClause() != null) {
            limit = (LimitFeature)visitLimitClause(ctx.limitClause());
            limit.setPresent(true);
            if (limit.getSkip() > -1){
                select.getRequiredCapabilities().add("select.limit");
                select.getRequiredCapabilities().add("select.limit.skip");
            }
            if (limit.getTake()> -1){
                select.getRequiredCapabilities().add("select.limit");
                select.getRequiredCapabilities().add("select.limit.take");
            }
        }
        
        GroupFeature        group            = new GroupFeature();
        if(ctx.groupClause() != null){
            group = (GroupFeature)visitGroupClause(ctx.groupClause());
            group.setPresent(true);
            select.getRequiredCapabilities().add("select.groupby");
        }

        // -> when all the clauses are described perform the second round validation to interconnect and validate them

        switch(source.getContainer().getDataContainerType()){
            // when the source is a variable it is not possible to have an explicitly declared perspective. 
            // So there should be an error message here if the perspective clause is present in the statement
            // The statement is reading from a variable which has a perspective. So if there is an explicit or inline 
            // perspective defined for the resultset (the target) it should be condiered.
            // otherwise the source perspective is implicitly used for the target.
            case Variable:
                if(projection.getPerspective() == null){ // there is no target perspective defined, so use the source perspective by following where it was used as target
                    // the source variable should have already been defined as target in a previous statement
                    PerspectiveDescriptor pers = variablesUsedAsTarget.get(((VariableContainer)source.getContainer()).getVariableName());
                    if(pers != null){
                        //projection.setPerspective(pers);
                        projection.setPerspective(pers.createCanonicPerspective());
                        // current statement would be depending on another. if that another statement is using an implicit perspective, it would be loaded lazily
                        // so there is a need to update the perspective and set the projection.isPresent to true
                        projection.setPresent(false); 
                        variablesUsedAsTarget.put(((VariableContainer)source.getContainer()).getVariableName(), pers); 
                    } else {
                        source.getLanguageExceptions().add(
                                LanguageExceptionBuilder.builder()
                                    .setMessageTemplate("Could not determine a perspective for variable '%s'. The variable is not defined as a target of any previous statement.")
                                    .setContextInfo1(((VariableContainer)source.getContainer()).getVariableName())
                                    //.setContextInfo2(projection.getPerspective().getId())
                                    .setLineNumber(ctx.getStart().getLine())
                                    .setColumnNumber(source.getParserContext().getStop().getCharPositionInLine())
                                    .build()
                        );                      
                    }         
                    // the current perspective is there either explicit or inline. no need to do anything else here
//                    if(projection.getPerspective().isExplicit()){ // 
//                        source.getLanguageExceptions().add(
//                            LanguageExceptionBuilder.builder()
//                                .setMessageTemplate("It is not allowed to use a perspective when data source is a variable. The statement has declared variable '%s' and perspective '%s'")
//                                .setContextInfo1(((VariableContainer)source.getContainer()).getVariableName())
//                                .setContextInfo2(projection.getPerspective().getId())
//                                .setLineNumber(ctx.getStart().getLine())
//                                .setColumnNumber(source.getParserContext().getStop().getCharPositionInLine())
//                                .build()
//                        );                
//                    } 
                }
                break;
            // Joined source statements should have no perspective, it is constructed by merging the perspectives of the left and right containers.    
            case Joined:
                if(projection.getPerspective() != null && projection.isPresent()){
                    source.getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("It is not allowed to use a perspective when JOIN is present. The statement has declared perspective '%s'")                            
                                .setContextInfo1(projection.getPerspective().getId())
                                .setLineNumber(ctx.getStart().getLine())
                                .setColumnNumber(ctx.getStop().getCharPositionInLine())
                                .build()
                    );                
                } else {
                    // if left and right perspectives are present, try combile them into the clause perspective
                    // and then repair the filter, order, anchor, and groupig clauses.
                    // otherwise it will be done by the adapter.
                    JoinedContainer join = ((JoinedContainer)source.getContainer());
                    if(join.getLeftContainer().getDataContainerType() == DataContainer.DataContainerType.Variable){
                        PerspectiveDescriptor pers = variablesUsedAsTarget.get(((VariableContainer)join.getLeftContainer()).getVariableName());
                        if(pers != null){
                            ((VariableContainer)join.getLeftContainer()).setPerspective(pers);
                            //variablesUsedAsTarget.put(((VariableContainer)join.getLeftContainer()).getVariableName(), pers); 
                        } else {
                            source.getLanguageExceptions().add(
                                    LanguageExceptionBuilder.builder()
                                        .setMessageTemplate("Could not determine a perspective for variable '%s'. The variable is not defined as a target of any previous statement.")
                                        .setContextInfo1(((VariableContainer)join.getLeftContainer()).getVariableName())
                                        //.setContextInfo2(projection.getPerspective().getId())
                                        .setLineNumber(ctx.getStart().getLine())
                                        .setColumnNumber(join.getLeftContainer().getParserContext().getStop().getCharPositionInLine())
                                        .build()
                            );                      
                        }                        
                    }
                    if(join.getRightContainer().getDataContainerType() == DataContainer.DataContainerType.Variable){
                        PerspectiveDescriptor pers = variablesUsedAsTarget.get(((VariableContainer)join.getRightContainer()).getVariableName());
                        if(pers != null){
                            ((VariableContainer)join.getRightContainer()).setPerspective(pers);
                            //variablesUsedAsTarget.put(((VariableContainer)join.getRightContainer()).getVariableName(), pers); 
                        } else {
                            source.getLanguageExceptions().add(
                                    LanguageExceptionBuilder.builder()
                                        .setMessageTemplate("Could not determine a perspective for variable '%s'. The variable is not defined as a target of any previous statement.")
                                        .setContextInfo1(((VariableContainer)join.getRightContainer()).getVariableName())
                                        //.setContextInfo2(projection.getPerspective().getId())
                                        .setLineNumber(ctx.getStart().getLine())
                                        .setColumnNumber(join.getRightContainer().getParserContext().getStop().getCharPositionInLine())
                                        .build()
                            );                      
                        }                                                
                    }
                }
                break;
            default:
                break;
        }
        
        // check for inline perspective and try to build it, like extractPerspective

        // check for implicit perspective if there is still no perspective detected/ determined
        if(projection.getPerspective() == null) {
            // the projection may not be defined by the source clause, so there is an implicit perspective to be extracted
            PerspectiveDescriptor implicitPerspective = extractPerspective(source);
            implicitPerspective.setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
            projection.setPerspective(implicitPerspective);
            projection.setPresent(false);
            select.getRequiredCapabilities().removeIf(p-> p.startsWith("select.projection.perspective.")); // remove the previous perspective type
            select.getRequiredCapabilities().add("select.projection.perspective." + projection.getPerspective().getPerspectiveType().toString().toLowerCase());
        }
        // -> expressions pointing to perpsective attributes should be transformed to their physical counterpart

        switch(target.getContainer().getDataContainerType()){
            case Variable:
                if(variablesUsedAsTarget.keySet().contains(((VariableContainer)target.getContainer()).getVariableName())){ // variables are immutable, using them in more than one target clause is not allowed
                    target.getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("Target variable %s is already in use! "
                                    + "Using one variable as the target of more than one statement is not allowed. ")
                                .setContextInfo1(((VariableContainer)target.getContainer()).getVariableName())
                                .setLineNumber(ctx.getStart().getLine())
                                .setColumnNumber(ctx.getStop().getCharPositionInLine())
                                .build()
                    );
                } else {                
                    variablesUsedAsTarget.put(((VariableContainer)target.getContainer()).getVariableName(), projection.getPerspective());
                }
                break;
            case Single:
                if(projection.getPerspective() != null){
                    //set the perspective of the statement also for the target
                    ((SingleContainer)target.getContainer()).setPerspective(projection.getPerspective());                    
                }
                break;
            case Plot:
                if(variablesUsedAsTarget.keySet().contains(((PlotContainer)target.getContainer()).getPlotName())){ // plot are immutable, using them in more than one target clause is not allowed
                    target.getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("The plot %s is already in use! "
                                    + "Using one plot name as the target of more than one statement is not allowed. ")
                                .setContextInfo1(((PlotContainer)target.getContainer()).getPlotName())
                                .setLineNumber(ctx.getStart().getLine())
                                .setColumnNumber(ctx.getStop().getCharPositionInLine())
                                .build()
                    );
                } else {                
                    variablesUsedAsTarget.put(((PlotContainer)target.getContainer()).getPlotName(), null);
                }
                break;
        }

        
        // the source and the target can not use a same container
        if(target.getContainer().getDataContainerType() == source.getContainer().getDataContainerType() 
                && target.getId().equalsIgnoreCase(source.getId())){
            source.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Using the same name '%s' for the source and the target of a statement is not allowed! ")
                        .setContextInfo1(source.getId())                            
                        .setLineNumber(ctx.getStart().getLine())
                        .setColumnNumber(source.getParserContext().getStop().getCharPositionInLine())
                        .build()
            );                                                
        }

        // if the target is going to be a plot, check whether h, v are pointing to the attributes of the associated perspective. also set the linked perspective
        // -> check whether Ids defined in the filter clause are defined as an attribute in the associated perspective
        // -> check whether Ids defined in the anchor clause are defined as an attribute in the associated perspective
        // -> check whether Ids defined in the grouping clause are defined as an attribute in the associated perspective
        // check postponed validations
        for(PostponedValidationRecord record: selectLateValidations){
            if(record.getContext3().toUpperCase().equals("TYPE-CHECK")){
                if( (projection.isPresent() && projection.getPerspective().getPerspectiveType() != PerspectiveDescriptor.PerspectiveType.Implicit)
                        && projection.getPerspective().getAttributes().values().stream()
                        .filter(p->p.getId().equals(record.getContext1()) && p.getDataType().equals(record.getContext2())).count() <=0){
                    // the requested attribute/type pair are not defined in the perspective;
                    String msg = MessageFormat.format("Attribute ''{0}'' of type ''{1}''", record.getContext1(), record.getContext2());
                    select.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate(msg + " is not found in perspective %s ")
                            .setContextInfo1(projection.getPerspective().getId())
                            //.setContextInfo2(projection.getPerspective().getId())
                            .setLineNumber(record.getParserContext().getStart().getLine())
                            .setColumnNumber(record.getParserContext().getStart().getCharPositionInLine())
                           .build()
                    );
                    
                }
            }
        }
        selectLateValidations.clear();
                
        // clauses are added in the grammar order, and the order is preserved in the clauses linked list.
        // also each clause knows its order in the parent element; Select in this case
        select.addClause(setQuantifier);
        select.getLanguageExceptions().addAll(setQuantifier.getLanguageExceptions());
        select.addClause(projection);
        select.getLanguageExceptions().addAll(projection.getLanguageExceptions());
        select.addClause(source);
        select.getLanguageExceptions().addAll(source.getLanguageExceptions());
        select.addClause(target);
        select.getLanguageExceptions().addAll(target.getLanguageExceptions());
        select.addClause(anchor);
        select.getLanguageExceptions().addAll(anchor.getLanguageExceptions());
        select.addClause(filter);
        select.getLanguageExceptions().addAll(filter.getLanguageExceptions());
        select.addClause(order);
        select.getLanguageExceptions().addAll(order.getLanguageExceptions());
        select.addClause(limit);
        select.getLanguageExceptions().addAll(limit.getLanguageExceptions());
        select.addClause(group);
        select.getLanguageExceptions().addAll(group.getLanguageExceptions());

        select.setOrderInParent(processModel.totalElementCount());
        processModel.addStatementDescriptor(select); //its better to return to visit processmodel and add the perspective there
        select.validate(); // should be back soon. its commented to test where clause attributes not defined in perspectives. 26.05.15
        stack.pop();
        // speciall case that deals with heterogneous joins!
        SelectDescriptor joinSelect = analizeJoin(select);
        if(joinSelect != null)
            return joinSelect;
        return select;
    }

//    @Override
//    public Object visitPlotStatement(@NotNull XQtParser.PlotStatementContext ctx) {
//        return visitChildren(ctx); 
//    }
    
    @Override
    public Object visitSetQualifierClause(@NotNull XQtParser.SetQualifierClauseContext ctx) {
        SetQualifierFeature qualifier = new SetQualifierFeature();
        try {
            qualifier.setQualifier(SetQualifierType.valueOf(ctx.getText().toUpperCase()));
        } catch (Exception ex) {
            qualifier.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Unknown Set Qualifier!")
                        .setLineNumber(ctx.getStart().getLine())
                        .setColumnNumber(ctx.getStop().getCharPositionInLine())
                        .build()
            );
        }
        return qualifier;
    }

    public Object visitProjectionClause(@NotNull XQtParser.ProjectionClauseContext ctx) {
        if(ctx instanceof XQtParser.ProjectionClause_PerspectiveContext){
            return(visitProjectionClause_Perspective((XQtParser.ProjectionClause_PerspectiveContext)ctx));
        } else if(ctx instanceof XQtParser.ProjectionClause_InlineContext){
            return(visitProjectionClause_Inline((XQtParser.ProjectionClause_InlineContext)ctx));
        }
        return null;
    }

    @Override 
    public Object visitProjectionClause_Perspective(@NotNull XQtParser.ProjectionClause_PerspectiveContext ctx) { 
        
        // if this method is not called (visited), means that the statement is porbably using an implicit
        //perpsective defined in the source clause
        // after visiting all the clauses, try to determine the perspective again.
        // the source node visitor should define the perspective and add it to the process model declarations
        ProjectionFeature projection = new ProjectionFeature();
        String perspectiveName = ctx.perspectiveName.getText();

        if(perspectiveName == null || perspectiveName.isEmpty())
            projection.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Perspective name expected!")
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
            );
        //ElementDescriptor parent = (ElementDescriptor)stack.peek();
        DeclarationDescriptor perspective = processModel.getDeclarations().get(perspectiveName);
        if(perspective == null){
            projection.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("The statement expects perspective %s, which is not defined!")
                    .setContextInfo1(perspectiveName)
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
            );
        }
        else {
            if(perspective instanceof PerspectiveDescriptor){                        
                projection.setPerspective((PerspectiveDescriptor)perspective);
                projection.getPerspective().setExplicit();// .setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Explicit);                
            }
            else{
                 projection.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                         .setMessageTemplate("\'%s\' is not a perspective.")
                         .setContextInfo1(perspectiveName)
                         .setLineNumber(ctx.getStart().getLine())
                         .setColumnNumber(ctx.getStart().getCharPositionInLine())
                         .build()
                 );
            }
        }
        //stack.push(attribute);
        //visitChildren(ctx);
        //stack.pop();

        return projection;

    }
    
    private String parsingContext = "";
    @Override 
    public Object visitProjectionClause_Inline(@NotNull XQtParser.ProjectionClause_InlineContext ctx) { 
        // see the issue #1 (https://github.com/javadch/XQt/issues/1) description and comments for the solution outline
        // currently just perspective attributes are supported, not physical fields. The fields need the modeler to access the source/ container
        // to obtain the data types.
        ProjectionFeature projection = new ProjectionFeature();
        PerspectiveDescriptor perspective = new PerspectiveDescriptor();
        perspective.setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Inline);
        projection.setPerspective(perspective);
        parsingContext = "Inline_Perspective";
        int index = 0;
        for(XQtParser.InlineAttributeContext inlineAttribute: ctx.inlineAttribute()){
            if(inlineAttribute.att != null){
                //PerspectiveAnnotator.describePerspectiveAttribute(inlineAttribute, perspective.getId());
                PerspectiveAttributeDescriptor attribute = new PerspectiveAttributeDescriptor();
                attribute.setParserContext(inlineAttribute);
                Expression exp = (Expression)visit(inlineAttribute.att);
                if(inlineAttribute.alias != null){
                    attribute.setId(inlineAttribute.alias.getText().toLowerCase());                    
                } else {
                    if(exp instanceof MemberExpression){
                        MemberExpression expAttr  = (MemberExpression)exp;
                        String name = expAttr.getId().replace(".", "_");
                        if(!perspective.getAttributes().containsKey(name)){
                            attribute.setId(name);
                        } else{
                            attribute.setId(name + index++); // lower case
                        }
                    } else {
                        attribute.setId("attribute" + index++); // lower case
                    }
                }
                
                // go through the expression and se whether there are any references to other perspectives' attributes
                // if so replace the forward and reverse mappings of the sub/ expression with their counterparts
                //exp = replaceReferencedAttributes(exp);
//                if(exp.getExpressionType() == ExpressionType.Member){
//                    if(((MemberExpression)exp).getMemberType() == MemberExpression.MemberType.Simple){
//                        projection.getLanguageExceptions().add(
//                           LanguageExceptionBuilder.builder()
//                                .setMessageTemplate("Identifier \'%s\' does not refer to a perspective attribute. Using physical fields is not yet supported.")
//                                .setContextInfo1(exp.getId())
//                                .setLineNumber(ctx.getStart().getLine())
//                                .setColumnNumber(ctx.getStart().getCharPositionInLine())
//                                .build()
//                        );                        
//                    }
//                }
//                if(exp.getExpressionType() == ExpressionType.Function){ // temprary check, because aggregate functions are not supported yet.
//                    FunctionExpression fExp = (FunctionExpression)exp;
//                    // chack all the parameters to see whether they are ferrefing to a physical field.
//                    
//                    // the following block should be removed when the aggregate functions are supported!
//                    if(fExp.getFunctionSpecification().isAggregate()){ // this is an aggregate function
//                        projection.getLanguageExceptions().add(
//                           LanguageExceptionBuilder.builder()
//                                .setMessageTemplate("Aggregate function \'%s.%s\' is not supported.")
//                                .setContextInfo1(fExp.getPackageId())
//                                .setContextInfo2(fExp.getId())
//                                .setLineNumber(ctx.getStart().getLine())
//                                .setColumnNumber(ctx.getStart().getCharPositionInLine())
//                                .build()
//                        );                        
//                    }
//                    
//                }
                // check for simple id, perspective.attribute id, aggregate function, general expression (not containing aggregate functions), ...
                // AND try setting the data type!
                
                attribute.setForwardExpression(exp);
                attribute.setDataType(exp.getReturnType());
                projection.getLanguageExceptions().addAll(exp.getLanguageExceptions());
                perspective.addAttribute(attribute);
            } else {
                // Add an exception to the perspective/ projection.
            }           
        }
        parsingContext = "";
        return projection;
    }

    @Override
    public Object visitSourceSelectionClause(@NotNull XQtParser.SourceSelectionClauseContext ctx) {
        SourceClause source = new SourceClause();
        if(ctx.sourceRef().singleContainer()!= null){
            source.setContainer((SingleContainer)visitSingleContainer(ctx.sourceRef().singleContainer()));
            source.setParserContext(ctx.sourceRef().singleContainer());
        } else if(ctx.sourceRef().joinedSource() != null){
            source.setContainer((JoinedContainer)visitJoinedSource(ctx.sourceRef().joinedSource()));
            source.setParserContext(ctx.sourceRef().joinedSource());
        } else if(ctx.sourceRef().variable() != null){
            source.setContainer((VariableContainer)visitVariable(ctx.sourceRef().variable()));
            source.setParserContext(ctx.sourceRef().variable());
        }
        //if(source != null) source.init();
        return source;
    }

    @Override
    public Object visitSingleContainer(@NotNull XQtParser.SingleContainerContext ctx) {
        SingleContainer item = new SingleContainer();
        
        String bindingName = ctx.bindingRef().getText();
        BindingDescriptor b = null;
        if(processModel.getConfigurations().containsKey(bindingName)){
            b = (BindingDescriptor)processModel.getConfigurations().get(bindingName);
            item.setBinding(b); // the source should be able to hold joined bindings and source variables
        }
        else {
            item.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("The statement expects data source %s, which is not defined!")
                    .setContextInfo1(bindingName)
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStop().getCharPositionInLine())
                    .build()
            );
        }

        // check out of range container index/ or not existing names
        // in case of named containers find their corresponding index and store the index.
        String container = ctx.containerRef().getText();                        
        try{ // the container is assumed to be a numerical index
            Integer index = Integer.parseInt(container);
            if( b.getScopes().size() > 0){ // it is possible that there is no scope defined, therefore the index is used as is.
                if(b != null && index >= 0 && index < b.getScopes().size()){
                    item.setContainerIndex(index);
                }
                else{
                    item.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("The container index %s is out of range!")
                            .setContextInfo1(container)
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStart().getCharPositionInLine())
                            .build()
                    );
                }            	
            } else {
            	item.setContainerIndex(index);
            }
        }
        catch (NumberFormatException  ex) {  // the container is not a numerical index, instead is a name, so the index should be identified
        	if( b.getScopes().size() > 0){
                if(b!= null && b.getScopes().contains(container)){
                    item.setContainerIndex(b.getScopes().indexOf(container)); // the position of the container in the scoped list
                }
                else{
                    item.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("The container %s is not in the scope of binding %s!")
                            .setContextInfo1(container)
                            .setContextInfo2(b!=null? b.getId(): "NA")
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStart().getCharPositionInLine())
                            .build()
                    );
                }        		
        	} else {
        		item.setContinaerName(container);
        	}
        }
        return item;
    }
     // add visit joined source here when it is supported in the grammar
    
    @Override
    public Object visitVariable(@NotNull XQtParser.VariableContext ctx) { 
        VariableContainer container = new VariableContainer();
        String varName = ctx.ID().getText();
        if(varName == null || varName.isEmpty()){
            container.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Expecting a variable name, but not provided!")
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStart().getCharPositionInLine())
                    .build()
            );
        }
        // check duplicate variable names!!!
        // it is not correct to check for duplicates here, as it is not clear whether the variable is used as source of target!
        container.setVariableName(varName);
        return container;
    }
    
    @Override
    public Object visitJoinedSource(@NotNull XQtParser.JoinedSourceContext ctx){
        JoinedContainer container = new JoinedContainer();
        //process the left container
        if(ctx.leftVariable != null){
            container.setLeftContainer((DataContainer)visitVariable(ctx.leftVariable));
            // in later phases, the variable should be checked against a source variable and its perspective should be determined
        } else if(ctx.leftSource != null) {
            container.setLeftContainer((DataContainer)visitSingleContainer(ctx.leftSource));
            if(ctx.leftPerspectiveName != null){
                // set the lefts perspective intsance using the name
                String perspectiveName = ctx.leftPerspectiveName.ID().getText();
                DeclarationDescriptor perspective = processModel.getDeclarations().get(perspectiveName);
                if(perspective == null || !(perspective instanceof PerspectiveDescriptor)){
                    container.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("The left container expects perspective %s, which is not defined!")
                            .setContextInfo1(perspectiveName)
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStop().getCharPositionInLine())
                            .build()
                    );
                } else {
                    ((SingleContainer)container.getLeftContainer()).setPerspective((PerspectiveDescriptor)perspective);
                }                
            }
        }
        // process the join type
        if(ctx.joinDescription().INNER() != null) {
            container.setJoinType(JoinedContainer.JoinType.InnerJoin);
        } else if(ctx.joinDescription().OUTER()!= null){
            container.setJoinType(JoinedContainer.JoinType.OuterJoin);
            if(ctx.joinDescription().LEFT()!= null){
                container.setJoinType(JoinedContainer.JoinType.LeftOuterJoin);
            } else if(ctx.joinDescription().RIGHT()!= null){
                container.setJoinType(JoinedContainer.JoinType.RightOuterJoin);
            }
        }
        // process the join operation and keys
        if(ctx.joinSpecification().EQ() != null && ctx.joinSpecification().op.getText().equalsIgnoreCase(ctx.joinSpecification().EQ().getText())){
            container.setJoinOperator(JoinedContainer.JoinOperator.EQ);
        } else if(ctx.joinSpecification().NotEQ() != null && ctx.joinSpecification().op.getText().equalsIgnoreCase(ctx.joinSpecification().NotEQ().getText())){
            container.setJoinOperator(JoinedContainer.JoinOperator.NotEQ);
        }  else if(ctx.joinSpecification().GT() != null && ctx.joinSpecification().op.getText().equalsIgnoreCase(ctx.joinSpecification().GT().getText())){
            container.setJoinOperator(JoinedContainer.JoinOperator.GT);
        }  else if(ctx.joinSpecification().GTEQ() != null && ctx.joinSpecification().op.getText().equalsIgnoreCase(ctx.joinSpecification().GTEQ().getText())){
            container.setJoinOperator(JoinedContainer.JoinOperator.GTEQ);
        }  else if(ctx.joinSpecification().LT() != null && ctx.joinSpecification().op.getText().equalsIgnoreCase(ctx.joinSpecification().LT().getText())){
            container.setJoinOperator(JoinedContainer.JoinOperator.LT);
        }  else if(ctx.joinSpecification().LTEQ() != null && ctx.joinSpecification().op.getText().equalsIgnoreCase(ctx.joinSpecification().LTEQ().getText())){
            container.setJoinOperator(JoinedContainer.JoinOperator.LTEQ);
        }

        container.setLeftKey((MemberExpression)visit(ctx.joinSpecification().leftKey));
        container.setRightKey((MemberExpression)visit(ctx.joinSpecification().rightKey));
        
        // process the right container
        if(ctx.rightVariable != null){
            container.setRightContainer((DataContainer)visitVariable(ctx.rightVariable));
            // in later phases, the variable should be checked against a source variable and its perspective should be determined
        } else if(ctx.rightSource != null) {
            container.setRightContainer((DataContainer)visitSingleContainer(ctx.rightSource));
            if(ctx.rightPerspectiveName != null){
                // set the lefts perspective intsance using the name
                String perspectiveName = ctx.rightPerspectiveName.ID().getText();
                DeclarationDescriptor perspective = processModel.getDeclarations().get(perspectiveName);
                if(perspective == null || !(perspective instanceof PerspectiveDescriptor)){
                    container.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("The right container expects perspective %s, which is not defined!")
                            .setContextInfo1(perspectiveName)
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStop().getCharPositionInLine())
                            .build()
                    );
                } else {
                    ((SingleContainer)container.getRightContainer()).setPerspective((PerspectiveDescriptor)perspective);
                }                
            }
        }        
        return container;
    }
    
    @Override
    public Object visitTargetSelectionClause(@NotNull XQtParser.TargetSelectionClauseContext ctx) {
        TargetClause target = new TargetClause();
        if(ctx.singleContainer() != null){
            target.setContainer((SingleContainer)visitSingleContainer(ctx.singleContainer()));
        } else if(ctx.variable() != null){
            target.setContainer((VariableContainer)visitVariable(ctx.variable()));
        } else if (ctx.plot() != null){
            target.setContainer((PlotContainer)visitPlot(ctx.plot()));
        }
        //if(target != null) target.init();
        return target;
    }

    @Override
    public Object visitPlot(@NotNull XQtParser.PlotContext ctx) { 
        PlotContainer plot = new PlotContainer();

        String plotName = ctx.id.getText();
        if(plotName == null || plotName.isEmpty()){
            plot.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Expecting a plot name, but not provided!")
                    .setLineNumber(ctx.id.getStart().getLine())
                    .setColumnNumber(ctx.id.getStart().getCharPositionInLine())
                    .build()
            );
        }
        // check duplicate variable names!!!
        // it is not correct to check for duplicates here, as it is not clear whether the variable is used as source of target!
        plot.setPlotName(plotName);
        
        plot.setHax((MemberExpression)visit(ctx.hx));
        if(plot.getHax().getMemberType() == MemberExpression.MemberType.Compound){ // to replace the R.X with R_X and make it lowecase
            plot.getHax().setId(plot.getHax().getComponents().stream().collect(Collectors.joining("_")).toLowerCase());
        }
        if(ctx.vx1 != null)
            plot.getVaxes().add((MemberExpression)visit(ctx.vx1)); // the first vertical axis, if exists!
        else{
            plot.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Expecting at least one vertical axis!")
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStart().getCharPositionInLine())
                    .build()
            );            
        }
        for(XQtParser.IdentifierContext v: ctx.vxs2) // the second and more vertical axes 
            plot.getVaxes().add((MemberExpression)visit(v));

        for(MemberExpression vax: plot.getVaxes()){
            if(vax.getMemberType() == MemberExpression.MemberType.Compound){ // to replace the R.X with R_X and make it lowecase
                vax.setId(vax.getComponents().stream().collect(Collectors.joining("_")).toLowerCase());
            }            
        }

        plot.setPlotType(ctx.plotType == null? "l" : ctx.plotType.getText());
        plot.sethLabel(ctx.pxl == null? plot.getHax().toString(): ctx.pxl.getText().replaceAll("\"", ""));
        plot.setvLabel(ctx.pvl == null? "" /*plot.getVaxes().get(0)*/: ctx.pvl.getText().replaceAll("\"", ""));
        plot.setPlotLabel(ctx.pll == null? "": ctx.pll.getText().replaceAll("\"", ""));
        
       // variablesUsedAsTarget.put(plotName, null); // to prevent duplicate plot/variable names
        return plot;
    }
    
    @Override
    public Object visitAnchorClause(@NotNull XQtParser.AnchorClauseContext ctx) {
        try {
            //Object ret = visitChildren(ctx);
            AnchorFeature anchor = new AnchorFeature();

            //experimental code
            if(ctx.startAnchor != null && ctx.startAnchor.expression() != null){
                Expression start = ctx.startAnchor.expression() == null? null: (Expression)visit(ctx.startAnchor.expression());
                //start.setBody(start.toString());
                anchor.setStartAnchor(start);
            }
            //experimental code
            if(ctx.stopAnchor != null && ctx.stopAnchor.expression() != null){
                Expression stop = ctx.stopAnchor.expression() == null? null: (Expression)visit(ctx.stopAnchor.expression());
                //stop.setBody(stop.toString());
                anchor.setStopAnchor(stop);
            }
             //stack.push(attribute);
            //visitChildren(ctx);
            //stack.pop();

            return anchor;

            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object visitFilterClause(@NotNull XQtParser.FilterClauseContext ctx) {
        //Object ret = visitChildren(ctx);
        SelectionFeature filter = new SelectionFeature();

        //experimental code
        // Interval inter = new Interval(ctx.searchPhrase().expression().getStart().getCharPositionInLine(), ctx.searchPhrase().expression().getStop().getCharPositionInLine());
        if(ctx.searchPhrase() != null && ctx.searchPhrase().expression() != null){
            Expression predicate = ctx.searchPhrase().expression() == null? null: (Expression)visit(ctx.searchPhrase().phrase);
            // check wheteher all the member expressions, that are supposed to refer to attributes, are really refering to a proper attributes
            filter.setPredicate(predicate);
        }

         //stack.push(attribute);
        //visitChildren(ctx);
        //stack.pop();

        return filter;
    }
    
    // On all expressions, if the operands are value expressions, its possible and recommended to apply the operation on the operands
    // and return the result as a value expression, instead of the whole original expression. This is a form of optimization. see issue #16
    @Override
    public Object visitExpression_power(@NotNull XQtParser.Expression_powerContext ctx) {
        Expression x = (Expression)visit(ctx.x);
        Expression y = (Expression)visit(ctx.y);
        BinaryExpression exp = Expression.Power(x, y);
        exp.setParserContext(ctx);
        //pe.setBody("need a proper class hierarchy for all the expressions");
        return exp;
        //return visitChildren(ctx); 
    }
    
    @Override
    public Object visitExpression_negate(@NotNull XQtParser.Expression_negateContext ctx) { 
        Expression operand = (Expression)visit(ctx.operand);
        UnaryExpression exp = Expression.Negate(operand);
        exp.setParserContext(ctx);
        return exp;
    }
    
    @Override
    public Object visitExpression_mult(@NotNull XQtParser.Expression_multContext ctx) {         
        Expression left = (Expression)visit(ctx.left);
        Expression right = (Expression)visit(ctx.right);
        BinaryExpression exp = null;
        if(ctx.MULT() != null && ctx.op.getText().equals(ctx.MULT().getText())){
            exp = Expression.Multiply(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.DIV() != null && ctx.op.getText().equals(ctx.DIV().getText())){
            exp = Expression.Divide(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.MOD() != null && ctx.op.getText().equals(ctx.MOD().getText())){
            exp = Expression.Modulo(left, right);
            exp.setParserContext(ctx);
        }
        return exp;
    }

    @Override
    public Object visitExpression_add(@NotNull XQtParser.Expression_addContext ctx) { 
        Expression left = (Expression)visit(ctx.left);
        Expression right = (Expression)visit(ctx.right);
        BinaryExpression exp = null;
        if(ctx.PLUS() != null && ctx.op.getText().equals(ctx.PLUS().getText())){
            exp = Expression.Add(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.MINUS() != null && ctx.op.getText().equals(ctx.MINUS().getText())){
            exp = Expression.Subtract(left, right);
            exp.setParserContext(ctx);
        }
        return exp;
    }
    
    @Override
    public Object visitExpression_compare(@NotNull XQtParser.Expression_compareContext ctx) { 
        Expression left = (Expression)visit(ctx.left);
        Expression right = (Expression)visit(ctx.right);
        BinaryExpression exp = null;
        //exp = Expression.GreaterThan(left, right);
        if(ctx.EQ() != null && ctx.op.getText().equals(ctx.EQ().getText())){
            exp = Expression.Equal(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.NotEQ()!= null && ctx.op.getText().equals(ctx.NotEQ().getText())){
            exp = Expression.NotEqual(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.GT() != null && ctx.op.getText().equals(ctx.GT().getText())){
            exp = Expression.GreaterThan(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.GTEQ()!= null && ctx.op.getText().equals(ctx.GTEQ().getText())){
            exp = Expression.GreaterThanOrEqual(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.LT() != null && ctx.op.getText().equals(ctx.LT().getText())){
            exp = Expression.LessThan(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.LTEQ() != null && ctx.op.getText().equals(ctx.LTEQ().getText())){
            exp = Expression.LessThanOrEqual(left, right);
            exp.setParserContext(ctx);
        } else if(ctx.LIKE() != null && ctx.op.getText().equals(ctx.LIKE().getText())){
            exp = Expression.Like(left, right);
            exp.setParserContext(ctx);
        }
        return exp;
    }
    
    List<PostponedValidationRecord> selectLateValidations = new ArrayList<>();
    @Override
    public Object visitExpression_is(@NotNull XQtParser.Expression_isContext ctx) { 
        Expression operand = (Expression)visit(ctx.operand);
        PostponedValidationRecord rec = new PostponedValidationRecord();
        rec.setContext1(operand.getId());
        rec.setContext2("String");
        rec.setContext3("TYPE-CHECK");
        rec.setParserContext(ctx.operand);
        selectLateValidations.add(rec);
        UnaryExpression exp = null;
        if(ctx.isType.getText().toUpperCase().equals("NULL")){
            exp = Expression.IsNull(operand);
            exp.setParserContext(ctx);
        } else if(ctx.isType.getText().toUpperCase().equals("NUMBER")){
            exp = Expression.IsNumber(operand);
            exp.setParserContext(ctx);
        } else if(ctx.isType.getText().toUpperCase().equals("DATE")){
            exp = Expression.IsDate(operand);
            exp.setParserContext(ctx);
        } else if(ctx.isType.getText().toUpperCase().equals("EMPTY")){
            exp = Expression.IsEmpty(operand);
            exp.setParserContext(ctx);
        }
        if(ctx.not != null && ctx.not.getText().toUpperCase().equals("NOT")){
            //exp.setParserContext(null); // the upper exprerssion gets the context
            exp = Expression.Not(exp);
            exp.setParserContext(ctx);
        }        
        return exp;        
    }
   
    @Override
    public Object visitExpression_not(@NotNull XQtParser.Expression_notContext ctx) { 
        Expression operand = (Expression)visit(ctx.operand);
        UnaryExpression exp = Expression.Not(operand);
        exp.setParserContext(ctx);        
        return exp;        
    }

    @Override
    public Object visitExpression_andOr(@NotNull XQtParser.Expression_andOrContext ctx) { 
        Expression left = (Expression)visit(ctx.left);
        Expression right = (Expression)visit(ctx.right);
        BinaryExpression exp = null;
        if(ctx.AND()!= null && ctx.op.getText().equals(ctx.AND().getText())){
            exp = Expression.And(left, right);
            exp.setParserContext(ctx);            
        } else if(ctx.OR() != null && ctx.op.getText().equals(ctx.OR().getText())){
            exp = Expression.Or(left, right);
            exp.setParserContext(ctx);
        }        
        return exp;
    }
    
    @Override
    public Object visitExpression_aAndOr(@NotNull XQtParser.Expression_aAndOrContext ctx) { 
        Expression left = (Expression)visit(ctx.left);
        Expression right = (Expression)visit(ctx.right);
        BinaryExpression exp = null;
        if(ctx.AAND()!= null && ctx.op.getText().equals(ctx.AAND().getText())){
            exp = Expression.ArithmeticAnd(left, right);
            exp.setParserContext(ctx);            
        } else if(ctx.AOR() != null && ctx.op.getText().equals(ctx.AOR().getText())){
            exp = Expression.ArithmeticOr(left, right);
            exp.setParserContext(ctx);            
        }        
        return exp;
    }
    
    @Override
    public Object visitExpression_function(@NotNull XQtParser.Expression_functionContext ctx) { 
        return visit(ctx.operand);        
    }
    
    @Override
    public Object visitFunction_simple(@NotNull XQtParser.Function_simpleContext ctx) { 
        //get the function name, and set the package id as Default
        // iterate over the parameters by visiting the expression again
        String pId="default";
        String id = ctx.simpleIdentifier().ID().getText().toLowerCase();
        return createFunction(id, pId, ctx.argument(), ctx); // change the argument to arg1 and args2 and keep them in order!
    }
    
    @Override
    public Object visitFunction_package(@NotNull XQtParser.Function_packageContext ctx) { 
        //get package id, function name
        // iterate over the parameters by visiting the expression again
        String pId=ctx.packagedIdentifier().packageId.getText().toLowerCase();
        String id = ctx.packagedIdentifier().id.getText().toLowerCase();
        return createFunction(id, pId, ctx.argument(), ctx);
    }
    
    private Expression createFunction(String id, String packageId, List<XQtParser.ArgumentContext> arguments, FunctionContext ctx){
        Optional<FunctionInfo> fInfo;
        InvalidExpression invalidExpr = Expression.Invalid();
        try{
            FunctionInfoContainer functionContainer;
            functionContainer = FunctionInfoContainer.getDefaultInstance(configPaths);
            fInfo = functionContainer.getRegisteredFunctions().stream()
                    .filter(p-> p.getPackageName().equalsIgnoreCase(packageId) && p.getName().equalsIgnoreCase(id)).findFirst();
            if(!fInfo.isPresent()){            
                invalidExpr.getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("Function \'%s\' is not found in package \'%s\'.")
                                .setContextInfo1(id)
                                .setContextInfo2(packageId)
                                .setLineNumber(ctx.getStart().getLine())
                                .setColumnNumber(ctx.getStop().getCharPositionInLine())
                                .build()
                        ); 
                return invalidExpr;
            } 
        } catch(Exception ex){
            invalidExpr.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate(ex.getMessage())
                            .build()
                    ); 
            return invalidExpr; 
        }
        
        List<Expression> parameters = new ArrayList<>();
        int paramIndex=0;
        for(XQtParser.ArgumentContext arg: arguments){
            //ParameterExpression pa = Expression.Parameter((Expression)visit(arg.expression()));
            //pa.setExpressionType(ExpressionType.Parameter);
            Expression pa = (Expression)visit(arg.expression());            
            // check whether the parameter is of proper type as described in the function specification.
            FunctionParameterInfo paramInfo = fInfo.get().getParameters().get(paramIndex);
            
            // the paramter type should be compatible with its specification as defined by the function spec.
            // there is an exception: when the parameter is a member, its a field of the physical data which its type is not defined yet. 
            // so I do trust it for the time being and wait to enouncter a possible type conversion execution later during the statement execution by the adapter.
            // enhancement: I will consider type determination at this stage when everything works as designed.
            
            // If the parameter type is unknown, it is probably pointing to a field in the underlying data source
            // which is typically not known at this moment. for this situation, the best guess is that the parameter should be of the type of the first permitted type
            if(pa instanceof MemberExpression && pa.getReturnType().equalsIgnoreCase(TypeSystem.TypeName.Unknown)) {                
                pa.setReturnType(paramInfo.getPermittedDataTypes().split(Pattern.quote("|"))[0].trim());
            }
            // this should not happen
            if(!paramInfo.getPermittedDataTypes().contains(pa.getReturnType())){                
                pa.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Parameter \'%s\' is of type \'%s\' which does not match its function's specification.")
                            .setContextInfo1(arg.expression().getText())
                            .setContextInfo2(pa.getReturnType())
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStop().getCharPositionInLine())
                            .build()
                    );                
            }
            parameters.add(pa);  
            // The last parameter can be a varargs (like java). If it is the case the paramindex should not proceed
            // and all the arguments are matched with it.
            if(!paramInfo.isVarArg() && paramIndex < fInfo.get().getParameters().size())
                paramIndex++;
        }
        FunctionExpression exp = Expression.Function(packageId, id, fInfo.get().getReturnType(), parameters);
        exp.setFunctionSpecification(fInfo.get());        
        return exp;         
    }
    
    @Override
    public Object visitExpression_nest(@NotNull XQtParser.Expression_nestContext ctx) { 
        return visit(ctx.operand); 
    }
    
    @Override
    public Object visitExpression_value(@NotNull XQtParser.Expression_valueContext ctx) { 
        ValueExpression exp = null;
        if(ctx.operand.UINT()!= null){
            String value = ctx.operand.getText();
            value = value.replaceFirst("^0+(?!$)", ""); // removes leading zeros
            if (value.isEmpty()) value = "0";
            exp = Expression.Value(value, TypeSystem.TypeName.Integer);
            exp.setParserContext(ctx);            
        } else if(ctx.operand.INT() != null){
            String value = ctx.operand.getText();
            value = value.replaceFirst("^0+(?!$)", ""); // removes leading zeros
            if (value.isEmpty()) value = "0";
            exp = Expression.Value(value, TypeSystem.TypeName.Integer);
            exp.setParserContext(ctx);            
        } else if(ctx.operand.FLOAT() != null){
            String value = ctx.operand.getText();
            value = value.replaceFirst("^0+(?!$)", ""); // removes leading zeros
            if (value.isEmpty()) value = "0";
            exp = Expression.Value(value, TypeSystem.TypeName.Real);            
            exp.setParserContext(ctx);
        } else if(ctx.operand.BOOLEAN() != null){
            exp = Expression.Value(ctx.operand.getText(), TypeSystem.TypeName.Boolean);
            exp.setParserContext(ctx);
        } else if(ctx.operand.STRING() != null){
            exp = Expression.Value(ctx.operand.getText(), TypeSystem.TypeName.String);
            exp.setParserContext(ctx);
        } else if(ctx.operand.DateValue()!= null){
            exp = Expression.Value(ctx.operand.getText(), TypeSystem.TypeName.Date);
            exp.setParserContext(ctx);
        }        
        return exp; 
    }
    
    @Override
    public Object visitExpression_identifier(@NotNull XQtParser.Expression_identifierContext ctx) { 
        Expression exp = (Expression)visit(ctx.operand);
        //exp.setParserContext(ctx); // is set during the visit, replacing it may cause in accurate error reporting
        return exp; 
    }
    
    @Override
    public Object visitIdentifier_simple(@NotNull XQtParser.Identifier_simpleContext ctx) { 
        MemberExpression exp = Expression.Member(ctx.simpleIdentifier().ID().getText());
        exp.setParserContext(ctx);        
        return exp; 
    }
    
    @Override
    public Object visitIdentifier_qulaified(@NotNull XQtParser.Identifier_qulaifiedContext ctx) { 
        List<String> idComponents = new ArrayList<>();
        idComponents.add(ctx.qualifiedIdentifier().firstId.getText());
        ctx.qualifiedIdentifier().otherIds.stream().forEach((cmpnt) -> {
            idComponents.add(cmpnt.getText());
        });
        // When in an Inline_Perspective, check wheteher the id is reffering to a previously defined perspective attribute
        // this is because it is only this oint that it is known that the id has a quilified name. Later this id would be part of a bigger
        // expression and validating it need traversing the whole expression tree.
        if(parsingContext.equalsIgnoreCase("Inline_Perspective")){ 
            if(idComponents.size()== 2) { 
                if(idComponents.get(0).equalsIgnoreCase("L") || idComponents.get(0).equalsIgnoreCase("R")){ // Left/ Right sides of JOIN
                    MemberExpression exp = Expression.CompoundMember(idComponents);
                    exp.setParserContext(ctx);
                    return exp;               
                } else { // the perspective name and the attribute name
                    Expression exp = Expression.Invalid();        
                    PerspectiveDescriptor pers = (PerspectiveDescriptor)processModel.getDeclarations().getOrDefault(idComponents.get(0), null);
                    PerspectiveAttributeDescriptor att = pers != null? pers.getAttributes().getOrDefault(idComponents.get(1).toLowerCase(), null): null;
                    if(att != null){
                        //exp = Expression.CompoundMember(idComponents);
                        exp = att.getForwardExpression(); // replace the compound member with its counterpart attribute's forward mapping
                        exp.setReturnType(att.getDataType());
                    } else { // no such a perspective / attribute
                        exp.getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("Either perspective \'%s\' or attribute \'%s\' not found!")
                                .setContextInfo1(idComponents.get(0))
                                .setContextInfo2(idComponents.get(1))    
                                .setLineNumber(ctx.getStart().getLine())
                                .setColumnNumber(ctx.getStop().getCharPositionInLine())
                                .build()
                        );
                    }
                    exp.setParserContext(ctx);        
                    return exp;   
                }
            }          
        }
        // do the normal task
        MemberExpression exp = Expression.CompoundMember(idComponents);
        exp.setParserContext(ctx);
        return exp;
    }
    @Override
    public Object visitExpression_smart(@NotNull XQtParser.Expression_smartContext ctx) { 
        return visitChildren(ctx); 
    }
    
    @Override
    public Object visitOrderClause(@NotNull XQtParser.OrderClauseContext ctx) {
        //Object ret = visitChildren(ctx);
        OrderFeature order = new OrderFeature();

        for(XQtParser.SortSpecificationContext sortItemCtx: ctx.sortSpecification()){
            MemberExpression sortKeyExpr = (MemberExpression)visit(sortItemCtx.sortKey().identifier());
            String sortKey = sortKeyExpr.getId();
            if(sortKey  != null && !sortKey.isEmpty()){
                if(order.getOrderItems().containsKey(sortKey)){ // duplicate key
                    order.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Duplicate ordering key %s found!")
                            .setContextInfo1(sortKey)
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStop().getCharPositionInLine())
                            .build()
                    );
                }
                else {
                    OrderEntry entry = new OrderEntry();
                    entry.setSortKey(sortKeyExpr);
                    if(sortItemCtx.sortOrder() != null){
                        String sortOrderString = XQtParser.tokenNames[sortItemCtx.sortOrder().getStart().getType()];
                        entry.setSortOrder(SortOrder.valueOf(sortOrderString));
                    }
                    if(sortItemCtx.nullOrder() != null){
                        String nullOrder = sortItemCtx.nullOrder().getText();
                        entry.setNullOrdering(NullOrdering.valueOf(nullOrder.replace(" ", "_")));
                    }
                    entry.setParserContext(sortItemCtx);
                    order.getOrderItems().put(entry.getSortKey().getId(), entry);
                }
            }                
        }
        //stack.push(attribute);
        //visitChildren(ctx);
        //stack.pop();

        return order;
    }

    @Override
    public Object visitLimitClause(@NotNull XQtParser.LimitClauseContext ctx) {
        LimitFeature limit = new LimitFeature();

        //experimental code
        if(ctx.skip != null){
            limit.setSkip(Integer.parseInt(ctx.skip.getText()));
        }
        if(ctx.take != null){
            limit.setTake(Integer.parseInt(ctx.take.getText()));
        }

         //stack.push(attribute);
        //visitChildren(ctx);
        //stack.pop();

        return limit;
    }

    @Override
    public Object visitGroupClause(@NotNull XQtParser.GroupClauseContext ctx) {
        GroupFeature group = new GroupFeature();

        for(XQtParser.IdentifierContext groupItemCtx: ctx.identifier()){
            MemberExpression groupKeyExpr = (MemberExpression)visit(groupItemCtx);
            String groupKey = groupKeyExpr.getId();
            if(groupKey  != null && !groupKey.isEmpty()){
                if(group.getGroupIds().containsKey(groupKey)){ // duplicate key
                    group.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Duplicate grouping key %s found! Line %s")
                            .setContextInfo1(groupKey)
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStop().getCharPositionInLine())
                            .build()
                    );
                }
                else{
                    GroupEntry g = new GroupEntry();
                    g.setKey(groupKeyExpr);
                    g.setParserContext(groupItemCtx);
                    group.getGroupIds().put(g.getKey().getId(), g);
                }
            }
        }

        //stack.push(attribute);
        //visitChildren(ctx);
        //stack.pop();

        return group;
    }

    private PerspectiveDescriptor extractPerspective(SourceClause source) {
        // The perspective definition needs to be created based on the source clause
        // this usually mean tooucing the data container(s). So this operation is bound to each adapter!
        // Here an empty perspective is returned so that later the adpater can populate it.
        return new PerspectiveDescriptor();
    }

    private MemberExpression validateAttributesInExpression(Expression expression, PerspectiveDescriptor perspective) {
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

//    private Expression replaceReferencedAttributes(Expression expression) {
//        if(expression instanceof BinaryExpression){
//            BinaryExpression exp = (BinaryExpression)expression;
//            exp.setLeft(replaceReferencedAttributes(exp.getLeft()));
//            exp.setRight(replaceReferencedAttributes(exp.getRight()));
//            return exp;            
//        } else if(expression instanceof FunctionExpression){
//            FunctionExpression exp = (FunctionExpression)expression;
//            List<Expression> tempParams = new ArrayList<>();
//            for(Expression parameter: exp.getParameters()){
//                tempParams.add(replaceReferencedAttributes(parameter));
//            }
//            exp.setParameters(tempParams);
//            return exp;
//        } else if(expression instanceof MemberExpression){
//            MemberExpression exp = (MemberExpression)expression;
//            // if its a compund type and has two components, it should be a reference to another perspective attribute.
//            if(exp.getMemberType() == MemberExpression.MemberType.Compound && exp.getComponents().size() == 2){
//                String perspectiveName = exp.getComponents().get(0);
//                String attributeName = exp.getComponents().get(1);
//                if(this.getProcessModel().getDeclarations().containsKey(perspectiveName)){
//                    PerspectiveDescriptor pers = (PerspectiveDescriptor)this.getProcessModel().getDeclarations().get(perspectiveName);
//                    if(pers.getAttributes().containsKey(attributeName)){
//                        PerspectiveAttributeDescriptor attribute = pers.getAttributes().get(attributeName);
//                        return attribute.getForwardExpression(); // it is replaced here
//                    } else {
//                        // no attribute in the perspective
//                        // add the exception to exp
//                    }
//                } else {
//                    // no such a perspective. add a language exception to exp
//                }
//            }
//            return exp;
//        } else if(expression instanceof UnaryExpression){
//            UnaryExpression exp = (UnaryExpression)expression;
//            exp.setOperand(replaceReferencedAttributes(exp.getOperand()));
//            return exp;
//        } else if(expression instanceof ValueExpression){
//            return expression;
//        } else {
//            return expression;
//        }
//    }

    private SelectDescriptor analizeJoin(SelectDescriptor select) {

        if (select.getSourceClause().getContainer().getDataContainerType() == DataContainer.DataContainerType.Joined){
            JoinedContainer joinedSource = (JoinedContainer)select.getSourceClause().getContainer();
            if(joinedSource.getLeftContainer().getDataContainerType() != joinedSource.getRightContainer().getDataContainerType()){
                // hetero. build and return
            }
            if(joinedSource.getLeftContainer() instanceof VariableContainer){
                if(joinedSource.getRightContainer() instanceof VariableContainer){
                    // homo case
                    return select;
                } else if(joinedSource.getRightContainer() instanceof SingleContainer){
                    return craeteHeteroJoin(select);
                }
            } 
            if(joinedSource.getLeftContainer() instanceof SingleContainer){
                if(joinedSource.getRightContainer() instanceof VariableContainer){
                    return craeteHeteroJoin(select);
                } 
                if(joinedSource.getRightContainer() instanceof SingleContainer){
                    // both sides are single containers.
                    SingleContainer left =  ((SingleContainer)joinedSource.getLeftContainer());
                    SingleContainer right = ((SingleContainer)joinedSource.getRightContainer());
                    if(!left.getBinding().getConnection().getAdapterName().equalsIgnoreCase(right.getBinding().getConnection().getAdapterName())){
                        // different adapters
                        return craeteHeteroJoin(select);
                    }
                    ConnectionParameterDescriptor leftDialect = left.getBinding().getConnection().getParameterValue("dialect", "default");
                    ConnectionParameterDescriptor rightDialect = right.getBinding().getConnection().getParameterValue("dialect", "default");
                    if(!leftDialect.getValue().equalsIgnoreCase(rightDialect.getValue())){
                        // homo-morph case but currently behaved like a hetro
                        return craeteHeteroJoin(select);
                    }
                }
            }

            // break down the select statement to its parts and craete Joined out of it
            return select; 
        }
        return null;
        
    }

    private SelectDescriptor craeteHeteroJoin(SelectDescriptor select) {
        
        return new JoinedSelectDescriptor(null, null, null);
    }
}