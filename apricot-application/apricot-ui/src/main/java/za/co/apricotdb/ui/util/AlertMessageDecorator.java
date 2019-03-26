package za.co.apricotdb.ui.util;

import java.util.Optional;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

@Component
public class AlertMessageDecorator {
    
    public final static int STANDARD_MESSAGE_LENGTH = 75;
    
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
    
    public boolean requestYesNoOption(String title, String text, String yesText) {
        ButtonType yes = new ButtonType(yesText, ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING, null, yes, no);
        alert.setTitle(title);
        alert.setHeaderText(WordUtils.wrap(text, STANDARD_MESSAGE_LENGTH));
        decorateAlert(alert);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.orElse(no) == yes) {
            return true;
        } else {
            return false;
        }
    }
}
