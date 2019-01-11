package za.co.apricotdb.viewport.topology;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

/**
 * The line segment, vertical or horizontal.
 * 
 * @author Anton Nazarov
 * @since 06/12/2018
 */
public class LineSegment {
    private Point2D point1;
    private Point2D point2;

    public LineSegment(Point2D point1, Point2D point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Rectangle2D getRectangle() {
        Rectangle2D ret = null;

        Point2D start = getStartPoint();
        double width = 0;
        double height = 0;

        if (isHorizontal()) {
            width = Math.abs(point1.getX() - point2.getX());
        } else {
            height = Math.abs(point1.getY() - point2.getY());
        }

        ret = new Rectangle2D(start.getX(), start.getY(), width, height);

        return ret;
    }

    public boolean isHorizontal() {
        return point1.getY() == point2.getY();
    }

    public Point2D getStartPoint() {
        Point2D ret = null;

        if (isHorizontal()) {
            if (point1.getX() < point2.getX()) {
                ret = point1;
            } else {
                ret = point2;
            }
        } else {
            if (point1.getY() < point2.getY()) {
                ret = point1;
            } else {
                ret = point2;
            }
        }

        return ret;
    }

    public Point2D getPoint1() {
        return point1;
    }

    public Point2D getPoint2() {
        return point2;
    }

    public boolean isDown() {
        boolean ret = false;

        if (!this.isHorizontal()) {
            if (point1.getY() < point2.getY()) {
                ret = true;
            }
        }

        return ret;
    }

    public boolean isRight() {
        boolean ret = false;

        if (this.isHorizontal()) {
            if (point1.getX() < point2.getX()) {
                ret = true;
            }
        }

        return ret;
    }

    @Override
    public String toString() {
        String direction;

        if (isHorizontal()) {
            if (isRight()) {
                direction = "RIGHT";
            } else {
                direction = "LEFT";
            }
        } else {
            if (isDown()) {
                direction = "DOWN";
            } else {
                direction = "UP";
            }
        }

        StringBuilder sb = new StringBuilder("LineSegment: ");
        sb.append("point1=[").append(point1).append("], point2=[").append(point2);
        sb.append("], isHorizontal=[").append(isHorizontal()).append("]");
        sb.append(", direction=[").append(direction).append("]");

        return sb.toString();
    }
}
