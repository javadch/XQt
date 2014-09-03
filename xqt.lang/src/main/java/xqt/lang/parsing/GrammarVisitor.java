/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.lang.parsing;

/***
 * Excerpted from "The Definitive ANTLR 4 Reference",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpantlr2 for more book information.
***/

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.misc.NotNull;
import xqt.lang.annotation.BindingAnnotator;
import xqt.lang.annotation.ConnectionAnnotator;
import xqt.lang.annotation.PerspectiveAnnotator;
import xqt.lang.annotation.SelectAnnotator;
import xqt.lang.grammar.XQtBaseVisitor;
import xqt.lang.grammar.XQtParser;
import xqt.model.DataContainerDescriptor;
import xqt.model.ElementDescriptor;
import xqt.model.ProcessModel;
import xqt.model.configurations.BindingDescriptor;
import xqt.model.configurations.ConnectionDescriptor;
import xqt.model.configurations.ConnectionParameterDescriptor;
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
import xqt.model.expressions.MemberExpression;
import xqt.model.expressions.UnaryExpression;
import xqt.model.expressions.ValueExpression;
import xqt.model.statements.query.AnchorClause;
import xqt.model.statements.query.FilterClause;
import xqt.model.statements.query.GroupClause;
import xqt.model.statements.query.GroupEntry;
import xqt.model.statements.query.LimitClause;
import xqt.model.statements.query.NullOrdering;
import xqt.model.statements.query.OrderClause;
import xqt.model.statements.query.OrderEntry;
import xqt.model.statements.query.PlotClause;
import xqt.model.statements.query.ProjectionClause;
import xqt.model.statements.query.SelectDescriptor;
import xqt.model.statements.query.SetQualifierClause;
import xqt.model.statements.query.SetQualifierType;
import xqt.model.statements.query.SortOrder;
import xqt.model.statements.query.SourceClause;
import xqt.model.statements.query.TargetClause;

public class GrammarVisitor extends XQtBaseVisitor<Object> {
        
    protected ProcessModel processModel;
    protected Stack<ElementDescriptor> stack = new Stack<>();
    protected HashMap<String, PerspectiveDescriptor> variablesUsedAsTarget = new HashMap<>();

    public GrammarVisitor(){
        processModel = new ProcessModel();
    }
   
    public ProcessModel getProcessModel(){
        return(processModel);
    }

