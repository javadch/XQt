// Generated from D:\Projects\PhD\Src\XQt\xqt.lang\src\main\java\xqt\lang\grammar\source\XQt.g4 by ANTLR 4.2.2
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
	 * Visit a parse tree produced by {@link XQtParser#function_package}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_package(@NotNull XQtParser.Function_packageContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(@NotNull XQtParser.ArgumentContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#bindingRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBindingRef(@NotNull XQtParser.BindingRefContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#parameter_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter_def(@NotNull XQtParser.Parameter_defContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_value(@NotNull XQtParser.Expression_valueContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#binding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinding(@NotNull XQtParser.BindingContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#idExpr_qulaified}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpr_qulaified(@NotNull XQtParser.IdExpr_qulaifiedContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#setQualifierClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetQualifierClause(@NotNull XQtParser.SetQualifierClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#joinSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinSpecification(@NotNull XQtParser.JoinSpecificationContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#path}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(@NotNull XQtParser.PathContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_add}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_add(@NotNull XQtParser.Expression_addContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_idExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_idExpr(@NotNull XQtParser.Expression_idExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#connection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnection(@NotNull XQtParser.ConnectionContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStatement(@NotNull XQtParser.SelectStatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_compare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_compare(@NotNull XQtParser.Expression_compareContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#createProcessModel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateProcessModel(@NotNull XQtParser.CreateProcessModelContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#dataRetrievalStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataRetrievalStatement(@NotNull XQtParser.DataRetrievalStatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_nest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_nest(@NotNull XQtParser.Expression_nestContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#sequenceVersionSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequenceVersionSelector(@NotNull XQtParser.SequenceVersionSelectorContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#pathAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathAttribute(@NotNull XQtParser.PathAttributeContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_function(@NotNull XQtParser.Expression_functionContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(@NotNull XQtParser.DataTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_power}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_power(@NotNull XQtParser.Expression_powerContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#groupClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupClause(@NotNull XQtParser.GroupClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#sortKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortKey(@NotNull XQtParser.SortKeyContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#idExpr_simple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpr_simple(@NotNull XQtParser.IdExpr_simpleContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#simpleIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleIdentifier(@NotNull XQtParser.SimpleIdentifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#orderClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderClause(@NotNull XQtParser.OrderClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#sortOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortOrder(@NotNull XQtParser.SortOrderContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#semanticKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSemanticKey(@NotNull XQtParser.SemanticKeyContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#sourceRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceRef(@NotNull XQtParser.SourceRefContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#smartId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSmartId(@NotNull XQtParser.SmartIdContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#joinedSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinedSource(@NotNull XQtParser.JoinedSourceContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_smart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_smart(@NotNull XQtParser.Expression_smartContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_not}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_not(@NotNull XQtParser.Expression_notContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#sourceSelectionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceSelectionClause(@NotNull XQtParser.SourceSelectionClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#joinDescription}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinDescription(@NotNull XQtParser.JoinDescriptionContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#labelVersionSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelVersionSelector(@NotNull XQtParser.LabelVersionSelectorContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#attribute_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_def(@NotNull XQtParser.Attribute_defContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProjectionClause(@NotNull XQtParser.ProjectionClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#sortSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSortSpecification(@NotNull XQtParser.SortSpecificationContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_mult}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_mult(@NotNull XQtParser.Expression_multContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#pathEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathEntity(@NotNull XQtParser.PathEntityContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#nullOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullOrder(@NotNull XQtParser.NullOrderContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedIdentifier(@NotNull XQtParser.QualifiedIdentifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull XQtParser.LiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#plotStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlotStatement(@NotNull XQtParser.PlotStatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_andOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_andOr(@NotNull XQtParser.Expression_andOrContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#targetSelectionClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTargetSelectionClause(@NotNull XQtParser.TargetSelectionClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(@NotNull XQtParser.StatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#perspective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPerspective(@NotNull XQtParser.PerspectiveContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_negate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_negate(@NotNull XQtParser.Expression_negateContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(@NotNull XQtParser.ValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#versionSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersionSelector(@NotNull XQtParser.VersionSelectorContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#anchorClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnchorClause(@NotNull XQtParser.AnchorClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#process}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcess(@NotNull XQtParser.ProcessContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#filterClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterClause(@NotNull XQtParser.FilterClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#searchPhrase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearchPhrase(@NotNull XQtParser.SearchPhraseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#function_simple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_simple(@NotNull XQtParser.Function_simpleContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#limitClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClause(@NotNull XQtParser.LimitClauseContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#dateVersionSelector}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateVersionSelector(@NotNull XQtParser.DateVersionSelectorContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#floatNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatNumber(@NotNull XQtParser.FloatNumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_aAndOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_aAndOr(@NotNull XQtParser.Expression_aAndOrContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#packagedIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackagedIdentifier(@NotNull XQtParser.PackagedIdentifierContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#simpleSource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSource(@NotNull XQtParser.SimpleSourceContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#containerRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainerRef(@NotNull XQtParser.ContainerRefContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(@NotNull XQtParser.VariableContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#expression_is}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_is(@NotNull XQtParser.Expression_isContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#dataStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataStatement(@NotNull XQtParser.DataStatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#intNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntNumber(@NotNull XQtParser.IntNumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link XQtParser#binding_scope_def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinding_scope_def(@NotNull XQtParser.Binding_scope_defContext ctx);
}