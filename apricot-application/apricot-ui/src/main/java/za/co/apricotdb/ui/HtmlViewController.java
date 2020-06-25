package za.co.apricotdb.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

/**
 * The controller of the apricot-html-viewer form.
 *
 * @author Anton Nazarov
 * @since 24/06/2020
 */
@Component
public class HtmlViewController {

    @FXML
    Pane mainPane;

    @FXML
    WebView webView;

    public void init(String html) {
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(html);
    }

    @FXML
    public void close() {
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
