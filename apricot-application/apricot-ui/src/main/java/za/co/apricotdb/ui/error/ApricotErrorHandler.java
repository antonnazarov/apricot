package za.co.apricotdb.ui.error;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.davidmoten.text.utils.WordWrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.ErrorFormController;
import za.co.apricotdb.ui.util.AlertMessageDecorator;

/**
 * This component contains the common error form logic.
 *
 * @author Anton Nazarov
 * @since 22/06/2020
 */
@Component
public class ApricotErrorHandler {

    @Autowired
    AlertMessageDecorator alertHandler;

    @Autowired
    ErrorFormController formController;

    public void showErrorInfo(String errorText, String title, Throwable t) {
        Platform.runLater(() -> {
            if (alertHandler.requestYesNoOption(title, errorText, "View the error details",
                    Alert.AlertType.ERROR)) {
                // show the stack trace
                try {
                    String stacktrace = title + "\n\n" + "---> The simplified stack trace:\n"
                            + getSimplifiedStackTrace(t) + "\n" + "---> The full stack trace:\n"
                            + ExceptionUtils.getStackTrace(t);
                    formController.openForm(stacktrace);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public String wrapText(String text) {
        return WordWrap.from(text).maxWidth(60).insertHyphens(true) // true is the default
                .wrap();
    }

    public boolean isApricotSpecificClass(String className) {
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
