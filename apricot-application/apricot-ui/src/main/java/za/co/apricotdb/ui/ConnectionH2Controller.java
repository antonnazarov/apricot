package za.co.apricotdb.ui;

import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.h2.H2Scanner;
import za.co.apricotdb.metascan.h2.H2UrlBuilder;

/**
 * The implementation of the H2 specific connection controller.
 *
 * @author Anton Nazarov
 * @since 27/09/2020
 */
@Component
public class ConnectionH2Controller extends AbstractFiledbController {

    @Autowired
    H2UrlBuilder h2UrlBuilder;

    @Autowired
    H2Scanner h2Scanner;

    @Override
    public void initScanner() {
        scanner = h2Scanner;
        urlBuilder = h2UrlBuilder;
        target = ApricotTargetDatabase.H2;
    }

    @Override
    public String getFileChooserTitle() {
        return "Open H2 Database file";
    }

    @Override
    public FileChooser.ExtensionFilter getExtensionFilter() {
        return new FileChooser.ExtensionFilter("H2 Database", "*.mv.db");
    }

    @Override
    public String getFileName(String path) {
        int pos = path.indexOf("mv.db");
        if (pos != -1) {
            return path.substring(0, pos);
        }

        return null;
    }
}
