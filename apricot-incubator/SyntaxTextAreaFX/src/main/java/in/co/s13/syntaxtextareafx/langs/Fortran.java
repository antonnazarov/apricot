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
public class Fortran implements Language {

    String OPERATORS[] = new String[]{"true","false","not","and","or","xor","eqv","neqv","eq","ne","gt","ge","lt","le"};
    String KEYWORDS[] = new String[]{"abstract interface","allocate","assign","assignment","block data","call","case","class","common","contains","continue","cycle","data","deallocate","default","do","do concurrent","elemental","elseif","else","elsewhere","enddo","endif","endselect","end","entry","equivalence","exit","external","forall","function","go to","goto","if","implicit none","in","[^#]include","inout","interface","intrinsic","kind","len","module","namelist","nullify","only","operator","out","pause","private","program","public","pure","recursive","result","return","save","select","stop","subroutine","then","type","use","where","while"};
    String READ_WRITE[] = new String[]{"backspace","close","endfile","format","inquire","open","print","read","rewind","write"};
    String INPUT_OUTPUT[] = new String[]{"access","action","advance","blank","delim","direct","end","eor","err","exist","file","fmt","form","formatted","iolength","iostat","name","named","nextrec","nml","number","opened","pad","position","readwrite","rec","recl","sequential","status","unformatted","unit","write"};
    String INTRINSICS[] = new String[]{"abs","achar","acos","adjustl","adjustr","aimag","aint","algama","allocated","all","alog10","alog","amax0","amax1","amin0","amin1","amod","anint","any","asin","associated","atan2","atan","bit_size","btest","c_associated","c_loc","c_funloc","c_f_pointer","c_f_procpointer","cabs","ccos","cdabs","cdcos","cdexp","cdlog","cdsin","cdsqrt","ceiling","cexp","char","clog","cmplx","command_argument_count","conjg","cosh","cos","count","cpu_time","cqabs","cqcos","cqexp","cqlog","cqsin","cqsqrt","cshift","csin","csqrt","dabs","dacos","dasin","datan2","datan","date_and_time","dble","dcmplx","dconjg","dcosh","dcos","ddim","derf","derfc","dexp","dfloat","dgamma","digits","dim","dimag","dint","dlgama","dlog10","dlog","dmax1","dmin1","dmod","dnint","dot_product","dprod","dsign","dsinh","dsin","dsqrt","dtanh","dtan","eoshift","epsilon","erfc","erf","exp","exponent","float","floor","fraction","gamma","getarg","get_command","get_command_argument","get_environment_variable","huge","iabs","iachar","iand","iargc","ibclr","ibits","ibset","ichar","idim","idint","idnint","ieor","ifix","index","int","ior","iqint","is_iostat_end","is_iostat_eor","ishftc","ishft","isign","kind","lbound","len_trim","len","lge","lgt","lle","llt","loc","log10","log","logical","matmul","max0","max1","maxexponent","maxloc","maxval","max","merge","min0","min1","minexponent","minloc","minval","min","mod","modulo","move_alloc","mvbits","nearest","new_line","nint","norm2","not",null,"or","pack","precision","present","product","qabs","qacos","qasin","qatan2","qatan","qcmplx","qconjg","qcosh","qcos","qdim","qerf","qerfc","qexp","qgamma","qimag","qlgama","qlog10","qlog","qmax1","qmin1","qmod","qnint","qsign","qsinh","qsin","qsqrt","qtanh","qtan","radix","random_number","random_seed","range","real","repeat","reshape","rrspacing","scale","scan","selected_char_kind","selected_int_kind","selected_real_kind","set_exponent","shape","sign","sinh","sin","size","sngl","spacing","spread","sqrt","sum","system_clock","tanh","tan","tiny","transfer","transpose","trim","ubound","unpack","verify","zabs","zcos","zexp","zlog","zsin","zsqrt"};
    String TYPES[] = new String[]{"byte","character","complex","double complex","double precision","integer","logical","procedure","real"};
    String TYPE_ATTRIBUTES[] = new String[]{"allocatable","dimension","external","intent","intrinsic","optional","parameter","pointer","private","public","save","target"};

    @Override
    public Pattern generatePattern() {
        Pattern pattern;
        String OPERATORS_PATTERN;
        String KEYWORDS_PATTERN;
        String READ_WRITE_PATTERN;
        String INPUT_OUTPUT_PATTERN;
        String INTRINSICS_PATTERN;
        String TYPES_PATTERN;
        String TYPE_ATTRIBUTES_PATTERN;

        OPERATORS_PATTERN = "\\b(" + String.join("|", OPERATORS) + ")\\b";
        KEYWORDS_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
        READ_WRITE_PATTERN = "\\b(" + String.join("|", READ_WRITE) + ")\\b";
        INPUT_OUTPUT_PATTERN = "\\b(" + String.join("|", INPUT_OUTPUT) + ")\\b";
        INTRINSICS_PATTERN = "\\b(" + String.join("|", INTRINSICS) + ")\\b";
        TYPES_PATTERN = "\\b(" + String.join("|", TYPES) + ")\\b";
        TYPE_ATTRIBUTES_PATTERN = "\\b(" + String.join("|", TYPE_ATTRIBUTES) + ")\\b";

        pattern = Pattern.compile(
                 "|(?<OPERATORS>" + OPERATORS_PATTERN + ")"
                + "|(?<KEYWORDS>" + KEYWORDS_PATTERN + ")"
                + "|(?<READWRITE>" + READ_WRITE_PATTERN + ")"
                + "|(?<INPUTOUTPUT>" + INPUT_OUTPUT_PATTERN + ")"
                + "|(?<INTRINSICS>" + INTRINSICS_PATTERN + ")"
                + "|(?<TYPES>" + TYPES_PATTERN + ")"
                + "|(?<TYPEATTRIBUTES>" + TYPE_ATTRIBUTES_PATTERN + ")"
        );
        return pattern;
    }

    @Override
    public String getStyleClass(Matcher matcher) {
        return matcher.group("DECLARATIONS") != null ? "declarations"
                : matcher.group("OPERATORS") != null ? "operators"
                : matcher.group("KEYWORDS") != null ? "keywords"
                : matcher.group("READWRITE") != null ? "read-write"
                : matcher.group("INPUTOUTPUT") != null ? "input-output"
                : matcher.group("INTRINSICS") != null ? "intrinsics"
                : matcher.group("TYPES") != null ? "types"
                : matcher.group("TYPEATTRIBUTES") != null ? "type-attributes"
                : null;
    }

    @Override
    public ArrayList<String> getKeywords() {
        ArrayList<String> keywordList = new ArrayList<>();
        keywordList.addAll(Arrays.asList(OPERATORS));
        keywordList.addAll(Arrays.asList(KEYWORDS));
        keywordList.addAll(Arrays.asList(READ_WRITE));
        keywordList.addAll(Arrays.asList(INPUT_OUTPUT));
        keywordList.addAll(Arrays.asList(INTRINSICS));
        keywordList.addAll(Arrays.asList(TYPES));
        keywordList.addAll(Arrays.asList(TYPE_ATTRIBUTES));
        Collections.sort(keywordList);
        return keywordList;
    }

}
