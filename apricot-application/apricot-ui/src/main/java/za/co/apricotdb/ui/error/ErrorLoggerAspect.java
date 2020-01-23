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
                StringBuffer text = new StringBuffer(l.text());
                if (StringUtils.isNotEmpty(l.text())) {
                    text.append("\n\n");
                }
                text.append(t.getMessage());

                if (alertHandler.requestYesNoOption(l.title(), text.toString(), "View the error details",
                        AlertType.ERROR)) {
                    // show the stacktrace
                    try {
                        String stacktrace = ExceptionUtils.getStackTrace(t);
                        formController.openForm(stacktrace);
                    } catch (Exception ex) {
                        ex.printStackTrace();
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
}
