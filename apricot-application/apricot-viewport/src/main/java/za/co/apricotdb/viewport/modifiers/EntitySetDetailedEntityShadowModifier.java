package za.co.apricotdb.viewport.modifiers;

import org.springframework.stereotype.Component;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

@Component
public class EntitySetDetailedEntityShadowModifier implements ElementVisualModifier {

    @Override
    public void modify(ApricotShape shape) {
        if (shape instanceof ApricotEntityShape) {
            VBox box = (VBox) shape;
            DropShadow shadow = buildShadow(shape);
            for (Node n : box.getChildren()) {
                if (n instanceof GridPane) {
                    GridPane gp = (GridPane) n;
                    gp.setEffect(shadow);
                }
            }
        }
    }

    private DropShadow buildShadow(ApricotShape shape) {
        DropShadow shadow = new DropShadow();
        shadow.setWidth(3);
        shadow.setHeight(3);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setRadius(4);

        switch (getEntityStatus(shape)) {
        case GRAYED:
            shadow.setColor(Color.LIGHTGRAY);
            break;
        default:
            shadow.setColor(Color.BLACK);
            break;
        }

        return shadow;
    }

    private ElementStatus getEntityStatus(ApricotShape shape) {
        ApricotEntityShape entityShape = (ApricotEntityShape) shape;

        return entityShape.getElement().getElementStatus();
    }
}
