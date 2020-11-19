package za.co.apricotdb.ui.service;

import javafx.concurrent.Worker;
import org.controlsfx.dialog.ProgressDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.ParentWindow;

import java.util.HashMap;
import java.util.Map;

/**
 * An abstract class which extends the Service and implements init() method.
 * All asynchronous services of Apricot have to extend this class.
 *
 * @author Anton Nazarov
 * @since 01/09/2020
 */
@Component
public class ProgressInitializer {

    @Autowired
    ParentWindow parentWindow;

    private Map<Worker, ProgressDialog> dialogs = new HashMap<>();

    public void init(String title, String headerText, Worker worker) {
        ProgressDialog progressDialog = dialogs.get(worker);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(worker);
            progressDialog.initOwner(parentWindow.getPrimaryStage());
            progressDialog.getDialogPane().getStylesheets().clear();
            progressDialog.getDialogPane().getStylesheets().add(getClass().getResource("/za/co/apricotdb/ui/progressdialog/progressdialog.css").toExternalForm());
            dialogs.put(worker, progressDialog);
        }

        progressDialog.setTitle(title);
        progressDialog.setHeaderText(headerText);
    }
}
