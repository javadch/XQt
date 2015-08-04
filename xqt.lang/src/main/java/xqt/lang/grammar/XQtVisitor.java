// Generated from D:\Projects\PhD\Src\XQt\xqt.lang\src\main\java\xqt\lang\grammar\source\XQt.g4 by ANTLR 4.5
package xqt.lang.grammar;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XQtParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface XQtVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link XQtParser#createProcessModel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProcessModel(XQtParser.CreateProcessModelContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#process}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcess(XQtParser.ProcessContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#perspective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPerspective(XQtParser.PerspectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(XQtParser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#connection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnection(XQtParser.ConnectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#parameter_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_def(XQtParser.Parameter_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#binding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinding(XQtParser.BindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#binding_scope_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinding_scope_def(XQtParser.Binding_scope_defContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(XQtParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#dataStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataStatement(XQtParser.DataStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#dataRetrievalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataRetrievalStatement(XQtParser.DataRetrievalStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStatement(XQtParser.SelectStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#setQualifierClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetQualifierClause(XQtParser.SetQualifierClauseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code projectionClause_Perspective}
	 * labeled alternative in {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProjectionClause_Perspective(XQtParser.ProjectionClause_PerspectiveContext ctx);
	/**
	 * Visit a parse tree produced by the {@code projectionClause_Inline}
	 * labeled alternative in {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProjectionClause_Inline(XQtParser.ProjectionClause_InlineContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#inlineAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineAttribute(XQtParser.InlineAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#sourceSelectionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceSelectionClause(XQtParser.SourceSelectionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#sourceRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRef(XQtParser.SourceRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#joinedSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinedSource(XQtParser.JoinedSourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#joinDescription}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinDescription(XQtParser.JoinDescriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#joinSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinSpecification(XQtParser.JoinSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#singleContainer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleContainer(XQtParser.SingleContainerContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#bindingRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBindingRef(XQtParser.BindingRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#containerRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainerRef(XQtParser.ContainerRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#targetSelectionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetSelectionClause(XQtParser.TargetSelectionClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#plot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlot(XQtParser.PlotContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#anchorClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnchorClause(XQtParser.AnchorClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#filterClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterClause(XQtParser.FilterClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#orderClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderClause(XQtParser.OrderClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#sortSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortSpecification(XQtParser.SortSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#sortKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortKey(XQtParser.SortKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#sortOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortOrder(XQtParser.SortOrderContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#nullOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullOrder(XQtParser.NullOrderContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#limitClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClause(XQtParser.LimitClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#groupClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupClause(XQtParser.GroupClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(XQtParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#searchPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchPhrase(XQtParser.SearchPhraseContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#intNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntNumber(XQtParser.IntNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#floatNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatNumber(XQtParser.FloatNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_not}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_not(XQtParser.Expression_notContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_nest}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_nest(XQtParser.Expression_nestContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_identifier}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_identifier(XQtParser.Expression_identifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_function}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_function(XQtParser.Expression_functionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_value}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_value(XQtParser.Expression_valueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_mult}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_mult(XQtParser.Expression_multContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_power}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_power(XQtParser.Expression_powerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_aAndOr}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_aAndOr(XQtParser.Expression_aAndOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_andOr}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_andOr(XQtParser.Expression_andOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_add}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_add(XQtParser.Expression_addContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_is}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_is(XQtParser.Expression_isContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_negate}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_negate(XQtParser.Expression_negateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_compare}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_compare(XQtParser.Expression_compareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code expression_smart}
	 * labeled alternative in {@link XQtParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_smart(XQtParser.Expression_smartContext ctx);
	/**
	 * Visit a parse tree produced by the {@code function_simple}
	 * labeled alternative in {@link XQtParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_simple(XQtParser.Function_simpleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code function_package}
	 * labeled alternative in {@link XQtParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_package(XQtParser.Function_packageContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(XQtParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#packagedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackagedIdentifier(XQtParser.PackagedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#smartId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSmartId(XQtParser.SmartIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(XQtParser.DataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#semanticKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSemanticKey(XQtParser.SemanticKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(XQtParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifier_qulaified}
	 * labeled alternative in {@link XQtParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier_qulaified(XQtParser.Identifier_qulaifiedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifier_simple}
	 * labeled alternative in {@link XQtParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier_simple(XQtParser.Identifier_simpleContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#versionSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersionSelector(XQtParser.VersionSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#labelVersionSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelVersionSelector(XQtParser.LabelVersionSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#sequenceVersionSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceVersionSelector(XQtParser.SequenceVersionSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#dateVersionSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateVersionSelector(XQtParser.DateVersionSelectorContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#simpleIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleIdentifier(XQtParser.SimpleIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdentifier(XQtParser.QualifiedIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(XQtParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(XQtParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#pathEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathEntity(XQtParser.PathEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link XQtParser#pathAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathAttribute(XQtParser.PathAttributeContext ctx);
}