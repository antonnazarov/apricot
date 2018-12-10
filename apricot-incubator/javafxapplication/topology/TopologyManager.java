package javafxapplication.topology;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.VLineTo;

/**
 * Topology algorithms are located here.
 *
 * @author Anton Nazarov
 * @since 06/12/2018
 */
public class TopologyManager {
    
    private IntersectionManager im = new IntersectionManager();

    public Path recalculatePath(Path path, List<Rectangle2D> rectangles, Path tildas) {

        Path resultPath = new Path();

        List<LineSegment> segments = getSegments(path);

        for (Rectangle2D r : rectangles) {
            for (LineSegment s : segments) {
                if (intersects(s, r)) {
                    // handle only if intersects
                    im.intersect(s, r, resultPath, tildas, rectangles);
                }
            }
        }

        return resultPath;
    }

    public List<LineSegment> getSegments(Path path) {
        List<LineSegment> segments = new ArrayList<>();

        Point2D point1 = null;
        Point2D point2 = null;
        for (PathElement pe : path.getElements()) {
            if (pe instanceof MoveTo) {
                MoveTo m = (MoveTo) pe;
                point1 = new Point2D(m.getX(), m.getY());
                point2 = null;
            }

            if (pe instanceof HLineTo) {
                HLineTo h = (HLineTo) pe;
                if (point1 != null) {
                    point2 = new Point2D(h.getX(), point1.getY());
                }
            }

            if (pe instanceof VLineTo) {
                VLineTo v = (VLineTo) pe;
                if (point1 != null) {
                    point2 = new Point2D(point1.getX(), v.getY());
                }
            }

            if (point1 != null && point2 != null) {
                // reset points
                LineSegment s = new LineSegment(point1, point2);
                segments.add(s);

                point1 = point2;
                point2 = null;
            }
        }

        return segments;
    }

    public boolean intersects(LineSegment s, Rectangle2D r) {
        return s.getRectangle().intersects(r);
    }
}
