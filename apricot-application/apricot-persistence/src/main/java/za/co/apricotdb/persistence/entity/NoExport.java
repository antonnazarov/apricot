package za.co.apricotdb.persistence.entity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation instructs the serialization strategy to not to use the
 * annotated filed in serialization/deserialization of the project meta
 * information.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
@Retention(RUNTIME)
@Target({TYPE, FIELD})
public @interface NoExport {}
