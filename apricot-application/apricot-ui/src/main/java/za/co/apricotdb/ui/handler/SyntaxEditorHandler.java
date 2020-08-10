package za.co.apricotdb.ui.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.SyntaxEditorController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;

/**
 * The Syntax Editor related business logic.
 *
 * @author Anton Nazarov
 * @since 04/04/2019
 */
@Component
public class SyntaxEditorHandler {

    @Autowired
    DialogFormHandler formHandler;

    @ApricotErrorLogger(title = "Unable to create the script editing form")
    public void createSyntaxEditorForm(String script, String formHeader) {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-syntax-editor.fxml",
                "script-s1.JPG", formHeader);
        SyntaxEditorController controller = form.getController();
        controller.init(script, formHeader);

        form.show();
    }
}
