/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.s13.syntaxtextareafx.meta;

import in.co.s13.syntaxtextareafx.SyntaxTextAreaFX;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author nika
 */
public class Generator {

    static String colors[] = {"#001F3F", "#0074D9", "#7FDBFF", "#39CCCC",
        "#3D9970", "#2ECC40", "#01FF70", "#FFDC00",
        "#FF851B", "#FF4136", "#F012BE", "#B10DC9",
        "#85144B", "#FFFFFF", "#AAAAAA", "#111111"};
    static int colorCounter = 0;

    public static void main(String[] args) {
        // System.out.println("" + (( "R".equalsIgnoreCase(""+SyntaxTextAreaFX.FILE_TYPES.R))));
        //      System.out.println("" + (SyntaxTextAreaFX.FILE_TYPES.valueOf("java")));
        generateJavaFiles();

//generateJSONsAndCSS();
    }

    public static void generateJavaFiles() {
        File f = new File("jsons/");
        File files[] = f.listFiles((File dir, String name) -> name.toLowerCase().endsWith(".json"));
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            String className = file.getName().substring(0, file.getName().indexOf("."));

            StringBuilder javaContent = new StringBuilder();

            StringBuilder arrayDecs = new StringBuilder();
            StringBuilder patternDecs = new StringBuilder();
            StringBuilder patternInits = new StringBuilder();
            StringBuilder patternCompiles = new StringBuilder();
            StringBuilder matcherStrings = new StringBuilder();
            StringBuilder addVartoArrayLists = new StringBuilder();

            JSONObject rules = new JSONObject(readFile(file.getAbsolutePath()));
            Object arr2 = rules.getJSONObject("language").getJSONObject("definitions").get("context");
            if (arr2 instanceof JSONArray) {
                JSONArray arr = (JSONArray) (arr2);
                for (int j = 0; j < arr.length(); j++) {
                    JSONObject arg = arr.getJSONObject(j);
                    if (arg.has("keyword")) {
                        String arrayName = arg.get("id").toString().toUpperCase().replaceAll("-", "_");
                        String arrayContent = arg.get("keyword").toString();//.substring(1, arg.get("keyword").toString().length()-1);
                        String arrayDec = "    String " + arrayName + "[] = new String[]{" + arrayContent + "};\n";
                        arrayDecs.append(arrayDec);
                        String patternDec = "        String " + arrayName + "_PATTERN;\n";
                        patternDecs.append(patternDec);
                        String patternInit = "        " + arrayName + "_PATTERN = \"\\\\b(\" + String.join(\"|\", " + arrayName + ") + \")\\\\b\";\n";
                        patternInits.append(patternInit);
                        String patternCompile = "                + \"|(?<" + arrayName.replaceAll("_", "") + ">\" + " + arrayName + "_PATTERN + \")\"\n";
                        patternCompiles.append(patternCompile);
                        String matcherString = "                : matcher.group(\"" + arrayName.replaceAll("_", "") + "\") != null ? \"" + arrayName.toLowerCase().replaceAll("_", "-") + "\"\n";
                        matcherStrings.append(matcherString);
                        String addVartoArrayList = "        keywordList.addAll(Arrays.asList(" + arrayName + "));\n";
                        addVartoArrayLists.append(addVartoArrayList);
                    }
                }

            } else {
                JSONObject arg = ((JSONObject) arr2);
                if (arg.has("keyword")) {
                    String arrayName = arg.getString("id").toUpperCase().replaceAll("-", "_");
                    String arrayContent = arg.get("keyword").toString();
                    String arrayDec = "    String " + arrayName + "[] = new String[]{" + arrayContent + "}\n";
                    arrayDecs.append(arrayDec);
                    String patternDec = "        String " + arrayName + "_PATTERN;\n";
                    patternDecs.append(patternDec);
                    String patternInit = "        " + arrayName + "_PATTERN = \"\\\\b(\" + String.join(\"|\", " + arrayName + ") + \")\\\\b\";\n";
                    patternInits.append(patternInit);
                    String patternCompile = "                + \"|(?<" + arrayName.replaceAll("_", "") + ">\" + " + arrayName + "_PATTERN + \")\"\n";
                    patternCompiles.append(patternCompile);
                    String matcherString = "                : matcher.group(\"" + arrayName.replaceAll("_", "") + "\") != null ? \"" + arrayName.toLowerCase().replaceAll("_", "-") + "\"\n";
                    matcherStrings.append(matcherString);
                    String addVartoArrayList = "        keywordList.addAll(Arrays.asList(" + arrayName + "));\n";
                    addVartoArrayLists.append(addVartoArrayList);
                }
            }
            javaContent.append("/*\n"
                    + " * To change this license header, choose License Headers in Project Properties.\n"
                    + " * To change this template file, choose Tools | Templates\n"
                    + " * and open the template in the editor.\n"
                    + " */\n"
                    + "package in.co.s13.syntaxtextareafx.langs;\n"
                    + "\n"
                    + "import java.util.ArrayList;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.regex.Matcher;\n"
                    + "import java.util.regex.Pattern;\n"
                    + "import in.co.s13.syntaxtextareafx.meta.Language;\n"
                    + "import java.util.Collections;\n"
                    + "\n"
                    + "/**\n"
                    + " *\n"
                    + " * @author nika\n"
                    + " */\n"
                    + "public class " + className + " implements Language {\n"
                    + "\n"
                    + arrayDecs.toString()
                    + "\n"
                    + "    @Override\n"
                    + "    public Pattern generatePattern() {\n"
                    + "        Pattern pattern;\n"
                    + patternDecs.toString()
                    + "\n"
                    + patternInits.toString()
                    + "\n"
                    + "        pattern = Pattern.compile(\n"
                    + patternCompiles.toString()
                    + "        );\n"
                    + "        return pattern;\n"
                    + "    }\n"
                    + "\n"
                    + "    @Override\n"
                    + "    public String getStyleClass(Matcher matcher) {\n"
                    + "        return matcher.group(\"DECLARATIONS\") != null ? \"declarations\"\n"
                    + matcherStrings.toString()
                    + "                : null;\n"
                    + "    }\n"
                    + "\n"
                    + "    @Override\n"
                    + "    public ArrayList<String> getKeywords() {\n"
                    + "        ArrayList<String> keywordList = new ArrayList<>();\n"
                    + addVartoArrayLists.toString()
                    + "        Collections.sort(keywordList);\n"
                    + "        return keywordList;\n"
                    + "    }\n"
                    + "\n"
                    + "}\n"
                    + "");
            try {
                write(new File("java/" + file.getName().substring(0, file.getName().lastIndexOf(".")) + ".java"), javaContent.toString());
            } catch (IOException ex) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void generateJSONsAndCSS() {
        File f = new File("jsons/");
        File files[] = f.listFiles((File dir, String name) -> name.toLowerCase().endsWith(".json"));
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String content = "{" + file.getName() + ":";
            String cssContent = "";
            JSONObject obj = new JSONObject(readFile(file.getAbsolutePath()));
            Iterator<String> it = obj.getJSONObject("language").getJSONObject("definitions").keys();
            while (it.hasNext()) {

                System.out.println(it.next());
            }
            Object arr2 = obj.getJSONObject("language").getJSONObject("definitions").get("context");
            if (arr2 instanceof JSONArray) {
                JSONArray arr = (JSONArray) (arr2);
                for (int j = 0; j < arr.length(); j++) {
                    JSONObject arg = arr.getJSONObject(j);
                    cssContent += assignCSScolor(arg);
                }

                content += arr.toString(4);
            } else {
                content += "" + ((JSONObject) arr2);
                cssContent += assignCSScolor((JSONObject) arr2);
            }
            System.out.println(file.getName() + "  " + content);
            try {
                write(new File("json2/" + file.getName().replace("-", "")), new JSONObject(content + "}").toString(4));
                write(new File("css/" + file.getName().substring(0, file.getName().lastIndexOf(".")) + ".css".replace("-", "")), cssContent);

            } catch (IOException ex) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void convertXMLsToJSONs() {
        ArrayList<File> list = Generator.getPFiles("/usr/share/gtksourceview-3.0/language-specs/");
        for (int i = 0; i < list.size(); i++) {
            File get = list.get(i);
            try {
                String str = readFile(get.getAbsolutePath());
                JSONObject xmlJSONObj = XML.toJSONObject(str);
                String jsonPrettyPrintString = xmlJSONObj.toString(4);
                write(new File(get.getName().substring(0, get.getName().lastIndexOf(".")) + ".json"), jsonPrettyPrintString);
            } catch (JSONException je) {
                System.out.println(je.toString());
            } catch (IOException ex) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public static String assignCSScolor(JSONObject obj) {
        StringBuilder sb = new StringBuilder("");

        if (colorCounter == colors.length) {
            colorCounter = 0;
        }
        if (obj.has("id")) {
            if (obj.get("id") instanceof String) {
                sb.append(".").append(obj.getString("id"));
                sb.append(" {\n" + "    -fx-fill:").append(colors[colorCounter]).append(";\n")
                        .append("    -fx-font-weight: bold;\n")
                        .append("}\n");
                colorCounter++;
            }
        }

        return sb.toString();
    }

    public static ArrayList<File> getPFiles(String dir) {
        ArrayList<File> list = new ArrayList<>();
        File directory = new File(dir);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {

                list.addAll(getPFiles(file.getAbsolutePath()));
            } else if (file.getName().endsWith(".lang")) {
                list.add(file);
            }
        }

        return list;
    }

    public static void write(File f, String text) throws IOException {
        try (FileWriter fw = new FileWriter(f);
                PrintWriter pw = new PrintWriter(fw)) {
            pw.print(text);
            pw.close();
            fw.close();
        }

    }

    static String readFile(String path) {
        String str = null;
        try {
            Charset encoding = Charset.defaultCharset();
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            str = new String(encoded, encoding);
        } catch (IOException ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

}
