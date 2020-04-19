package za.co.apricotdb.ui.repository;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * A cell of repository (left/local, buttons, right/remote).
 * 
 * @author Anton Nazarov
 * @since 16/04/2020
 */
public class RepositoryCell extends HBox {

    private ImageView image;
    private Text text;
    RepositoryRow row;

    public RepositoryCell(String text, boolean remote, RepositoryRow row) {
        super();

        this.text = new Text(text);
        this.image = getImageView();
        this.row = row;

        setSpacing(4);

        getChildren().add(this.image);
        if (text != null) {
            getChildren().add(this.text);

            if (!row.isEqual()) {
                if (remote) {
                    getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("import-27.png"))));
                } else {
                    getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("export-27.png"))));
                }
            }
        }
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public Text getText() {
        return text;
    }

    private ImageView getImageView() {
        ImageView ret = new ImageView(new Image(getClass().getResourceAsStream("does-not-exist-27.png")));
        if (text != null) {
            ret = new ImageView(new Image(getClass().getResourceAsStream(row.getRowType().getImageFile())));
        }
        return ret;
    }
}
