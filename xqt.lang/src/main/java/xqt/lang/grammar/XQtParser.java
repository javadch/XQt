// Generated from D:\Projects\PhD\Src\XQt\xqt.lang\src\main\java\xqt\lang\grammar\source\XQt.g4 by ANTLR 4.2.2
package xqt.lang.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class XQtParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__9=1, T__8=2, T__7=3, T__6=4, T__5=5, T__4=6, T__3=7, T__2=8, T__1=9, 
		T__0=10, PERSPECTIVE=11, SELECT=12, FROM=13, EXTENDS=14, ATTRIBUTE=15, 
		MapTo=16, REVERSEMAP=17, CONNECTION=18, ADAPTER=19, SOURCE_URI=20, PARAMETERS=21, 
		BIND=22, SCOPE=23, VERSION=24, LATEST=25, DISTINCT=26, UNION=27, ON=28, 
		JOIN=29, INTO=30, ANCHOR=31, START=32, STOP=33, WHERE=34, ORDERBY=35, 
		ASC=36, DESC=37, NULL=38, FIRST=39, LAST=40, LIMIT=41, SKIP=42, TAKE=43, 
		GROUPBY=44, IS=45, NoT=46, NUMBER=47, DatE=48, EMPTY=49, PLOT=50, PLOTTYPE=51, 
		HAXIS=52, VAXIS=53, PLOT_H_LABEL=54, PLOT_V_LABEL=55, PLOT_LABEL=56, Boolean=57, 
		String=58, Byte=59, Integer=60, Long=61, Real=62, Date=63, EQ=64, NotEQ=65, 
		GT=66, GTEQ=67, LT=68, LTEQ=69, LIKE=70, PLUS=71, MINUS=72, NEGATE=73, 
		MULT=74, DIV=75, MOD=76, POW=77, NOT=78, ASN=79, AOR=80, AAND=81, COMMA=82, 
		UNDERSCORE=83, LPAR=84, RPAR=85, DOT=86, LCUR=87, RCUR=88, DCOLON=89, 
		COLON=90, OR=91, AND=92, RELATIONSHIP=93, RELATION_FW=94, RELATION_RW=95, 
		RELATION_UN=96, RELATION_BI=97, CHAR=98, STRING=99, LINE_COMMENT=100, 
		COMMENT=101, NEWLINE=102, WS=103, ID=104, BOOLEAN=105, DATE=106, UINT=107, 
		INT=108, FLOAT=109;
	public static final String[] tokenNames = {
		"<INVALID>", "'after'", "'p'", "'s'", "'exact'", "'before'", "'pie'", 
		"'b'", "'#'", "'$'", "'l'", "PERSPECTIVE", "SELECT", "FROM", "EXTENDS", 
		"ATTRIBUTE", "MapTo", "REVERSEMAP", "CONNECTION", "ADAPTER", "SOURCE_URI", 
		"PARAMETERS", "BIND", "SCOPE", "VERSION", "LATEST", "DISTINCT", "UNION", 
		"ON", "JOIN", "INTO", "ANCHOR", "START", "STOP", "WHERE", "ORDERBY", "ASC", 
		"DESC", "NULL", "FIRST", "LAST", "LIMIT", "SKIP", "TAKE", "GROUPBY", "IS", 
		"NoT", "NUMBER", "DatE", "EMPTY", "PLOT", "PLOTTYPE", "HAXIS", "VAXIS", 
		"PLOT_H_LABEL", "PLOT_V_LABEL", "PLOT_LABEL", "Boolean", "String", "Byte", 
		"Integer", "Long", "Real", "Date", "'=='", "'<>'", "'>'", "'>='", "'<'", 
		"'<='", "'~'", "'+'", "'-'", "NEGATE", "'*'", "'/'", "'%'", "'^'", "'!'", 
		"'='", "'|'", "'&'", "','", "'_'", "'('", "')'", "'.'", "'{'", "'}'", 
		"'::'", "':'", "OR", "AND", "RELATIONSHIP", "'->'", "'<-'", "'--'", "'<->'", 
		"CHAR", "STRING", "LINE_COMMENT", "COMMENT", "NEWLINE", "WS", "ID", "BOOLEAN", 
		"DATE", "UINT", "INT", "FLOAT"
	};
	public static final int
		RULE_createProcessModel = 0, RULE_process = 1, RULE_perspective = 2, RULE_attribute_def = 3, 
		RULE_connection = 4, RULE_parameter_def = 5, RULE_binding = 6, RULE_binding_scope_def = 7, 
		RULE_statement = 8, RULE_dataStatement = 9, RULE_dataRetrievalStatement = 10, 
		RULE_selectStatement = 11, RULE_setQualifierClause = 12, RULE_projectionClause = 13, 
		RULE_sourceSelectionClause = 14, RULE_sourceRef = 15, RULE_joinedSource = 16, 
		RULE_joinDescription = 17, RULE_joinSpecification = 18, RULE_simpleSource = 19, 
		RULE_bindingRef = 20, RULE_containerRef = 21, RULE_targetSelectionClause = 22, 
		RULE_plot = 23, RULE_anchorClause = 24, RULE_filterClause = 25, RULE_orderClause = 26, 
		RULE_sortSpecification = 27, RULE_sortKey = 28, RULE_sortOrder = 29, RULE_nullOrder = 30, 
		RULE_limitClause = 31, RULE_groupClause = 32, RULE_variable = 33, RULE_searchPhrase = 34, 
		RULE_intNumber = 35, RULE_floatNumber = 36, RULE_expression = 37, RULE_function = 38, 
		RULE_argument = 39, RULE_packagedIdentifier = 40, RULE_smartId = 41, RULE_dataType = 42, 
		RULE_semanticKey = 43, RULE_value = 44, RULE_idExpr = 45, RULE_versionSelector = 46, 
		RULE_labelVersionSelector = 47, RULE_sequenceVersionSelector = 48, RULE_dateVersionSelector = 49, 
		RULE_simpleIdentifier = 50, RULE_qualifiedIdentifier = 51, RULE_literal = 52, 
		RULE_path = 53, RULE_pathEntity = 54, RULE_pathAttribute = 55;
	public static final String[] ruleNames = {
		"createProcessModel", "process", "perspective", "attribute_def", "connection", 
		"parameter_def", "binding", "binding_scope_def", "statement", "dataStatement", 
		"dataRetrievalStatement", "selectStatement", "setQualifierClause", "projectionClause", 
		"sourceSelectionClause", "sourceRef", "joinedSource", "joinDescription", 
		"joinSpecification", "simpleSource", "bindingRef", "containerRef", "targetSelectionClause", 
		"plot", "anchorClause", "filterClause", "orderClause", "sortSpecification", 
		"sortKey", "sortOrder", "nullOrder", "limitClause", "groupClause", "variable", 
		"searchPhrase", "intNumber", "floatNumber", "expression", "function", 
		"argument", "packagedIdentifier", "smartId", "dataType", "semanticKey", 
		"value", "idExpr", "versionSelector", "labelVersionSelector", "sequenceVersionSelector", 
		"dateVersionSelector", "simpleIdentifier", "qualifiedIdentifier", "literal", 
		"path", "pathEntity", "pathAttribute"
	};

	@Override
	public String getGrammarFileName() { return "XQt.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public XQtParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class CreateProcessModelContext extends ParserRuleContext {
		public ProcessContext process() {
			return getRuleContext(ProcessContext.class,0);
		}
		public CreateProcessModelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createProcessModel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterCreateProcessModel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitCreateProcessModel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitCreateProcessModel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateProcessModelContext createProcessModel() throws RecognitionException {
		CreateProcessModelContext _localctx = new CreateProcessModelContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_createProcessModel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112); process();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProcessContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(XQtParser.EOF, 0); }
		public List<PerspectiveContext> perspective() {
			return getRuleContexts(PerspectiveContext.class);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(XQtParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(XQtParser.NEWLINE, i);
		}
		public List<ConnectionContext> connection() {
			return getRuleContexts(ConnectionContext.class);
		}
		public ConnectionContext connection(int i) {
			return getRuleContext(ConnectionContext.class,i);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<BindingContext> binding() {
			return getRuleContexts(BindingContext.class);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public BindingContext binding(int i) {
			return getRuleContext(BindingContext.class,i);
		}
		public PerspectiveContext perspective(int i) {
			return getRuleContext(PerspectiveContext.class,i);
		}
		public ProcessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_process; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterProcess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitProcess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitProcess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProcessContext process() throws RecognitionException {
		ProcessContext _localctx = new ProcessContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_process);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PERSPECTIVE) {
				{
				{
				setState(114); perspective();
				}
				}
				setState(119);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CONNECTION) {
				{
				{
				setState(120); connection();
				}
				}
				setState(125);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(129);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BIND) {
				{
				{
				setState(126); binding();
				}
				}
				setState(131);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(139); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(132); statement();
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE) {
					{
					{
					setState(133); match(NEWLINE);
					}
					}
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				}
				setState(141); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==SELECT );
			setState(143); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PerspectiveContext extends ParserRuleContext {
		public Token name;
		public Token superPerspective;
		public List<TerminalNode> ID() { return getTokens(XQtParser.ID); }
		public TerminalNode PERSPECTIVE() { return getToken(XQtParser.PERSPECTIVE, 0); }
		public TerminalNode RCUR() { return getToken(XQtParser.RCUR, 0); }
		public TerminalNode ID(int i) {
			return getToken(XQtParser.ID, i);
		}
		public List<Attribute_defContext> attribute_def() {
			return getRuleContexts(Attribute_defContext.class);
		}
		public Attribute_defContext attribute_def(int i) {
			return getRuleContext(Attribute_defContext.class,i);
		}
		public TerminalNode EXTENDS() { return getToken(XQtParser.EXTENDS, 0); }
		public TerminalNode LCUR() { return getToken(XQtParser.LCUR, 0); }
		public PerspectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_perspective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterPerspective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitPerspective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitPerspective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PerspectiveContext perspective() throws RecognitionException {
		PerspectiveContext _localctx = new PerspectiveContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_perspective);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145); match(PERSPECTIVE);
			setState(146); ((PerspectiveContext)_localctx).name = match(ID);
			setState(149);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(147); match(EXTENDS);
				setState(148); ((PerspectiveContext)_localctx).superPerspective = match(ID);
				}
			}

			setState(151); match(LCUR);
			setState(153); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(152); attribute_def();
				}
				}
				setState(155); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ATTRIBUTE );
			setState(157); match(RCUR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Attribute_defContext extends ParserRuleContext {
		public ExpressionContext fwd;
		public ExpressionContext rvs;
		public TerminalNode MapTo() { return getToken(XQtParser.MapTo, 0); }
		public TerminalNode ASN(int i) {
			return getToken(XQtParser.ASN, i);
		}
		public SmartIdContext smartId() {
			return getRuleContext(SmartIdContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(XQtParser.COMMA, 0); }
		public List<TerminalNode> ASN() { return getTokens(XQtParser.ASN); }
		public TerminalNode ATTRIBUTE() { return getToken(XQtParser.ATTRIBUTE, 0); }
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode REVERSEMAP() { return getToken(XQtParser.REVERSEMAP, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public Attribute_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterAttribute_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitAttribute_def(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitAttribute_def(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Attribute_defContext attribute_def() throws RecognitionException {
		Attribute_defContext _localctx = new Attribute_defContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_attribute_def);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159); match(ATTRIBUTE);
			setState(160); smartId();
			setState(164);
			_la = _input.LA(1);
			if (_la==MapTo) {
				{
				setState(161); match(MapTo);
				setState(162); match(ASN);
				setState(163); ((Attribute_defContext)_localctx).fwd = expression(0);
				}
			}

			setState(169);
			_la = _input.LA(1);
			if (_la==REVERSEMAP) {
				{
				setState(166); match(REVERSEMAP);
				setState(167); match(ASN);
				setState(168); ((Attribute_defContext)_localctx).rvs = expression(0);
				}
			}

			setState(171); match(COMMA);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConnectionContext extends ParserRuleContext {
		public Token name;
		public Token adapterName;
		public Token srcURI;
		public TerminalNode ASN(int i) {
			return getToken(XQtParser.ASN, i);
		}
		public TerminalNode ADAPTER() { return getToken(XQtParser.ADAPTER, 0); }
		public List<Parameter_defContext> parameter_def() {
			return getRuleContexts(Parameter_defContext.class);
		}
		public TerminalNode CONNECTION() { return getToken(XQtParser.CONNECTION, 0); }
		public List<TerminalNode> ASN() { return getTokens(XQtParser.ASN); }
		public TerminalNode ID(int i) {
			return getToken(XQtParser.ID, i);
		}
		public TerminalNode STRING() { return getToken(XQtParser.STRING, 0); }
		public TerminalNode COMMA(int i) {
			return getToken(XQtParser.COMMA, i);
		}
		public List<TerminalNode> ID() { return getTokens(XQtParser.ID); }
		public Parameter_defContext parameter_def(int i) {
			return getRuleContext(Parameter_defContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQtParser.COMMA); }
		public TerminalNode PARAMETERS() { return getToken(XQtParser.PARAMETERS, 0); }
		public TerminalNode SOURCE_URI() { return getToken(XQtParser.SOURCE_URI, 0); }
		public ConnectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_connection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterConnection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitConnection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitConnection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConnectionContext connection() throws RecognitionException {
		ConnectionContext _localctx = new ConnectionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_connection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173); match(CONNECTION);
			setState(174); ((ConnectionContext)_localctx).name = match(ID);
			setState(175); match(ADAPTER);
			setState(176); match(ASN);
			setState(177); ((ConnectionContext)_localctx).adapterName = match(ID);
			setState(178); match(SOURCE_URI);
			setState(179); match(ASN);
			setState(180); ((ConnectionContext)_localctx).srcURI = match(STRING);
			setState(191);
			_la = _input.LA(1);
			if (_la==PARAMETERS) {
				{
				setState(181); match(PARAMETERS);
				setState(182); match(ASN);
				{
				setState(183); parameter_def();
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(184); match(COMMA);
					setState(185); parameter_def();
					}
					}
					setState(190);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Parameter_defContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Parameter_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterParameter_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitParameter_def(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitParameter_def(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Parameter_defContext parameter_def() throws RecognitionException {
		Parameter_defContext _localctx = new Parameter_defContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parameter_def);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193); match(ID);
			setState(194); match(COLON);
			setState(195); expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BindingContext extends ParserRuleContext {
		public Token name;
		public Token connectionName;
		public TerminalNode ASN(int i) {
			return getToken(XQtParser.ASN, i);
		}
		public TerminalNode VERSION() { return getToken(XQtParser.VERSION, 0); }
		public TerminalNode CONNECTION() { return getToken(XQtParser.CONNECTION, 0); }
		public List<TerminalNode> ASN() { return getTokens(XQtParser.ASN); }
		public Binding_scope_defContext binding_scope_def(int i) {
			return getRuleContext(Binding_scope_defContext.class,i);
		}
		public TerminalNode BIND() { return getToken(XQtParser.BIND, 0); }
		public TerminalNode ID(int i) {
			return getToken(XQtParser.ID, i);
		}
		public VersionSelectorContext versionSelector() {
			return getRuleContext(VersionSelectorContext.class,0);
		}
		public TerminalNode COMMA(int i) {
			return getToken(XQtParser.COMMA, i);
		}
		public List<TerminalNode> ID() { return getTokens(XQtParser.ID); }
		public List<Binding_scope_defContext> binding_scope_def() {
			return getRuleContexts(Binding_scope_defContext.class);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQtParser.COMMA); }
		public TerminalNode SCOPE() { return getToken(XQtParser.SCOPE, 0); }
		public BindingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binding; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterBinding(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitBinding(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BindingContext binding() throws RecognitionException {
		BindingContext _localctx = new BindingContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_binding);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197); match(BIND);
			setState(198); ((BindingContext)_localctx).name = match(ID);
			setState(199); match(CONNECTION);
			setState(200); match(ASN);
			setState(201); ((BindingContext)_localctx).connectionName = match(ID);
			setState(212);
			_la = _input.LA(1);
			if (_la==SCOPE) {
				{
				setState(202); match(SCOPE);
				setState(203); match(ASN);
				setState(204); binding_scope_def();
				setState(209);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(205); match(COMMA);
					setState(206); binding_scope_def();
					}
					}
					setState(211);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(217);
			_la = _input.LA(1);
			if (_la==VERSION) {
				{
				setState(214); match(VERSION);
				setState(215); match(ASN);
				setState(216); versionSelector();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Binding_scope_defContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public Binding_scope_defContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binding_scope_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterBinding_scope_def(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitBinding_scope_def(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitBinding_scope_def(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Binding_scope_defContext binding_scope_def() throws RecognitionException {
		Binding_scope_defContext _localctx = new Binding_scope_defContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_binding_scope_def);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public DataStatementContext dataStatement() {
			return getRuleContext(DataStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221); dataStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataStatementContext extends ParserRuleContext {
		public DataRetrievalStatementContext dataRetrievalStatement() {
			return getRuleContext(DataRetrievalStatementContext.class,0);
		}
		public DataStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterDataStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitDataStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitDataStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataStatementContext dataStatement() throws RecognitionException {
		DataStatementContext _localctx = new DataStatementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_dataStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223); dataRetrievalStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataRetrievalStatementContext extends ParserRuleContext {
		public SelectStatementContext selectStatement() {
			return getRuleContext(SelectStatementContext.class,0);
		}
		public DataRetrievalStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataRetrievalStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterDataRetrievalStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitDataRetrievalStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitDataRetrievalStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataRetrievalStatementContext dataRetrievalStatement() throws RecognitionException {
		DataRetrievalStatementContext _localctx = new DataRetrievalStatementContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_dataRetrievalStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225); selectStatement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectStatementContext extends ParserRuleContext {
		public SourceSelectionClauseContext sourceSelectionClause() {
			return getRuleContext(SourceSelectionClauseContext.class,0);
		}
		public GroupClauseContext groupClause() {
			return getRuleContext(GroupClauseContext.class,0);
		}
		public TargetSelectionClauseContext targetSelectionClause() {
			return getRuleContext(TargetSelectionClauseContext.class,0);
		}
		public LimitClauseContext limitClause() {
			return getRuleContext(LimitClauseContext.class,0);
		}
		public OrderClauseContext orderClause() {
			return getRuleContext(OrderClauseContext.class,0);
		}
		public TerminalNode SELECT() { return getToken(XQtParser.SELECT, 0); }
		public AnchorClauseContext anchorClause() {
			return getRuleContext(AnchorClauseContext.class,0);
		}
		public ProjectionClauseContext projectionClause() {
			return getRuleContext(ProjectionClauseContext.class,0);
		}
		public SetQualifierClauseContext setQualifierClause() {
			return getRuleContext(SetQualifierClauseContext.class,0);
		}
		public FilterClauseContext filterClause() {
			return getRuleContext(FilterClauseContext.class,0);
		}
		public SelectStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSelectStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSelectStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSelectStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectStatementContext selectStatement() throws RecognitionException {
		SelectStatementContext _localctx = new SelectStatementContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_selectStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227); match(SELECT);
			setState(229);
			_la = _input.LA(1);
			if (_la==DISTINCT || _la==UNION) {
				{
				setState(228); setQualifierClause();
				}
			}

			setState(232);
			_la = _input.LA(1);
			if (_la==PERSPECTIVE) {
				{
				setState(231); projectionClause();
				}
			}

			setState(234); sourceSelectionClause();
			setState(236);
			_la = _input.LA(1);
			if (_la==INTO) {
				{
				setState(235); targetSelectionClause();
				}
			}

			setState(239);
			_la = _input.LA(1);
			if (_la==ANCHOR) {
				{
				setState(238); anchorClause();
				}
			}

			setState(242);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(241); filterClause();
				}
			}

			setState(245);
			_la = _input.LA(1);
			if (_la==ORDERBY) {
				{
				setState(244); orderClause();
				}
			}

			setState(248);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(247); limitClause();
				}
			}

			setState(251);
			_la = _input.LA(1);
			if (_la==GROUPBY) {
				{
				setState(250); groupClause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetQualifierClauseContext extends ParserRuleContext {
		public TerminalNode UNION() { return getToken(XQtParser.UNION, 0); }
		public TerminalNode DISTINCT() { return getToken(XQtParser.DISTINCT, 0); }
		public SetQualifierClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setQualifierClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSetQualifierClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSetQualifierClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSetQualifierClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetQualifierClauseContext setQualifierClause() throws RecognitionException {
		SetQualifierClauseContext _localctx = new SetQualifierClauseContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_setQualifierClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			_la = _input.LA(1);
			if ( !(_la==DISTINCT || _la==UNION) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProjectionClauseContext extends ParserRuleContext {
		public SimpleIdentifierContext perspectiveName;
		public TerminalNode PERSPECTIVE() { return getToken(XQtParser.PERSPECTIVE, 0); }
		public SimpleIdentifierContext simpleIdentifier() {
			return getRuleContext(SimpleIdentifierContext.class,0);
		}
		public ProjectionClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_projectionClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterProjectionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitProjectionClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitProjectionClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProjectionClauseContext projectionClause() throws RecognitionException {
		ProjectionClauseContext _localctx = new ProjectionClauseContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_projectionClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255); match(PERSPECTIVE);
			setState(256); ((ProjectionClauseContext)_localctx).perspectiveName = simpleIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SourceSelectionClauseContext extends ParserRuleContext {
		public SourceRefContext sourceRef() {
			return getRuleContext(SourceRefContext.class,0);
		}
		public TerminalNode FROM() { return getToken(XQtParser.FROM, 0); }
		public SourceSelectionClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceSelectionClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSourceSelectionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSourceSelectionClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSourceSelectionClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceSelectionClauseContext sourceSelectionClause() throws RecognitionException {
		SourceSelectionClauseContext _localctx = new SourceSelectionClauseContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_sourceSelectionClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258); match(FROM);
			setState(259); sourceRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SourceRefContext extends ParserRuleContext {
		public SimpleSourceContext simpleSource() {
			return getRuleContext(SimpleSourceContext.class,0);
		}
		public JoinedSourceContext joinedSource() {
			return getRuleContext(JoinedSourceContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public SourceRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sourceRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSourceRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSourceRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSourceRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SourceRefContext sourceRef() throws RecognitionException {
		SourceRefContext _localctx = new SourceRefContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_sourceRef);
		try {
			setState(264);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(261); joinedSource();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(262); simpleSource();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(263); variable();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JoinedSourceContext extends ParserRuleContext {
		public List<SimpleSourceContext> simpleSource() {
			return getRuleContexts(SimpleSourceContext.class);
		}
		public TerminalNode ON() { return getToken(XQtParser.ON, 0); }
		public JoinDescriptionContext joinDescription() {
			return getRuleContext(JoinDescriptionContext.class,0);
		}
		public SimpleSourceContext simpleSource(int i) {
			return getRuleContext(SimpleSourceContext.class,i);
		}
		public JoinSpecificationContext joinSpecification() {
			return getRuleContext(JoinSpecificationContext.class,0);
		}
		public JoinedSourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joinedSource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterJoinedSource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitJoinedSource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitJoinedSource(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinedSourceContext joinedSource() throws RecognitionException {
		JoinedSourceContext _localctx = new JoinedSourceContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_joinedSource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266); simpleSource();
			setState(267); joinDescription();
			setState(268); simpleSource();
			setState(269); match(ON);
			setState(270); joinSpecification();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JoinDescriptionContext extends ParserRuleContext {
		public TerminalNode JOIN() { return getToken(XQtParser.JOIN, 0); }
		public JoinDescriptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joinDescription; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterJoinDescription(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitJoinDescription(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitJoinDescription(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinDescriptionContext joinDescription() throws RecognitionException {
		JoinDescriptionContext _localctx = new JoinDescriptionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_joinDescription);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(272); match(JOIN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JoinSpecificationContext extends ParserRuleContext {
		public Token op;
		public List<TerminalNode> ID() { return getTokens(XQtParser.ID); }
		public TerminalNode NotEQ() { return getToken(XQtParser.NotEQ, 0); }
		public TerminalNode LTEQ() { return getToken(XQtParser.LTEQ, 0); }
		public TerminalNode LT() { return getToken(XQtParser.LT, 0); }
		public TerminalNode GT() { return getToken(XQtParser.GT, 0); }
		public TerminalNode ID(int i) {
			return getToken(XQtParser.ID, i);
		}
		public TerminalNode EQ() { return getToken(XQtParser.EQ, 0); }
		public TerminalNode LIKE() { return getToken(XQtParser.LIKE, 0); }
		public TerminalNode GTEQ() { return getToken(XQtParser.GTEQ, 0); }
		public JoinSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joinSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterJoinSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitJoinSpecification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitJoinSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinSpecificationContext joinSpecification() throws RecognitionException {
		JoinSpecificationContext _localctx = new JoinSpecificationContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_joinSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274); match(ID);
			setState(275);
			((JoinSpecificationContext)_localctx).op = _input.LT(1);
			_la = _input.LA(1);
			if ( !(((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EQ - 64)) | (1L << (NotEQ - 64)) | (1L << (GT - 64)) | (1L << (GTEQ - 64)) | (1L << (LT - 64)) | (1L << (LTEQ - 64)) | (1L << (LIKE - 64)))) != 0)) ) {
				((JoinSpecificationContext)_localctx).op = (Token)_errHandler.recoverInline(this);
			}
			consume();
			setState(276); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleSourceContext extends ParserRuleContext {
		public ContainerRefContext containerRef() {
			return getRuleContext(ContainerRefContext.class,0);
		}
		public TerminalNode DOT() { return getToken(XQtParser.DOT, 0); }
		public BindingRefContext bindingRef() {
			return getRuleContext(BindingRefContext.class,0);
		}
		public SimpleSourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleSource; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSimpleSource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSimpleSource(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSimpleSource(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleSourceContext simpleSource() throws RecognitionException {
		SimpleSourceContext _localctx = new SimpleSourceContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_simpleSource);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(278); bindingRef();
			setState(279); match(DOT);
			setState(280); containerRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BindingRefContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public BindingRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bindingRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterBindingRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitBindingRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitBindingRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BindingRefContext bindingRef() throws RecognitionException {
		BindingRefContext _localctx = new BindingRefContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_bindingRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ContainerRefContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public TerminalNode UINT() { return getToken(XQtParser.UINT, 0); }
		public ContainerRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_containerRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterContainerRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitContainerRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitContainerRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContainerRefContext containerRef() throws RecognitionException {
		ContainerRefContext _localctx = new ContainerRefContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_containerRef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			_la = _input.LA(1);
			if ( !(_la==ID || _la==UINT) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TargetSelectionClauseContext extends ParserRuleContext {
		public PlotContext plot() {
			return getRuleContext(PlotContext.class,0);
		}
		public SimpleSourceContext simpleSource() {
			return getRuleContext(SimpleSourceContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode INTO() { return getToken(XQtParser.INTO, 0); }
		public TargetSelectionClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_targetSelectionClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterTargetSelectionClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitTargetSelectionClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitTargetSelectionClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TargetSelectionClauseContext targetSelectionClause() throws RecognitionException {
		TargetSelectionClauseContext _localctx = new TargetSelectionClauseContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_targetSelectionClause);
		try {
			setState(292);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(286); match(INTO);
				setState(287); plot();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(288); match(INTO);
				setState(289); variable();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(290); match(INTO);
				setState(291); simpleSource();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PlotContext extends ParserRuleContext {
		public VariableContext id;
		public VariableContext hx;
		public VariableContext vx;
		public Token plotType;
		public Token pxl;
		public Token pvl;
		public Token pll;
		public List<TerminalNode> STRING() { return getTokens(XQtParser.STRING); }
		public TerminalNode PLOT_V_LABEL() { return getToken(XQtParser.PLOT_V_LABEL, 0); }
		public TerminalNode COMMA(int i) {
			return getToken(XQtParser.COMMA, i);
		}
		public TerminalNode HAXIS() { return getToken(XQtParser.HAXIS, 0); }
		public TerminalNode STRING(int i) {
			return getToken(XQtParser.STRING, i);
		}
		public TerminalNode PLOTTYPE() { return getToken(XQtParser.PLOTTYPE, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQtParser.COMMA); }
		public TerminalNode PLOT_LABEL() { return getToken(XQtParser.PLOT_LABEL, 0); }
		public TerminalNode VAXIS() { return getToken(XQtParser.VAXIS, 0); }
		public TerminalNode PLOT() { return getToken(XQtParser.PLOT, 0); }
		public VariableContext variable(int i) {
			return getRuleContext(VariableContext.class,i);
		}
		public TerminalNode PLOT_H_LABEL() { return getToken(XQtParser.PLOT_H_LABEL, 0); }
		public List<VariableContext> variable() {
			return getRuleContexts(VariableContext.class);
		}
		public PlotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterPlot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitPlot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitPlot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PlotContext plot() throws RecognitionException {
		PlotContext _localctx = new PlotContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_plot);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			_la = _input.LA(1);
			if (_la==PLOT) {
				{
				setState(294); match(PLOT);
				}
			}

			setState(297); ((PlotContext)_localctx).id = variable();
			{
			setState(300);
			_la = _input.LA(1);
			if (_la==HAXIS) {
				{
				setState(298); match(HAXIS);
				setState(299); match(COLON);
				}
			}

			setState(302); ((PlotContext)_localctx).hx = variable();
			}
			{
			setState(306);
			_la = _input.LA(1);
			if (_la==VAXIS) {
				{
				setState(304); match(VAXIS);
				setState(305); match(COLON);
				}
			}

			setState(308); ((PlotContext)_localctx).vx = variable();
			setState(313);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(309); match(COMMA);
				setState(310); variable();
				}
				}
				setState(315);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(321);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 2) | (1L << 3) | (1L << 6) | (1L << 7) | (1L << 10) | (1L << PLOTTYPE))) != 0)) {
				{
				setState(318);
				_la = _input.LA(1);
				if (_la==PLOTTYPE) {
					{
					setState(316); match(PLOTTYPE);
					setState(317); match(COLON);
					}
				}

				setState(320);
				((PlotContext)_localctx).plotType = _input.LT(1);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 2) | (1L << 3) | (1L << 6) | (1L << 7) | (1L << 10))) != 0)) ) {
					((PlotContext)_localctx).plotType = (Token)_errHandler.recoverInline(this);
				}
				consume();
				}
			}

			setState(328);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(325);
				_la = _input.LA(1);
				if (_la==PLOT_H_LABEL) {
					{
					setState(323); match(PLOT_H_LABEL);
					setState(324); match(COLON);
					}
				}

				setState(327); ((PlotContext)_localctx).pxl = match(STRING);
				}
				break;
			}
			setState(335);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(332);
				_la = _input.LA(1);
				if (_la==PLOT_V_LABEL) {
					{
					setState(330); match(PLOT_V_LABEL);
					setState(331); match(COLON);
					}
				}

				setState(334); ((PlotContext)_localctx).pvl = match(STRING);
				}
				break;
			}
			setState(342);
			_la = _input.LA(1);
			if (_la==PLOT_LABEL || _la==STRING) {
				{
				setState(339);
				_la = _input.LA(1);
				if (_la==PLOT_LABEL) {
					{
					setState(337); match(PLOT_LABEL);
					setState(338); match(COLON);
					}
				}

				setState(341); ((PlotContext)_localctx).pll = match(STRING);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnchorClauseContext extends ParserRuleContext {
		public SearchPhraseContext startAnchor;
		public SearchPhraseContext stopAnchor;
		public TerminalNode STOP() { return getToken(XQtParser.STOP, 0); }
		public TerminalNode START() { return getToken(XQtParser.START, 0); }
		public List<SearchPhraseContext> searchPhrase() {
			return getRuleContexts(SearchPhraseContext.class);
		}
		public TerminalNode ANCHOR() { return getToken(XQtParser.ANCHOR, 0); }
		public SearchPhraseContext searchPhrase(int i) {
			return getRuleContext(SearchPhraseContext.class,i);
		}
		public AnchorClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anchorClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterAnchorClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitAnchorClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitAnchorClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnchorClauseContext anchorClause() throws RecognitionException {
		AnchorClauseContext _localctx = new AnchorClauseContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_anchorClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344); match(ANCHOR);
			setState(347);
			_la = _input.LA(1);
			if (_la==START) {
				{
				setState(345); match(START);
				setState(346); ((AnchorClauseContext)_localctx).startAnchor = searchPhrase();
				}
			}

			setState(351);
			_la = _input.LA(1);
			if (_la==STOP) {
				{
				setState(349); match(STOP);
				setState(350); ((AnchorClauseContext)_localctx).stopAnchor = searchPhrase();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterClauseContext extends ParserRuleContext {
		public TerminalNode WHERE() { return getToken(XQtParser.WHERE, 0); }
		public SearchPhraseContext searchPhrase() {
			return getRuleContext(SearchPhraseContext.class,0);
		}
		public TerminalNode LPAR() { return getToken(XQtParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(XQtParser.RPAR, 0); }
		public FilterClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filterClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterFilterClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitFilterClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitFilterClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterClauseContext filterClause() throws RecognitionException {
		FilterClauseContext _localctx = new FilterClauseContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_filterClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353); match(WHERE);
			setState(354); match(LPAR);
			setState(355); searchPhrase();
			setState(356); match(RPAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrderClauseContext extends ParserRuleContext {
		public TerminalNode ORDERBY() { return getToken(XQtParser.ORDERBY, 0); }
		public List<TerminalNode> COMMA() { return getTokens(XQtParser.COMMA); }
		public SortSpecificationContext sortSpecification(int i) {
			return getRuleContext(SortSpecificationContext.class,i);
		}
		public List<SortSpecificationContext> sortSpecification() {
			return getRuleContexts(SortSpecificationContext.class);
		}
		public TerminalNode COMMA(int i) {
			return getToken(XQtParser.COMMA, i);
		}
		public OrderClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterOrderClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitOrderClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitOrderClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderClauseContext orderClause() throws RecognitionException {
		OrderClauseContext _localctx = new OrderClauseContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_orderClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(358); match(ORDERBY);
			setState(359); sortSpecification();
			setState(364);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(360); match(COMMA);
				setState(361); sortSpecification();
				}
				}
				setState(366);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SortSpecificationContext extends ParserRuleContext {
		public SortOrderContext sortOrder() {
			return getRuleContext(SortOrderContext.class,0);
		}
		public NullOrderContext nullOrder() {
			return getRuleContext(NullOrderContext.class,0);
		}
		public SortKeyContext sortKey() {
			return getRuleContext(SortKeyContext.class,0);
		}
		public SortSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSortSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSortSpecification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSortSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortSpecificationContext sortSpecification() throws RecognitionException {
		SortSpecificationContext _localctx = new SortSpecificationContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_sortSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367); sortKey();
			setState(369);
			_la = _input.LA(1);
			if (_la==ASC || _la==DESC) {
				{
				setState(368); sortOrder();
				}
			}

			setState(372);
			_la = _input.LA(1);
			if (_la==NULL) {
				{
				setState(371); nullOrder();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SortKeyContext extends ParserRuleContext {
		public SimpleIdentifierContext simpleIdentifier() {
			return getRuleContext(SimpleIdentifierContext.class,0);
		}
		public SortKeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortKey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSortKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSortKey(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSortKey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortKeyContext sortKey() throws RecognitionException {
		SortKeyContext _localctx = new SortKeyContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_sortKey);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(374); simpleIdentifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SortOrderContext extends ParserRuleContext {
		public TerminalNode ASC() { return getToken(XQtParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(XQtParser.DESC, 0); }
		public SortOrderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sortOrder; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSortOrder(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSortOrder(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSortOrder(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SortOrderContext sortOrder() throws RecognitionException {
		SortOrderContext _localctx = new SortOrderContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_sortOrder);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			_la = _input.LA(1);
			if ( !(_la==ASC || _la==DESC) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NullOrderContext extends ParserRuleContext {
		public TerminalNode NULL() { return getToken(XQtParser.NULL, 0); }
		public TerminalNode FIRST() { return getToken(XQtParser.FIRST, 0); }
		public TerminalNode LAST() { return getToken(XQtParser.LAST, 0); }
		public NullOrderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nullOrder; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterNullOrder(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitNullOrder(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitNullOrder(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NullOrderContext nullOrder() throws RecognitionException {
		NullOrderContext _localctx = new NullOrderContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_nullOrder);
		try {
			setState(382);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(378); match(NULL);
				setState(379); match(FIRST);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(380); match(NULL);
				setState(381); match(LAST);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LimitClauseContext extends ParserRuleContext {
		public Token skip;
		public Token take;
		public TerminalNode TAKE() { return getToken(XQtParser.TAKE, 0); }
		public TerminalNode LIMIT() { return getToken(XQtParser.LIMIT, 0); }
		public List<TerminalNode> UINT() { return getTokens(XQtParser.UINT); }
		public TerminalNode SKIP() { return getToken(XQtParser.SKIP, 0); }
		public TerminalNode UINT(int i) {
			return getToken(XQtParser.UINT, i);
		}
		public LimitClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limitClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterLimitClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitLimitClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitLimitClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LimitClauseContext limitClause() throws RecognitionException {
		LimitClauseContext _localctx = new LimitClauseContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_limitClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384); match(LIMIT);
			setState(387);
			_la = _input.LA(1);
			if (_la==SKIP) {
				{
				setState(385); match(SKIP);
				setState(386); ((LimitClauseContext)_localctx).skip = match(UINT);
				}
			}

			setState(391);
			_la = _input.LA(1);
			if (_la==TAKE) {
				{
				setState(389); match(TAKE);
				setState(390); ((LimitClauseContext)_localctx).take = match(UINT);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupClauseContext extends ParserRuleContext {
		public TerminalNode GROUPBY() { return getToken(XQtParser.GROUPBY, 0); }
		public SimpleIdentifierContext simpleIdentifier(int i) {
			return getRuleContext(SimpleIdentifierContext.class,i);
		}
		public List<SimpleIdentifierContext> simpleIdentifier() {
			return getRuleContexts(SimpleIdentifierContext.class);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQtParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(XQtParser.COMMA, i);
		}
		public GroupClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterGroupClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitGroupClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitGroupClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupClauseContext groupClause() throws RecognitionException {
		GroupClauseContext _localctx = new GroupClauseContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_groupClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393); match(GROUPBY);
			setState(394); simpleIdentifier();
			setState(399);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(395); match(COMMA);
				setState(396); simpleIdentifier();
				}
				}
				setState(401);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SearchPhraseContext extends ParserRuleContext {
		public ExpressionContext phrase;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SearchPhraseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_searchPhrase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSearchPhrase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSearchPhrase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSearchPhrase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SearchPhraseContext searchPhrase() throws RecognitionException {
		SearchPhraseContext _localctx = new SearchPhraseContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_searchPhrase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404); ((SearchPhraseContext)_localctx).phrase = expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntNumberContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(XQtParser.INT, 0); }
		public IntNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterIntNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitIntNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitIntNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntNumberContext intNumber() throws RecognitionException {
		IntNumberContext _localctx = new IntNumberContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_intNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(406); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FloatNumberContext extends ParserRuleContext {
		public TerminalNode FLOAT() { return getToken(XQtParser.FLOAT, 0); }
		public FloatNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterFloatNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitFloatNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitFloatNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FloatNumberContext floatNumber() throws RecognitionException {
		FloatNumberContext _localctx = new FloatNumberContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_floatNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(408); match(FLOAT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Expression_notContext extends ExpressionContext {
		public Token op;
		public ExpressionContext operand;
		public TerminalNode NOT() { return getToken(XQtParser.NOT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Expression_notContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_not(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_not(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_not(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_nestContext extends ExpressionContext {
		public ExpressionContext operand;
		public TerminalNode LPAR() { return getToken(XQtParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(XQtParser.RPAR, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Expression_nestContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_nest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_nest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_nest(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_functionContext extends ExpressionContext {
		public FunctionContext operand;
		public FunctionContext function() {
			return getRuleContext(FunctionContext.class,0);
		}
		public Expression_functionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_function(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_valueContext extends ExpressionContext {
		public ValueContext operand;
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public Expression_valueContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_value(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_value(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_multContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public TerminalNode MULT() { return getToken(XQtParser.MULT, 0); }
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode MOD() { return getToken(XQtParser.MOD, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public TerminalNode DIV() { return getToken(XQtParser.DIV, 0); }
		public Expression_multContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_mult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_mult(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_mult(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_powerContext extends ExpressionContext {
		public ExpressionContext x;
		public Token op;
		public ExpressionContext y;
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public Expression_powerContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_power(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_power(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_power(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_aAndOrContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AAND() { return getToken(XQtParser.AAND, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public TerminalNode AOR() { return getToken(XQtParser.AOR, 0); }
		public Expression_aAndOrContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_aAndOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_aAndOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_aAndOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_andOrContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(XQtParser.AND, 0); }
		public TerminalNode OR() { return getToken(XQtParser.OR, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public Expression_andOrContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_andOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_andOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_andOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_addContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(XQtParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(XQtParser.MINUS, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public Expression_addContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_add(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_add(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_add(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_isContext extends ExpressionContext {
		public ExpressionContext operand;
		public Token not;
		public Token isType;
		public TerminalNode IS() { return getToken(XQtParser.IS, 0); }
		public TerminalNode NULL() { return getToken(XQtParser.NULL, 0); }
		public TerminalNode NoT() { return getToken(XQtParser.NoT, 0); }
		public TerminalNode DatE() { return getToken(XQtParser.DatE, 0); }
		public TerminalNode NUMBER() { return getToken(XQtParser.NUMBER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EMPTY() { return getToken(XQtParser.EMPTY, 0); }
		public Expression_isContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_is(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_is(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_is(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_idExprContext extends ExpressionContext {
		public IdExprContext operand;
		public IdExprContext idExpr() {
			return getRuleContext(IdExprContext.class,0);
		}
		public Expression_idExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_idExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_idExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_idExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_negateContext extends ExpressionContext {
		public Token op;
		public ExpressionContext operand;
		public TerminalNode NEGATE() { return getToken(XQtParser.NEGATE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Expression_negateContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_negate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_negate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_negate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_compareContext extends ExpressionContext {
		public ExpressionContext left;
		public Token op;
		public ExpressionContext right;
		public TerminalNode NotEQ() { return getToken(XQtParser.NotEQ, 0); }
		public TerminalNode LTEQ() { return getToken(XQtParser.LTEQ, 0); }
		public TerminalNode LT() { return getToken(XQtParser.LT, 0); }
		public TerminalNode GT() { return getToken(XQtParser.GT, 0); }
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode EQ() { return getToken(XQtParser.EQ, 0); }
		public TerminalNode LIKE() { return getToken(XQtParser.LIKE, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public TerminalNode GTEQ() { return getToken(XQtParser.GTEQ, 0); }
		public Expression_compareContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_compare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_compare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_compare(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Expression_smartContext extends ExpressionContext {
		public SmartIdContext operand;
		public SmartIdContext smartId() {
			return getRuleContext(SmartIdContext.class,0);
		}
		public Expression_smartContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterExpression_smart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitExpression_smart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitExpression_smart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 74;
		enterRecursionRule(_localctx, 74, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				{
				_localctx = new Expression_negateContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(411); ((Expression_negateContext)_localctx).op = match(NEGATE);
				setState(412); ((Expression_negateContext)_localctx).operand = expression(13);
				}
				break;

			case 2:
				{
				_localctx = new Expression_notContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(413); ((Expression_notContext)_localctx).op = match(NOT);
				setState(414); ((Expression_notContext)_localctx).operand = expression(7);
				}
				break;

			case 3:
				{
				_localctx = new Expression_functionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(415); ((Expression_functionContext)_localctx).operand = function();
				}
				break;

			case 4:
				{
				_localctx = new Expression_nestContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(416); match(LPAR);
				setState(417); ((Expression_nestContext)_localctx).operand = expression(0);
				setState(418); match(RPAR);
				}
				break;

			case 5:
				{
				_localctx = new Expression_valueContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(420); ((Expression_valueContext)_localctx).operand = value();
				}
				break;

			case 6:
				{
				_localctx = new Expression_idExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(421); ((Expression_idExprContext)_localctx).operand = idExpr();
				}
				break;

			case 7:
				{
				_localctx = new Expression_smartContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(422); ((Expression_smartContext)_localctx).operand = smartId();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(451);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(449);
					switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
					case 1:
						{
						_localctx = new Expression_powerContext(new ExpressionContext(_parentctx, _parentState));
						((Expression_powerContext)_localctx).x = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(425);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(426); ((Expression_powerContext)_localctx).op = match(POW);
						setState(427); ((Expression_powerContext)_localctx).y = expression(15);
						}
						break;

					case 2:
						{
						_localctx = new Expression_multContext(new ExpressionContext(_parentctx, _parentState));
						((Expression_multContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(428);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(429);
						((Expression_multContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (MULT - 74)) | (1L << (DIV - 74)) | (1L << (MOD - 74)))) != 0)) ) {
							((Expression_multContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(430); ((Expression_multContext)_localctx).right = expression(13);
						}
						break;

					case 3:
						{
						_localctx = new Expression_addContext(new ExpressionContext(_parentctx, _parentState));
						((Expression_addContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(431);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(432);
						((Expression_addContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((Expression_addContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(433); ((Expression_addContext)_localctx).right = expression(12);
						}
						break;

					case 4:
						{
						_localctx = new Expression_aAndOrContext(new ExpressionContext(_parentctx, _parentState));
						((Expression_aAndOrContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(434);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(435);
						((Expression_aAndOrContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==AOR || _la==AAND) ) {
							((Expression_aAndOrContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(436); ((Expression_aAndOrContext)_localctx).right = expression(11);
						}
						break;

					case 5:
						{
						_localctx = new Expression_compareContext(new ExpressionContext(_parentctx, _parentState));
						((Expression_compareContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(437);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(438);
						((Expression_compareContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (EQ - 64)) | (1L << (NotEQ - 64)) | (1L << (GT - 64)) | (1L << (GTEQ - 64)) | (1L << (LT - 64)) | (1L << (LTEQ - 64)) | (1L << (LIKE - 64)))) != 0)) ) {
							((Expression_compareContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(439); ((Expression_compareContext)_localctx).right = expression(10);
						}
						break;

					case 6:
						{
						_localctx = new Expression_andOrContext(new ExpressionContext(_parentctx, _parentState));
						((Expression_andOrContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(440);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(441);
						((Expression_andOrContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==OR || _la==AND) ) {
							((Expression_andOrContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						consume();
						setState(442); ((Expression_andOrContext)_localctx).right = expression(7);
						}
						break;

					case 7:
						{
						_localctx = new Expression_isContext(new ExpressionContext(_parentctx, _parentState));
						((Expression_isContext)_localctx).operand = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(443);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(444); match(IS);
						setState(446);
						_la = _input.LA(1);
						if (_la==NoT) {
							{
							setState(445); ((Expression_isContext)_localctx).not = match(NoT);
							}
						}

						setState(448);
						((Expression_isContext)_localctx).isType = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << NUMBER) | (1L << DatE) | (1L << EMPTY))) != 0)) ) {
							((Expression_isContext)_localctx).isType = (Token)_errHandler.recoverInline(this);
						}
						consume();
						}
						break;
					}
					} 
				}
				setState(453);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FunctionContext extends ParserRuleContext {
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
	 
		public FunctionContext() { }
		public void copyFrom(FunctionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Function_packageContext extends FunctionContext {
		public List<TerminalNode> COMMA() { return getTokens(XQtParser.COMMA); }
		public PackagedIdentifierContext packagedIdentifier() {
			return getRuleContext(PackagedIdentifierContext.class,0);
		}
		public TerminalNode LPAR() { return getToken(XQtParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(XQtParser.RPAR, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(XQtParser.COMMA, i);
		}
		public Function_packageContext(FunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterFunction_package(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitFunction_package(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitFunction_package(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Function_simpleContext extends FunctionContext {
		public SimpleIdentifierContext simpleIdentifier() {
			return getRuleContext(SimpleIdentifierContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(XQtParser.COMMA); }
		public TerminalNode LPAR() { return getToken(XQtParser.LPAR, 0); }
		public TerminalNode RPAR() { return getToken(XQtParser.RPAR, 0); }
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public TerminalNode COMMA(int i) {
			return getToken(XQtParser.COMMA, i);
		}
		public Function_simpleContext(FunctionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterFunction_simple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitFunction_simple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitFunction_simple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_function);
		int _la;
		try {
			setState(482);
			switch ( getInterpreter().adaptivePredict(_input,53,_ctx) ) {
			case 1:
				_localctx = new Function_simpleContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(454); simpleIdentifier();
				setState(455); match(LPAR);
				setState(464);
				_la = _input.LA(1);
				if (((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & ((1L << (NEGATE - 73)) | (1L << (NOT - 73)) | (1L << (LPAR - 73)) | (1L << (STRING - 73)) | (1L << (ID - 73)) | (1L << (BOOLEAN - 73)) | (1L << (DATE - 73)) | (1L << (UINT - 73)) | (1L << (INT - 73)) | (1L << (FLOAT - 73)))) != 0)) {
					{
					setState(456); argument();
					setState(461);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(457); match(COMMA);
						setState(458); argument();
						}
						}
						setState(463);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(466); match(RPAR);
				}
				break;

			case 2:
				_localctx = new Function_packageContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(468); packagedIdentifier();
				setState(469); match(LPAR);
				setState(478);
				_la = _input.LA(1);
				if (((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & ((1L << (NEGATE - 73)) | (1L << (NOT - 73)) | (1L << (LPAR - 73)) | (1L << (STRING - 73)) | (1L << (ID - 73)) | (1L << (BOOLEAN - 73)) | (1L << (DATE - 73)) | (1L << (UINT - 73)) | (1L << (INT - 73)) | (1L << (FLOAT - 73)))) != 0)) {
					{
					setState(470); argument();
					setState(475);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==COMMA) {
						{
						{
						setState(471); match(COMMA);
						setState(472); argument();
						}
						}
						setState(477);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(480); match(RPAR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(484); expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PackagedIdentifierContext extends ParserRuleContext {
		public Token packageId;
		public Token id;
		public TerminalNode DOT() { return getToken(XQtParser.DOT, 0); }
		public List<TerminalNode> ID() { return getTokens(XQtParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(XQtParser.ID, i);
		}
		public PackagedIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packagedIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterPackagedIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitPackagedIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitPackagedIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackagedIdentifierContext packagedIdentifier() throws RecognitionException {
		PackagedIdentifierContext _localctx = new PackagedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_packagedIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(486); ((PackagedIdentifierContext)_localctx).packageId = match(ID);
			setState(487); match(DOT);
			setState(488); ((PackagedIdentifierContext)_localctx).id = match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SmartIdContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public SemanticKeyContext semanticKey() {
			return getRuleContext(SemanticKeyContext.class,0);
		}
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public SmartIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_smartId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSmartId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSmartId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSmartId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SmartIdContext smartId() throws RecognitionException {
		SmartIdContext _localctx = new SmartIdContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_smartId);
		try {
			setState(503);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(490); match(ID);
				setState(491); match(COLON);
				setState(492); dataType();
				setState(493); match(DCOLON);
				setState(494); semanticKey();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(496); match(ID);
				setState(497); match(DCOLON);
				setState(498); semanticKey();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(499); match(ID);
				setState(500); match(COLON);
				setState(501); dataType();
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(502); match(ID);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataTypeContext extends ParserRuleContext {
		public TerminalNode Long() { return getToken(XQtParser.Long, 0); }
		public TerminalNode Byte() { return getToken(XQtParser.Byte, 0); }
		public TerminalNode Date() { return getToken(XQtParser.Date, 0); }
		public List<TerminalNode> String() { return getTokens(XQtParser.String); }
		public TerminalNode String(int i) {
			return getToken(XQtParser.String, i);
		}
		public TerminalNode Integer() { return getToken(XQtParser.Integer, 0); }
		public TerminalNode Real() { return getToken(XQtParser.Real, 0); }
		public TerminalNode Boolean() { return getToken(XQtParser.Boolean, 0); }
		public DataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataTypeContext dataType() throws RecognitionException {
		DataTypeContext _localctx = new DataTypeContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_dataType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(505);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Boolean) | (1L << String) | (1L << Byte) | (1L << Integer) | (1L << Long) | (1L << Real) | (1L << Date))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SemanticKeyContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public SemanticKeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_semanticKey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSemanticKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSemanticKey(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSemanticKey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SemanticKeyContext semanticKey() throws RecognitionException {
		SemanticKeyContext _localctx = new SemanticKeyContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_semanticKey);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(507); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TerminalNode UINT() { return getToken(XQtParser.UINT, 0); }
		public TerminalNode STRING() { return getToken(XQtParser.STRING, 0); }
		public TerminalNode BOOLEAN() { return getToken(XQtParser.BOOLEAN, 0); }
		public TerminalNode DATE() { return getToken(XQtParser.DATE, 0); }
		public TerminalNode INT() { return getToken(XQtParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(XQtParser.FLOAT, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			_la = _input.LA(1);
			if ( !(((((_la - 99)) & ~0x3f) == 0 && ((1L << (_la - 99)) & ((1L << (STRING - 99)) | (1L << (BOOLEAN - 99)) | (1L << (DATE - 99)) | (1L << (UINT - 99)) | (1L << (INT - 99)) | (1L << (FLOAT - 99)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdExprContext extends ParserRuleContext {
		public IdExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idExpr; }
	 
		public IdExprContext() { }
		public void copyFrom(IdExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class IdExpr_simpleContext extends IdExprContext {
		public SimpleIdentifierContext simpleIdentifier() {
			return getRuleContext(SimpleIdentifierContext.class,0);
		}
		public IdExpr_simpleContext(IdExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterIdExpr_simple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitIdExpr_simple(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitIdExpr_simple(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdExpr_qulaifiedContext extends IdExprContext {
		public QualifiedIdentifierContext qualifiedIdentifier() {
			return getRuleContext(QualifiedIdentifierContext.class,0);
		}
		public IdExpr_qulaifiedContext(IdExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterIdExpr_qulaified(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitIdExpr_qulaified(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitIdExpr_qulaified(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdExprContext idExpr() throws RecognitionException {
		IdExprContext _localctx = new IdExprContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_idExpr);
		try {
			setState(513);
			switch ( getInterpreter().adaptivePredict(_input,55,_ctx) ) {
			case 1:
				_localctx = new IdExpr_simpleContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(511); simpleIdentifier();
				}
				break;

			case 2:
				_localctx = new IdExpr_qulaifiedContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(512); qualifiedIdentifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VersionSelectorContext extends ParserRuleContext {
		public LabelVersionSelectorContext labelVersionSelector() {
			return getRuleContext(LabelVersionSelectorContext.class,0);
		}
		public SequenceVersionSelectorContext sequenceVersionSelector() {
			return getRuleContext(SequenceVersionSelectorContext.class,0);
		}
		public VersionSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_versionSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterVersionSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitVersionSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitVersionSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VersionSelectorContext versionSelector() throws RecognitionException {
		VersionSelectorContext _localctx = new VersionSelectorContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_versionSelector);
		try {
			setState(517);
			switch (_input.LA(1)) {
			case LATEST:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(515); labelVersionSelector();
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(516); sequenceVersionSelector();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LabelVersionSelectorContext extends ParserRuleContext {
		public TerminalNode LATEST() { return getToken(XQtParser.LATEST, 0); }
		public TerminalNode STRING() { return getToken(XQtParser.STRING, 0); }
		public LabelVersionSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labelVersionSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterLabelVersionSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitLabelVersionSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitLabelVersionSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelVersionSelectorContext labelVersionSelector() throws RecognitionException {
		LabelVersionSelectorContext _localctx = new LabelVersionSelectorContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_labelVersionSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			_la = _input.LA(1);
			if ( !(_la==LATEST || _la==STRING) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SequenceVersionSelectorContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(XQtParser.INT, 0); }
		public SequenceVersionSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sequenceVersionSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSequenceVersionSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSequenceVersionSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSequenceVersionSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SequenceVersionSelectorContext sequenceVersionSelector() throws RecognitionException {
		SequenceVersionSelectorContext _localctx = new SequenceVersionSelectorContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_sequenceVersionSelector);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(521); match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateVersionSelectorContext extends ParserRuleContext {
		public TerminalNode DATE() { return getToken(XQtParser.DATE, 0); }
		public DateVersionSelectorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateVersionSelector; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterDateVersionSelector(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitDateVersionSelector(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitDateVersionSelector(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateVersionSelectorContext dateVersionSelector() throws RecognitionException {
		DateVersionSelectorContext _localctx = new DateVersionSelectorContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_dateVersionSelector);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(523);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << 1) | (1L << 4) | (1L << 5))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(524); match(DATE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleIdentifierContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public SimpleIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterSimpleIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitSimpleIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitSimpleIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleIdentifierContext simpleIdentifier() throws RecognitionException {
		SimpleIdentifierContext _localctx = new SimpleIdentifierContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_simpleIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(526); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QualifiedIdentifierContext extends ParserRuleContext {
		public TerminalNode INT(int i) {
			return getToken(XQtParser.INT, i);
		}
		public List<TerminalNode> ID() { return getTokens(XQtParser.ID); }
		public List<TerminalNode> DOT() { return getTokens(XQtParser.DOT); }
		public TerminalNode ID(int i) {
			return getToken(XQtParser.ID, i);
		}
		public List<TerminalNode> INT() { return getTokens(XQtParser.INT); }
		public TerminalNode DOT(int i) {
			return getToken(XQtParser.DOT, i);
		}
		public QualifiedIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterQualifiedIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitQualifiedIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitQualifiedIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedIdentifierContext qualifiedIdentifier() throws RecognitionException {
		QualifiedIdentifierContext _localctx = new QualifiedIdentifierContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_qualifiedIdentifier);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(528); match(ID);
			setState(531); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(529); match(DOT);
					setState(530);
					_la = _input.LA(1);
					if ( !(_la==ID || _la==INT) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(533); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,57,_ctx);
			} while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PathContext extends ParserRuleContext {
		public List<PathEntityContext> pathEntity() {
			return getRuleContexts(PathEntityContext.class);
		}
		public List<TerminalNode> RELATIONSHIP() { return getTokens(XQtParser.RELATIONSHIP); }
		public PathAttributeContext pathAttribute() {
			return getRuleContext(PathAttributeContext.class,0);
		}
		public TerminalNode RELATIONSHIP(int i) {
			return getToken(XQtParser.RELATIONSHIP, i);
		}
		public PathEntityContext pathEntity(int i) {
			return getRuleContext(PathEntityContext.class,i);
		}
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_path);
		int _la;
		try {
			int _alt;
			setState(578);
			switch ( getInterpreter().adaptivePredict(_input,64,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(537); pathEntity();
				setState(540); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(538); match(RELATIONSHIP);
					setState(539); pathEntity();
					}
					}
					setState(542); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==RELATIONSHIP );
				setState(546);
				_la = _input.LA(1);
				if (_la==8) {
					{
					setState(544); match(8);
					setState(545); pathAttribute();
					}
				}

				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(548); pathEntity();
				setState(553);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
				while ( _alt!=2 && _alt!=ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(549); match(RELATIONSHIP);
						setState(550); pathEntity();
						}
						} 
					}
					setState(555);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
				}
				{
				setState(556); match(RELATIONSHIP);
				setState(557); pathEntity();
				}
				setState(561);
				_la = _input.LA(1);
				if (_la==9) {
					{
					setState(559); match(9);
					setState(560); pathAttribute();
					}
				}

				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(563); pathEntity();
				setState(566);
				_la = _input.LA(1);
				if (_la==9) {
					{
					setState(564); match(9);
					setState(565); pathAttribute();
					}
				}

				setState(570); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(568); match(RELATIONSHIP);
					setState(569); pathEntity();
					}
					}
					setState(572); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==RELATIONSHIP );
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(574); pathEntity();
				setState(575); match(8);
				setState(576); pathAttribute();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PathEntityContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public PathEntityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathEntity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterPathEntity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitPathEntity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitPathEntity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathEntityContext pathEntity() throws RecognitionException {
		PathEntityContext _localctx = new PathEntityContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_pathEntity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(580); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PathAttributeContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(XQtParser.ID, 0); }
		public PathAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathAttribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).enterPathAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XQtListener ) ((XQtListener)listener).exitPathAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XQtVisitor ) return ((XQtVisitor<? extends T>)visitor).visitPathAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathAttributeContext pathAttribute() throws RecognitionException {
		PathAttributeContext _localctx = new PathAttributeContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_pathAttribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(582); match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 37: return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 14);

		case 1: return precpred(_ctx, 12);

		case 2: return precpred(_ctx, 11);

		case 3: return precpred(_ctx, 10);

		case 4: return precpred(_ctx, 9);

		case 5: return precpred(_ctx, 6);

		case 6: return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3o\u024b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\3\2\3\2\3\3\7\3v\n\3\f\3"+
		"\16\3y\13\3\3\3\7\3|\n\3\f\3\16\3\177\13\3\3\3\7\3\u0082\n\3\f\3\16\3"+
		"\u0085\13\3\3\3\3\3\7\3\u0089\n\3\f\3\16\3\u008c\13\3\6\3\u008e\n\3\r"+
		"\3\16\3\u008f\3\3\3\3\3\4\3\4\3\4\3\4\5\4\u0098\n\4\3\4\3\4\6\4\u009c"+
		"\n\4\r\4\16\4\u009d\3\4\3\4\3\5\3\5\3\5\3\5\3\5\5\5\u00a7\n\5\3\5\3\5"+
		"\3\5\5\5\u00ac\n\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\7\6\u00bd\n\6\f\6\16\6\u00c0\13\6\5\6\u00c2\n\6\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\7\b\u00d2\n\b\f\b\16\b\u00d5"+
		"\13\b\5\b\u00d7\n\b\3\b\3\b\3\b\5\b\u00dc\n\b\3\t\3\t\3\n\3\n\3\13\3\13"+
		"\3\f\3\f\3\r\3\r\5\r\u00e8\n\r\3\r\5\r\u00eb\n\r\3\r\3\r\5\r\u00ef\n\r"+
		"\3\r\5\r\u00f2\n\r\3\r\5\r\u00f5\n\r\3\r\5\r\u00f8\n\r\3\r\5\r\u00fb\n"+
		"\r\3\r\5\r\u00fe\n\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\5\21\u010b\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\24\3\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\5\30\u0127\n\30\3\31\5\31\u012a\n\31\3\31\3\31\3\31\5\31\u012f"+
		"\n\31\3\31\3\31\3\31\3\31\5\31\u0135\n\31\3\31\3\31\3\31\7\31\u013a\n"+
		"\31\f\31\16\31\u013d\13\31\3\31\3\31\5\31\u0141\n\31\3\31\5\31\u0144\n"+
		"\31\3\31\3\31\5\31\u0148\n\31\3\31\5\31\u014b\n\31\3\31\3\31\5\31\u014f"+
		"\n\31\3\31\5\31\u0152\n\31\3\31\3\31\5\31\u0156\n\31\3\31\5\31\u0159\n"+
		"\31\3\32\3\32\3\32\5\32\u015e\n\32\3\32\3\32\5\32\u0162\n\32\3\33\3\33"+
		"\3\33\3\33\3\33\3\34\3\34\3\34\3\34\7\34\u016d\n\34\f\34\16\34\u0170\13"+
		"\34\3\35\3\35\5\35\u0174\n\35\3\35\5\35\u0177\n\35\3\36\3\36\3\37\3\37"+
		"\3 \3 \3 \3 \5 \u0181\n \3!\3!\3!\5!\u0186\n!\3!\3!\5!\u018a\n!\3\"\3"+
		"\"\3\"\3\"\7\"\u0190\n\"\f\"\16\"\u0193\13\"\3#\3#\3$\3$\3%\3%\3&\3&\3"+
		"\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\5\'\u01aa\n\'\3\'\3"+
		"\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'"+
		"\3\'\3\'\5\'\u01c1\n\'\3\'\7\'\u01c4\n\'\f\'\16\'\u01c7\13\'\3(\3(\3("+
		"\3(\3(\7(\u01ce\n(\f(\16(\u01d1\13(\5(\u01d3\n(\3(\3(\3(\3(\3(\3(\3(\7"+
		"(\u01dc\n(\f(\16(\u01df\13(\5(\u01e1\n(\3(\3(\5(\u01e5\n(\3)\3)\3*\3*"+
		"\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\3+\5+\u01fa\n+\3,\3,\3-\3-"+
		"\3.\3.\3/\3/\5/\u0204\n/\3\60\3\60\5\60\u0208\n\60\3\61\3\61\3\62\3\62"+
		"\3\63\3\63\3\63\3\64\3\64\3\65\3\65\3\65\6\65\u0216\n\65\r\65\16\65\u0217"+
		"\3\66\3\66\3\67\3\67\3\67\6\67\u021f\n\67\r\67\16\67\u0220\3\67\3\67\5"+
		"\67\u0225\n\67\3\67\3\67\3\67\7\67\u022a\n\67\f\67\16\67\u022d\13\67\3"+
		"\67\3\67\3\67\3\67\3\67\5\67\u0234\n\67\3\67\3\67\3\67\5\67\u0239\n\67"+
		"\3\67\3\67\6\67\u023d\n\67\r\67\16\67\u023e\3\67\3\67\3\67\3\67\5\67\u0245"+
		"\n\67\38\38\39\39\39\2\3L:\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$"+
		"&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnp\2\21\3\2\34\35\3\2BH\4"+
		"\2jjmm\5\2\4\5\b\t\f\f\3\2&\'\3\2LN\3\2IJ\3\2RS\3\2]^\4\2((\61\63\3\2"+
		";A\4\2eeko\4\2\33\33ee\4\2\3\3\6\7\4\2jjnn\u0263\2r\3\2\2\2\4w\3\2\2\2"+
		"\6\u0093\3\2\2\2\b\u00a1\3\2\2\2\n\u00af\3\2\2\2\f\u00c3\3\2\2\2\16\u00c7"+
		"\3\2\2\2\20\u00dd\3\2\2\2\22\u00df\3\2\2\2\24\u00e1\3\2\2\2\26\u00e3\3"+
		"\2\2\2\30\u00e5\3\2\2\2\32\u00ff\3\2\2\2\34\u0101\3\2\2\2\36\u0104\3\2"+
		"\2\2 \u010a\3\2\2\2\"\u010c\3\2\2\2$\u0112\3\2\2\2&\u0114\3\2\2\2(\u0118"+
		"\3\2\2\2*\u011c\3\2\2\2,\u011e\3\2\2\2.\u0126\3\2\2\2\60\u0129\3\2\2\2"+
		"\62\u015a\3\2\2\2\64\u0163\3\2\2\2\66\u0168\3\2\2\28\u0171\3\2\2\2:\u0178"+
		"\3\2\2\2<\u017a\3\2\2\2>\u0180\3\2\2\2@\u0182\3\2\2\2B\u018b\3\2\2\2D"+
		"\u0194\3\2\2\2F\u0196\3\2\2\2H\u0198\3\2\2\2J\u019a\3\2\2\2L\u01a9\3\2"+
		"\2\2N\u01e4\3\2\2\2P\u01e6\3\2\2\2R\u01e8\3\2\2\2T\u01f9\3\2\2\2V\u01fb"+
		"\3\2\2\2X\u01fd\3\2\2\2Z\u01ff\3\2\2\2\\\u0203\3\2\2\2^\u0207\3\2\2\2"+
		"`\u0209\3\2\2\2b\u020b\3\2\2\2d\u020d\3\2\2\2f\u0210\3\2\2\2h\u0212\3"+
		"\2\2\2j\u0219\3\2\2\2l\u0244\3\2\2\2n\u0246\3\2\2\2p\u0248\3\2\2\2rs\5"+
		"\4\3\2s\3\3\2\2\2tv\5\6\4\2ut\3\2\2\2vy\3\2\2\2wu\3\2\2\2wx\3\2\2\2x}"+
		"\3\2\2\2yw\3\2\2\2z|\5\n\6\2{z\3\2\2\2|\177\3\2\2\2}{\3\2\2\2}~\3\2\2"+
		"\2~\u0083\3\2\2\2\177}\3\2\2\2\u0080\u0082\5\16\b\2\u0081\u0080\3\2\2"+
		"\2\u0082\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u008d"+
		"\3\2\2\2\u0085\u0083\3\2\2\2\u0086\u008a\5\22\n\2\u0087\u0089\7h\2\2\u0088"+
		"\u0087\3\2\2\2\u0089\u008c\3\2\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2"+
		"\2\2\u008b\u008e\3\2\2\2\u008c\u008a\3\2\2\2\u008d\u0086\3\2\2\2\u008e"+
		"\u008f\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0091\3\2"+
		"\2\2\u0091\u0092\7\2\2\3\u0092\5\3\2\2\2\u0093\u0094\7\r\2\2\u0094\u0097"+
		"\7j\2\2\u0095\u0096\7\20\2\2\u0096\u0098\7j\2\2\u0097\u0095\3\2\2\2\u0097"+
		"\u0098\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009b\7Y\2\2\u009a\u009c\5\b"+
		"\5\2\u009b\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d"+
		"\u009e\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\7Z\2\2\u00a0\7\3\2\2\2"+
		"\u00a1\u00a2\7\21\2\2\u00a2\u00a6\5T+\2\u00a3\u00a4\7\22\2\2\u00a4\u00a5"+
		"\7Q\2\2\u00a5\u00a7\5L\'\2\u00a6\u00a3\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7"+
		"\u00ab\3\2\2\2\u00a8\u00a9\7\23\2\2\u00a9\u00aa\7Q\2\2\u00aa\u00ac\5L"+
		"\'\2\u00ab\u00a8\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad"+
		"\u00ae\7T\2\2\u00ae\t\3\2\2\2\u00af\u00b0\7\24\2\2\u00b0\u00b1\7j\2\2"+
		"\u00b1\u00b2\7\25\2\2\u00b2\u00b3\7Q\2\2\u00b3\u00b4\7j\2\2\u00b4\u00b5"+
		"\7\26\2\2\u00b5\u00b6\7Q\2\2\u00b6\u00c1\7e\2\2\u00b7\u00b8\7\27\2\2\u00b8"+
		"\u00b9\7Q\2\2\u00b9\u00be\5\f\7\2\u00ba\u00bb\7T\2\2\u00bb\u00bd\5\f\7"+
		"\2\u00bc\u00ba\3\2\2\2\u00bd\u00c0\3\2\2\2\u00be\u00bc\3\2\2\2\u00be\u00bf"+
		"\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3\2\2\2\u00c1\u00b7\3\2\2\2\u00c1"+
		"\u00c2\3\2\2\2\u00c2\13\3\2\2\2\u00c3\u00c4\7j\2\2\u00c4\u00c5\7\\\2\2"+
		"\u00c5\u00c6\5L\'\2\u00c6\r\3\2\2\2\u00c7\u00c8\7\30\2\2\u00c8\u00c9\7"+
		"j\2\2\u00c9\u00ca\7\24\2\2\u00ca\u00cb\7Q\2\2\u00cb\u00d6\7j\2\2\u00cc"+
		"\u00cd\7\31\2\2\u00cd\u00ce\7Q\2\2\u00ce\u00d3\5\20\t\2\u00cf\u00d0\7"+
		"T\2\2\u00d0\u00d2\5\20\t\2\u00d1\u00cf\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3"+
		"\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d7\3\2\2\2\u00d5\u00d3\3\2"+
		"\2\2\u00d6\u00cc\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00db\3\2\2\2\u00d8"+
		"\u00d9\7\32\2\2\u00d9\u00da\7Q\2\2\u00da\u00dc\5^\60\2\u00db\u00d8\3\2"+
		"\2\2\u00db\u00dc\3\2\2\2\u00dc\17\3\2\2\2\u00dd\u00de\7j\2\2\u00de\21"+
		"\3\2\2\2\u00df\u00e0\5\24\13\2\u00e0\23\3\2\2\2\u00e1\u00e2\5\26\f\2\u00e2"+
		"\25\3\2\2\2\u00e3\u00e4\5\30\r\2\u00e4\27\3\2\2\2\u00e5\u00e7\7\16\2\2"+
		"\u00e6\u00e8\5\32\16\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00ea"+
		"\3\2\2\2\u00e9\u00eb\5\34\17\2\u00ea\u00e9\3\2\2\2\u00ea\u00eb\3\2\2\2"+
		"\u00eb\u00ec\3\2\2\2\u00ec\u00ee\5\36\20\2\u00ed\u00ef\5.\30\2\u00ee\u00ed"+
		"\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f1\3\2\2\2\u00f0\u00f2\5\62\32\2"+
		"\u00f1\u00f0\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f4\3\2\2\2\u00f3\u00f5"+
		"\5\64\33\2\u00f4\u00f3\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f7\3\2\2\2"+
		"\u00f6\u00f8\5\66\34\2\u00f7\u00f6\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00fa"+
		"\3\2\2\2\u00f9\u00fb\5@!\2\u00fa\u00f9\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb"+
		"\u00fd\3\2\2\2\u00fc\u00fe\5B\"\2\u00fd\u00fc\3\2\2\2\u00fd\u00fe\3\2"+
		"\2\2\u00fe\31\3\2\2\2\u00ff\u0100\t\2\2\2\u0100\33\3\2\2\2\u0101\u0102"+
		"\7\r\2\2\u0102\u0103\5f\64\2\u0103\35\3\2\2\2\u0104\u0105\7\17\2\2\u0105"+
		"\u0106\5 \21\2\u0106\37\3\2\2\2\u0107\u010b\5\"\22\2\u0108\u010b\5(\25"+
		"\2\u0109\u010b\5D#\2\u010a\u0107\3\2\2\2\u010a\u0108\3\2\2\2\u010a\u0109"+
		"\3\2\2\2\u010b!\3\2\2\2\u010c\u010d\5(\25\2\u010d\u010e\5$\23\2\u010e"+
		"\u010f\5(\25\2\u010f\u0110\7\36\2\2\u0110\u0111\5&\24\2\u0111#\3\2\2\2"+
		"\u0112\u0113\7\37\2\2\u0113%\3\2\2\2\u0114\u0115\7j\2\2\u0115\u0116\t"+
		"\3\2\2\u0116\u0117\7j\2\2\u0117\'\3\2\2\2\u0118\u0119\5*\26\2\u0119\u011a"+
		"\7X\2\2\u011a\u011b\5,\27\2\u011b)\3\2\2\2\u011c\u011d\7j\2\2\u011d+\3"+
		"\2\2\2\u011e\u011f\t\4\2\2\u011f-\3\2\2\2\u0120\u0121\7 \2\2\u0121\u0127"+
		"\5\60\31\2\u0122\u0123\7 \2\2\u0123\u0127\5D#\2\u0124\u0125\7 \2\2\u0125"+
		"\u0127\5(\25\2\u0126\u0120\3\2\2\2\u0126\u0122\3\2\2\2\u0126\u0124\3\2"+
		"\2\2\u0127/\3\2\2\2\u0128\u012a\7\64\2\2\u0129\u0128\3\2\2\2\u0129\u012a"+
		"\3\2\2\2\u012a\u012b\3\2\2\2\u012b\u012e\5D#\2\u012c\u012d\7\66\2\2\u012d"+
		"\u012f\7\\\2\2\u012e\u012c\3\2\2\2\u012e\u012f\3\2\2\2\u012f\u0130\3\2"+
		"\2\2\u0130\u0131\5D#\2\u0131\u0134\3\2\2\2\u0132\u0133\7\67\2\2\u0133"+
		"\u0135\7\\\2\2\u0134\u0132\3\2\2\2\u0134\u0135\3\2\2\2\u0135\u0136\3\2"+
		"\2\2\u0136\u013b\5D#\2\u0137\u0138\7T\2\2\u0138\u013a\5D#\2\u0139\u0137"+
		"\3\2\2\2\u013a\u013d\3\2\2\2\u013b\u0139\3\2\2\2\u013b\u013c\3\2\2\2\u013c"+
		"\u0143\3\2\2\2\u013d\u013b\3\2\2\2\u013e\u013f\7\65\2\2\u013f\u0141\7"+
		"\\\2\2\u0140\u013e\3\2\2\2\u0140\u0141\3\2\2\2\u0141\u0142\3\2\2\2\u0142"+
		"\u0144\t\5\2\2\u0143\u0140\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u014a\3\2"+
		"\2\2\u0145\u0146\78\2\2\u0146\u0148\7\\\2\2\u0147\u0145\3\2\2\2\u0147"+
		"\u0148\3\2\2\2\u0148\u0149\3\2\2\2\u0149\u014b\7e\2\2\u014a\u0147\3\2"+
		"\2\2\u014a\u014b\3\2\2\2\u014b\u0151\3\2\2\2\u014c\u014d\79\2\2\u014d"+
		"\u014f\7\\\2\2\u014e\u014c\3\2\2\2\u014e\u014f\3\2\2\2\u014f\u0150\3\2"+
		"\2\2\u0150\u0152\7e\2\2\u0151\u014e\3\2\2\2\u0151\u0152\3\2\2\2\u0152"+
		"\u0158\3\2\2\2\u0153\u0154\7:\2\2\u0154\u0156\7\\\2\2\u0155\u0153\3\2"+
		"\2\2\u0155\u0156\3\2\2\2\u0156\u0157\3\2\2\2\u0157\u0159\7e\2\2\u0158"+
		"\u0155\3\2\2\2\u0158\u0159\3\2\2\2\u0159\61\3\2\2\2\u015a\u015d\7!\2\2"+
		"\u015b\u015c\7\"\2\2\u015c\u015e\5F$\2\u015d\u015b\3\2\2\2\u015d\u015e"+
		"\3\2\2\2\u015e\u0161\3\2\2\2\u015f\u0160\7#\2\2\u0160\u0162\5F$\2\u0161"+
		"\u015f\3\2\2\2\u0161\u0162\3\2\2\2\u0162\63\3\2\2\2\u0163\u0164\7$\2\2"+
		"\u0164\u0165\7V\2\2\u0165\u0166\5F$\2\u0166\u0167\7W\2\2\u0167\65\3\2"+
		"\2\2\u0168\u0169\7%\2\2\u0169\u016e\58\35\2\u016a\u016b\7T\2\2\u016b\u016d"+
		"\58\35\2\u016c\u016a\3\2\2\2\u016d\u0170\3\2\2\2\u016e\u016c\3\2\2\2\u016e"+
		"\u016f\3\2\2\2\u016f\67\3\2\2\2\u0170\u016e\3\2\2\2\u0171\u0173\5:\36"+
		"\2\u0172\u0174\5<\37\2\u0173\u0172\3\2\2\2\u0173\u0174\3\2\2\2\u0174\u0176"+
		"\3\2\2\2\u0175\u0177\5> \2\u0176\u0175\3\2\2\2\u0176\u0177\3\2\2\2\u0177"+
		"9\3\2\2\2\u0178\u0179\5f\64\2\u0179;\3\2\2\2\u017a\u017b\t\6\2\2\u017b"+
		"=\3\2\2\2\u017c\u017d\7(\2\2\u017d\u0181\7)\2\2\u017e\u017f\7(\2\2\u017f"+
		"\u0181\7*\2\2\u0180\u017c\3\2\2\2\u0180\u017e\3\2\2\2\u0181?\3\2\2\2\u0182"+
		"\u0185\7+\2\2\u0183\u0184\7,\2\2\u0184\u0186\7m\2\2\u0185\u0183\3\2\2"+
		"\2\u0185\u0186\3\2\2\2\u0186\u0189\3\2\2\2\u0187\u0188\7-\2\2\u0188\u018a"+
		"\7m\2\2\u0189\u0187\3\2\2\2\u0189\u018a\3\2\2\2\u018aA\3\2\2\2\u018b\u018c"+
		"\7.\2\2\u018c\u0191\5f\64\2\u018d\u018e\7T\2\2\u018e\u0190\5f\64\2\u018f"+
		"\u018d\3\2\2\2\u0190\u0193\3\2\2\2\u0191\u018f\3\2\2\2\u0191\u0192\3\2"+
		"\2\2\u0192C\3\2\2\2\u0193\u0191\3\2\2\2\u0194\u0195\7j\2\2\u0195E\3\2"+
		"\2\2\u0196\u0197\5L\'\2\u0197G\3\2\2\2\u0198\u0199\7n\2\2\u0199I\3\2\2"+
		"\2\u019a\u019b\7o\2\2\u019bK\3\2\2\2\u019c\u019d\b\'\1\2\u019d\u019e\7"+
		"K\2\2\u019e\u01aa\5L\'\17\u019f\u01a0\7P\2\2\u01a0\u01aa\5L\'\t\u01a1"+
		"\u01aa\5N(\2\u01a2\u01a3\7V\2\2\u01a3\u01a4\5L\'\2\u01a4\u01a5\7W\2\2"+
		"\u01a5\u01aa\3\2\2\2\u01a6\u01aa\5Z.\2\u01a7\u01aa\5\\/\2\u01a8\u01aa"+
		"\5T+\2\u01a9\u019c\3\2\2\2\u01a9\u019f\3\2\2\2\u01a9\u01a1\3\2\2\2\u01a9"+
		"\u01a2\3\2\2\2\u01a9\u01a6\3\2\2\2\u01a9\u01a7\3\2\2\2\u01a9\u01a8\3\2"+
		"\2\2\u01aa\u01c5\3\2\2\2\u01ab\u01ac\f\20\2\2\u01ac\u01ad\7O\2\2\u01ad"+
		"\u01c4\5L\'\21\u01ae\u01af\f\16\2\2\u01af\u01b0\t\7\2\2\u01b0\u01c4\5"+
		"L\'\17\u01b1\u01b2\f\r\2\2\u01b2\u01b3\t\b\2\2\u01b3\u01c4\5L\'\16\u01b4"+
		"\u01b5\f\f\2\2\u01b5\u01b6\t\t\2\2\u01b6\u01c4\5L\'\r\u01b7\u01b8\f\13"+
		"\2\2\u01b8\u01b9\t\3\2\2\u01b9\u01c4\5L\'\f\u01ba\u01bb\f\b\2\2\u01bb"+
		"\u01bc\t\n\2\2\u01bc\u01c4\5L\'\t\u01bd\u01be\f\n\2\2\u01be\u01c0\7/\2"+
		"\2\u01bf\u01c1\7\60\2\2\u01c0\u01bf\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1"+
		"\u01c2\3\2\2\2\u01c2\u01c4\t\13\2\2\u01c3\u01ab\3\2\2\2\u01c3\u01ae\3"+
		"\2\2\2\u01c3\u01b1\3\2\2\2\u01c3\u01b4\3\2\2\2\u01c3\u01b7\3\2\2\2\u01c3"+
		"\u01ba\3\2\2\2\u01c3\u01bd\3\2\2\2\u01c4\u01c7\3\2\2\2\u01c5\u01c3\3\2"+
		"\2\2\u01c5\u01c6\3\2\2\2\u01c6M\3\2\2\2\u01c7\u01c5\3\2\2\2\u01c8\u01c9"+
		"\5f\64\2\u01c9\u01d2\7V\2\2\u01ca\u01cf\5P)\2\u01cb\u01cc\7T\2\2\u01cc"+
		"\u01ce\5P)\2\u01cd\u01cb\3\2\2\2\u01ce\u01d1\3\2\2\2\u01cf\u01cd\3\2\2"+
		"\2\u01cf\u01d0\3\2\2\2\u01d0\u01d3\3\2\2\2\u01d1\u01cf\3\2\2\2\u01d2\u01ca"+
		"\3\2\2\2\u01d2\u01d3\3\2\2\2\u01d3\u01d4\3\2\2\2\u01d4\u01d5\7W\2\2\u01d5"+
		"\u01e5\3\2\2\2\u01d6\u01d7\5R*\2\u01d7\u01e0\7V\2\2\u01d8\u01dd\5P)\2"+
		"\u01d9\u01da\7T\2\2\u01da\u01dc\5P)\2\u01db\u01d9\3\2\2\2\u01dc\u01df"+
		"\3\2\2\2\u01dd\u01db\3\2\2\2\u01dd\u01de\3\2\2\2\u01de\u01e1\3\2\2\2\u01df"+
		"\u01dd\3\2\2\2\u01e0\u01d8\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e2\3\2"+
		"\2\2\u01e2\u01e3\7W\2\2\u01e3\u01e5\3\2\2\2\u01e4\u01c8\3\2\2\2\u01e4"+
		"\u01d6\3\2\2\2\u01e5O\3\2\2\2\u01e6\u01e7\5L\'\2\u01e7Q\3\2\2\2\u01e8"+
		"\u01e9\7j\2\2\u01e9\u01ea\7X\2\2\u01ea\u01eb\7j\2\2\u01ebS\3\2\2\2\u01ec"+
		"\u01ed\7j\2\2\u01ed\u01ee\7\\\2\2\u01ee\u01ef\5V,\2\u01ef\u01f0\7[\2\2"+
		"\u01f0\u01f1\5X-\2\u01f1\u01fa\3\2\2\2\u01f2\u01f3\7j\2\2\u01f3\u01f4"+
		"\7[\2\2\u01f4\u01fa\5X-\2\u01f5\u01f6\7j\2\2\u01f6\u01f7\7\\\2\2\u01f7"+
		"\u01fa\5V,\2\u01f8\u01fa\7j\2\2\u01f9\u01ec\3\2\2\2\u01f9\u01f2\3\2\2"+
		"\2\u01f9\u01f5\3\2\2\2\u01f9\u01f8\3\2\2\2\u01faU\3\2\2\2\u01fb\u01fc"+
		"\t\f\2\2\u01fcW\3\2\2\2\u01fd\u01fe\7j\2\2\u01feY\3\2\2\2\u01ff\u0200"+
		"\t\r\2\2\u0200[\3\2\2\2\u0201\u0204\5f\64\2\u0202\u0204\5h\65\2\u0203"+
		"\u0201\3\2\2\2\u0203\u0202\3\2\2\2\u0204]\3\2\2\2\u0205\u0208\5`\61\2"+
		"\u0206\u0208\5b\62\2\u0207\u0205\3\2\2\2\u0207\u0206\3\2\2\2\u0208_\3"+
		"\2\2\2\u0209\u020a\t\16\2\2\u020aa\3\2\2\2\u020b\u020c\7n\2\2\u020cc\3"+
		"\2\2\2\u020d\u020e\t\17\2\2\u020e\u020f\7l\2\2\u020fe\3\2\2\2\u0210\u0211"+
		"\7j\2\2\u0211g\3\2\2\2\u0212\u0215\7j\2\2\u0213\u0214\7X\2\2\u0214\u0216"+
		"\t\20\2\2\u0215\u0213\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0215\3\2\2\2"+
		"\u0217\u0218\3\2\2\2\u0218i\3\2\2\2\u0219\u021a\3\2\2\2\u021ak\3\2\2\2"+
		"\u021b\u021e\5n8\2\u021c\u021d\7_\2\2\u021d\u021f\5n8\2\u021e\u021c\3"+
		"\2\2\2\u021f\u0220\3\2\2\2\u0220\u021e\3\2\2\2\u0220\u0221\3\2\2\2\u0221"+
		"\u0224\3\2\2\2\u0222\u0223\7\n\2\2\u0223\u0225\5p9\2\u0224\u0222\3\2\2"+
		"\2\u0224\u0225\3\2\2\2\u0225\u0245\3\2\2\2\u0226\u022b\5n8\2\u0227\u0228"+
		"\7_\2\2\u0228\u022a\5n8\2\u0229\u0227\3\2\2\2\u022a\u022d\3\2\2\2\u022b"+
		"\u0229\3\2\2\2\u022b\u022c\3\2\2\2\u022c\u022e\3\2\2\2\u022d\u022b\3\2"+
		"\2\2\u022e\u022f\7_\2\2\u022f\u0230\5n8\2\u0230\u0233\3\2\2\2\u0231\u0232"+
		"\7\13\2\2\u0232\u0234\5p9\2\u0233\u0231\3\2\2\2\u0233\u0234\3\2\2\2\u0234"+
		"\u0245\3\2\2\2\u0235\u0238\5n8\2\u0236\u0237\7\13\2\2\u0237\u0239\5p9"+
		"\2\u0238\u0236\3\2\2\2\u0238\u0239\3\2\2\2\u0239\u023c\3\2\2\2\u023a\u023b"+
		"\7_\2\2\u023b\u023d\5n8\2\u023c\u023a\3\2\2\2\u023d\u023e\3\2\2\2\u023e"+
		"\u023c\3\2\2\2\u023e\u023f\3\2\2\2\u023f\u0245\3\2\2\2\u0240\u0241\5n"+
		"8\2\u0241\u0242\7\n\2\2\u0242\u0243\5p9\2\u0243\u0245\3\2\2\2\u0244\u021b"+
		"\3\2\2\2\u0244\u0226\3\2\2\2\u0244\u0235\3\2\2\2\u0244\u0240\3\2\2\2\u0245"+
		"m\3\2\2\2\u0246\u0247\7j\2\2\u0247o\3\2\2\2\u0248\u0249\7j\2\2\u0249q"+
		"\3\2\2\2Cw}\u0083\u008a\u008f\u0097\u009d\u00a6\u00ab\u00be\u00c1\u00d3"+
		"\u00d6\u00db\u00e7\u00ea\u00ee\u00f1\u00f4\u00f7\u00fa\u00fd\u010a\u0126"+
		"\u0129\u012e\u0134\u013b\u0140\u0143\u0147\u014a\u014e\u0151\u0155\u0158"+
		"\u015d\u0161\u016e\u0173\u0176\u0180\u0185\u0189\u0191\u01a9\u01c0\u01c3"+
		"\u01c5\u01cf\u01d2\u01dd\u01e0\u01e4\u01f9\u0203\u0207\u0217\u0220\u0224"+
		"\u022b\u0233\u0238\u023e\u0244";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}