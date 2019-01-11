package za.co.apricotdb.viewport.topology;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;

public class IntersectionManager {

    private static final double BORDER_DISP = 7;
    private Tildas t = new Tildas();

    public void intersect(LineSegment s, Rectangle2D rect, Path path, Path tildas, List<Rectangle2D> rectangles) {

        Rectangle2D r = null;
        if (tildas != null) {
            r = new Rectangle2D(rect.getMinX() - BORDER_DISP, rect.getMinY() - BORDER_DISP,
                    rect.getWidth() + BORDER_DISP * 2, rect.getHeight() + BORDER_DISP * 2);
        } else {
            r = new Rectangle2D(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
        }

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
                if (tildas != null) {
                    t.buildTilda(new Point2D(r.getMinX(), s.getPoint1().getY()), false, tildas, rectangles);
                    t.buildTilda(new Point2D(r.getMaxX(), s.getPoint1().getY()), false, tildas, rectangles);
                }

                path.getElements().add(new MoveTo(r.getMinX(), s.getPoint1().getY()));
                path.getElements().add(new HLineTo(r.getMaxX()));
            } else {
                if (tildas != null) {
                    t.buildTilda(new Point2D(s.getPoint1().getX(), r.getMinY()), true, tildas, rectangles);
                    t.buildTilda(new Point2D(s.getPoint1().getX(), r.getMaxY()), true, tildas, rectangles);
                }

                path.getElements().add(new MoveTo(s.getPoint1().getX(), r.getMinY()));
                path.getElements().add(new VLineTo(r.getMaxY()));
            }
        } else if (r.contains(s.getPoint1()) && !r.contains(s.getPoint2())) {
            intersect(s.getPoint1(), s.getPoint2(), r, path, s.isHorizontal(), tildas, rectangles);
        } else if (!r.contains(s.getPoint1()) && r.contains(s.getPoint2())) {
            intersect(s.getPoint2(), s.getPoint1(), r, path, s.isHorizontal(), tildas, rectangles);
        }
    }

    private void intersect(Point2D inside, Point2D outside, Rectangle2D r, Path p, boolean isHoriziontal, Path tildas,
            List<Rectangle2D> rectangles) {
        p.getElements().add(new MoveTo(inside.getX(), inside.getY()));
        if (isHoriziontal) {
            double intersectX = 0;
            if (outside.getX() < r.getMinX()) {
                intersectX = r.getMinX();
            } else {
                intersectX = r.getMaxX();
            }
            p.getElements().add(new HLineTo(intersectX));

            if (tildas != null) {
                t.buildTilda(new Point2D(intersectX, inside.getY()), false, tildas, rectangles);
            }
        } else {
            double intersectY = 0;
            if (outside.getY() < r.getMinY()) {
                intersectY = r.getMinY();
            } else {
                intersectY = r.getMaxY();
            }
            p.getElements().add(new VLineTo(intersectY));

            if (tildas != null) {
                t.buildTilda(new Point2D(inside.getX(), intersectY), true, tildas, rectangles);
            }
        }
    }
}
