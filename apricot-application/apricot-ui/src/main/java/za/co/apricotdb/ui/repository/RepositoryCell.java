package za.co.apricotdb.ui.repository;

import javafx.geometry.Pos;
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
    private RepositoryRow row;
    private String objectName;

    public RepositoryCell(String objectName, boolean remote, RepositoryRow row) {
        super();

        this.objectName = objectName;
        this.text = new Text(objectName);
        this.row = row;
        this.image = getObjectTypeImageView();

        setSpacing(4);

        // populate the horizontal box with the text and graphical definitions of the
        // cell
        if (row.getRowType() == RowType.PROJECT) {
            getChildren().add(getPlaceholder());
        } else {
            getChildren().addAll(getPlaceholder(), getPlaceholder());
        }

        if (!row.includesSnapshots() && objectName != null && !row.isEqual() && remote) {
            getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("import-27.png"))));
        }

        getChildren().add(this.image);
        if (objectName != null) {
            getChildren().add(this.text);

            if (!row.includesSnapshots() && !row.isEqual() && !remote) {
                getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("export-27.png"))));
            }
        }
        
        setAlignment(Pos.CENTER_LEFT);
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

    public boolean isEmpty() {
        return text.getText() == null;
    }

    private ImageView getObjectTypeImageView() {
        ImageView ret = new ImageView(new Image(getClass().getResourceAsStream("does-not-exist-grey-27.png")));
        // ImageView ret = getPlaceholder();
        if (objectName != null) {
            ret = new ImageView(new Image(getClass().getResourceAsStream(row.getRowType().getImageFile())));
        }

        return ret;
    }

    private ImageView getPlaceholder() {
        return new ImageView(new Image(getClass().getResourceAsStream("placeholder-27.png")));
    }
}
