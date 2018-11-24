package javafxapplication.relationship;

import javafxapplication.entity.ApricotEntity;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafxapplication.relationship.shape.BasicLinkPrimitivesBuilder;
import javafxapplication.relationship.shape.LinkPrimitivesBuilder;

public class OrdinaryLinkBuilder implements EntityLinkBuilder {

    LinkPrimitivesBuilder pb = new BasicLinkPrimitivesBuilder();

    @Override
    public Group buildLink(ApricotEntityLink l) {
        ApricotOrdinaryLink link = (ApricotOrdinaryLink) l;

        Polyline p = null;
        Shape end = null;
        Point2D startPoint = getStartPoint(link.getParent(), link.getChild(), link.getPrimaryFieldLayoutY());
        Point2D endPoint = getEndPoint(link.getParent(), link.getChild(), link.getForeignFieldLayoutY());

        double horDist = link.getParent().getHorizontalDistance(link.getChild());
        if (isParentLeft(link.getParent(), link.getChild())) {
            p = buildPolyline(startPoint, endPoint, horDist);
            link.setMiddleStepLayoutX(startPoint.getX() + horDist / 2);
            end = pb.getEnd(endPoint.subtract(new Point2D(LinkPrimitivesBuilder.LINK_END_DIAMETER, 0)));
        } else {
            p = buildPolyline(endPoint, startPoint, horDist);
            link.setMiddleStepLayoutX(endPoint.getX() + horDist / 2);
             end = pb.getEnd(endPoint.add(new Point2D(LinkPrimitivesBuilder.LINK_END_DIAMETER, 0)));
        }
        

// Group g = new OrdinaryShapeGroup(p);
        Group g = new Group(p, end);

        return g;
    }

    private Polyline buildPolyline(Point2D start, Point2D end, double horizontalDistance) {
        return new Polyline(start.getX(), start.getY(),
                start.getX() + horizontalDistance / 2, start.getY(),
                start.getX() + horizontalDistance / 2, end.getY(),
                end.getX(), end.getY()
        );
    }

    private Point2D getStartPoint(ApricotEntity parent, ApricotEntity child, double primaryFieldLayoutY) {
        Point2D startPoint = null;
        double x = 0;
        double y = 0;
        if (isParentLeft(parent, child)) {
            x = parent.getLayoutX() + parent.getWidth();
        } else {
            x = parent.getLayoutX();
        }
        y = parent.getLayoutY() + primaryFieldLayoutY;
        startPoint = new Point2D(x, y);

        return startPoint;
    }

    private Point2D getEndPoint(ApricotEntity parent, ApricotEntity child, double foreignFieldLayoutY) {
        Point2D endPoint = null;
        double x = 0;
        double y = 0;
        if (isParentLeft(parent, child)) {
            x = child.getLayoutX();
        } else {
            x = child.getLayoutX() + child.getWidth();
        }
        y = child.getLayoutY() + foreignFieldLayoutY;
        endPoint = new Point2D(x, y);

        return endPoint;
    }

    private boolean isParentLeft(ApricotEntity parent, ApricotEntity child) {
        return parent.getLayoutX() < child.getLayoutX();
    }
}
