package za.co.apricotdb.ui.error;

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
 * The implementation of the aspect system logic, which handles the errors in
 * the application (wired to the user interface).
 *
 * @author Anton Nazarov
 * @since 16/01/2020
 */
@Aspect
@Component
public class ErrorLoggerAspect {

    private Logger logger = null;

    @Autowired
    ApricotErrorHandler errorHandler;

    /**
     * Handle the exception which happened in the annotated method.
     */
    @Around("@annotation(ApricotErrorLogger)")
    public Object handleError(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        ApricotErrorLogger l = method.getAnnotation(ApricotErrorLogger.class);
        Object proceed = null;

        if (l != null) {
            try {
                proceed = joinPoint.proceed();
            } catch (Throwable t) {
                if (!hasErrorHandledParent(t, signature.getDeclaringTypeName(), method.getName())) {
                    StringBuffer text = new StringBuffer(l.text());
                    if (StringUtils.isNotEmpty(l.text())) {
                        text.append("\n\n");
                    }

                    if (t.getMessage() != null) {
                        text.append(errorHandler.wrapText(t.getMessage()));
                    }

                    errorHandler.showErrorInfo(text.toString(), l.title(), t);
                }

                if (l.stop()) {
                    throw t;
                } else {
                    logger = LoggerFactory.getLogger(l.getClass());
                    logger.error(l.text(), t);
                }
            }
        }

        return proceed;
    }

    private boolean hasErrorHandledParent(Throwable t, String entryClassName, String entryMethodName) {

        StackTraceElement[] elms = t.getStackTrace();
        boolean entryClassPassed = false;
        for (StackTraceElement elm : elms) {
            String className = elm.getClassName();
            if (errorHandler.isApricotSpecificClass(className)) {
                boolean isErrorHandlingAnnotated = false;
                try {
                    Class<?> clazz = Class.forName(elm.getClassName());
                    Method[] mtds = clazz.getMethods();
                    for (Method m : mtds) {
                        if (m.getName().equals(elm.getMethodName())) {
                            if (m.getAnnotation(ApricotErrorLogger.class) != null) {
                                isErrorHandlingAnnotated = true;
                                break;
                            }
                        }
                    }

                    if (entryClassPassed && isErrorHandlingAnnotated) {
                        return true;
                    }

                    if (elm.getClassName().equals(entryClassName) && elm.getMethodName().equals(entryMethodName)) {
                        entryClassPassed = true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}
