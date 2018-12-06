package javafxapplication.topology;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;

public class IntersectionManager {

    public void intersect(LineSegment s, Rectangle2D r, Path path) {

        if (r.contains(s.getPoint1()) && r.contains(s.getPoint2())) {
            // the whole segment is inside the rectangle: just redraw the original line
            path.getElements().add(new MoveTo(s.getPoint1().getX(), s.getPoint1().getY()));
            if (s.isHorizontal()) {
                path.getElements().add(new HLineTo(s.getPoint2().getX()));
            } else {
                path.getElements().add(new VLineTo(s.getPoint2().getY()));
            }
        } else if (!r.contains(s.getPoint1()) && !r.contains(s.getPoint2())) {
            // the both ends of the segment are outside of the rectangle
            if (s.isHorizontal()) {
                path.getElements().add(new MoveTo(r.getMinX(), s.getPoint1().getY()));
                path.getElements().add(new HLineTo(r.getMaxX()));
            } else {
                path.getElements().add(new MoveTo(s.getPoint1().getX(), r.getMinY()));
                path.getElements().add(new VLineTo(r.getMaxY()));
            }
        } else if (r.contains(s.getPoint1()) && !r.contains(s.getPoint2())) {
            intersect(s.getPoint1(), s.getPoint2(), r, path, s.isHorizontal());
        } else if (!r.contains(s.getPoint1()) && r.contains(s.getPoint2())) {
            intersect(s.getPoint2(), s.getPoint1(), r, path, s.isHorizontal());
        }
    }

    private void intersect(Point2D inside, Point2D outside, Rectangle2D r, Path p, boolean isHoriziontal) {
        p.getElements().add(new MoveTo(inside.getX(), inside.getY()));
        if (isHoriziontal) {
            double intersectX = 0;
            if (outside.getX() < r.getMinX()) {
                intersectX = r.getMinX();
            } else {
                intersectX = r.getMaxX();
            }
            p.getElements().add(new HLineTo(intersectX));
        } else {
            double intersectY = 0;
            if (outside.getY() < r.getMinY()) {
                intersectY = r.getMinY();
            } else {
                intersectY = r.getMaxY();
            }
            p.getElements().add(new VLineTo(intersectY));
        }
    }
}
