package za.co.apricotdb.ui.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import za.co.apricotdb.support.export.ApricotExclusionStrategy;

/**
 * The helper class to create Gson processor.
 * 
 * @author Anton Nazarov
 * @since 06/04/2020
 */
public class GsonFactory {
    
    public static Gson initGson() {
        ExclusionStrategy strategy = new ApricotExclusionStrategy();
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(strategy);
        builder.setPrettyPrinting();

        return builder.create();
    }
}
