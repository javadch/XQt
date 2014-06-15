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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.misc.NotNull;
import xqt.lang.annotation.BindingAnnotator;
import xqt.lang.annotation.ConnectionAnnotator;
import xqt.lang.annotation.PerspectiveAnnotator;
import xqt.lang.annotation.SelectAnnotator;
import xqt.lang.grammar.XQtBaseVisitor;
import xqt.lang.grammar.XQtParser;
import xqt.model.ElementDescriptor;
import xqt.model.ProcessModel;
import xqt.model.configurations.BindingDescriptor;
import xqt.model.configurations.ConnectionDescriptor;
import xqt.model.configurations.ConnectionParameterDescriptor;
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
    protected List<String> targetedVariables = new ArrayList<>();

    public GrammarVisitor(){
        processModel = new ProcessModel();
    }
   
    public ProcessModel getProcessModel(){
        return(processModel);
    }

    /** visit each and every rule starting from the root and create a ProcessModel object which holds abstract
     representation of all the process elements i.e. perspectives, connections, statements, bindings, etc
     */
    @Override
    public Object visitCreateProcessModel(XQtParser.CreateProcessModelContext ctx) {
        //VarDefModel vdm = new VarDefModel();
        //processModel.addStatementModel(vdm);
        
        return visitChildren(ctx);
    }

    @Override
    public Object visitPerspective(@NotNull XQtParser.PerspectiveContext ctx) { 
        try {
            //Object ret = visitChildren(ctx); 
            PerspectiveDescriptor perspective = PerspectiveAnnotator.describePerspective(ctx, processModel);
            stack.push(perspective); // it would be better if there were no need for data communication :-(
            for(XQtParser.Attribute_defContext attCtx: ctx.attribute_def()){
                PerspectiveAttributeDescriptor att = (PerspectiveAttributeDescriptor)visitAttribute_def(attCtx);
                perspective.addAttribute(att);
            }
            perspective.setOrderInParent(processModel.totalElementCount());
            processModel.addDeclaration(perspective); //its better to return to visit processmodel and add the perspective there
            stack.pop();
            
            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override 
    public Object visitAttribute_def(@NotNull XQtParser.Attribute_defContext ctx) { 
        try {
            //Object ret = visitChildren(ctx); 
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
            
            //stack.push(attribute);
            //visitChildren(ctx);
            //stack.pop();
            
            return attribute;
            
            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override 
    public Object visitSmartId(@NotNull XQtParser.SmartIdContext ctx) { 
        return visitChildren(ctx); 
    }
    
    @Override
    public Object visitConnection(@NotNull XQtParser.ConnectionContext ctx) { 
       try {
            //Object ret = visitChildren(ctx); 
            ConnectionDescriptor connection = ConnectionAnnotator.describeConnection(ctx, processModel);
            stack.push(connection); // it would be better if there were no need for data communication :-(
            for(XQtParser.Parameter_defContext paramCtx: ctx.parameter_def()){
                ConnectionParameterDescriptor param = (ConnectionParameterDescriptor)visitParameter_def(paramCtx);
                connection.addParameter(param);
            }
            connection.setOrderInParent(processModel.totalElementCount());
            processModel.addConfiguration(connection); //its better to return to visit processmodel and add the perspective there
            stack.pop();
            
            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the upper element
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public Object visitParameter_def(@NotNull XQtParser.Parameter_defContext ctx) { 
         try {
            //Object ret = visitChildren(ctx); 
            ConnectionDescriptor connection = (ConnectionDescriptor)stack.peek();
            ConnectionParameterDescriptor parameter = ConnectionAnnotator.describeConnectionParameter(ctx, connection.getId());
            
            //stack.push(attribute);
            //visitChildren(ctx);
            //stack.pop();
            
            return parameter;
            
            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public Object visitBinding(@NotNull XQtParser.BindingContext ctx) { 
       try {
            //Object ret = visitChildren(ctx); 
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
            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the upper element
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object visitBinding_scope_def(@NotNull XQtParser.Binding_scope_defContext ctx) { 
         try {
            //Object ret = visitChildren(ctx); 
            BindingDescriptor binding = (BindingDescriptor)stack.peek();
            String scope = BindingAnnotator.describeBindingSource(ctx, binding.getId());
            
            //stack.push(attribute);
            //visitChildren(ctx);
            //stack.pop();
            
            return scope;
            
            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object visitSelectStatement(@NotNull XQtParser.SelectStatementContext ctx) {
        try {
            //Object ret = visitChildren(ctx);
            SelectDescriptor selectDesc = SelectAnnotator.describeSelect(ctx, processModel);
            stack.push(selectDesc); // it would be better if there were no need for data communication :-(
            // process clauses, add new objects instead of null if the corresponding visitor returns null
            SetQualifierClause setQuantifier  = ctx.setQualifierClause() == null? new SetQualifierClause(): (SetQualifierClause)visitSetQualifierClause(ctx.setQualifierClause());
            ProjectionClause    projection  = ctx.projectionClause()== null? new ProjectionClause(): (ProjectionClause)visitProjectionClause(ctx.projectionClause());
            SourceClause        source      = (SourceClause)visitSourceSelectionClause(ctx.sourceSelectionClause());
            TargetClause        target      = ctx.targetSelectionClause() == null? new TargetClause(): (TargetClause)visitTargetSelectionClause(ctx.targetSelectionClause());
            AnchorClause        anchor      = ctx.anchorClause() == null? new AnchorClause(): (AnchorClause)visitAnchorClause(ctx.anchorClause());
            FilterClause        filter      = ctx.filterClause() == null? new FilterClause(): (FilterClause)visitFilterClause(ctx.filterClause());
            OrderClause         order       = ctx.orderClause() == null? new OrderClause(): (OrderClause)visitOrderClause(ctx.orderClause());
            LimitClause         limit       = ctx.limitClause() == null? new LimitClause(): (LimitClause)visitLimitClause(ctx.limitClause());
            GroupClause         group       = ctx.groupClause() == null? new GroupClause(): (GroupClause)visitGroupClause(ctx.groupClause());

            // -> when all the clauses are described perform the second round to interconnect and validate them
            if(projection.getPerspective() == null) {
                // projection may be defined by the source clause, so there is an implicit perspective to be extracted
                PerspectiveDescriptor implicitPerspective = extractPerspective(source);
                implicitPerspective.setExplicit(false);
                projection.setPerspective(implicitPerspective);
            }
            // -> expressions pointing to perpsective attributes should be transformed to their physical counterpart

            // -> the target variable should point to its statement.
//            if(target.getVariable() != null) // select may have no target
//                target.getVariable().setStatement(selectDesc);
            
            // -> check whether Ids defined in the filter clause are defined as an attribute in the associated perspective
            MemberExpression faultyAttribute = validateAttributesInExpression(((FilterClause)filter).getPredicate(), projection.getPerspective());
            if(faultyAttribute != null){
                 throw LanguageExceptionBuilder.builder()
                           .setMessageTemplate("The WHERE clause is using attribute \"%s\" that is not defined in perspective \"%s\"")
                           .setContextInfo1(faultyAttribute.getId())
                           .setContextInfo2(projection.getPerspective().getId())
                           .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                           .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                           .build()
                           ;
            }
            // -> check whether Ids defined in the anchor clause are defined as an attribute in the associated perspective
            faultyAttribute = validateAttributesInExpression(((AnchorClause)anchor).getStartAnchor(), projection.getPerspective());
            if(faultyAttribute != null){
                 throw LanguageExceptionBuilder.builder()
                           .setMessageTemplate("The ANCHOR START clause is using attribute \"%s\" that is not defined in perspective \"%s\"")
                           .setContextInfo1(faultyAttribute.getId())
                           .setContextInfo2(projection.getPerspective().getId())
                           .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                           .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                           .build()
                           ;
            }

            faultyAttribute = validateAttributesInExpression(((AnchorClause)anchor).getStopAnchor(), projection.getPerspective());
            if(faultyAttribute != null){
                 throw LanguageExceptionBuilder.builder()
                           .setMessageTemplate("The ANCHOR STOP clause is using attribute \"%s\" that is not defined in perspective \"%s\"")
                           .setContextInfo1(faultyAttribute.getId())
                           .setContextInfo2(projection.getPerspective().getId())
                           .setLineNumber(faultyAttribute.getParserContext().getStart().getLine())
                           .setColumnNumber(faultyAttribute.getParserContext().getStart().getCharPositionInLine())
                           .build()
                           ;
            }

            // -> check whether Ids defined in the ordering clause are defined as an attribute in the associated perspective
            for(OrderEntry orderItem : order.getOrderItems().values()){
                if(!projection.getPerspective().getAttributes().containsKey(orderItem.getSortKey())){
                   throw LanguageExceptionBuilder.builder()
                           .setMessageTemplate("The ORDER BY clause is using attribute \"%s\" that is not defined in associated perspective \"%s\"")
                            .setContextInfo1(orderItem.getSortKey())
                            .setContextInfo2(projection.getPerspective().getId())
                            .setLineNumber(orderItem.getParserContext().getStart().getLine())
                            .setColumnNumber(orderItem.getParserContext().getStop().getCharPositionInLine())
                           .build()
                           ;
                }
            }
            // -> check whether Ids defined in the grouping clause are defined as an attribute in the associated perspective
            for(GroupEntry groupItem : group.getGroupIds().values()){
                if(!projection.getPerspective().getAttributes().containsKey(groupItem.getId())){
                   throw LanguageExceptionBuilder.builder()
                            .setMessageTemplate("The group by clause refers to attribute %s "
                                 + ", which is not defined in the associated perspective %s! Line %s")
                            .setContextInfo1(groupItem.getId())
                            .setContextInfo2(projection.getPerspective().getId())
                            .setLineNumber(groupItem.getParserContext().getStart().getLine())
                            .setColumnNumber(groupItem.getParserContext().getStart().getCharPositionInLine())
                           .build()
                            ;
                }
            }

            // clauses are added in the grammar order, and the order is preserved in the clauses linked list.
            // also each clause knows its order in the parent element; Select in this case
            selectDesc.addClause(setQuantifier);
            selectDesc.addClause(projection);
            selectDesc.addClause(source);
            selectDesc.addClause(target);
            selectDesc.addClause(anchor);
            selectDesc.addClause(filter);
            selectDesc.addClause(order);
            selectDesc.addClause(limit);
            selectDesc.addClause(group);

            selectDesc.setOrderInParent(processModel.totalElementCount());
            processModel.addStatementDescriptor(selectDesc); //its better to return to visit processmodel and add the perspective there
            stack.pop();
            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the upper element
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object visitSetQualifierClause(@NotNull XQtParser.SetQualifierClauseContext ctx) {
        try {
            //Object ret = visitChildren(ctx);
            SetQualifierClause clause = new SetQualifierClause();
            clause.setQualifier(SetQualifierType.valueOf(ctx.getText().toUpperCase()));
            //stack.push(attribute);
            //visitChildren(ctx);
            //stack.pop();

            return clause;

            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
             try {
                 // where? add statement and location information
                 throw LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Unknown Set Qualifier!")
                        .setLineNumber(ctx.getStart().getLine())
                        .setColumnNumber(ctx.getStop().getCharPositionInLine())
                        .build()
                        ; // where? add statement and location information
             } catch (LanguageException ex1) {
                 Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex1);
             }
        }
        return null;
    }

    @Override
    public Object visitProjectionClause(@NotNull XQtParser.ProjectionClauseContext ctx) {
        // if this method is not called (visited), means that the statement is porbably using an implicit
        //perpsective defined in the source clause
        // after visiting all the clauses, try to determine the perspective again.
        // the source node visitor should define the perspective and add it to the process model declarations
        try {
            //Object ret = visitChildren(ctx);
            ProjectionClause projection = new ProjectionClause();
            String perspectiveName = ctx.perspectiveName.getText();

            if(perspectiveName == null || perspectiveName.isEmpty())
                throw LanguageExceptionBuilder.builder()
                        .setMessageTemplate("Perspective name expected!")
                        .setLineNumber(ctx.getStart().getLine())
                        .setColumnNumber(ctx.getStop().getCharPositionInLine())
                        .build()
                        ;
            //ElementDescriptor parent = (ElementDescriptor)stack.peek();
            DeclarationDescriptor perspective = processModel.getDeclarations().get(perspectiveName);
            if(perspective == null){
                throw LanguageExceptionBuilder.builder()
                        .setMessageTemplate("The statement expects perspective %s, which is not defined!")
                        .setContextInfo1(perspectiveName)
                        .setLineNumber(ctx.getStart().getLine())
                        .setColumnNumber(ctx.getStop().getCharPositionInLine())
                        .build()
                        ;
            }
            else {
                if(perspective instanceof PerspectiveDescriptor){
                    projection.setPerspective((PerspectiveDescriptor)perspective);
                }
                else{
                     throw LanguageExceptionBuilder.builder()
                             .setMessageTemplate("The statement is pointing to declaration %s that is not a perspective")
                             .setContextInfo1(perspectiveName)
                             .setLineNumber(ctx.getStart().getLine())
                             .setColumnNumber(ctx.getStart().getCharPositionInLine())
                             .build()
                             ;
                }
            }
            //stack.push(attribute);
            //visitChildren(ctx);
            //stack.pop();

            return projection;

            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object visitSourceSelectionClause(@NotNull XQtParser.SourceSelectionClauseContext ctx) {
        try {
            //Object ret = visitChildren(ctx);
            SourceClause source = new SourceClause();
            // check whether the binding exists
            String bindingName = ctx.sourceRef().simpleSource().bindingRef().getText();
            BindingDescriptor b = null;
            if(processModel.getConfigurations().containsKey(bindingName)){
                b = (BindingDescriptor)processModel.getConfigurations().get(bindingName);
                source.setBinding(b); // the source should be able to hold joined bindings and source variables
            }
            else {
                throw LanguageExceptionBuilder.builder()
                        .setMessageTemplate("The statement expects data source %s, which is not defined!")
                        .setContextInfo1(bindingName)
                        .setLineNumber(ctx.getStart().getLine())
                        .setColumnNumber(ctx.getStop().getCharPositionInLine())
                        .build()
                        ;
            }
            
            // check out of range container index/ or not existing names
            // in case of named containers find their corresponding index and store the index.
            String container = ctx.sourceRef().simpleSource().containerRef().getText();                        
            try{ // the container is assumed to be a numerical index
                Integer index = Integer.parseInt(container);
                if(index >= 0 && index < b.getScopes().size()){
                    source.setContainerIndex(index);
                }
                else{
                    throw LanguageExceptionBuilder.builder()
                            .setMessageTemplate("The container index %s is out of range!")
                            .setContextInfo1(container)
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStart().getCharPositionInLine())
                            .build()
                            ;
                }
            }
            catch (NumberFormatException  ex) {  // the container is not a numerical index, instead is a name, so the index should be identified
                if(b.getScopes().contains(container)){
                    source.setContainerIndex(b.getScopes().indexOf(container)); // the position of the container in the scoped list
                }
                else{
                    throw LanguageExceptionBuilder.builder()
                            .setMessageTemplate("The container %s is not in the scope of binding %s!")
                            .setContextInfo1(container)
                            .setContextInfo2(b.getId())
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStart().getCharPositionInLine())
                            .build()
                            ;
                }
            }
            
            //stack.push(attribute);
            //visitChildren(ctx);
            //stack.pop();

            return source;

            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object visitTargetSelectionClause(@NotNull XQtParser.TargetSelectionClauseContext ctx) {
        try {
            //Object ret = visitChildren(ctx);
            TargetClause target = new TargetClause();
            // check duplicate variable names!!!
            if(ctx.variable() != null){
                String varName = ctx.variable().ID().getText();
                if(targetedVariables.contains(varName)){ // variables are immutable, using them in more than one target clause is not allowed
                    throw LanguageExceptionBuilder.builder()
                            .setMessageTemplate("Target variable %s is already in use! "
                                + "Using one variable as the target of more than one statement is not allowed. ")
                            .setContextInfo1(varName)
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStop().getCharPositionInLine())
                            .build()
                            ;
                }
                if(varName == null || varName.isEmpty()){
                    throw LanguageExceptionBuilder.builder()
                            .setMessageTemplate("The target selection clause should point to a variable! Line %s.")
                            .setLineNumber(ctx.getStart().getLine())
                            .setColumnNumber(ctx.getStart().getCharPositionInLine())
                            .build()
                            ;
                }
                // keep track of variables for duplicate control. just the variables used in target selection clauses
                targetedVariables.add(varName);
                target.setVariableName(varName);

                //stack.push(attribute);
                //visitChildren(ctx);
                //stack.pop();

                return target;
            }
            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        try {
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

            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
    
    @Override
    public Object visitExpression_is(@NotNull XQtParser.Expression_isContext ctx) { 
        Expression operand = (Expression)visit(ctx.operand);
        UnaryExpression exp = null;
        if(ctx.isType.getText().toUpperCase().equals("NULL")){
            exp = Expression.IsNull(operand);
            exp.setParserContext(ctx);
        } else if(ctx.isType.getText().toUpperCase().equals("NULL")){
            exp = Expression.IsNaN(operand);
            exp.setParserContext(ctx);
        }
        if(ctx.not.getText().toUpperCase().equals("NOT")){
            exp.setParserContext(null); // the upper exprerssion gets the context
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
        try {
            //Object ret = visitChildren(ctx);
            OrderClause order = new OrderClause();

            for(XQtParser.SortSpecificationContext sortItemCtx: ctx.sortSpecification()){
                String sortKey = sortItemCtx.sortKey().getText();
                if(sortKey  != null && !sortKey.isEmpty()){
                    if(order.getOrderItems().containsKey(sortKey)){ // duplicate key
                        throw LanguageExceptionBuilder.builder()
                                .setMessageTemplate("Duplicate ordering key %s found!")
                                .setContextInfo1(sortKey)
                                .setLineNumber(ctx.getStart().getLine())
                                .setColumnNumber(ctx.getStop().getCharPositionInLine())
                                .build()
                                ;
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

            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object visitLimitClause(@NotNull XQtParser.LimitClauseContext ctx) {
        try {
            //Object ret = visitChildren(ctx);
            LimitClause limit = new LimitClause();

//            limit.setSkip(-1);
//            limit.setTake(-1);
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

            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;    }

    @Override
    public Object visitGroupClause(@NotNull XQtParser.GroupClauseContext ctx) {
        try {
            //Object ret = visitChildren(ctx);
            GroupClause group = new GroupClause();

            for(XQtParser.SimpleIdentifierContext groupItemCtx: ctx.simpleIdentifier()){
                String id = groupItemCtx.getText();
                if(id  != null && !id.isEmpty()){
                    if(group.getGroupIds().containsKey(id)){ // duplicate key
                        throw LanguageExceptionBuilder.builder()
                                .setMessageTemplate("Duplicate grouping key %s found! Line %s")
                                .setContextInfo1(id)
                                .setLineNumber(ctx.getStart().getLine())
                                .setColumnNumber(ctx.getStop().getCharPositionInLine())
                                .build()
                                ;
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

            // take care of visitChildren, it should be done first and return proper info
            // try use visitSmartID, visitExpression, etc and attach their results to the perspective
        } catch (Exception ex) {
            Logger.getLogger(GrammarVisitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private PerspectiveDescriptor extractPerspective(SourceClause source) {
        // try to extract as mush field information as possible fron the source clause.
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