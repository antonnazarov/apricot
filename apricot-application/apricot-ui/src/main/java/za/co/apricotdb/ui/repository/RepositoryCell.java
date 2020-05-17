package za.co.apricotdb.ui.repository;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.context.ApplicationContext;
import za.co.apricotdb.ui.handler.RepositoryHandler;

/**
 * A cell of repository (left/local, buttons, right/remote).
 * 
 * @author Anton Nazarov
 * @since 16/04/2020
 */
public class RepositoryCell extends HBox {

    private ImageView image;
    private Label text;
    private RepositoryRow row;
    private String objectName;
    private boolean remote;

    public RepositoryCell(String objectName, boolean remote, RepositoryRow row) {
        super();

        this.objectName = objectName;
        this.text = new Label(objectName);
        this.row = row;
        this.image = getObjectTypeImageView();
        this.remote = remote;
    }
    
    public void init(ApplicationContext applicationContext) {
        RepositoryHandler handler = applicationContext.getBean(RepositoryHandler.class);

        text.setPrefWidth(300);
        if (row.getRowType() == RowType.PROJECT) {
            text.setStyle("-fx-font-weight: bold;");
            getChildren().add(getPlaceholder());
            //  for the Projects make the background grey to distinguish them from the Snapshots
            setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            getChildren().addAll(getPlaceholder(), getPlaceholder());
        }

        // populate the horizontal box with the text and graphical definitions of the
        // cell
        if (!row.includesSnapshots() && objectName != null && !row.isEqual() && remote) {
            Button btn = getButton("import-27.png");
            if (row.getRowType() == RowType.PROJECT) {
                btn.setTooltip(getToolTip("Import Project"));
                btn.setOnAction(e -> {
                    handler.importRepoProject(row);
                });
            } else {
                btn.setTooltip(getToolTip("Import Snapshot into the Project"));
                btn.setOnAction(e -> {
                    handler.importRepoSnapshpot(row, objectName);
                });
            }

            getChildren().add(btn);
        }

        getChildren().add(this.image);
        if (objectName != null) {
            getChildren().add(this.text);

            if (!row.includesSnapshots() && !row.isEqual() && !remote) {
                Button btn = getButton("export-27.png");
                getChildren().addAll(btn, getPlaceholder());
            }
        }

        if (remote && objectName != null) {
            Button btn = getButton("triple-dot-27.png");
            getChildren().add(btn);
        }

        setSpacing(4);
        setAlignment(Pos.CENTER_LEFT);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public Label getText() {
        return text;
    }

    public boolean isEmpty() {
        return text.getText() == null;
    }

    private ImageView getObjectTypeImageView() {
        ImageView ret = new ImageView(new Image(getClass().getResourceAsStream("does-not-exist-grey-27.png")));
        if (objectName != null) {
            ret = new ImageView(new Image(getClass().getResourceAsStream(row.getRowType().getImageFile())));
        }

        return ret;
    }

    private ImageView getPlaceholder() {
        return new ImageView(new Image(getClass().getResourceAsStream("placeholder-27.png")));
    }

    private Button getButton(String imageFile) {
        Button btn = new Button();
        btn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(imageFile))));

        return btn;
    }

    private Tooltip getToolTip(String text) {
        Tooltip tip = new Tooltip();
        tip.setText(text);
        Font f = new Font(15);
        tip.setFont(f);

        return tip;
    }
}
