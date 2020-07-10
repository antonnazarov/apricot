package za.co.apricotdb.ui.repository;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.context.ApplicationContext;
import za.co.apricotdb.ui.handler.RepositoryHandler;
import za.co.apricotdb.ui.util.ImageHelper;

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

        // populate the horizontal box with the textual and graphical definitions of the
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
                    handler.importRepoSnapshot(row);
                });
            }

            getChildren().add(btn);
        }

        getChildren().add(this.image);
        if (objectName != null) {
            getChildren().add(this.text);

            //  the export part
            if (row.getRowType() == RowType.PROJECT && !row.includesSnapshots() && !row.isEqual() && !remote) {
                Button btn = getButton("export-27.png");
                btn.setTooltip(getToolTip("Export the Project into the Repository"));
                btn.setOnAction(e -> {
                    handler.exportLocalProject(row);
                });
                getChildren().addAll(btn, getPlaceholder());
            } else if (row.getRowType() == RowType.SNAPSHOT && !row.isEqual() && !remote) {
                Button btn = getButton("export-27.png");
                btn.setTooltip(getToolTip("Export the Snapshot into the Repository"));
                btn.setOnAction(e -> {
                    handler.exportLocalSnapshot(row);
                });
                getChildren().addAll(btn, getPlaceholder());
            }
        }

        if (remote && objectName != null) {
            Button btn = getButton("triple-dot-27.png");
            ContextMenu contextMenu = new ContextMenu();
            if (row.getRowType() == RowType.PROJECT) {
                MenuItem item = new MenuItem("View Project Info");
                item.setOnAction(e -> {
                    handler.showRemoteProjectInfo(row);
                });
                contextMenu.getItems().add(item);
                item = new MenuItem("Delete Project from Remote Repository");
                item.setOnAction(e -> {
                    handler.deleteRemoteProject(row);
                });
                contextMenu.getItems().add(item);
            } else {
                MenuItem item = new MenuItem("View Snapshot Info");
                item.setOnAction(e -> {
                    handler.showRemoteSnapshotInfo(row);
                });
                contextMenu.getItems().add(item);
                item = new MenuItem("Delete Snapshot from Remote Repository");
                item.setOnAction(e -> {
                    handler.deleteRemoteSnapshot(row);
                });
                contextMenu.getItems().add(item);
            }
            btn.setOnMouseClicked(e -> {
                contextMenu.setAutoHide(true);
                contextMenu.show(btn, e.getScreenX(), e.getScreenY());
            });
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
        ImageView ret = new ImageView(ImageHelper.getImage("does-not-exist-grey-27.png", getClass()));
        if (objectName != null) {
            ret = new ImageView(ImageHelper.getImage(row.getRowType().getImageFile(), getClass()));
        }

        return ret;
    }

    private ImageView getPlaceholder() {
        return new ImageView(ImageHelper.getImage("placeholder-27.png", getClass()));
    }

    private Button getButton(String imageFile) {
        Button btn = new Button();
        btn.setGraphic(new ImageView(ImageHelper.getImage(imageFile, getClass())));

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
