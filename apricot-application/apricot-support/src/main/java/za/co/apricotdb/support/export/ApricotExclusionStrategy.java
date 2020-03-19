package za.co.apricotdb.support.export;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import za.co.apricotdb.persistence.entity.NoExport;

/**
 * The custom strategy to exclude the annotated as @NoExport field from
 * serialization/deserialization.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
public class ApricotExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        return field.getAnnotation(NoExport.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getAnnotation(NoExport.class) != null;
    }
}
