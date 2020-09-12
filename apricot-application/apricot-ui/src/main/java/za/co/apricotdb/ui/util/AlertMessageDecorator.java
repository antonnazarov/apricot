package za.co.apricotdb.ui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.ParentWindow;

import java.util.Optional;

@Component
public class AlertMessageDecorator {

    public final static int STANDARD_MESSAGE_LENGTH = 100;

    @Autowired
    ParentWindow parent;

    /**
     * Apply the custom style sheet to the Alert given.
     */
    public void decorateAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets()
                .add(getClass().getResource("/za/co/apricotdb/ui/apricot-alert-style.css").toExternalForm());
        dialogPane.getStyleClass().add("apricotDialog");
    }

    public void decorateAlert(Alert alert, AlertType type) {
        if (type == AlertType.CONFIRMATION || type == AlertType.INFORMATION) {
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets()
                    .add(getClass().getResource("/za/co/apricotdb/ui/apricot-info-style.css").toExternalForm());
            dialogPane.getStyleClass().add("apricotDialog");
        } else {
            decorateAlert(alert);
        }
    }

    public Alert getErrorAlert(String title, String text) {
        Alert alert = new Alert(AlertType.ERROR, null, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(WordUtils.wrap(text, STANDARD_MESSAGE_LENGTH));
        alert.initOwner(parent.getWindow());
        decorateAlert(alert);

        return alert;
    }

    public Alert getAlert(String title, String text, AlertType type) {
        Alert alert = new Alert(type, null, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(WordUtils.wrap(text, STANDARD_MESSAGE_LENGTH));
        alert.initOwner(parent.getWindow());
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
        alert.initOwner(parent.getWindow());
        decorateAlert(alert, type);
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
