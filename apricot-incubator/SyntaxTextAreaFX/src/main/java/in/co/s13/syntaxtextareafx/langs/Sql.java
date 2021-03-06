package in.co.s13.syntaxtextareafx.langs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.co.s13.syntaxtextareafx.meta.Language;
import java.util.Collections;

/**
 *
 * @author nika
 */
public class Sql implements Language {

    String ORACLE_BUILT_IN_DATATYPES[] = new String[]{"N?VARCHAR2","NUMBER","LONG","DATE","DATETIME", "BIT", "BIGINT", "TIMESTAMP","INTERVAL","(LONG[ \\t\\n]+)?RAW","U?ROWID","N?CHAR","(N?C|B)LOB","BFILE","BINARY_(FLOAT|DOUBLE)"};
    String ANSI_DATATYPES[] = new String[]{"(NATIONAL[ \\t\\n]+)?CHAR(ACTER)?([ \\t\\n]+VARYING)?","NCHAR([ \\t\\n]+VARYING)?","NUMERIC|DECIMAL","INTEGER|INT|SMALLINT","FLOAT|DOUBLE[ \\t\\n]+PRECISION|REAL"};
    String SQL_DS_AND_DB2_DATATYPES[] = new String[]{"CHARACTER","(LONG[ \\t\\n]+)?VARCHAR","DECIMAL","INTEGER|SMALLINT","FLOAT"};
    String ORACLE_SUPPLIED_TYPES[] = new String[]{"SYS\\.ANY(TYPE|DATA(SET)?)","XMLType","(HTTP|XDB|DB)?URIType","(MDSYS\\.)?SDO_((TOPO_)?GEOMETRY|GEORASTER)","ORDSYS\\.ORD(Audio|Doc|Image(Signature)?|Video)","SI_(StillImage|(Average|Positional)?Color|ColorHistogram|Texture|FeatureList)"};
    String UNLIMITED[] = new String[]{"UNLIMITED"};
    String NULL[] = new String[]{"NULL"};
    String NUMERIC_FUNCTIONS[] = new String[]{"ABS","A(COS|SIN|TAN2?)","BITAND","CEIL","(COS|SIN|TAN)H?","EXP","FLOOR","LN","LOG","MOD","NANVL","POWER","REMAINDER","ROUND","SIGN","SQRT","TRUNC","WIDTH_BUCKET"};
    String CHARACTER_FUNCTIONS_RETURNING_CHARACTER_VALUES[] = new String[]{"N?CHR","CONCAT","(NLS_)?(INITCAP|LOWER|UPPER)","(L|R)PAD","(L|R)?TRIM","NLSSORT","REGEXP_(REPLACE|SUBSTR)","REPLACE","SOUNDEX","SUBSTR","TRANSLATE","TREAT"};
    String NLS_CHARACTER_FUNCTIONS[] = new String[]{"NLS_CHARSET_DECL_LEN","NLS_CHARSET_(ID|NAME)"};
    String CHARACTER_FUNCTIONS_RETURNING_NUMBER_VALUES[] = new String[]{"ASCII","INSTR","LENGTH","REGEXP_INSTR"};
    String DATETIME_FUNCTIONS[] = new String[]{"ADD_MONTHS","CURRENT_(DATE|TIMESTAMP)","DBTIMEZONE","EXTRACT","FROM_TZ","(LAST|NEXT)_DAY","LOCALTIMESTAMP","MONTHS_BETWEEN","NEW_TIME","NUMTO(DS|YM)INTERVAL","ROUND","SESSIONTIMEZONE","SYS_EXTRACT_UTC","SYS(DATE|TIMESTAMP)","TO_CHAR","TO_(DS|YM)INTERVAL","TO_TIMESTAMP(_TZ)?","TRUNC","TZ_OFFSET"};
    String GENERAL_COMPARISON_FUNCTIONS[] = new String[]{"GREATEST","LEAST"};
    String CONVERSION_FUNCTIONS[] = new String[]{"ASCIISTR","BIN_TO_NUM","CAST","CHARTOROWID","(DE)?COMPOSE","CONVERT","HEXTORAW","NUMTO(DS|YM)INTERVAL","RAWTON?HEX","ROWIDTON?CHAR","SCN_TO_TIMESTAMP","TIMESTAMP_TO_SCN","TO_BINARY_(DOUBLE|FLOAT)","TO_N?(CHAR|CLOB)","TO_DATE","TO_(DS|YM)INTERVAL","TO_LOB","TO_(MULTI|SINGLE)_BYTE","TO_NUMBER","TRANSLATE","UNISTR"};
    String LARGE_OBJECT_FUNCTIONS[] = new String[]{"BFILENAME","EMPTY_(B|C)LOB"};
    String COLLECTION_FUNCTIONS[] = new String[]{"CARDINALITY","COLLECT","POWERMULTISET(_BY_CARDINALITY)?"};
    String HIERARCHICAL_FUNCTION[] = new String[]{"SYS_CONNECT_BY_PATH"};
    String DATA_MINING_FUNCTIONS[] = new String[]{"CLUSTER_(ID|PROBABILITY|SET)","FEATURE_(ID|SET|VALUE)","PREDICTION","PREDICTION_(COST|DETAILS|PROBABILITY|SET)"};
    String XML_FUNCTIONS[] = new String[]{"(APPEND|INSERT)CHILDXML","(DELETE|UPDATE)XML","DEPTH","EXISTSNODE","EXTRACT(VALUE)?","INSERTXMLBEFORE","PATH","SYS_DBURIGEN","SYS_XML(AGG|GEN)","XML(AGG|CDATA|COLATTVAL|COMMENT|CONCAT|ELEMENT|FOREST|PARSE|PI|QUERY|ROOT|SEQUENCE|SERIALIZE|TABLE|TRANSFORM)"};
    String ENCODING_AND_DECODING_FUNCTIONS[] = new String[]{"DECODE","DUMP","ORA_HASH","VSIZE"};
    String NULL_RELATED_FUNCTIONS[] = new String[]{"COALESCE","LNNVL","NULLIF","NVL2?"};
    String ENVIRONMENT_AND_IDENTIFIER_FUNCTIONS[] = new String[]{"SYS_CONTEXT","SYS_GUID","SYS_TYPEID","UID","USER","USERENV"};
    String AGGREGATE_FUNCTIONS[] = new String[]{"AVG","CORR(_(S|K))?","COUNT","COVAR_(POP|SAMP)","CUME_DIST","(DENSE|PERCENT)_RANK","FIRST|LAST","GROUP_ID","GROUPING(_ID)?","MAX|MIN","MEDIAN","PERCENTILE_(CONT|DISC)","RANK","REGR_(SLOPE|INTERCEPT|COUNT|R2|AVGX|AVGY|SXX|SYY|SXY)","STATS_((BINOMIAL|F|KS|MW|WSR)_TEST|CROSSTAB|MODE|ONE_WAY_ANOVA|T_TEST_(ONE|PAIRED|INDEPU?))","STDDEV|VARIANCE","(STDDEV|VAR)_(POP|SAMP)","SUM"};
    String ANALYTIC_FUNCTIONS[] = new String[]{"AVG","CORR","COVAR_(POP|SAMP)","COUNT","CUME_DIST","(DENSE|PERCENT)_RANK","(FIRST|LAST)(_VALUE)?","LAG","LEAD","MAX|MIN","NTILE","PERCENTILE_(CONT|DISC)","RANK","RATIO_TO_REPORT","REGR_(SLOPE|INTERCEPT|COUNT|R2|AVGX|AVGY|SXX|SYY|SXY)","ROW_NUMBER","STDDEV|VARIANCE","(STDDEV|VAR)_(POP|SAMP)","SUM"};
    String OBJECT_REFERENCE_FUNCTIONS[] = new String[]{"DEREF","MAKE_REF","REF","REFTOHEX","VALUE"};
    String MODEL_FUNCTIONS[] = new String[]{"CV","ITERATION_NUMBER","PRESENT(NN)?V","PREVIOUS"};
    String ANSI_RESERVED_WORDS[] = new String[]{"ADD","ALL","ALTER","CONSTRAINT", "AND","ANY","AS","ASC","BETWEEN","BY","CASE","CHECK","CREATE","CROSS","CURRENT","DECIMAL","DEFAULT","DELETE","DISTINCT","DROP","ELSE","END","FLOAT","FOR","FROM","FULL","GRANT","GROUP","HAVING","IMMEDIATE","INNER","INSERT","INTEGER","INTERSECT","INTO","IN","IS","JOIN","LEFT","LEVEL","LIKE","NATURAL","NOT","OF","ON","OPTION","ORDER","OR","OUTER","PRIOR","PRIVILEGES","PUBLIC","REVOKE","RIGHT","ROWS","SELECT","SESSION","SET","SIZE","SMALLINT","TABLE","THEN","TO","UNION","UNIQUE","UPDATE","USING","VALUES","VIEW","WHEN","WITH", "PRIMARY KEY", "FOREIGN KEY", "REFERENCES"};
    String ORACLE_RESERVED_WORDS[] = new String[]{"ACCESS","AUDIT","CLUSTER","COMMENT","COMPRESS","CONNECT[ \\t]+BY","CUBE","EXCLUSIVE","EXISTS","FILE","GROUPING[ \\t]+SETS","IDENTIFIED","INCREMENT","INDEX","INITIAL","LOCK","MAXEXTENTS","MINUS","MLSLABEL","MODE","MODIFY","NOAUDIT","NOCOMPRESS","NOCYCLE","NOWAIT","OFFLINE","ONLINE","PCTFREE","RENAME","RESOURCE","ROLLUP","ROW","ROWNUM","SHARE","SIBLINGS","START[ \\t]+WITH","SUCCESSFUL","SYNONYM","TRIGGER","VALIDATE","WHERE"};
    String SQL_STATEMENTS[] = new String[]{"ALTER[ \\t]+(CLUSTER|DATABASE|DIMENSION|DISKGROUP|FUNCTION|INDEX(TYPE)?|JAVA|MATERIALIZED[ \\t]+VIEW([ \\t]+LOG)?|OPERATOR|OUTLINE|PACKAGE|PROCEDURE|PROFILE|RESOURCE[ \\t]+COST|ROLE|ROLLBACK[ \\t]+SEGMENT|SEQUENCE|SESSION|SYSTEM|TABLE(SPACE)?|TRIGGER|TYPE|USER|VIEW)","ANALYZE","(DIS)?ASSOCIATE[ \\t]+STATISTICS","CALL","COMMIT([ \\t]+WORK)?","CREATE[ \\t]+(CLUSTER|CONTEXT|CONTROLFILE|DATABASE([ \\t]+LINK)?|DIMENSION|DIRECTORY|DISKGROUP|FUNCTION|INDEX(TYPE)?|JAVA|LIBRARY|MATERIALIZED[ \\t]+VIEW([ \\t]+LOG)?|OPERATOR|OUTLINE|PACKAGE([ \\t]+BODY)?|S?PFILE|PROCEDURE|PROFILE|RESTORE[ \\t]+POINT|ROLE|ROLLBACK[ \\t]+SEGMENT|SCHEMA|SEQUENCE|SYNONYM|TABLE(SPACE)?|TRIGGER|TYPE([ \\t]+BODY)?|USER|VIEW)","DROP[ \\t]+(CLUSTER|CONTEXT|DATABASE([ \\t]+LINK)?|DIMENSION|DIRECTORY|DISKGROUP|FUNCTION|INDEX(TYPE)?|JAVA|LIBRARY|MATERIALIZED[ \\t]+VIEW([ \\t]+LOG)?|OPERATOR|OUTLINE|PACKAGE|PROCEDURE|PROFILE|RESTORE[ \\t]+POINT|ROLE|ROLLBACK[ \\t]+SEGMENT|SEQUENCE|SYNONYM|TABLE(SPACE)?|TRIGGER|TYPE([ \\t]+BODY)?|USER|VIEW)","EXPLAIN[ \\t]+PLAN","FLASHBACK[ \\t]+(DATABASE|TABLE)","LOCK[ \\t]+TABLE","MERGE","PURGE","ROLLBACK","SAVEPOINT","SET[ \\t]+CONSTRAINTS?","SET[ \\t]+ROLE","SET[ \\t]+TRANSACTION","TRUNCATE"};
    String OPERATORS[] = new String[]{"CONNECT_BY_ROOT","MULTISET[ \\t]+(EXCEPT|INTERSECT|UNION)"};
    String CONDITIONS[] = new String[]{"SOME","IS[ \\t]+(NOT[ \\t]+)?(NAN|INFINITE)","IS[ \\t]+(NOT[ \\t]+)?NULL","(EQUALS|UNDER)_PATH","(NOT[ \\t]+)?IN","IS[ \\t]+(NOT[ \\t]+)?A[ \\t]+SET","IS[ \\t]+(NOT[ \\t]+)?EMPTY","IS[ \\t]+(NOT[ \\t]+)?OF([ \\t]+TYPE)?","IS[ \\t]+PRESENT","(NOT[ \\t]+)?LIKE(C|2|4)?","(NOT[ \\t]+)?MEMBER([ \\t]+OF)?","REGEXP_LIKE","(NOT[ \\t]+)?SUBMULTISET([ \\t]+OF)?"};
    String SQL_PLUS_COMMANDS[] = new String[]{"ACC(EPT)?","A(PPEND)?","ARCHIVE[ \\t]LOG","ATTRIBUTE","BRE(AK)?","BTI(TLE)?","C(HANGE)?","CL(EAR)?","COL(UMN)?","COMP(UTE)?","CONN(ECT)?","COPY","DEF(INE)?","DEL","DESC(RIBE)?","DISC(ONNECT)?","ED(IT)?","EXEC(UTE)?","EXIT|QUIT","GET","HELP","HO(ST)?","I(NPUT)?","L(IST)?","PASSW(ORD)?","PAU(SE)?","PRI(NT)?","PRO(MPT)?","RECOVER","REM(ARK)?","REPF(OOTER)?","REPH(EADER)?","R(UN)?","SAV(E)?","SET[ \\t]+(APPI(NFO)?|ARRAY(SIZE)?|AUTO(COMMIT)?|AUTOP(RINT)?|AUTORECOVERY|AUTOT(RACE)?|BLO(CKTERMINATOR)?|CMDS(EP)?|COLSEP|COM(PATIBILITY)?|CON(CAT)?|COPYC(OMMIT)?|COPYTYPECHECK|DEF(INE)?|DESCRIBE|ECHO|EDITF(ILE)?|EMB(EDDED)?|ESC(APE)?|FEED(BACK)?|FLAGGER|FLU(SH)?|HEA(DING)?|HEADS(EP)?|INSTANCE|LIN(ESIZE)?|LOBOF(FSET)?|LOGSOURCE|LONG|LONGC(HUNKSIZE)?|MARK(UP)?|NEWP(AGE)?|NULL|NUMF(ORMAT)?|NUM(WIDTH)?|PAGES(IZE)?|PAU(SE)?|RECSEP|RECSEPCHAR|SERVEROUT(PUT)?|SHIFT(INOUT)?|SHOW(MODE)?|SQLBL(ANKLINES)?|SQLC(ASE)?|SQLCO(NTINUE)?|SQLN(UMBER)?|SQLPLUSCOMPAT(IBILITY)?|SQLPRE(FIX)?|SQLP(ROMPT)?|SQLT(ERMINATOR)?|SUF(FIX)?|TAB|TERM(OUT)?|TI(ME)?|TIMI(NG)?|TRIM(OUT)?|TRIMS(POOL)?|UND(ERLINE)?|VER(IFY)?|WRA(P)?)","SHO(W)?","SHUTDOWN","SPO(OL)?","STA(RT)?","STARTUP","STORE","TIMI(NG)?","TTI(TLE)?","UNDEF(INE)?","VAR(IABLE)?","WHENEVER[ \\t]+(OS|SQL)ERROR"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String ORACLE_BUILT_IN_DATATYPES_PATTERN;
        String ANSI_DATATYPES_PATTERN;
        String SQL_DS_AND_DB2_DATATYPES_PATTERN;
        String ORACLE_SUPPLIED_TYPES_PATTERN;
        String UNLIMITED_PATTERN;
        String NULL_PATTERN;
        String NUMERIC_FUNCTIONS_PATTERN;
        String CHARACTER_FUNCTIONS_RETURNING_CHARACTER_VALUES_PATTERN;
        String NLS_CHARACTER_FUNCTIONS_PATTERN;
        String CHARACTER_FUNCTIONS_RETURNING_NUMBER_VALUES_PATTERN;
        String DATETIME_FUNCTIONS_PATTERN;
        String GENERAL_COMPARISON_FUNCTIONS_PATTERN;
        String CONVERSION_FUNCTIONS_PATTERN;
        String LARGE_OBJECT_FUNCTIONS_PATTERN;
        String COLLECTION_FUNCTIONS_PATTERN;
        String HIERARCHICAL_FUNCTION_PATTERN;
        String DATA_MINING_FUNCTIONS_PATTERN;
        String XML_FUNCTIONS_PATTERN;
        String ENCODING_AND_DECODING_FUNCTIONS_PATTERN;
        String NULL_RELATED_FUNCTIONS_PATTERN;
        String ENVIRONMENT_AND_IDENTIFIER_FUNCTIONS_PATTERN;
        String AGGREGATE_FUNCTIONS_PATTERN;
        String ANALYTIC_FUNCTIONS_PATTERN;
        String OBJECT_REFERENCE_FUNCTIONS_PATTERN;
        String MODEL_FUNCTIONS_PATTERN;
        String ANSI_RESERVED_WORDS_PATTERN;
        String ORACLE_RESERVED_WORDS_PATTERN;
        String SQL_STATEMENTS_PATTERN;
        String OPERATORS_PATTERN;
        String CONDITIONS_PATTERN;
        String SQL_PLUS_COMMANDS_PATTERN;
        String COMMENT_PATTERN;
        String STRING_LITERAL_PATTERN;
        
        ORACLE_BUILT_IN_DATATYPES_PATTERN = "\\b(" + String.join("|", ORACLE_BUILT_IN_DATATYPES) + ")\\b";
        ANSI_DATATYPES_PATTERN = "\\b(" + String.join("|", ANSI_DATATYPES) + ")\\b";
        SQL_DS_AND_DB2_DATATYPES_PATTERN = "\\b(" + String.join("|", SQL_DS_AND_DB2_DATATYPES) + ")\\b";
        ORACLE_SUPPLIED_TYPES_PATTERN = "\\b(" + String.join("|", ORACLE_SUPPLIED_TYPES) + ")\\b";
        UNLIMITED_PATTERN = "\\b(" + String.join("|", UNLIMITED) + ")\\b";
        NULL_PATTERN = "\\b(" + String.join("|", NULL) + ")\\b";
        NUMERIC_FUNCTIONS_PATTERN = "\\b(" + String.join("|", NUMERIC_FUNCTIONS) + ")\\b";
        CHARACTER_FUNCTIONS_RETURNING_CHARACTER_VALUES_PATTERN = "\\b(" + String.join("|", CHARACTER_FUNCTIONS_RETURNING_CHARACTER_VALUES) + ")\\b";
        NLS_CHARACTER_FUNCTIONS_PATTERN = "\\b(" + String.join("|", NLS_CHARACTER_FUNCTIONS) + ")\\b";
        CHARACTER_FUNCTIONS_RETURNING_NUMBER_VALUES_PATTERN = "\\b(" + String.join("|", CHARACTER_FUNCTIONS_RETURNING_NUMBER_VALUES) + ")\\b";
        DATETIME_FUNCTIONS_PATTERN = "\\b(" + String.join("|", DATETIME_FUNCTIONS) + ")\\b";
        GENERAL_COMPARISON_FUNCTIONS_PATTERN = "\\b(" + String.join("|", GENERAL_COMPARISON_FUNCTIONS) + ")\\b";
        CONVERSION_FUNCTIONS_PATTERN = "\\b(" + String.join("|", CONVERSION_FUNCTIONS) + ")\\b";
        LARGE_OBJECT_FUNCTIONS_PATTERN = "\\b(" + String.join("|", LARGE_OBJECT_FUNCTIONS) + ")\\b";
        COLLECTION_FUNCTIONS_PATTERN = "\\b(" + String.join("|", COLLECTION_FUNCTIONS) + ")\\b";
        HIERARCHICAL_FUNCTION_PATTERN = "\\b(" + String.join("|", HIERARCHICAL_FUNCTION) + ")\\b";
        DATA_MINING_FUNCTIONS_PATTERN = "\\b(" + String.join("|", DATA_MINING_FUNCTIONS) + ")\\b";
        XML_FUNCTIONS_PATTERN = "\\b(" + String.join("|", XML_FUNCTIONS) + ")\\b";
        ENCODING_AND_DECODING_FUNCTIONS_PATTERN = "\\b(" + String.join("|", ENCODING_AND_DECODING_FUNCTIONS) + ")\\b";
        NULL_RELATED_FUNCTIONS_PATTERN = "\\b(" + String.join("|", NULL_RELATED_FUNCTIONS) + ")\\b";
        ENVIRONMENT_AND_IDENTIFIER_FUNCTIONS_PATTERN = "\\b(" + String.join("|", ENVIRONMENT_AND_IDENTIFIER_FUNCTIONS) + ")\\b";
        AGGREGATE_FUNCTIONS_PATTERN = "\\b(" + String.join("|", AGGREGATE_FUNCTIONS) + ")\\b";
        ANALYTIC_FUNCTIONS_PATTERN = "\\b(" + String.join("|", ANALYTIC_FUNCTIONS) + ")\\b";
        OBJECT_REFERENCE_FUNCTIONS_PATTERN = "\\b(" + String.join("|", OBJECT_REFERENCE_FUNCTIONS) + ")\\b";
        MODEL_FUNCTIONS_PATTERN = "\\b(" + String.join("|", MODEL_FUNCTIONS) + ")\\b";
        ANSI_RESERVED_WORDS_PATTERN = "\\b(" + String.join("|", ANSI_RESERVED_WORDS) + ")\\b";
        ORACLE_RESERVED_WORDS_PATTERN = "\\b(" + String.join("|", ORACLE_RESERVED_WORDS) + ")\\b";
        SQL_STATEMENTS_PATTERN = "\\b(" + String.join("|", SQL_STATEMENTS) + ")\\b";
        OPERATORS_PATTERN = "\\b(" + String.join("|", OPERATORS) + ")\\b";
        CONDITIONS_PATTERN = "\\b(" + String.join("|", CONDITIONS) + ")\\b";
        SQL_PLUS_COMMANDS_PATTERN = "\\b(" + String.join("|", SQL_PLUS_COMMANDS) + ")\\b";
        COMMENT_PATTERN = "--[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
        STRING_LITERAL_PATTERN = "'(.|\\\\R)*?'"; 

        pattern = Pattern.compile(
                "(?<ORACLEBUILTINDATATYPES>" + ORACLE_BUILT_IN_DATATYPES_PATTERN + ")"
                + "|(?<ANSIDATATYPES>" + ANSI_DATATYPES_PATTERN + ")"
                + "|(?<SQLDSANDDB2DATATYPES>" + SQL_DS_AND_DB2_DATATYPES_PATTERN + ")"
                + "|(?<ORACLESUPPLIEDTYPES>" + ORACLE_SUPPLIED_TYPES_PATTERN + ")"
                + "|(?<UNLIMITED>" + UNLIMITED_PATTERN + ")"
                + "|(?<NULL>" + NULL_PATTERN + ")"
                + "|(?<NUMERICFUNCTIONS>" + NUMERIC_FUNCTIONS_PATTERN + ")"
                + "|(?<CHARACTERFUNCTIONSRETURNINGCHARACTERVALUES>" + CHARACTER_FUNCTIONS_RETURNING_CHARACTER_VALUES_PATTERN + ")"
                + "|(?<NLSCHARACTERFUNCTIONS>" + NLS_CHARACTER_FUNCTIONS_PATTERN + ")"
                + "|(?<CHARACTERFUNCTIONSRETURNINGNUMBERVALUES>" + CHARACTER_FUNCTIONS_RETURNING_NUMBER_VALUES_PATTERN + ")"
                + "|(?<DATETIMEFUNCTIONS>" + DATETIME_FUNCTIONS_PATTERN + ")"
                + "|(?<GENERALCOMPARISONFUNCTIONS>" + GENERAL_COMPARISON_FUNCTIONS_PATTERN + ")"
                + "|(?<CONVERSIONFUNCTIONS>" + CONVERSION_FUNCTIONS_PATTERN + ")"
                + "|(?<LARGEOBJECTFUNCTIONS>" + LARGE_OBJECT_FUNCTIONS_PATTERN + ")"
                + "|(?<COLLECTIONFUNCTIONS>" + COLLECTION_FUNCTIONS_PATTERN + ")"
                + "|(?<HIERARCHICALFUNCTION>" + HIERARCHICAL_FUNCTION_PATTERN + ")"
                + "|(?<DATAMININGFUNCTIONS>" + DATA_MINING_FUNCTIONS_PATTERN + ")"
                + "|(?<XMLFUNCTIONS>" + XML_FUNCTIONS_PATTERN + ")"
                + "|(?<ENCODINGANDDECODINGFUNCTIONS>" + ENCODING_AND_DECODING_FUNCTIONS_PATTERN + ")"
                + "|(?<NULLRELATEDFUNCTIONS>" + NULL_RELATED_FUNCTIONS_PATTERN + ")"
                + "|(?<ENVIRONMENTANDIDENTIFIERFUNCTIONS>" + ENVIRONMENT_AND_IDENTIFIER_FUNCTIONS_PATTERN + ")"
                + "|(?<AGGREGATEFUNCTIONS>" + AGGREGATE_FUNCTIONS_PATTERN + ")"
                + "|(?<ANALYTICFUNCTIONS>" + ANALYTIC_FUNCTIONS_PATTERN + ")"
                + "|(?<OBJECTREFERENCEFUNCTIONS>" + OBJECT_REFERENCE_FUNCTIONS_PATTERN + ")"
                + "|(?<MODELFUNCTIONS>" + MODEL_FUNCTIONS_PATTERN + ")"
                + "|(?<ANSIRESERVEDWORDS>" + ANSI_RESERVED_WORDS_PATTERN + ")"
                + "|(?<ORACLERESERVEDWORDS>" + ORACLE_RESERVED_WORDS_PATTERN + ")"
                + "|(?<SQLSTATEMENTS>" + SQL_STATEMENTS_PATTERN + ")"
                + "|(?<OPERATORS>" + OPERATORS_PATTERN + ")"
                + "|(?<CONDITIONS>" + CONDITIONS_PATTERN + ")"
                + "|(?<SQLPLUSCOMMANDS>" + SQL_PLUS_COMMANDS_PATTERN + ")"
                + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
                + "|(?<STRINGLITERAL>" + STRING_LITERAL_PATTERN + ")",
                Pattern.CASE_INSENSITIVE  //| Pattern.MULTILINE
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return matcher.group("ORACLEBUILTINDATATYPES") != null ? "oracle-built-in-datatypes"
                : matcher.group("ANSIDATATYPES") != null ? "ansi-datatypes"
                : matcher.group("SQLDSANDDB2DATATYPES") != null ? "sql-ds-and-db2-datatypes"
                : matcher.group("ORACLESUPPLIEDTYPES") != null ? "oracle-supplied-types"
                : matcher.group("UNLIMITED") != null ? "unlimited"
                : matcher.group("NULL") != null ? "null"
                : matcher.group("NUMERICFUNCTIONS") != null ? "numeric-functions"
                : matcher.group("CHARACTERFUNCTIONSRETURNINGCHARACTERVALUES") != null ? "character-functions-returning-character-values"
                : matcher.group("NLSCHARACTERFUNCTIONS") != null ? "nls-character-functions"
                : matcher.group("CHARACTERFUNCTIONSRETURNINGNUMBERVALUES") != null ? "character-functions-returning-number-values"
                : matcher.group("DATETIMEFUNCTIONS") != null ? "datetime-functions"
                : matcher.group("GENERALCOMPARISONFUNCTIONS") != null ? "general-comparison-functions"
                : matcher.group("CONVERSIONFUNCTIONS") != null ? "conversion-functions"
                : matcher.group("LARGEOBJECTFUNCTIONS") != null ? "large-object-functions"
                : matcher.group("COLLECTIONFUNCTIONS") != null ? "collection-functions"
                : matcher.group("HIERARCHICALFUNCTION") != null ? "hierarchical-function"
                : matcher.group("DATAMININGFUNCTIONS") != null ? "data-mining-functions"
                : matcher.group("XMLFUNCTIONS") != null ? "xml-functions"
                : matcher.group("ENCODINGANDDECODINGFUNCTIONS") != null ? "encoding-and-decoding-functions"
                : matcher.group("NULLRELATEDFUNCTIONS") != null ? "null-related-functions"
                : matcher.group("ENVIRONMENTANDIDENTIFIERFUNCTIONS") != null ? "environment-and-identifier-functions"
                : matcher.group("AGGREGATEFUNCTIONS") != null ? "aggregate-functions"
                : matcher.group("ANALYTICFUNCTIONS") != null ? "analytic-functions"
                : matcher.group("OBJECTREFERENCEFUNCTIONS") != null ? "object-reference-functions"
                : matcher.group("MODELFUNCTIONS") != null ? "model-functions"
                : matcher.group("ANSIRESERVEDWORDS") != null ? "ansi-reserved-words"
                : matcher.group("ORACLERESERVEDWORDS") != null ? "oracle-reserved-words"
                : matcher.group("SQLSTATEMENTS") != null ? "sql-statements"
                : matcher.group("OPERATORS") != null ? "operators"
                : matcher.group("CONDITIONS") != null ? "conditions"
                : matcher.group("SQLPLUSCOMMANDS") != null ? "sql-plus-commands"
                : matcher.group("COMMENT") != null ? "sql-comment"
                : matcher.group("STRINGLITERAL") != null ? "string-literal"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(ORACLE_BUILT_IN_DATATYPES));
        keywordList.addAll(Arrays.asList(ANSI_DATATYPES));
        keywordList.addAll(Arrays.asList(SQL_DS_AND_DB2_DATATYPES));
        keywordList.addAll(Arrays.asList(ORACLE_SUPPLIED_TYPES));
        keywordList.addAll(Arrays.asList(UNLIMITED));
        keywordList.addAll(Arrays.asList(NULL));
        keywordList.addAll(Arrays.asList(NUMERIC_FUNCTIONS));
        keywordList.addAll(Arrays.asList(CHARACTER_FUNCTIONS_RETURNING_CHARACTER_VALUES));
        keywordList.addAll(Arrays.asList(NLS_CHARACTER_FUNCTIONS));
        keywordList.addAll(Arrays.asList(CHARACTER_FUNCTIONS_RETURNING_NUMBER_VALUES));
        keywordList.addAll(Arrays.asList(DATETIME_FUNCTIONS));
        keywordList.addAll(Arrays.asList(GENERAL_COMPARISON_FUNCTIONS));
        keywordList.addAll(Arrays.asList(CONVERSION_FUNCTIONS));
        keywordList.addAll(Arrays.asList(LARGE_OBJECT_FUNCTIONS));
        keywordList.addAll(Arrays.asList(COLLECTION_FUNCTIONS));
        keywordList.addAll(Arrays.asList(HIERARCHICAL_FUNCTION));
        keywordList.addAll(Arrays.asList(DATA_MINING_FUNCTIONS));
        keywordList.addAll(Arrays.asList(XML_FUNCTIONS));
        keywordList.addAll(Arrays.asList(ENCODING_AND_DECODING_FUNCTIONS));
        keywordList.addAll(Arrays.asList(NULL_RELATED_FUNCTIONS));
        keywordList.addAll(Arrays.asList(ENVIRONMENT_AND_IDENTIFIER_FUNCTIONS));
        keywordList.addAll(Arrays.asList(AGGREGATE_FUNCTIONS));
        keywordList.addAll(Arrays.asList(ANALYTIC_FUNCTIONS));
        keywordList.addAll(Arrays.asList(OBJECT_REFERENCE_FUNCTIONS));
        keywordList.addAll(Arrays.asList(MODEL_FUNCTIONS));
        keywordList.addAll(Arrays.asList(ANSI_RESERVED_WORDS));
        keywordList.addAll(Arrays.asList(ORACLE_RESERVED_WORDS));
        keywordList.addAll(Arrays.asList(SQL_STATEMENTS));
        keywordList.addAll(Arrays.asList(OPERATORS));
        keywordList.addAll(Arrays.asList(CONDITIONS));
        keywordList.addAll(Arrays.asList(SQL_PLUS_COMMANDS));
        Collections.sort(keywordList);
        return keywordList;
    }

}
