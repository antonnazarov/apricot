package za.co.apricotdb.viewport.entity.shape;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.notification.AbsentRelatedClickedEvent;

/**
 * The header of an entity.
 * 
 * @author Anton Nazarov
 * @since 13/04/2020
 */
public class EntityHeader extends HBox {

    private boolean absentParent;
    private boolean absentChild;
    private Text text;
    private ApricotCanvas canvas;

    public EntityHeader(String entityName, ApricotCanvas canvas) {
        super();
        this.canvas = canvas;
        text = new Text(entityName);
        text.setFont(EntityShapeBuilder.HEADER_FONT);
        setSpacing(4);
        draw();
    }

    public void setAbsentParent(boolean absentParent) {
        this.absentParent = absentParent;
        draw();
    }

    public void setAbsentChild(boolean absentChild) {
        this.absentChild = absentChild;
        draw();
    }

    public void draw() {
        AbsentRelatedClickedEvent event = new AbsentRelatedClickedEvent(text.getText());
        getChildren().clear();
        if (absentParent) {
            ImageView iv = getImageView();
            iv.setOnMousePressed(e -> {
                canvas.publishEvent(event);
            });
            Tooltip.install(iv, new Tooltip("There are some parent Entity(s) not included into this view"));
            getChildren().add(iv);
        }
        getChildren().add(text);
        if (absentChild) {
            ImageView iv = getImageView();
            iv.setOnMousePressed(e -> {
                canvas.publishEvent(event);
            });
            Tooltip.install(iv, new Tooltip("There are some child Entity(s) not included into this view"));
            getChildren().add(iv);
        }
    }

    public void setFill(Color color) {
        text.setFill(color);
    }

    private ImageView getImageView() {
        Image image = new Image(getClass().getResourceAsStream("crow-foot-small-12.png"));

        return new ImageView(image);
    }
}
