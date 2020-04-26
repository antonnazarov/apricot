package za.co.apricotdb.ui.repository;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * The control which is allocated between two Repository cells: the local and
 * remote one.
 * 
 * @author Anton Nazarov
 * @since 18/04/2020
 */
public class RepositoryControl extends HBox {

    private RepositoryRow row;

    public RepositoryControl(RepositoryRow row) {
        super();

        this.row = row;

        init();
    }

    private void init() {
        if (row.isEqual()) {
            ImageView v = getImageView("equal-27.png");
            HBox.setMargin(v, new Insets(0, 0, 0, 8));
            getChildren().add(v);
        }

        if (!row.isEqual() && row.getRowType() == RowType.PROJECT && row.hasBothSides()) {
            ImageView v = getImageView("not-equal-27.png");
            HBox.setMargin(v, new Insets(0, 0, 0, 8));
            getChildren().add(v);
        }

        if (!row.isEqual() && row.getRowType() == RowType.SNAPSHOT && row.hasBothSides()) {
            Button btn = new Button();
            btn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("not-equal-27.png"))));
            getChildren().add(btn);
        }
    }

    private ImageView getImageView(String file) {
        return new ImageView(new Image(getClass().getResourceAsStream(file)));
    }
}
