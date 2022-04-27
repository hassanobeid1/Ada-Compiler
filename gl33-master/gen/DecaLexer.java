// Generated from /home/tanguy/ensimag/gl/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.9
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DecaLexer extends AbstractDecaLexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LT=1, GT=2, EQUALS=3, PLUS=4, MINUS=5, TIMES=6, SLASH=7, PERCENT=8, DOT=9, 
		COMMA=10, OPARENT=11, CPARENT=12, OBRACE=13, CBRACE=14, EXCLAM=15, SEMI=16, 
		EQEQ=17, NEQ=18, GEQ=19, LEQ=20, AND=21, OR=22, PRINT=23, PRINTLN=24, 
		PRINTX=25, PRINTLNX=26, WHILE=27, RETURN=28, IF=29, ELSE=30, INSTANCEOF=31, 
		TRUE=32, FALSE=33, NULL=34, CLASS=35, EXTENDS=36, PROTECTED=37, ASM=38, 
		THIS=39, NEW=40, READFLOAT=41, READINT=42, INT=43, FLOAT=44, STRING=45, 
		MULTI_LINE_STRING=46, SEP=47, INCLUDE=48, IDENT=49;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LT", "GT", "EQUALS", "PLUS", "MINUS", "TIMES", "SLASH", "PERCENT", "DOT", 
			"COMMA", "OPARENT", "CPARENT", "OBRACE", "CBRACE", "EXCLAM", "SEMI", 
			"EQEQ", "NEQ", "GEQ", "LEQ", "AND", "OR", "PRINT", "PRINTLN", "PRINTX", 
			"PRINTLNX", "WHILE", "RETURN", "IF", "ELSE", "INSTANCEOF", "TRUE", "FALSE", 
			"NULL", "CLASS", "EXTENDS", "PROTECTED", "ASM", "THIS", "NEW", "READFLOAT", 
			"READINT", "POSITIVE_DIGIT", "INT", "NUM", "SIGN", "EXP", "DEC", "FLOATDEC", 
			"DIGITHEX", "NUMHEX", "FLOATHEX", "FLOAT", "STRING_CAR", "STRING", "MULTI_LINE_STRING", 
			"COMMENT", "SEP", "FILENAME", "INCLUDE", "DIGIT", "LETTER", "IDENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'<'", "'>'", "'='", "'+'", "'-'", "'*'", "'/'", "'%'", "'.'", 
			"','", "'('", "')'", "'{'", "'}'", "'!'", "';'", "'=='", "'!='", "'>='", 
			"'<='", "'&&'", "'||'", "'print'", "'println'", "'printx'", "'printlnx'", 
			"'while'", "'return'", "'if'", "'else'", "'instanceof'", "'true'", "'false'", 
			"'null'", "'class'", "'extends'", "'protected'", "'asm'", "'this'", "'new'", 
			"'readFloat'", "'readInt'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LT", "GT", "EQUALS", "PLUS", "MINUS", "TIMES", "SLASH", "PERCENT", 
			"DOT", "COMMA", "OPARENT", "CPARENT", "OBRACE", "CBRACE", "EXCLAM", "SEMI", 
			"EQEQ", "NEQ", "GEQ", "LEQ", "AND", "OR", "PRINT", "PRINTLN", "PRINTX", 
			"PRINTLNX", "WHILE", "RETURN", "IF", "ELSE", "INSTANCEOF", "TRUE", "FALSE", 
			"NULL", "CLASS", "EXTENDS", "PROTECTED", "ASM", "THIS", "NEW", "READFLOAT", 
			"READINT", "INT", "FLOAT", "STRING", "MULTI_LINE_STRING", "SEP", "INCLUDE", 
			"IDENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}




	public DecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DecaLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 57:
			SEP_action((RuleContext)_localctx, actionIndex);
			break;
		case 59:
			INCLUDE_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void SEP_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			skip();
			break;
		}
	}
	private void INCLUDE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 doInclude(getText());
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\63\u01db\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3"+
		"\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3"+
		"\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3"+
		"\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3"+
		"\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3"+
		"\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3"+
		"\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3\"\3\""+
		"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3"+
		"%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3"+
		")\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3+\3,\3,\3"+
		"-\3-\3-\7-\u013e\n-\f-\16-\u0141\13-\5-\u0143\n-\3.\6.\u0146\n.\r.\16"+
		".\u0147\3/\5/\u014b\n/\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\62\3"+
		"\62\3\62\3\62\5\62\u0159\n\62\3\62\5\62\u015c\n\62\3\63\3\63\3\64\6\64"+
		"\u0161\n\64\r\64\16\64\u0162\3\65\3\65\3\65\3\65\5\65\u0169\n\65\3\65"+
		"\3\65\3\65\3\65\3\65\3\65\3\65\5\65\u0172\n\65\3\66\3\66\5\66\u0176\n"+
		"\66\3\67\3\67\38\38\38\38\38\38\78\u0180\n8\f8\168\u0183\138\38\38\39"+
		"\39\39\39\39\39\39\79\u018e\n9\f9\169\u0191\139\39\39\3:\3:\3:\3:\7:\u0199"+
		"\n:\f:\16:\u019c\13:\3:\3:\3:\3:\7:\u01a2\n:\f:\16:\u01a5\13:\3:\3:\5"+
		":\u01a9\n:\3;\3;\5;\u01ad\n;\3;\3;\3<\3<\3<\6<\u01b4\n<\r<\16<\u01b5\3"+
		"=\3=\3=\3=\3=\3=\3=\3=\3=\3=\7=\u01c2\n=\f=\16=\u01c5\13=\3=\3=\3=\3="+
		"\3=\3>\3>\3?\3?\3@\3@\5@\u01d2\n@\3@\3@\3@\7@\u01d7\n@\f@\16@\u01da\13"+
		"@\3\u01a3\2A\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W\2Y-[\2]\2_\2a\2c\2e\2g\2i"+
		"\2k.m\2o/q\60s\2u\61w\2y\62{\2}\2\177\63\3\2\r\4\2--//\4\2GGgg\4\2HHh"+
		"h\5\2\62;CHch\4\2RRrr\5\2\f\f$$^^\3\2\f\f\5\2\13\f\17\17\"\"\4\2/\60a"+
		"a\4\2C\\c|\4\2&&aa\2\u01e9\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2"+
		"\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67"+
		"\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2"+
		"\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2"+
		"\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2Y\3\2\2\2\2k\3\2\2\2\2o\3\2\2\2\2q"+
		"\3\2\2\2\2u\3\2\2\2\2y\3\2\2\2\2\177\3\2\2\2\3\u0081\3\2\2\2\5\u0083\3"+
		"\2\2\2\7\u0085\3\2\2\2\t\u0087\3\2\2\2\13\u0089\3\2\2\2\r\u008b\3\2\2"+
		"\2\17\u008d\3\2\2\2\21\u008f\3\2\2\2\23\u0091\3\2\2\2\25\u0093\3\2\2\2"+
		"\27\u0095\3\2\2\2\31\u0097\3\2\2\2\33\u0099\3\2\2\2\35\u009b\3\2\2\2\37"+
		"\u009d\3\2\2\2!\u009f\3\2\2\2#\u00a1\3\2\2\2%\u00a4\3\2\2\2\'\u00a7\3"+
		"\2\2\2)\u00aa\3\2\2\2+\u00ad\3\2\2\2-\u00b0\3\2\2\2/\u00b3\3\2\2\2\61"+
		"\u00b9\3\2\2\2\63\u00c1\3\2\2\2\65\u00c8\3\2\2\2\67\u00d1\3\2\2\29\u00d7"+
		"\3\2\2\2;\u00de\3\2\2\2=\u00e1\3\2\2\2?\u00e6\3\2\2\2A\u00f1\3\2\2\2C"+
		"\u00f6\3\2\2\2E\u00fc\3\2\2\2G\u0101\3\2\2\2I\u0107\3\2\2\2K\u010f\3\2"+
		"\2\2M\u0119\3\2\2\2O\u011d\3\2\2\2Q\u0122\3\2\2\2S\u0126\3\2\2\2U\u0130"+
		"\3\2\2\2W\u0138\3\2\2\2Y\u0142\3\2\2\2[\u0145\3\2\2\2]\u014a\3\2\2\2_"+
		"\u014c\3\2\2\2a\u0150\3\2\2\2c\u0158\3\2\2\2e\u015d\3\2\2\2g\u0160\3\2"+
		"\2\2i\u0168\3\2\2\2k\u0175\3\2\2\2m\u0177\3\2\2\2o\u0179\3\2\2\2q\u0186"+
		"\3\2\2\2s\u01a8\3\2\2\2u\u01ac\3\2\2\2w\u01b3\3\2\2\2y\u01b7\3\2\2\2{"+
		"\u01cb\3\2\2\2}\u01cd\3\2\2\2\177\u01d1\3\2\2\2\u0081\u0082\7>\2\2\u0082"+
		"\4\3\2\2\2\u0083\u0084\7@\2\2\u0084\6\3\2\2\2\u0085\u0086\7?\2\2\u0086"+
		"\b\3\2\2\2\u0087\u0088\7-\2\2\u0088\n\3\2\2\2\u0089\u008a\7/\2\2\u008a"+
		"\f\3\2\2\2\u008b\u008c\7,\2\2\u008c\16\3\2\2\2\u008d\u008e\7\61\2\2\u008e"+
		"\20\3\2\2\2\u008f\u0090\7\'\2\2\u0090\22\3\2\2\2\u0091\u0092\7\60\2\2"+
		"\u0092\24\3\2\2\2\u0093\u0094\7.\2\2\u0094\26\3\2\2\2\u0095\u0096\7*\2"+
		"\2\u0096\30\3\2\2\2\u0097\u0098\7+\2\2\u0098\32\3\2\2\2\u0099\u009a\7"+
		"}\2\2\u009a\34\3\2\2\2\u009b\u009c\7\177\2\2\u009c\36\3\2\2\2\u009d\u009e"+
		"\7#\2\2\u009e \3\2\2\2\u009f\u00a0\7=\2\2\u00a0\"\3\2\2\2\u00a1\u00a2"+
		"\7?\2\2\u00a2\u00a3\7?\2\2\u00a3$\3\2\2\2\u00a4\u00a5\7#\2\2\u00a5\u00a6"+
		"\7?\2\2\u00a6&\3\2\2\2\u00a7\u00a8\7@\2\2\u00a8\u00a9\7?\2\2\u00a9(\3"+
		"\2\2\2\u00aa\u00ab\7>\2\2\u00ab\u00ac\7?\2\2\u00ac*\3\2\2\2\u00ad\u00ae"+
		"\7(\2\2\u00ae\u00af\7(\2\2\u00af,\3\2\2\2\u00b0\u00b1\7~\2\2\u00b1\u00b2"+
		"\7~\2\2\u00b2.\3\2\2\2\u00b3\u00b4\7r\2\2\u00b4\u00b5\7t\2\2\u00b5\u00b6"+
		"\7k\2\2\u00b6\u00b7\7p\2\2\u00b7\u00b8\7v\2\2\u00b8\60\3\2\2\2\u00b9\u00ba"+
		"\7r\2\2\u00ba\u00bb\7t\2\2\u00bb\u00bc\7k\2\2\u00bc\u00bd\7p\2\2\u00bd"+
		"\u00be\7v\2\2\u00be\u00bf\7n\2\2\u00bf\u00c0\7p\2\2\u00c0\62\3\2\2\2\u00c1"+
		"\u00c2\7r\2\2\u00c2\u00c3\7t\2\2\u00c3\u00c4\7k\2\2\u00c4\u00c5\7p\2\2"+
		"\u00c5\u00c6\7v\2\2\u00c6\u00c7\7z\2\2\u00c7\64\3\2\2\2\u00c8\u00c9\7"+
		"r\2\2\u00c9\u00ca\7t\2\2\u00ca\u00cb\7k\2\2\u00cb\u00cc\7p\2\2\u00cc\u00cd"+
		"\7v\2\2\u00cd\u00ce\7n\2\2\u00ce\u00cf\7p\2\2\u00cf\u00d0\7z\2\2\u00d0"+
		"\66\3\2\2\2\u00d1\u00d2\7y\2\2\u00d2\u00d3\7j\2\2\u00d3\u00d4\7k\2\2\u00d4"+
		"\u00d5\7n\2\2\u00d5\u00d6\7g\2\2\u00d68\3\2\2\2\u00d7\u00d8\7t\2\2\u00d8"+
		"\u00d9\7g\2\2\u00d9\u00da\7v\2\2\u00da\u00db\7w\2\2\u00db\u00dc\7t\2\2"+
		"\u00dc\u00dd\7p\2\2\u00dd:\3\2\2\2\u00de\u00df\7k\2\2\u00df\u00e0\7h\2"+
		"\2\u00e0<\3\2\2\2\u00e1\u00e2\7g\2\2\u00e2\u00e3\7n\2\2\u00e3\u00e4\7"+
		"u\2\2\u00e4\u00e5\7g\2\2\u00e5>\3\2\2\2\u00e6\u00e7\7k\2\2\u00e7\u00e8"+
		"\7p\2\2\u00e8\u00e9\7u\2\2\u00e9\u00ea\7v\2\2\u00ea\u00eb\7c\2\2\u00eb"+
		"\u00ec\7p\2\2\u00ec\u00ed\7e\2\2\u00ed\u00ee\7g\2\2\u00ee\u00ef\7q\2\2"+
		"\u00ef\u00f0\7h\2\2\u00f0@\3\2\2\2\u00f1\u00f2\7v\2\2\u00f2\u00f3\7t\2"+
		"\2\u00f3\u00f4\7w\2\2\u00f4\u00f5\7g\2\2\u00f5B\3\2\2\2\u00f6\u00f7\7"+
		"h\2\2\u00f7\u00f8\7c\2\2\u00f8\u00f9\7n\2\2\u00f9\u00fa\7u\2\2\u00fa\u00fb"+
		"\7g\2\2\u00fbD\3\2\2\2\u00fc\u00fd\7p\2\2\u00fd\u00fe\7w\2\2\u00fe\u00ff"+
		"\7n\2\2\u00ff\u0100\7n\2\2\u0100F\3\2\2\2\u0101\u0102\7e\2\2\u0102\u0103"+
		"\7n\2\2\u0103\u0104\7c\2\2\u0104\u0105\7u\2\2\u0105\u0106\7u\2\2\u0106"+
		"H\3\2\2\2\u0107\u0108\7g\2\2\u0108\u0109\7z\2\2\u0109\u010a\7v\2\2\u010a"+
		"\u010b\7g\2\2\u010b\u010c\7p\2\2\u010c\u010d\7f\2\2\u010d\u010e\7u\2\2"+
		"\u010eJ\3\2\2\2\u010f\u0110\7r\2\2\u0110\u0111\7t\2\2\u0111\u0112\7q\2"+
		"\2\u0112\u0113\7v\2\2\u0113\u0114\7g\2\2\u0114\u0115\7e\2\2\u0115\u0116"+
		"\7v\2\2\u0116\u0117\7g\2\2\u0117\u0118\7f\2\2\u0118L\3\2\2\2\u0119\u011a"+
		"\7c\2\2\u011a\u011b\7u\2\2\u011b\u011c\7o\2\2\u011cN\3\2\2\2\u011d\u011e"+
		"\7v\2\2\u011e\u011f\7j\2\2\u011f\u0120\7k\2\2\u0120\u0121\7u\2\2\u0121"+
		"P\3\2\2\2\u0122\u0123\7p\2\2\u0123\u0124\7g\2\2\u0124\u0125\7y\2\2\u0125"+
		"R\3\2\2\2\u0126\u0127\7t\2\2\u0127\u0128\7g\2\2\u0128\u0129\7c\2\2\u0129"+
		"\u012a\7f\2\2\u012a\u012b\7H\2\2\u012b\u012c\7n\2\2\u012c\u012d\7q\2\2"+
		"\u012d\u012e\7c\2\2\u012e\u012f\7v\2\2\u012fT\3\2\2\2\u0130\u0131\7t\2"+
		"\2\u0131\u0132\7g\2\2\u0132\u0133\7c\2\2\u0133\u0134\7f\2\2\u0134\u0135"+
		"\7K\2\2\u0135\u0136\7p\2\2\u0136\u0137\7v\2\2\u0137V\3\2\2\2\u0138\u0139"+
		"\4\63;\2\u0139X\3\2\2\2\u013a\u0143\7\62\2\2\u013b\u013f\5W,\2\u013c\u013e"+
		"\5{>\2\u013d\u013c\3\2\2\2\u013e\u0141\3\2\2\2\u013f\u013d\3\2\2\2\u013f"+
		"\u0140\3\2\2\2\u0140\u0143\3\2\2\2\u0141\u013f\3\2\2\2\u0142\u013a\3\2"+
		"\2\2\u0142\u013b\3\2\2\2\u0143Z\3\2\2\2\u0144\u0146\5{>\2\u0145\u0144"+
		"\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0145\3\2\2\2\u0147\u0148\3\2\2\2\u0148"+
		"\\\3\2\2\2\u0149\u014b\t\2\2\2\u014a\u0149\3\2\2\2\u014a\u014b\3\2\2\2"+
		"\u014b^\3\2\2\2\u014c\u014d\t\3\2\2\u014d\u014e\5]/\2\u014e\u014f\5[."+
		"\2\u014f`\3\2\2\2\u0150\u0151\5[.\2\u0151\u0152\7\60\2\2\u0152\u0153\5"+
		"[.\2\u0153b\3\2\2\2\u0154\u0159\5a\61\2\u0155\u0156\5a\61\2\u0156\u0157"+
		"\5_\60\2\u0157\u0159\3\2\2\2\u0158\u0154\3\2\2\2\u0158\u0155\3\2\2\2\u0159"+
		"\u015b\3\2\2\2\u015a\u015c\t\4\2\2\u015b\u015a\3\2\2\2\u015b\u015c\3\2"+
		"\2\2\u015cd\3\2\2\2\u015d\u015e\t\5\2\2\u015ef\3\2\2\2\u015f\u0161\5e"+
		"\63\2\u0160\u015f\3\2\2\2\u0161\u0162\3\2\2\2\u0162\u0160\3\2\2\2\u0162"+
		"\u0163\3\2\2\2\u0163h\3\2\2\2\u0164\u0165\7\62\2\2\u0165\u0169\7z\2\2"+
		"\u0166\u0167\7\62\2\2\u0167\u0169\7Z\2\2\u0168\u0164\3\2\2\2\u0168\u0166"+
		"\3\2\2\2\u0169\u016a\3\2\2\2\u016a\u016b\5g\64\2\u016b\u016c\7\60\2\2"+
		"\u016c\u016d\5g\64\2\u016d\u016e\t\6\2\2\u016e\u016f\5]/\2\u016f\u0171"+
		"\5[.\2\u0170\u0172\t\4\2\2\u0171\u0170\3\2\2\2\u0171\u0172\3\2\2\2\u0172"+
		"j\3\2\2\2\u0173\u0176\5c\62\2\u0174\u0176\5i\65\2\u0175\u0173\3\2\2\2"+
		"\u0175\u0174\3\2\2\2\u0176l\3\2\2\2\u0177\u0178\n\7\2\2\u0178n\3\2\2\2"+
		"\u0179\u0181\7$\2\2\u017a\u0180\5m\67\2\u017b\u017c\7^\2\2\u017c\u0180"+
		"\7$\2\2\u017d\u017e\7^\2\2\u017e\u0180\7^\2\2\u017f\u017a\3\2\2\2\u017f"+
		"\u017b\3\2\2\2\u017f\u017d\3\2\2\2\u0180\u0183\3\2\2\2\u0181\u017f\3\2"+
		"\2\2\u0181\u0182\3\2\2\2\u0182\u0184\3\2\2\2\u0183\u0181\3\2\2\2\u0184"+
		"\u0185\7$\2\2\u0185p\3\2\2\2\u0186\u018f\7$\2\2\u0187\u018e\5m\67\2\u0188"+
		"\u018e\7\f\2\2\u0189\u018a\7^\2\2\u018a\u018e\7$\2\2\u018b\u018c\7^\2"+
		"\2\u018c\u018e\7^\2\2\u018d\u0187\3\2\2\2\u018d\u0188\3\2\2\2\u018d\u0189"+
		"\3\2\2\2\u018d\u018b\3\2\2\2\u018e\u0191\3\2\2\2\u018f\u018d\3\2\2\2\u018f"+
		"\u0190\3\2\2\2\u0190\u0192\3\2\2\2\u0191\u018f\3\2\2\2\u0192\u0193\7$"+
		"\2\2\u0193r\3\2\2\2\u0194\u0195\7\61\2\2\u0195\u0196\7\61\2\2\u0196\u019a"+
		"\3\2\2\2\u0197\u0199\n\b\2\2\u0198\u0197\3\2\2\2\u0199\u019c\3\2\2\2\u019a"+
		"\u0198\3\2\2\2\u019a\u019b\3\2\2\2\u019b\u01a9\3\2\2\2\u019c\u019a\3\2"+
		"\2\2\u019d\u019e\7\61\2\2\u019e\u019f\7,\2\2\u019f\u01a3\3\2\2\2\u01a0"+
		"\u01a2\13\2\2\2\u01a1\u01a0\3\2\2\2\u01a2\u01a5\3\2\2\2\u01a3\u01a4\3"+
		"\2\2\2\u01a3\u01a1\3\2\2\2\u01a4\u01a6\3\2\2\2\u01a5\u01a3\3\2\2\2\u01a6"+
		"\u01a7\7,\2\2\u01a7\u01a9\7\61\2\2\u01a8\u0194\3\2\2\2\u01a8\u019d\3\2"+
		"\2\2\u01a9t\3\2\2\2\u01aa\u01ad\t\t\2\2\u01ab\u01ad\5s:\2\u01ac\u01aa"+
		"\3\2\2\2\u01ac\u01ab\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae\u01af\b;\2\2\u01af"+
		"v\3\2\2\2\u01b0\u01b4\5}?\2\u01b1\u01b4\5{>\2\u01b2\u01b4\t\n\2\2\u01b3"+
		"\u01b0\3\2\2\2\u01b3\u01b1\3\2\2\2\u01b3\u01b2\3\2\2\2\u01b4\u01b5\3\2"+
		"\2\2\u01b5\u01b3\3\2\2\2\u01b5\u01b6\3\2\2\2\u01b6x\3\2\2\2\u01b7\u01b8"+
		"\7%\2\2\u01b8\u01b9\7k\2\2\u01b9\u01ba\7p\2\2\u01ba\u01bb\7e\2\2\u01bb"+
		"\u01bc\7n\2\2\u01bc\u01bd\7w\2\2\u01bd\u01be\7f\2\2\u01be\u01bf\7g\2\2"+
		"\u01bf\u01c3\3\2\2\2\u01c0\u01c2\7\"\2\2\u01c1\u01c0\3\2\2\2\u01c2\u01c5"+
		"\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c3\u01c4\3\2\2\2\u01c4\u01c6\3\2\2\2\u01c5"+
		"\u01c3\3\2\2\2\u01c6\u01c7\7$\2\2\u01c7\u01c8\5w<\2\u01c8\u01c9\7$\2\2"+
		"\u01c9\u01ca\b=\3\2\u01caz\3\2\2\2\u01cb\u01cc\4\62;\2\u01cc|\3\2\2\2"+
		"\u01cd\u01ce\t\13\2\2\u01ce~\3\2\2\2\u01cf\u01d2\5}?\2\u01d0\u01d2\t\f"+
		"\2\2\u01d1\u01cf\3\2\2\2\u01d1\u01d0\3\2\2\2\u01d2\u01d8\3\2\2\2\u01d3"+
		"\u01d7\5}?\2\u01d4\u01d7\5{>\2\u01d5\u01d7\t\f\2\2\u01d6\u01d3\3\2\2\2"+
		"\u01d6\u01d4\3\2\2\2\u01d6\u01d5\3\2\2\2\u01d7\u01da\3\2\2\2\u01d8\u01d6"+
		"\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9\u0080\3\2\2\2\u01da\u01d8\3\2\2\2\33"+
		"\2\u013f\u0142\u0147\u014a\u0158\u015b\u0162\u0168\u0171\u0175\u017f\u0181"+
		"\u018d\u018f\u019a\u01a3\u01a8\u01ac\u01b3\u01b5\u01c3\u01d1\u01d6\u01d8"+
		"\4\3;\2\3=\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}