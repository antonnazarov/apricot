package za.co.apricotdb.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * This controller handles the form: apricot-about-form.fxml.
 * 
 * @author Anton Nazarov
 * @since 01/07/2019
 */
@Component
public class AboutFormController {

    @Autowired
    ParentWindow parentWindow;

    @FXML
    public void mailTo(ActionEvent event) {
        Application app = parentWindow.getApplication();
        app.getHostServices().showDocument("mailto:anton_nazarov@hotmail.com");
    }
}
