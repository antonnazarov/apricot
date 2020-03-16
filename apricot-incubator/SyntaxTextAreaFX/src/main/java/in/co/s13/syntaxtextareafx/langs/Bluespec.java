/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class Bluespec implements Language {

    String SYSTEM_TASK[] = new String[]{"display", "dumpoff", "dumpon", "dumpvars", "fclose", "fdisplay", "fflush", "fgetc", "finish", "fopen", "fwrite", "stime", "stop", "test\\$plusargs", "time", "ungetc", "write"};
    String IMPORT_BVI[] = new String[]{"ancestor", "clocked_by", "default_clock", "default_reset", "enable", "input_clock", "input_reset", "method", "no_reset", "output_clock", "output_reset", "parameter", "path", "port", "ready", "reset_by", "same_family", "schedule"};
    String KEYWORD[] = new String[]{"action", "clocked_by", "deriving", "endaction", "endfunction", "endinterface", "endmethod", "endmodule", "endpackage", "endrule", "endrules", "enum", "function", "if", "import", "interface", "let", "match", "method", "module", "numeric", "package", "provisos", "reset_by", "rule", "rules", "struct", "tagged", "type", "typedef", "union"};
    String TYPE[] = new String[]{"Action", "ActionValue", "Bit", "Bool", "int", "Int", "Integer", "Maybe", "Nat", "Rules", "String", "Tuple[2-7]", "UInt"};
    String STANDARD_INTERFACE[] = new String[]{"Client", "ClientServer", "Connectable", "FIFO", "FIFOF", "Get", "GetPut", "PulseWire", "Put", "Reg", "Server", "Wire"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String SYSTEM_TASK_PATTERN;
        String IMPORT_BVI_PATTERN;
        String KEYWORD_PATTERN;
        String TYPE_PATTERN;
        String STANDARD_INTERFACE_PATTERN;
        String COMMENT_PATTERN;

        SYSTEM_TASK_PATTERN = "\\b(" + String.join("|", SYSTEM_TASK) + ")\\b";
        IMPORT_BVI_PATTERN = "\\b(" + String.join("|", IMPORT_BVI) + ")\\b";
        KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORD) + ")\\b";
        TYPE_PATTERN = "\\b(" + String.join("|", TYPE) + ")\\b";
        STANDARD_INTERFACE_PATTERN = "\\b(" + String.join("|", STANDARD_INTERFACE) + ")\\b";
        COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

        pattern = Pattern.compile(
                "(?<SYSTEMTASK>" + SYSTEM_TASK_PATTERN + ")"
                + "|(?<IMPORTBVI>" + IMPORT_BVI_PATTERN + ")"
                + "|(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                + "|(?<TYPE>" + TYPE_PATTERN + ")"
                + "|(?<STANDARDINTERFACE>" + STANDARD_INTERFACE_PATTERN + ")"
                + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return matcher.group("SYSTEMTASK") != null ? "system-task"
                : matcher.group("IMPORTBVI") != null ? "import-bvi"
                : matcher.group("KEYWORD") != null ? "keyword"
                : matcher.group("TYPE") != null ? "type"
                : matcher.group("STANDARDINTERFACE") != null ? "standard-interface"
                : matcher.group("COMMENT") != null ? "comment"
        : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(SYSTEM_TASK));
        keywordList.addAll(Arrays.asList(IMPORT_BVI));
        keywordList.addAll(Arrays.asList(KEYWORD));
        keywordList.addAll(Arrays.asList(TYPE));
        keywordList.addAll(Arrays.asList(STANDARD_INTERFACE));
        Collections.sort(keywordList);
        return keywordList;
    }

}
