package za.co.apricotdb.viewport.relationship.shape;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Implementation of the graphical primitives of the link.
 * 
 * @author Anton Nazarov
 * @since 23/11/2018
 */
public abstract class PrimitivesBuilderImpl implements PrimitivesBuilder {

    public static final double RULER_LENGTH = 7;

    @Override
    public Shape getRuler() {
        Rectangle r = new Rectangle(RULER_LENGTH, RULER_LENGTH);
        r.setFill(Color.BLACK);
        r.setStroke(Color.BLACK);

        r.managedProperty().bind(r.visibleProperty());
        r.setVisible(false);

        return r;
    }
}