    /** 
     * visit each and every rule starting from the root and create a ProcessModel object which holds abstract
     * representation of all the process elements i.e. perspectives, connections, statements, bindings, etc
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
        stack.push(perspective); // it would be better if there were no need for data communication :-(
        for(XQtParser.Attribute_defContext attCtx: ctx.attribute_def()){
            PerspectiveAttributeDescriptor att = (PerspectiveAttributeDescriptor)visitAttribute_def(attCtx);
            perspective.addAttribute(att);
            perspective.getLanguageExceptions().addAll(att.getLanguageExceptions());
        }
        perspective.setOrderInParent(processModel.totalElementCount());
        processModel.addDeclaration(perspective); //its better to return to visit processmodel and add the perspective there
        stack.pop();
        return perspective;
    }
    
    @Override 
    public Object visitAttribute_def(@NotNull XQtParser.Attribute_defContext ctx) { 
        PerspectiveDescriptor perspective = (PerspectiveDescriptor)stack.peek();
        PerspectiveAttributeDescriptor attribute = PerspectiveAnnotator.describePerspectiveAttribute(ctx, perspective.getId());
        if(ctx.fwd != null){ //these items will be moved to expression visitor/ descriptor methds
            Expression fwd = (Expression)visit(ctx.fwd);
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
        processModel.addConfiguration(connection); //its better to return to visit processmodel and add the perspective there
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
        SelectDescriptor select = SelectAnnotator.describeSelect(ctx, processModel);
        stack.push(select); // it would be better if there were no need for data communication :-(
        // process clauses, add new objects instead of null if the corresponding visitor returns null
        SetQualifierClause setQuantifier    = new SetQualifierClause();
        if(ctx.setQualifierClause() != null){
            setQuantifier = (SetQualifierClause)visitSetQualifierClause(ctx.setQualifierClause());
            setQuantifier.setIsPresent(true);
            select.getRequiredCapabilities().add("select.qualifier");
        }
        ProjectionClause   projection       = new ProjectionClause();
        if(ctx.projectionClause()!= null) {
            projection = (ProjectionClause)visitProjectionClause(ctx.projectionClause());
            projection.setIsPresent(true);
            select.getRequiredCapabilities().add("select.projection.perspective");
            select.getRequiredCapabilities().add("select.projection.perspective." + projection.getPerspective().getPerspectiveType().toString().toLowerCase());
        }
        SourceClause       source           = (SourceClause)visitSourceSelectionClause(ctx.sourceSelectionClause());
        select.getRequiredCapabilities().add("select.source." + source.getDataContainerType().toString().toLowerCase());

        TargetClause       target           = new TargetClause();
        if( ctx.targetSelectionClause() != null){
            target = (TargetClause)visitTargetSelectionClause(ctx.targetSelectionClause());
            target.setIsPresent(true);
            select.getRequiredCapabilities().add("select.target." + target.getDataContainerType().toString().toLowerCase());
        }
        
        AnchorClause       anchor           = new AnchorClause();
        if(ctx.anchorClause() != null){
            anchor = (AnchorClause)visitAnchorClause(ctx.anchorClause());
            anchor.setIsPresent(true);
            select.getRequiredCapabilities().add("select.anchor");
        }
        
        FilterClause       filter           = new FilterClause();
        if(ctx.filterClause() != null){
            filter = (FilterClause)visitFilterClause(ctx.filterClause());
            filter.setIsPresent(true);
            select.getRequiredCapabilities().add("select.filter");
        }
        
        OrderClause        order            = new OrderClause();
        if(ctx.orderClause() != null){
            order = (OrderClause)visitOrderClause(ctx.orderClause());
            order.setIsPresent(true);
            select.getRequiredCapabilities().add("select.orderby");
        }
        
        LimitClause        limit            = new LimitClause();
        if(ctx.limitClause() != null) {
            limit = (LimitClause)visitLimitClause(ctx.limitClause());
            limit.setIsPresent(true);
            if (limit.getSkip() > -1){
                select.getRequiredCapabilities().add("select.limit");
                select.getRequiredCapabilities().add("select.limit.skip");
            }
            if (limit.getTake()> -1){
                select.getRequiredCapabilities().add("select.limit");
                select.getRequiredCapabilities().add("select.limit.take");
            }
        }
        
        GroupClause        group            = new GroupClause();
        if(ctx.groupClause() != null){
            group = (GroupClause)visitGroupClause(ctx.groupClause());
            group.setIsPresent(true);
            select.getRequiredCapabilities().add("select.groupby");
        }

        // -> when all the clauses are described perform the second round validation to interconnect and validate them
        if(projection.getPerspective() == null) {
            // the projection may not be defined by the source clause, so there is an implicit perspective to be extracted
            PerspectiveDescriptor implicitPerspective = extractPerspective(source);
            implicitPerspective.setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Implicit);
            projection.setPerspective(implicitPerspective);
            select.getRequiredCapabilities().removeIf(p-> p.startsWith("select.projection.perspective.")); // remove the previous perspective type
            select.getRequiredCapabilities().add("select.projection.perspective." + projection.getPerspective().getPerspectiveType().toString().toLowerCase());
        }
        // check for inline perspective and try to build it, like extractPerspective
        // -> expressions pointing to perpsective attributes should be transformed to their physical counterpart

        if(target.getDataContainerType() == DataContainerDescriptor.DataContainerType.Variable){
            if(variablesUsedAsTarget.keySet().contains(target.getVariableName())){ // variables are immutable, using them in more than one target clause is not allowed
                target.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Target variable %s is already in use! "
                                + "Using one variable as the target of more than one statement is not allowed. ")
                            .setContextInfo1(target.getVariableName())
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStop().getCharPositionInLine())
                            .build()
                );
            } else {                
                variablesUsedAsTarget.put(target.getVariableName(), projection.getPerspective());
            }
        }

            // when the source is a variable it is not possible to change the perspective. 
            // So there should be an error message here if the perspective clause is present in the statement
        if(source.getDataContainerType() == DataContainerDescriptor.DataContainerType.Variable){
            if(projection.getPerspective() != null && projection.getPerspective().isExplicit()){
                source.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("It is not allowed to use a perspective when data source is a variable. The statement has declared "
                                    + " variable '%s' and perspective '%s'")
                            .setContextInfo1(source.getVariableName())
                            .setContextInfo2(projection.getPerspective().getId())
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(source.getParserContext().getStop().getCharPositionInLine())
                            .build()
                );                
            } else { // the source variable should have already been defined as target in a previous statement
                PerspectiveDescriptor pers = variablesUsedAsTarget.get(source.getVariableName());
                if(pers != null){
                    projection.setPerspective(pers);
                    variablesUsedAsTarget.put(source.getVariableName(), pers); // replace the previouslly set perspective
                } else {
                    source.getLanguageExceptions().add(
                            LanguageExceptionBuilder.builder()
                                .setMessageTemplate("Could not determine a perspective for variable '%s'. The variable is not defined as a target of any previous statement.")
                                .setContextInfo1(source.getVariableName())
                                //.setContextInfo2(projection.getPerspective().getId())
                                .setLineNumber(ctx.getStart().getLine())
                                .setColumnNumber(source.getParserContext().getStop().getCharPositionInLine())
                                .build()
                    );                      
                }
            }
        }
        
        // the source and the target can not use a same container
        if(target.getDataContainerType() == source.getDataContainerType() 
                && target.getId().toUpperCase().equals(source.getId().toUpperCase())){
            source.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Using the same name '%s' for the source and target of a statement is not allowed! ")
                        .setContextInfo1(source.getId())                            
                        .setLineNumber(ctx.getStart().getLine())
                        .setColumnNumber(source.getParserContext().getStop().getCharPositionInLine())
                        .build()
            );                                                
        }

        // if the target is going to be a plot, check whther h, v are pointing to the attributes of the associated perspective. also set the linked perspective
        //if()
            // -> the target variable should point to its statement.
//            if(target.getVariable() != null) // select may have no target
//                target.getVariable().setStatement(selectDesc);

        // -> check whether Ids defined in the filter clause are defined as an attribute in the associated perspective
        MemberExpression faultyAttribute = validateAttributesInExpression(((FilterClause)filter).getPredicate(), projection.getPerspective());
        if(faultyAttribute != null){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                       .setMessageTemplate("The WHERE clause is using attribute '%s' that is not defined in perspective '%s'.")
                       .setContextInfo1(faultyAttribute.getId())
                       .setContextInfo2(projection.getPerspective().getId())
                       .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                       .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                       .build()
            );
        }
        // -> check whether Ids defined in the anchor clause are defined as an attribute in the associated perspective
        faultyAttribute = validateAttributesInExpression(((AnchorClause)anchor).getStartAnchor(), projection.getPerspective());
        if(faultyAttribute != null){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                       .setMessageTemplate("The ANCHOR START clause is using attribute '%s' that is not defined in perspective '%s'")
                       .setContextInfo1(faultyAttribute.getId())
                       .setContextInfo2(projection.getPerspective().getId())
                       .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                       .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                       .build()
            );
        }

        faultyAttribute = validateAttributesInExpression(((AnchorClause)anchor).getStopAnchor(), projection.getPerspective());
        if(faultyAttribute != null){
            select.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                       .setMessageTemplate("The ANCHOR STOP clause is using attribute '%s' that is not defined in perspective '%s'")
                       .setContextInfo1(faultyAttribute.getId())
                       .setContextInfo2(projection.getPerspective().getId())
                       .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                       .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                       .build()
            );
        }

        // -> check whether Ids defined in the ordering clause are defined as an attribute in the associated perspective
        for(OrderEntry orderItem : order.getOrderItems().values()){
            if(!projection.getPerspective().getAttributes().containsKey(orderItem.getSortKey())){
                select.getLanguageExceptions().add(
                    LanguageExceptionBuilder.builder()
                       .setMessageTemplate("The ORDER BY clause is using attribute '%s' that is not defined in associated perspective '%s'")
                        .setContextInfo1(orderItem.getSortKey())
                        .setContextInfo2(projection.getPerspective().getId())
                        .setLineNumber(orderItem.getParserContext().getStart().getLine())
                        .setColumnNumber(orderItem.getParserContext().getStop().getCharPositionInLine())
                       .build()
                );
            }
        }
        // -> check whether Ids defined in the grouping clause are defined as an attribute in the associated perspective
        for(GroupEntry groupItem : group.getGroupIds().values()){
            if(!projection.getPerspective().getAttributes().containsKey(groupItem.getId())){
                select.getLanguageExceptions().add(
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
        // check postponed validations
        for(PostponedValidationRecord record: selectLateValidations){
            if(record.getContext3().toUpperCase().equals("TYPE-CHECK")){
                if(projection.getPerspective().getAttributes().values().stream()
                        .filter(p->p.getId().equals(record.getContext1()) && p.getDataType().equals(record.getContext2())).count() <=0){
                    // the requested attrinute/type pair are not defined in the perspective;
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
        stack.pop();
        
        return select;
    }

//    @Override
//    public Object visitPlotStatement(@NotNull XQtParser.PlotStatementContext ctx) {
//        return visitChildren(ctx); 
//    }
    
    @Override
    public Object visitSetQualifierClause(@NotNull XQtParser.SetQualifierClauseContext ctx) {
        SetQualifierClause qualifier = new SetQualifierClause();
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

    @Override 
    public Object visitProjectionClause_Perspective(@NotNull XQtParser.ProjectionClause_PerspectiveContext ctx) { 
        
        // if this method is not called (visited), means that the statement is porbably using an implicit
        //perpsective defined in the source clause
        // after visiting all the clauses, try to determine the perspective again.
        // the source node visitor should define the perspective and add it to the process model declarations
        ProjectionClause projection = new ProjectionClause();
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
                         .setMessageTemplate("The statement is pointing to declaration %s that is not a perspective")
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
    
    @Override 
    public Object visitProjectionClause_Inline(@NotNull XQtParser.ProjectionClause_InlineContext ctx) { 
        // see the issue #1 (https://github.com/javadch/XQt/issues/1) description and comments for the solution outline
        // currently just perspective attributes are supported, not physical fields. The fields need the modeler to access the source/ container
        // to obtain the data types.
        ProjectionClause projection = new ProjectionClause();
        projection.getPerspective().setPerspectiveType(PerspectiveDescriptor.PerspectiveType.Inline);
        return projection;
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
    public Object visitSourceSelectionClause(@NotNull XQtParser.SourceSelectionClauseContext ctx) {
        SourceClause source = null;
        if(ctx.sourceRef().simpleSource() != null){
            source = SourceClause.convert((DataContainerDescriptor)visitSimpleSource(ctx.sourceRef().simpleSource()));
            source.setParserContext(ctx.sourceRef().simpleSource());
        } else if(ctx.sourceRef().joinedSource() != null){
            source = SourceClause.convert((DataContainerDescriptor)visit(ctx.sourceRef().joinedSource()));
            source.setParserContext(ctx.sourceRef().joinedSource());
        } else if(ctx.sourceRef().variable() != null){
            source = SourceClause.convert((DataContainerDescriptor)visitVariable(ctx.sourceRef().variable()));
            source.setParserContext(ctx.sourceRef().variable());
        }
        //if(source != null) source.init();
        return source;
    }

    @Override
    public Object visitSimpleSource(@NotNull XQtParser.SimpleSourceContext ctx) {
        DataContainerDescriptor item = new DataContainerDescriptor();
        item.setDataContainerType(DataContainerDescriptor.DataContainerType.SimpleContainer);
        
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
        }
        catch (NumberFormatException  ex) {  // the container is not a numerical index, instead is a name, so the index should be identified
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
        }
        return item;
    }
     // add visit joined source here when it is supprted in the grammar
    
    @Override
    public Object visitVariable(@NotNull XQtParser.VariableContext ctx) { 
        DataContainerDescriptor item = new DataContainerDescriptor();
        item.setDataContainerType(DataContainerDescriptor.DataContainerType.Variable);
        String varName = ctx.ID().getText();
        if(varName == null || varName.isEmpty()){
            item.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Expecting a variable name, but not provided!")
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStart().getCharPositionInLine())
                    .build()
            );
        }
        // check duplicate variable names!!!
        // it is not correct to check for duplicates here, as it is not clear whether the variable is used as source of target!
        item.setVariableName(varName);
        return item;
    }
    
    @Override
    public Object visitTargetSelectionClause(@NotNull XQtParser.TargetSelectionClauseContext ctx) {
        TargetClause target = null;
        if(ctx.simpleSource() != null){
            target = TargetClause.convert((DataContainerDescriptor)visitSimpleSource(ctx.simpleSource()));
        } else if(ctx.variable() != null){
            target = TargetClause.convert((DataContainerDescriptor)visitVariable(ctx.variable()));
        } else if (ctx.plot() != null){
            target = (PlotClause)visitPlot(ctx.plot());
        }
        //if(target != null) target.init();
        return target;
    }

    @Override
    public Object visitPlot(@NotNull XQtParser.PlotContext ctx) { 
        PlotClause plot = new PlotClause();
        plot.setDataContainerType(DataContainerDescriptor.DataContainerType.Plot);
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
        plot.setHax(ctx.hx.getText());
        if(ctx.vx1 != null)
            plot.getVaxes().add(ctx.vx1.getText()); // the first vertical axis, if exists!
        else{
            plot.getLanguageExceptions().add(
                LanguageExceptionBuilder.builder()
                    .setMessageTemplate("Expecting at least one vertical axis!")
                    .setLineNumber(ctx.getStart().getLine())
                    .setColumnNumber(ctx.getStart().getCharPositionInLine())
                    .build()
            );            
        }
        for(XQtParser.VariableContext v: ctx.vxs2) // the second and more vertical axes 
            plot.getVaxes().add(v.ID().getText());
        plot.setPlotType(ctx.plotType == null? "l" : ctx.plotType.getText());
        plot.sethLabel(ctx.pxl == null? plot.getHax(): ctx.pxl.getText().replaceAll("\"", ""));
        plot.setvLabel(ctx.pvl == null? plot.getVaxes().get(0): ctx.pvl.getText().replaceAll("\"", ""));
        plot.setPlotLabel(ctx.pll == null? "": ctx.pll.getText().replaceAll("\"", ""));
        
        return plot;
    }
    
    @Override
    public Object visitAnchorClause(@NotNull XQtParser.AnchorClauseContext ctx) {
        try {
            //Object ret = visitChildren(ctx);
            AnchorClause anchor = new AnchorClause();

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
        FilterClause filter = new FilterClause();

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
        String pId="Default";
        String id = ctx.simpleIdentifier().ID().getText();
        List<Expression> parameters = new ArrayList<>();
        ctx.argument().stream().forEach((arg) -> {
            Expression pa = (Expression)visit(arg.expression());
            pa.setExpressionType(ExpressionType.Parameter);
            parameters.add(pa);
        });
        // if all the parameters are value expressions, then it is possible to compute the function and return the result of the function
        // as a value expression!
        FunctionExpression exp = Expression.Function(pId, id, parameters);
        return exp; 
    }
    
    @Override
    public Object visitFunction_package(@NotNull XQtParser.Function_packageContext ctx) { 
        //get package id, function name
        // iterate over the parameters by visiting the expression again
        String pId=ctx.packagedIdentifier().packageId.getText();
        String id = ctx.packagedIdentifier().id.getText();
        List<Expression> parameters = new ArrayList<>();
        ctx.argument().stream().forEach((arg) -> {
            parameters.add((Expression)visit(arg.expression()));
        });
        FunctionExpression exp = Expression.Function(pId, id, parameters);
        return exp; 
    }
    
    @Override
    public Object visitExpression_nest(@NotNull XQtParser.Expression_nestContext ctx) { 
        return visitChildren(ctx.operand); 
    }
    
    @Override
    public Object visitExpression_value(@NotNull XQtParser.Expression_valueContext ctx) { 
        ValueExpression exp = null;
        if(ctx.operand.UINT()!= null){
            exp = Expression.Value(ctx.operand.getText(), "Integer");
            exp.setParserContext(ctx);            
        } else if(ctx.operand.INT() != null){
            exp = Expression.Value(ctx.operand.getText(), "Integer");
            exp.setParserContext(ctx);            
        } else if(ctx.operand.FLOAT() != null){
            exp = Expression.Value(ctx.operand.getText(), "Real");            
            exp.setParserContext(ctx);
        } else if(ctx.operand.BOOLEAN() != null){
            exp = Expression.Value(ctx.operand.getText(), "Boolean");
            exp.setParserContext(ctx);
        } else if(ctx.operand.STRING() != null){
            exp = Expression.Value(ctx.operand.getText(), "String");
            exp.setParserContext(ctx);
        } else if(ctx.operand.DATE() != null){
            exp = Expression.Value(ctx.operand.getText(), "Date");
            exp.setParserContext(ctx);
        }        
        return exp; 
    }
    
    @Override
    public Object visitExpression_idExpr(@NotNull XQtParser.Expression_idExprContext ctx) { 
        MemberExpression exp = (MemberExpression)visit(ctx.operand);
        exp.setParserContext(ctx);
        return exp; 
    }
    
    @Override
    public Object visitIdExpr_simple(@NotNull XQtParser.IdExpr_simpleContext ctx) { 
        MemberExpression exp = Expression.Member(ctx.simpleIdentifier().ID().getText());
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
        OrderClause order = new OrderClause();

        for(XQtParser.SortSpecificationContext sortItemCtx: ctx.sortSpecification()){
            String sortKey = sortItemCtx.sortKey().getText();
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
                    entry.setSortKey(sortKey);
                    if(sortItemCtx.sortOrder() != null){
                        String sortOrderString = XQtParser.tokenNames[sortItemCtx.sortOrder().getStart().getType()];
                        entry.setSortOrder(SortOrder.valueOf(sortOrderString));
                    }
                    if(sortItemCtx.nullOrder() != null){
                        String nullOrder = sortItemCtx.nullOrder().getText();
                        entry.setNullOrdering(NullOrdering.valueOf(nullOrder.replace(" ", "_")));
                    }
                    entry.setParserContext(sortItemCtx);
                    order.getOrderItems().put(entry.getSortKey(), entry);
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
        LimitClause limit = new LimitClause();

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
        GroupClause group = new GroupClause();

        for(XQtParser.SimpleIdentifierContext groupItemCtx: ctx.simpleIdentifier()){
            String id = groupItemCtx.getText();
            if(id  != null && !id.isEmpty()){
                if(group.getGroupIds().containsKey(id)){ // duplicate key
                    group.getLanguageExceptions().add(
                        LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Duplicate grouping key %s found! Line %s")
                            .setContextInfo1(id)
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStop().getCharPositionInLine())
                            .build()
                    );
                }
                else{
                    GroupEntry g = new GroupEntry();
                    g.setId(id);
                    g.setParserContext(groupItemCtx);
                    group.getGroupIds().put(g.getId(), g);
                }
            }
        }

        //stack.push(attribute);
        //visitChildren(ctx);
        //stack.pop();

        return group;
    }

    private PerspectiveDescriptor extractPerspective(SourceClause source) {
        // try to extract as mush field information as possible from the source clause.
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

}