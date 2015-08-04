// Generated from D:\Projects\PhD\Src\XQt\xqt.lang\src\main\java\xqt\lang\grammar\source\XQt.g4 by ANTLR 4.5
package xqt.lang.grammar;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XQtParser}.
 */
public interface XQtListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link XQtParser#createProcessModel}.
	 * @param ctx the parse tree
	 */
	void enterCreateProcessModel(XQtParser.CreateProcessModelContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#createProcessModel}.
	 * @param ctx the parse tree
	 */
	void exitCreateProcessModel(XQtParser.CreateProcessModelContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#process}.
	 * @param ctx the parse tree
	 */
	void enterProcess(XQtParser.ProcessContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#process}.
	 * @param ctx the parse tree
	 */
	void exitProcess(XQtParser.ProcessContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#perspective}.
	 * @param ctx the parse tree
	 */
	void enterPerspective(XQtParser.PerspectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#perspective}.
	 * @param ctx the parse tree
	 */
	void exitPerspective(XQtParser.PerspectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(XQtParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(XQtParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#connection}.
	 * @param ctx the parse tree
	 */
	void enterConnection(XQtParser.ConnectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#connection}.
	 * @param ctx the parse tree
	 */
	void exitConnection(XQtParser.ConnectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#parameter_def}.
	 * @param ctx the parse tree
	 */
	void enterParameter_def(XQtParser.Parameter_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#parameter_def}.
	 * @param ctx the parse tree
	 */
	void exitParameter_def(XQtParser.Parameter_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#binding}.
	 * @param ctx the parse tree
	 */
	void enterBinding(XQtParser.BindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#binding}.
	 * @param ctx the parse tree
	 */
	void exitBinding(XQtParser.BindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#binding_scope_def}.
	 * @param ctx the parse tree
	 */
	void enterBinding_scope_def(XQtParser.Binding_scope_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#binding_scope_def}.
	 * @param ctx the parse tree
	 */
	void exitBinding_scope_def(XQtParser.Binding_scope_defContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(XQtParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(XQtParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#dataStatement}.
	 * @param ctx the parse tree
	 */
	void enterDataStatement(XQtParser.DataStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#dataStatement}.
	 * @param ctx the parse tree
	 */
	void exitDataStatement(XQtParser.DataStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#dataRetrievalStatement}.
	 * @param ctx the parse tree
	 */
	void enterDataRetrievalStatement(XQtParser.DataRetrievalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#dataRetrievalStatement}.
	 * @param ctx the parse tree
	 */
	void exitDataRetrievalStatement(XQtParser.DataRetrievalStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStatement(XQtParser.SelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStatement(XQtParser.SelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#setQualifierClause}.
	 * @param ctx the parse tree
	 */
	void enterSetQualifierClause(XQtParser.SetQualifierClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#setQualifierClause}.
	 * @param ctx the parse tree
	 */
	void exitSetQualifierClause(XQtParser.SetQualifierClauseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code projectionClause_Perspective}
	 * labeled alternative in {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 */
	void enterProjectionClause_Perspective(XQtParser.ProjectionClause_PerspectiveContext ctx);
	/**
	 * Exit a parse tree produced by the {@code projectionClause_Perspective}
	 * labeled alternative in {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 */
	void exitProjectionClause_Perspective(XQtParser.ProjectionClause_PerspectiveContext ctx);
	/**
	 * Enter a parse tree produced by the {@code projectionClause_Inline}
	 * labeled alternative in {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 */
	void enterProjectionClause_Inline(XQtParser.ProjectionClause_InlineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code projectionClause_Inline}
	 * labeled alternative in {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 */
	void exitProjectionClause_Inline(XQtParser.ProjectionClause_InlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#inlineAttribute}.
	 * @param ctx the parse tree
	 */
	void enterInlineAttribute(XQtParser.InlineAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#inlineAttribute}.
	 * @param ctx the parse tree
	 */
	void exitInlineAttribute(XQtParser.InlineAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#sourceSelectionClause}.
	 * @param ctx the parse tree
	 */
	void enterSourceSelectionClause(XQtParser.SourceSelectionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sourceSelectionClause}.
	 * @param ctx the parse tree
	 */
	void exitSourceSelectionClause(XQtParser.SourceSelectionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#sourceRef}.
	 * @param ctx the parse tree
	 */
	void enterSourceRef(XQtParser.SourceRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sourceRef}.
	 * @param ctx the parse tree
	 */
	void exitSourceRef(XQtParser.SourceRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#joinedSource}.
	 * @param ctx the parse tree
	 */
	void enterJoinedSource(XQtParser.JoinedSourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#joinedSource}.
	 * @param ctx the parse tree
	 */
	void exitJoinedSource(XQtParser.JoinedSourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#joinDescription}.
	 * @param ctx the parse tree
	 */
	void enterJoinDescription(XQtParser.JoinDescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#joinDescription}.
	 * @param ctx the parse tree
	 */
	void exitJoinDescription(XQtParser.JoinDescriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#joinSpecification}.
	 * @param ctx the parse tree
	 */
	void enterJoinSpecification(XQtParser.JoinSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#joinSpecification}.
	 * @param ctx the parse tree
	 */
	void exitJoinSpecification(XQtParser.JoinSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#singleContainer}.
	 * @param ctx the parse tree
	 */
	void enterSingleContainer(XQtParser.SingleContainerContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#singleContainer}.
	 * @param ctx the parse tree
	 */
	void exitSingleContainer(XQtParser.SingleContainerContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#bindingRef}.
	 * @param ctx the parse tree
	 */
	void enterBindingRef(XQtParser.BindingRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#bindingRef}.
	 * @param ctx the parse tree
	 */
	void exitBindingRef(XQtParser.BindingRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#containerRef}.
	 * @param ctx the parse tree
	 */
	void enterContainerRef(XQtParser.ContainerRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#containerRef}.
	 * @param ctx the parse tree
	 */
	void exitContainerRef(XQtParser.ContainerRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#targetSelectionClause}.
	 * @param ctx the parse tree
	 */
	void enterTargetSelectionClause(XQtParser.TargetSelectionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#targetSelectionClause}.
	 * @param ctx the parse tree
	 */
	void exitTargetSelectionClause(XQtParser.TargetSelectionClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#plot}.
	 * @param ctx the parse tree
	 */
	void enterPlot(XQtParser.PlotContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#plot}.
	 * @param ctx the parse tree
	 */
	void exitPlot(XQtParser.PlotContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#anchorClause}.
	 * @param ctx the parse tree
	 */
	void enterAnchorClause(XQtParser.AnchorClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#anchorClause}.
	 * @param ctx the parse tree
	 */
	void exitAnchorClause(XQtParser.AnchorClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#filterClause}.
	 * @param ctx the parse tree
	 */
	void enterFilterClause(XQtParser.FilterClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#filterClause}.
	 * @param ctx the parse tree
	 */
	void exitFilterClause(XQtParser.FilterClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#orderClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderClause(XQtParser.OrderClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#orderClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderClause(XQtParser.OrderClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#sortSpecification}.
	 * @param ctx the parse tree
	 */
	void enterSortSpecification(XQtParser.SortSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sortSpecification}.
	 * @param ctx the parse tree
	 */
	void exitSortSpecification(XQtParser.SortSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#sortKey}.
	 * @param ctx the parse tree
	 */
	void enterSortKey(XQtParser.SortKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sortKey}.
	 * @param ctx the parse tree
	 */
	void exitSortKey(XQtParser.SortKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#sortOrder}.
	 * @param ctx the parse tree
	 */
	void enterSortOrder(XQtParser.SortOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sortOrder}.
	 * @param ctx the parse tree
	 */
	void exitSortOrder(XQtParser.SortOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#nullOrder}.
	 * @param ctx the parse tree
	 */
	void enterNullOrder(XQtParser.NullOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#nullOrder}.
	 * @param ctx the parse tree
	 */
	void exitNullOrder(XQtParser.NullOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(XQtParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(XQtParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupClause(XQtParser.GroupClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupClause(XQtParser.GroupClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(XQtParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(XQtParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#searchPhrase}.
	 * @param ctx the parse tree
	 */
	void enterSearchPhrase(XQtParser.SearchPhraseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#searchPhrase}.
	 * @param ctx the parse tree
	 */
	void exitSearchPhrase(XQtParser.SearchPhraseContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#intNumber}.
	 * @param ctx the parse tree
	 */
	void enterIntNumber(XQtParser.IntNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#intNumber}.
	 * @param ctx the parse tree
	 */
	void exitIntNumber(XQtParser.IntNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#floatNumber}.
	 * @param ctx the parse tree
	 */
	void enterFloatNumber(XQtParser.FloatNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#floatNumber}.
	 * @param ctx the parse tree
	 */
	void exitFloatNumber(XQtParser.FloatNumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_not}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_not(XQtParser.Expression_notContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_not}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_not(XQtParser.Expression_notContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_nest}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_nest(XQtParser.Expression_nestContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_nest}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_nest(XQtParser.Expression_nestContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_identifier}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_identifier(XQtParser.Expression_identifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_identifier}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_identifier(XQtParser.Expression_identifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_function}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_function(XQtParser.Expression_functionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_function}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_function(XQtParser.Expression_functionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_value}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_value(XQtParser.Expression_valueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_value}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_value(XQtParser.Expression_valueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_mult}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_mult(XQtParser.Expression_multContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_mult}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_mult(XQtParser.Expression_multContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_power}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_power(XQtParser.Expression_powerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_power}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_power(XQtParser.Expression_powerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_aAndOr}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_aAndOr(XQtParser.Expression_aAndOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_aAndOr}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_aAndOr(XQtParser.Expression_aAndOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_andOr}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_andOr(XQtParser.Expression_andOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_andOr}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_andOr(XQtParser.Expression_andOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_add}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_add(XQtParser.Expression_addContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_add}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_add(XQtParser.Expression_addContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_is}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_is(XQtParser.Expression_isContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_is}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_is(XQtParser.Expression_isContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_negate}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_negate(XQtParser.Expression_negateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_negate}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_negate(XQtParser.Expression_negateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_compare}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_compare(XQtParser.Expression_compareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_compare}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_compare(XQtParser.Expression_compareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expression_smart}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression_smart(XQtParser.Expression_smartContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expression_smart}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression_smart(XQtParser.Expression_smartContext ctx);
	/**
	 * Enter a parse tree produced by the {@code function_simple}
	 * labeled alternative in {@link XQtParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction_simple(XQtParser.Function_simpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code function_simple}
	 * labeled alternative in {@link XQtParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction_simple(XQtParser.Function_simpleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code function_package}
	 * labeled alternative in {@link XQtParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction_package(XQtParser.Function_packageContext ctx);
	/**
	 * Exit a parse tree produced by the {@code function_package}
	 * labeled alternative in {@link XQtParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction_package(XQtParser.Function_packageContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(XQtParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(XQtParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#packagedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterPackagedIdentifier(XQtParser.PackagedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#packagedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitPackagedIdentifier(XQtParser.PackagedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#smartId}.
	 * @param ctx the parse tree
	 */
	void enterSmartId(XQtParser.SmartIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#smartId}.
	 * @param ctx the parse tree
	 */
	void exitSmartId(XQtParser.SmartIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(XQtParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(XQtParser.DataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#semanticKey}.
	 * @param ctx the parse tree
	 */
	void enterSemanticKey(XQtParser.SemanticKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#semanticKey}.
	 * @param ctx the parse tree
	 */
	void exitSemanticKey(XQtParser.SemanticKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(XQtParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(XQtParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifier_qulaified}
	 * labeled alternative in {@link XQtParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier_qulaified(XQtParser.Identifier_qulaifiedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifier_qulaified}
	 * labeled alternative in {@link XQtParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier_qulaified(XQtParser.Identifier_qulaifiedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifier_simple}
	 * labeled alternative in {@link XQtParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier_simple(XQtParser.Identifier_simpleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifier_simple}
	 * labeled alternative in {@link XQtParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier_simple(XQtParser.Identifier_simpleContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#versionSelector}.
	 * @param ctx the parse tree
	 */
	void enterVersionSelector(XQtParser.VersionSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#versionSelector}.
	 * @param ctx the parse tree
	 */
	void exitVersionSelector(XQtParser.VersionSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#labelVersionSelector}.
	 * @param ctx the parse tree
	 */
	void enterLabelVersionSelector(XQtParser.LabelVersionSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#labelVersionSelector}.
	 * @param ctx the parse tree
	 */
	void exitLabelVersionSelector(XQtParser.LabelVersionSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#sequenceVersionSelector}.
	 * @param ctx the parse tree
	 */
	void enterSequenceVersionSelector(XQtParser.SequenceVersionSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sequenceVersionSelector}.
	 * @param ctx the parse tree
	 */
	void exitSequenceVersionSelector(XQtParser.SequenceVersionSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#dateVersionSelector}.
	 * @param ctx the parse tree
	 */
	void enterDateVersionSelector(XQtParser.DateVersionSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#dateVersionSelector}.
	 * @param ctx the parse tree
	 */
	void exitDateVersionSelector(XQtParser.DateVersionSelectorContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#simpleIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterSimpleIdentifier(XQtParser.SimpleIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#simpleIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitSimpleIdentifier(XQtParser.SimpleIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedIdentifier(XQtParser.QualifiedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedIdentifier(XQtParser.QualifiedIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(XQtParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(XQtParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(XQtParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(XQtParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#pathEntity}.
	 * @param ctx the parse tree
	 */
	void enterPathEntity(XQtParser.PathEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#pathEntity}.
	 * @param ctx the parse tree
	 */
	void exitPathEntity(XQtParser.PathEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link XQtParser#pathAttribute}.
	 * @param ctx the parse tree
	 */
	void enterPathAttribute(XQtParser.PathAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#pathAttribute}.
	 * @param ctx the parse tree
	 */
	void exitPathAttribute(XQtParser.PathAttributeContext ctx);
}