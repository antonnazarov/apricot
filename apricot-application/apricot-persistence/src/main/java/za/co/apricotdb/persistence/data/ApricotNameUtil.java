package za.co.apricotdb.persistence.data;

public class ApricotNameUtil {

    public static String generateSeqUniqueName(String name) {
        String ret = null;
        if (name.contains("_")) {
            String suffix = name.substring(name.lastIndexOf("_") + 1);
            try {
                int version = Integer.parseInt(suffix);
                version++;
                String prefix = name.substring(0, name.lastIndexOf("_"));
                ret = prefix + "_" + version;
            } catch (NumberFormatException nfe) {
                ret = name + "_2";
            }
        } else {
            ret = name + "_2";
        }

        return ret;
    }
}
