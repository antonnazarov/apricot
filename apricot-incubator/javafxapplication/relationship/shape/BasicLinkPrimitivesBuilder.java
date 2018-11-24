package javafxapplication.relationship.shape;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * Implementation of the graphical primitives of the link.
 * 
 * @author Anton Nazarov
 * @since 23/11/2018
 */
public class BasicLinkPrimitivesBuilder implements LinkPrimitivesBuilder {

    @Override
    public Shape getOptionalStart() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Shape getRuler() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Shape getEnd(Point2D p) {
        Circle c = new Circle(LinkPrimitivesBuilder.LINK_END_DIAMETER);
        c.setCenterX(p.getX());
        c.setCenterY(p.getY());
        
        c.setFill(Color.BLACK);
        
        return c;
    }
    
}
