package gsontest;

import com.google.gson.Gson;
import in.co.s13.syntaxtextareafx.meta.Generator;;

public class GsonTestRunner {
    
    public static void main(String[] args) {
        System.out.println("Test");
        
        Gson gson = new Gson();
        System.out.println(gson.toJson(1));
        
        DruidMaster dm = new DruidMaster("Anton", 23);
        System.out.println(dm);
        System.out.println(gson.toJson(dm));  // {"druidName":"Anton","druidNum":23}
        
        DruidMaster dmg = gson.fromJson("{\"druidName\":\"Anton\",\"druidNum\":23}", DruidMaster.class);
        System.out.println(dmg);
        
        DruidHolder dh = new DruidHolder("paap-swe");
        dh.getMasters().add(dmg);
        System.out.println(dh);
        System.out.println(gson.toJson(dh));
        
        String sDh = "{\"holderId\":\"paap-swe\",\"hDate\":\"Mar 15, 2020, 6:58:16 PM\",\"masters\":[{\"druidName\":\"Anton\",\"druidNum\":23}]}";
        DruidHolder dhg = gson.fromJson(sDh, DruidHolder.class);
        System.out.println(dhg);
        
        System.out.println(System.getProperty("user.dir"));
        Generator.generateJavaFiles();
    }
}
