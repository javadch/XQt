// Generated from D:\Projects\PhD\Src\XQt\xqt.lang\src\main\java\xqt\lang\grammar\source\XQt.g4 by ANTLR 4.2.2
package xqt.lang.grammar;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XQtParser}.
 */
public interface XQtListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link XQtParser#function_package}.
	 * @param ctx the parse tree
	 */
	void enterFunction_package(@NotNull XQtParser.Function_packageContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#function_package}.
	 * @param ctx the parse tree
	 */
	void exitFunction_package(@NotNull XQtParser.Function_packageContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(@NotNull XQtParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(@NotNull XQtParser.ArgumentContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#bindingRef}.
	 * @param ctx the parse tree
	 */
	void enterBindingRef(@NotNull XQtParser.BindingRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#bindingRef}.
	 * @param ctx the parse tree
	 */
	void exitBindingRef(@NotNull XQtParser.BindingRefContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#parameter_def}.
	 * @param ctx the parse tree
	 */
	void enterParameter_def(@NotNull XQtParser.Parameter_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#parameter_def}.
	 * @param ctx the parse tree
	 */
	void exitParameter_def(@NotNull XQtParser.Parameter_defContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_value}.
	 * @param ctx the parse tree
	 */
	void enterExpression_value(@NotNull XQtParser.Expression_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_value}.
	 * @param ctx the parse tree
	 */
	void exitExpression_value(@NotNull XQtParser.Expression_valueContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#binding}.
	 * @param ctx the parse tree
	 */
	void enterBinding(@NotNull XQtParser.BindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#binding}.
	 * @param ctx the parse tree
	 */
	void exitBinding(@NotNull XQtParser.BindingContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#idExpr_qulaified}.
	 * @param ctx the parse tree
	 */
	void enterIdExpr_qulaified(@NotNull XQtParser.IdExpr_qulaifiedContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#idExpr_qulaified}.
	 * @param ctx the parse tree
	 */
	void exitIdExpr_qulaified(@NotNull XQtParser.IdExpr_qulaifiedContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#setQualifierClause}.
	 * @param ctx the parse tree
	 */
	void enterSetQualifierClause(@NotNull XQtParser.SetQualifierClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#setQualifierClause}.
	 * @param ctx the parse tree
	 */
	void exitSetQualifierClause(@NotNull XQtParser.SetQualifierClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#joinSpecification}.
	 * @param ctx the parse tree
	 */
	void enterJoinSpecification(@NotNull XQtParser.JoinSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#joinSpecification}.
	 * @param ctx the parse tree
	 */
	void exitJoinSpecification(@NotNull XQtParser.JoinSpecificationContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(@NotNull XQtParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(@NotNull XQtParser.PathContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_add}.
	 * @param ctx the parse tree
	 */
	void enterExpression_add(@NotNull XQtParser.Expression_addContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_add}.
	 * @param ctx the parse tree
	 */
	void exitExpression_add(@NotNull XQtParser.Expression_addContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_idExpr}.
	 * @param ctx the parse tree
	 */
	void enterExpression_idExpr(@NotNull XQtParser.Expression_idExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_idExpr}.
	 * @param ctx the parse tree
	 */
	void exitExpression_idExpr(@NotNull XQtParser.Expression_idExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#connection}.
	 * @param ctx the parse tree
	 */
	void enterConnection(@NotNull XQtParser.ConnectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#connection}.
	 * @param ctx the parse tree
	 */
	void exitConnection(@NotNull XQtParser.ConnectionContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStatement(@NotNull XQtParser.SelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStatement(@NotNull XQtParser.SelectStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_compare}.
	 * @param ctx the parse tree
	 */
	void enterExpression_compare(@NotNull XQtParser.Expression_compareContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_compare}.
	 * @param ctx the parse tree
	 */
	void exitExpression_compare(@NotNull XQtParser.Expression_compareContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#createProcessModel}.
	 * @param ctx the parse tree
	 */
	void enterCreateProcessModel(@NotNull XQtParser.CreateProcessModelContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#createProcessModel}.
	 * @param ctx the parse tree
	 */
	void exitCreateProcessModel(@NotNull XQtParser.CreateProcessModelContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#dataRetrievalStatement}.
	 * @param ctx the parse tree
	 */
	void enterDataRetrievalStatement(@NotNull XQtParser.DataRetrievalStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#dataRetrievalStatement}.
	 * @param ctx the parse tree
	 */
	void exitDataRetrievalStatement(@NotNull XQtParser.DataRetrievalStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_nest}.
	 * @param ctx the parse tree
	 */
	void enterExpression_nest(@NotNull XQtParser.Expression_nestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_nest}.
	 * @param ctx the parse tree
	 */
	void exitExpression_nest(@NotNull XQtParser.Expression_nestContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#sequenceVersionSelector}.
	 * @param ctx the parse tree
	 */
	void enterSequenceVersionSelector(@NotNull XQtParser.SequenceVersionSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sequenceVersionSelector}.
	 * @param ctx the parse tree
	 */
	void exitSequenceVersionSelector(@NotNull XQtParser.SequenceVersionSelectorContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#pathAttribute}.
	 * @param ctx the parse tree
	 */
	void enterPathAttribute(@NotNull XQtParser.PathAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#pathAttribute}.
	 * @param ctx the parse tree
	 */
	void exitPathAttribute(@NotNull XQtParser.PathAttributeContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_function}.
	 * @param ctx the parse tree
	 */
	void enterExpression_function(@NotNull XQtParser.Expression_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_function}.
	 * @param ctx the parse tree
	 */
	void exitExpression_function(@NotNull XQtParser.Expression_functionContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(@NotNull XQtParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(@NotNull XQtParser.DataTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_power}.
	 * @param ctx the parse tree
	 */
	void enterExpression_power(@NotNull XQtParser.Expression_powerContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_power}.
	 * @param ctx the parse tree
	 */
	void exitExpression_power(@NotNull XQtParser.Expression_powerContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupClause(@NotNull XQtParser.GroupClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#groupClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupClause(@NotNull XQtParser.GroupClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#sortKey}.
	 * @param ctx the parse tree
	 */
	void enterSortKey(@NotNull XQtParser.SortKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sortKey}.
	 * @param ctx the parse tree
	 */
	void exitSortKey(@NotNull XQtParser.SortKeyContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#idExpr_simple}.
	 * @param ctx the parse tree
	 */
	void enterIdExpr_simple(@NotNull XQtParser.IdExpr_simpleContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#idExpr_simple}.
	 * @param ctx the parse tree
	 */
	void exitIdExpr_simple(@NotNull XQtParser.IdExpr_simpleContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#simpleIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterSimpleIdentifier(@NotNull XQtParser.SimpleIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#simpleIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitSimpleIdentifier(@NotNull XQtParser.SimpleIdentifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#orderClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderClause(@NotNull XQtParser.OrderClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#orderClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderClause(@NotNull XQtParser.OrderClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#sortOrder}.
	 * @param ctx the parse tree
	 */
	void enterSortOrder(@NotNull XQtParser.SortOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sortOrder}.
	 * @param ctx the parse tree
	 */
	void exitSortOrder(@NotNull XQtParser.SortOrderContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#semanticKey}.
	 * @param ctx the parse tree
	 */
	void enterSemanticKey(@NotNull XQtParser.SemanticKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#semanticKey}.
	 * @param ctx the parse tree
	 */
	void exitSemanticKey(@NotNull XQtParser.SemanticKeyContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#sourceRef}.
	 * @param ctx the parse tree
	 */
	void enterSourceRef(@NotNull XQtParser.SourceRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sourceRef}.
	 * @param ctx the parse tree
	 */
	void exitSourceRef(@NotNull XQtParser.SourceRefContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#smartId}.
	 * @param ctx the parse tree
	 */
	void enterSmartId(@NotNull XQtParser.SmartIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#smartId}.
	 * @param ctx the parse tree
	 */
	void exitSmartId(@NotNull XQtParser.SmartIdContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#joinedSource}.
	 * @param ctx the parse tree
	 */
	void enterJoinedSource(@NotNull XQtParser.JoinedSourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#joinedSource}.
	 * @param ctx the parse tree
	 */
	void exitJoinedSource(@NotNull XQtParser.JoinedSourceContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_smart}.
	 * @param ctx the parse tree
	 */
	void enterExpression_smart(@NotNull XQtParser.Expression_smartContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_smart}.
	 * @param ctx the parse tree
	 */
	void exitExpression_smart(@NotNull XQtParser.Expression_smartContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_not}.
	 * @param ctx the parse tree
	 */
	void enterExpression_not(@NotNull XQtParser.Expression_notContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_not}.
	 * @param ctx the parse tree
	 */
	void exitExpression_not(@NotNull XQtParser.Expression_notContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#sourceSelectionClause}.
	 * @param ctx the parse tree
	 */
	void enterSourceSelectionClause(@NotNull XQtParser.SourceSelectionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sourceSelectionClause}.
	 * @param ctx the parse tree
	 */
	void exitSourceSelectionClause(@NotNull XQtParser.SourceSelectionClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#joinDescription}.
	 * @param ctx the parse tree
	 */
	void enterJoinDescription(@NotNull XQtParser.JoinDescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#joinDescription}.
	 * @param ctx the parse tree
	 */
	void exitJoinDescription(@NotNull XQtParser.JoinDescriptionContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#labelVersionSelector}.
	 * @param ctx the parse tree
	 */
	void enterLabelVersionSelector(@NotNull XQtParser.LabelVersionSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#labelVersionSelector}.
	 * @param ctx the parse tree
	 */
	void exitLabelVersionSelector(@NotNull XQtParser.LabelVersionSelectorContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#attribute_def}.
	 * @param ctx the parse tree
	 */
	void enterAttribute_def(@NotNull XQtParser.Attribute_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#attribute_def}.
	 * @param ctx the parse tree
	 */
	void exitAttribute_def(@NotNull XQtParser.Attribute_defContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 */
	void enterProjectionClause(@NotNull XQtParser.ProjectionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#projectionClause}.
	 * @param ctx the parse tree
	 */
	void exitProjectionClause(@NotNull XQtParser.ProjectionClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#sortSpecification}.
	 * @param ctx the parse tree
	 */
	void enterSortSpecification(@NotNull XQtParser.SortSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#sortSpecification}.
	 * @param ctx the parse tree
	 */
	void exitSortSpecification(@NotNull XQtParser.SortSpecificationContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_mult}.
	 * @param ctx the parse tree
	 */
	void enterExpression_mult(@NotNull XQtParser.Expression_multContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_mult}.
	 * @param ctx the parse tree
	 */
	void exitExpression_mult(@NotNull XQtParser.Expression_multContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#pathEntity}.
	 * @param ctx the parse tree
	 */
	void enterPathEntity(@NotNull XQtParser.PathEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#pathEntity}.
	 * @param ctx the parse tree
	 */
	void exitPathEntity(@NotNull XQtParser.PathEntityContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#nullOrder}.
	 * @param ctx the parse tree
	 */
	void enterNullOrder(@NotNull XQtParser.NullOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#nullOrder}.
	 * @param ctx the parse tree
	 */
	void exitNullOrder(@NotNull XQtParser.NullOrderContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedIdentifier(@NotNull XQtParser.QualifiedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#qualifiedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedIdentifier(@NotNull XQtParser.QualifiedIdentifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull XQtParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull XQtParser.LiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_andOr}.
	 * @param ctx the parse tree
	 */
	void enterExpression_andOr(@NotNull XQtParser.Expression_andOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_andOr}.
	 * @param ctx the parse tree
	 */
	void exitExpression_andOr(@NotNull XQtParser.Expression_andOrContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#targetSelectionClause}.
	 * @param ctx the parse tree
	 */
	void enterTargetSelectionClause(@NotNull XQtParser.TargetSelectionClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#targetSelectionClause}.
	 * @param ctx the parse tree
	 */
	void exitTargetSelectionClause(@NotNull XQtParser.TargetSelectionClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(@NotNull XQtParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(@NotNull XQtParser.StatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#perspective}.
	 * @param ctx the parse tree
	 */
	void enterPerspective(@NotNull XQtParser.PerspectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#perspective}.
	 * @param ctx the parse tree
	 */
	void exitPerspective(@NotNull XQtParser.PerspectiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_negate}.
	 * @param ctx the parse tree
	 */
	void enterExpression_negate(@NotNull XQtParser.Expression_negateContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_negate}.
	 * @param ctx the parse tree
	 */
	void exitExpression_negate(@NotNull XQtParser.Expression_negateContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(@NotNull XQtParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(@NotNull XQtParser.ValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#versionSelector}.
	 * @param ctx the parse tree
	 */
	void enterVersionSelector(@NotNull XQtParser.VersionSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#versionSelector}.
	 * @param ctx the parse tree
	 */
	void exitVersionSelector(@NotNull XQtParser.VersionSelectorContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#anchorClause}.
	 * @param ctx the parse tree
	 */
	void enterAnchorClause(@NotNull XQtParser.AnchorClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#anchorClause}.
	 * @param ctx the parse tree
	 */
	void exitAnchorClause(@NotNull XQtParser.AnchorClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#process}.
	 * @param ctx the parse tree
	 */
	void enterProcess(@NotNull XQtParser.ProcessContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#process}.
	 * @param ctx the parse tree
	 */
	void exitProcess(@NotNull XQtParser.ProcessContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#filterClause}.
	 * @param ctx the parse tree
	 */
	void enterFilterClause(@NotNull XQtParser.FilterClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#filterClause}.
	 * @param ctx the parse tree
	 */
	void exitFilterClause(@NotNull XQtParser.FilterClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#searchPhrase}.
	 * @param ctx the parse tree
	 */
	void enterSearchPhrase(@NotNull XQtParser.SearchPhraseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#searchPhrase}.
	 * @param ctx the parse tree
	 */
	void exitSearchPhrase(@NotNull XQtParser.SearchPhraseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#function_simple}.
	 * @param ctx the parse tree
	 */
	void enterFunction_simple(@NotNull XQtParser.Function_simpleContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#function_simple}.
	 * @param ctx the parse tree
	 */
	void exitFunction_simple(@NotNull XQtParser.Function_simpleContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(@NotNull XQtParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(@NotNull XQtParser.LimitClauseContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#dateVersionSelector}.
	 * @param ctx the parse tree
	 */
	void enterDateVersionSelector(@NotNull XQtParser.DateVersionSelectorContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#dateVersionSelector}.
	 * @param ctx the parse tree
	 */
	void exitDateVersionSelector(@NotNull XQtParser.DateVersionSelectorContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#floatNumber}.
	 * @param ctx the parse tree
	 */
	void enterFloatNumber(@NotNull XQtParser.FloatNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#floatNumber}.
	 * @param ctx the parse tree
	 */
	void exitFloatNumber(@NotNull XQtParser.FloatNumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_aAndOr}.
	 * @param ctx the parse tree
	 */
	void enterExpression_aAndOr(@NotNull XQtParser.Expression_aAndOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_aAndOr}.
	 * @param ctx the parse tree
	 */
	void exitExpression_aAndOr(@NotNull XQtParser.Expression_aAndOrContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#packagedIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterPackagedIdentifier(@NotNull XQtParser.PackagedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#packagedIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitPackagedIdentifier(@NotNull XQtParser.PackagedIdentifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#simpleSource}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSource(@NotNull XQtParser.SimpleSourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#simpleSource}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSource(@NotNull XQtParser.SimpleSourceContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#containerRef}.
	 * @param ctx the parse tree
	 */
	void enterContainerRef(@NotNull XQtParser.ContainerRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#containerRef}.
	 * @param ctx the parse tree
	 */
	void exitContainerRef(@NotNull XQtParser.ContainerRefContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(@NotNull XQtParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(@NotNull XQtParser.VariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#expression_is}.
	 * @param ctx the parse tree
	 */
	void enterExpression_is(@NotNull XQtParser.Expression_isContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#expression_is}.
	 * @param ctx the parse tree
	 */
	void exitExpression_is(@NotNull XQtParser.Expression_isContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#dataStatement}.
	 * @param ctx the parse tree
	 */
	void enterDataStatement(@NotNull XQtParser.DataStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#dataStatement}.
	 * @param ctx the parse tree
	 */
	void exitDataStatement(@NotNull XQtParser.DataStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#intNumber}.
	 * @param ctx the parse tree
	 */
	void enterIntNumber(@NotNull XQtParser.IntNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#intNumber}.
	 * @param ctx the parse tree
	 */
	void exitIntNumber(@NotNull XQtParser.IntNumberContext ctx);

	/**
	 * Enter a parse tree produced by {@link XQtParser#binding_scope_def}.
	 * @param ctx the parse tree
	 */
	void enterBinding_scope_def(@NotNull XQtParser.Binding_scope_defContext ctx);
	/**
	 * Exit a parse tree produced by {@link XQtParser#binding_scope_def}.
	 * @param ctx the parse tree
	 */
	void exitBinding_scope_def(@NotNull XQtParser.Binding_scope_defContext ctx);
}