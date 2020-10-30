package za.co.apricotdb.ui.service;

import javafx.concurrent.Worker;
import org.controlsfx.dialog.ProgressDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.ParentWindow;

/**
 * An abstract class which extends the Service and implements init() method.
 * All asynchronous services of Apricot have to extend this class.
 *
 * @author Anton Nazarov
 * @since 30/12/2020
 */
@Component
public class ProgressInitializer {

    @Autowired
    ParentWindow parentWindow;

    public void init(String title, String headerText, Worker worker) {
        ProgressDialog progressDialog = new ProgressDialog(worker);

        progressDialog.initOwner(parentWindow.getPrimaryStage());

        progressDialog.setTitle(title);
        progressDialog.setHeaderText(headerText);
    }
}
