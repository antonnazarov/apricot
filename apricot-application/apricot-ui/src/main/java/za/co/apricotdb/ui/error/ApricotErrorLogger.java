package za.co.apricotdb.ui.error;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The Error Logger annotation. Any method marked as @ApricotErrorLogger will
 * handle the exceptions in the unified way.
 * 
 * @author Anton Nazarov
 * @since 16/01/2020
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ApricotErrorLogger {

    String title();

    String text() default "";
    
    boolean stop() default true;
}
