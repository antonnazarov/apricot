package za.co.apricotdb.ui.log;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
@Repeatable(LogParams.class)
public @interface LogParam {

    String name();
    String paramValue();
}
