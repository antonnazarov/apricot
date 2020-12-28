package za.co.apricotdb.ui;

import javafx.stage.FileChooser;

/**
 * The Controller of the file based database (H2, SQLite).
 *
 * @author Anton Nazarov
 * @since 02/10/2020
 */
public interface FiledbController {

    public void init();
    void initScanner();
    String getFileChooserTitle();
    FileChooser.ExtensionFilter getExtensionFilter();
    String getFileName(String path);
}
