package za.co.apricotdb.viewport.topology;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Tildas {

    private static final double HALF_WIDE = 4;
    private static final double CONTROL_DISP = 4;

    public void buildTilda(Point2D pos, boolean isHorisontal, Path path, List<Rectangle2D> rectangles) {

        if (rectangles != null) {
            // don't draw tilda if it is included into any rectangle
            for (Rectangle2D r : rectangles) {
                if (r.contains(pos)) {
                    return;
                }
            }
        }

        Point2D startPoint = null;
        Point2D endPoint = null;
        Point2D control1 = null;
        Point2D control2 = null;

        if (isHorisontal) {
            startPoint = new Point2D(pos.getX() - HALF_WIDE, pos.getY());
            endPoint = new Point2D(pos.getX() + HALF_WIDE, pos.getY());
            control1 = new Point2D(pos.getX() - HALF_WIDE / 2, pos.getY() - CONTROL_DISP);
            control2 = new Point2D(pos.getX() + HALF_WIDE / 2, pos.getY() + CONTROL_DISP);
        } else {
            startPoint = new Point2D(pos.getX(), pos.getY() - HALF_WIDE);
            endPoint = new Point2D(pos.getX(), pos.getY() + HALF_WIDE);
            control1 = new Point2D(pos.getX() - CONTROL_DISP, pos.getY() - HALF_WIDE / 2);
            control2 = new Point2D(pos.getX() + CONTROL_DISP, pos.getY() + HALF_WIDE / 2);
        }

        MoveTo move = new MoveTo(startPoint.getX(), startPoint.getY());
        CubicCurveTo cubicTo = new CubicCurveTo(control1.getX(), control1.getY(), control2.getX(), control2.getY(),
                endPoint.getX(), endPoint.getY());
        path.getElements().add(move);
        path.getElements().add(cubicTo);
    }

    public Path getTilda(Point2D pos, boolean isHorisontal) {
        Path p = new Path();
        buildTilda(pos, isHorisontal, p, null);

        return p;
    }
}
