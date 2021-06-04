package za.co.apricotdb.ui.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * This component implements all the logging parameters business logic.
 *
 * @author Anton Nazarov
 * @since 27/02/2021
 */
@Component
public class LogParamsHandler {

    public String logParameters(ProceedingJoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();

        Object[] args = joinPoint.getArgs();

        boolean first = true;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogParam[] logParams = method.getAnnotationsByType(LogParam.class);
        if (logParams != null && logParams.length > 0) {
            for (LogParam lp : logParams) {
                Parameter[] params = method.getParameters();
                int idx = 0;
                for (Parameter p : params) {
                    if (p.getName().equals(lp.name())) {
                        Object argument = args[idx];
                        String val = executeValue(argument, lp.paramValue());
                        if (first) {
                            first = false;
                        } else {
                            sb.append("; ");
                        }
                        sb.append(p.getName()).append("=[").append(val).append("]");
                    }

                    idx++;
                }
            }
        }

        return sb.toString();
    }

    private String executeValue(Object argument, String method) {
        try {
            Method m = argument.getClass().getMethod(method);
            Object ret = m.invoke(argument);
            if (ret != null && ret instanceof String) {
                return (String) ret;
            }
        } catch (Exception e) {
            return "Unable to execute method[" + method + "] on the Object=[" + argument + "]";

        }

        return null;
    }
}
