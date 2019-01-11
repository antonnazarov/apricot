package za.co.apricotdb.viewport.topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.VLineTo;

public class LineMutualIntersectionManager {

    private static final double HALF_MASK = 4;
    private static final double CONTROL_X = 5;

    TopologyManager manager = new TopologyManager();

    public void getIntersectionMask(List<Path> lines, List<Rectangle2D> rectangles, Path masks, Path detours) {

        Map<LineSegment, List<LineSegment>> intersections = new HashMap<>();
        List<LineSegment> allSegments = new ArrayList<>();
        for (Path p : lines) {
            allSegments.addAll(manager.getSegments(p));
        }

        for (LineSegment s : allSegments) {
            int idx = allSegments.indexOf(s);
            if (idx < allSegments.size()) {
                ListIterator<LineSegment> iterator = allSegments.listIterator(idx + 1);
                while (iterator.hasNext()) {
                    LineSegment ls = iterator.next();

                    if (manager.intersects(s, ls.getRectangle())) {
                        if (s.isHorizontal() && !ls.isHorizontal()) {
                            addIntersection(intersections, s, ls);
                        } else if (!s.isHorizontal() && ls.isHorizontal()) {
                            addIntersection(intersections, ls, s);
                        }
                    }
                }
            }
        }

        for (Entry<LineSegment, List<LineSegment>> e : intersections.entrySet()) {
            for (LineSegment vs : e.getValue()) {
                Point2D iPoint = getIntersectionPoint(e.getKey(), vs);

                boolean isContained = false;

                for (Rectangle2D r : rectangles) {
                    if (r.contains(iPoint)) {
                        isContained = true;
                        break;
                    }
                }

                if (!isContained) {
                    masks.getElements().add(new MoveTo(iPoint.getX(), iPoint.getY() - HALF_MASK));
                    masks.getElements().add(new VLineTo(iPoint.getY() + HALF_MASK));

                    detours.getElements().add(new MoveTo(iPoint.getX(), iPoint.getY() - HALF_MASK));
                    detours.getElements().add(new QuadCurveTo(iPoint.getX() + CONTROL_X, iPoint.getY(), iPoint.getX(),
                            iPoint.getY() + HALF_MASK));
                }
            }
        }
    }

    private void addIntersection(Map<LineSegment, List<LineSegment>> intersections, LineSegment horizontal,
            LineSegment vertical) {
        List<LineSegment> sgm = intersections.get(horizontal);
        if (sgm == null) {
            sgm = new ArrayList<>();
            intersections.put(horizontal, sgm);
        }
        sgm.add(vertical);

    }

    private Point2D getIntersectionPoint(LineSegment s1, LineSegment s2) {

        return new Point2D(s2.getPoint1().getX(), s1.getPoint1().getY());
    }
}
