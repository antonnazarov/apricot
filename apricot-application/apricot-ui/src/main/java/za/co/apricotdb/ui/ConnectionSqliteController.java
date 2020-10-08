package za.co.apricotdb.ui;


import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.sqlite.SqliteScanner;
import za.co.apricotdb.metascan.sqlite.SqliteUrlBuilder;

/**
 * The implementation of the H2 specific connection controller.
 *
 * @author Anton Nazarov
 * @since 27/09/2020
 */
@Component
public class ConnectionSqliteController extends AbstractFiledbController {

    @Autowired
    SqliteUrlBuilder sqliteUrlBuilder;

    @Autowired
    SqliteScanner sqliteScanner;

    @Override
    public void initScanner() {
        scanner = sqliteScanner;
        urlBuilder = sqliteUrlBuilder;
        target = ApricotTargetDatabase.SQLite;
    }

    @Override
    public String getFileChooserTitle() {
        return "Open SQLite Database file";
    }

    @Override
    public FileChooser.ExtensionFilter getExtensionFilter() {
        return new FileChooser.ExtensionFilter("SQLite", "*.db");
    }

    @Override
    public String getFileName(String path) {
        return path;
    }
}
