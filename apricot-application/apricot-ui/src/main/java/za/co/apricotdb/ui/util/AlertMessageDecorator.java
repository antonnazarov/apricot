package za.co.apricotdb.ui.util;

import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

@Component
public class AlertMessageDecorator {
    
    /**
     * Apply the custom style sheet to the Alert given.
     */
    public void decorateAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets()
                .add(getClass().getResource("/za/co/apricotdb/ui/apricot-alert-style.css").toExternalForm());
        dialogPane.getStyleClass().add("apricotDialog");
    }
}
