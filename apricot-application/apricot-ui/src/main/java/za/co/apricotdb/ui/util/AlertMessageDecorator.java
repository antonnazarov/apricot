package za.co.apricotdb.ui.util;

import java.util.Optional;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@Component
public class AlertMessageDecorator {

    public final static int STANDARD_MESSAGE_LENGTH = 100;

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

    public Alert getAlert(String title, String text, AlertType type) {
        Alert alert = new Alert(type, null, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(WordUtils.wrap(text, STANDARD_MESSAGE_LENGTH));
        if (type == AlertType.ERROR || type == AlertType.WARNING) {
            decorateAlert(alert);
        }

        return alert;
    }

    public boolean requestYesNoOption(String title, String text, String yesText, AlertType type) {
        ButtonType yes = new ButtonType(yesText, ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(type, null, yes, no);
        alert.setTitle(title);
        alert.setHeaderText(WordUtils.wrap(text, STANDARD_MESSAGE_LENGTH));
        // alert.setHeaderText(text);
        decorateAlert(alert);
        Stage alertWindow = (Stage) alert.getDialogPane().getScene().getWindow();
        // alertWindow.getIcons().add(new Image(getClass().getResourceAsStream("/za/co/apricotdb/ui/handler/system-error-small.png")));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(no) == yes) {
            return true;
        } else {
            return false;
        }
    }

    public boolean requestYesNoOption(String title, String text, String yesText) {
        return requestYesNoOption(title, text, yesText, AlertType.WARNING);
    }
}
