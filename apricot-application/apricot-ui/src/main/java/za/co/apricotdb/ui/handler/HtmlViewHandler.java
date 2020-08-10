package za.co.apricotdb.ui.handler;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.HtmlViewController;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.ui.model.ApricotForm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * This service class is responsible for presenting the HTML form in Apricot application.
 *
 * @author Anton Nazarov
 * @since 24/06/2020
 */
@Component
public class HtmlViewHandler {

    @Autowired
    DialogFormHandler formHandler;

    @ApricotErrorLogger(title = "Unable to show information form")
    public void showHtmlViewForm(String html, String formTitle) {
        ApricotForm form = formHandler.buildApricotForm("/za/co/apricotdb/ui/apricot-html-viewer.fxml",
                "/za/co/apricotdb/ui/toolbar/tbEditProjectEnabled.png", formTitle);
        HtmlViewController controller = form.getController();
        controller.init(html);

        form.show();
    }

    public void showHtmlViewForm(Map<String, String> values, String templateFile, String formTitle) {
        try (InputStream stream = getClass().getResourceAsStream(templateFile)) {
            String template = IOUtils.toString(stream, Charset.defaultCharset());
            StringSubstitutor substitutor = new StringSubstitutor(values);
            String html = substitutor.replace(template);
            showHtmlViewForm(html, formTitle);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read template: " + templateFile, ex);
        }
    }
}
