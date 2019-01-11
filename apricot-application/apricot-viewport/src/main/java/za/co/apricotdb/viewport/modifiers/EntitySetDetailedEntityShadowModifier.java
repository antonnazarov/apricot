package za.co.apricotdb.viewport.modifiers;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

public class EntitySetDetailedEntityShadowModifier implements ElementVisualModifier {

    @Override
    public void modify(ApricotShape shape) {
        if (shape instanceof ApricotEntityShape) {
            VBox box = (VBox) shape;
            DropShadow shadow = buildShadow();
            for (Node n : box.getChildren()) {
                if (n instanceof GridPane) {
                    GridPane gp = (GridPane) n;
                    gp.setEffect(shadow);
                }
            }
        }
    }

    private DropShadow buildShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setWidth(3);
        shadow.setHeight(3);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setRadius(4);

        return shadow;
    }
}
