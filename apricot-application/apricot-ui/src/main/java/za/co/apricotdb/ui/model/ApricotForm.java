package za.co.apricotdb.ui.model;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

/**
 * A DTO for the unified Apricot dialog (modal) form.
 *
 * @author Anton Nazarov
 * @since 07/08/2020
 */
public class ApricotForm {

    private FXMLLoader loader;
    private Stage dialog;

    public ApricotForm(FXMLLoader loader, Stage dialog) {
        this.loader = loader;
        this.dialog = dialog;
    }

    public <T> T getController() {
        return loader.getController();
    }

    public void show() {
        dialog.show();
    }

    public void showAndWait() {
        dialog.showAndWait();
    }

    public Stage getDialog() {
        return dialog;
    }
}
