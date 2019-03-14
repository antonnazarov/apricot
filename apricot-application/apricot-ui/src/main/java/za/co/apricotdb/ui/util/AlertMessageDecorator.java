package za.co.apricotdb.ui.util;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;

@Component
public class AlertMessageDecorator {
    
    public final static int STANDARD_MESSAGE_LENGTH = 60;
    
    /**
     * Apply the custom style sheet to the Alert given.
     */
    public void decorateAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets()
                .add(getClass().getResource("/za/co/apricotdb/ui/apricot-alert-style.css").toExternalForm());
        dialogPane.getStyleClass().add("apricotDialog");
    }
    
    public Alert getErrorAlert(String title, String text) {
        Alert alert = new Alert(AlertType.ERROR, null, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(WordUtils.wrap(text, STANDARD_MESSAGE_LENGTH));
        decorateAlert(alert);

        return alert;
    }
}
