package javafxapplication.relationship.shape;

import javafx.scene.Group;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafxapplication.canvas.ApricotShape;
import javafxapplication.relationship.ApricotEntityLink;

/**
 * The common features of all link shapes.
 * 
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public abstract class ApricotLinkShape extends Group implements ApricotShape {

    private final ApricotEntityLink entityLink;
    private Path path;
    private Shape startElement;
    private Shape endElement;

    public ApricotLinkShape(ApricotEntityLink entityLink) {
        this.entityLink = entityLink;
    }
}
