package za.co.apricotdb.ui.log;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The generic method logger annotation.
 *
 * @author Anton Nazarov
 * @since 06/02/2021
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ApricotInfoLogger {

    String info() default "";
}
