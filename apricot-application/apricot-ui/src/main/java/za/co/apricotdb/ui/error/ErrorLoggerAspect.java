package za.co.apricotdb.ui.error;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert.AlertType;
import za.co.apricotdb.ui.ErrorFormController;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

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
    AlertMessageDecorator alertHandler;

    @Autowired
    ErrorFormController formController;

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
                    text.append(t.getMessage());

                    if (alertHandler.requestYesNoOption(l.title(), text.toString(), "View the error details",
                            AlertType.ERROR)) {
                        // show the stack trace
                        try {
                            String stacktrace = l.title() + "\n\n" +
                                    "---> The simplified stack trace:\n" + 
                                    getSimplifiedStackTrace(t) + "\n" +
                                    "---> The full stack trace:\n" +
                                    ExceptionUtils.getStackTrace(t);
                            formController.openForm(stacktrace);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
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
            if (isApricotSpecificClass(className)) {
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

    private boolean isApricotSpecificClass(String className) {
        return className.startsWith("za.co.apricotdb") && !className.contains("ErrorLoggerAspect")
                && !className.contains("CGLIB");
    }

    private String getSimplifiedStackTrace(Throwable t) {
        StringBuilder sb = new StringBuilder();

        StackTraceElement[] elms = t.getStackTrace();
        for (StackTraceElement elm : elms) {
            if (isApricotSpecificClass(elm.getClassName())) {
                sb.append(elm.getClassName()).append(".").append(elm.getMethodName()).append("(")
                        .append(elm.getClassName()).append(":").append(elm.getLineNumber()).append(")\n");
            }
        }

        return sb.toString();
    }
}
