package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.model.ApricotForm;

import javax.annotation.Resource;

/**
 * This is a handler that created the Apricot About form.
 *
 * @author Anton Nazarov
 * @since 01/07/2019
 */
@Component
public class ApricotAboutHandler {

    @Resource
    ApplicationContext context;

    @Autowired
    DialogFormHandler formHandler;

    public void showAboutForm() {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-about-form.fxml",
                "favicon-32x32.png", "About Apricot DB");
        form.show();
    }
}
