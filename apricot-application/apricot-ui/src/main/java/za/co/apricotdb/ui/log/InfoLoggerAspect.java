package za.co.apricotdb.ui.log;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * This aspect has been used for utility logging of the into of the ApricotDB methods.
 *
 * @author Anton Nazarov
 * @since 06/02/2021
 */
@Aspect
@Component
public class InfoLoggerAspect {

    @Autowired
    LogParamsHandler paramsHandler;

    @Around("@annotation(ApricotInfoLogger)")
    public Object logInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        ApricotInfoLogger l = method.getAnnotation(ApricotInfoLogger.class);
        String infoText = l.info();
        Object proceed = null;

        if (l != null) {
            long start = System.currentTimeMillis();
            proceed = joinPoint.proceed();
            long finish = System.currentTimeMillis();
            long duration = finish - start;

            StringBuilder sb = new StringBuilder();
            if (StringUtils.isEmpty(infoText)) {
                //  if the info text has not been provided, compose it
                sb.append(joinPoint.getTarget().getClass().getSimpleName())
                        .append(".")
                        .append(method.getName()).append(" [")
                        .append(duration).append(" ms]");
                sb.append(" <- ").append(getCallerMethod());
            } else {
                sb.append(infoText);
                sb.append(" [").append(duration).append("]");
            }

            String params = paramsHandler.logParameters(joinPoint);
            if (StringUtils.isNotEmpty(params)) {
                sb.append(", ").append(params);
            }

            infoText = sb.toString();

            logger.info(infoText);
        }

        return proceed;
    }

    private String getCallerMethod() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        for (StackTraceElement elm : stackTraceElements) {
            String className = elm.getClassName();
            if (className.contains("za.co.apricotdb.ui")
            && !className.contains("Aspect")
            && !className.contains("Enhancer")) {
                String[] parts = className.split("\\.");
                StringBuffer sb = new StringBuffer();
                sb.append(parts[parts.length-1]).append(".")
                        .append(cleanLambda(elm.getMethodName())).append(" (")
                        .append(elm.getLineNumber()).append(")");
                return sb.toString();
            }
        }

        return null;
    }

    private String cleanLambda(String methodName) {
        if (methodName.contains("lambda")) {
            int start = methodName.indexOf("$");
            int end = methodName.indexOf("$", start+1);
            return methodName.substring(start+1, end);
        }

        return methodName;
    }
}