package za.co.apricotdb.viewport.entity.shape;

import java.util.Collections;

import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.VLineTo;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.ChildRelationshipStackComparator;
import za.co.apricotdb.viewport.relationship.RelationshipsInNonIdentifyingStackComparator;

public class NonIdentifyingStack extends PrimaryKeyStack {

    private final Side side;

    public NonIdentifyingStack(ApricotEntityShape entityShape, Side side) {
        super(entityShape);
        this.side = side;
    }

    @Override
    public void sortRelationships() {
        Collections.sort(relationships, new RelationshipsInNonIdentifyingStackComparator());
        Collections.sort(childRelationships, new ChildRelationshipStackComparator());
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public Point2D getRelationshipStart(ApricotRelationship relationship) {
        Point2D ret = null;

        if (!hasRelationships()) {
            return null;
        }

        int idx = relationships.indexOf(relationship);
        if (idx >= 0) {
            Point2D stackStart = getStackStartingPoint();
            double y = stackStart.getY() + (idx + 1) * STACK_PARTICLE_LENGTH;
            double x = 0;
            if (side == Side.LEFT) {
                x = stackStart.getX() - STACK_ENTITY_DISTANCE;
            } else {
                x = stackStart.getX() + STACK_ENTITY_DISTANCE;
            }

            ret = new Point2D(x, y);
        }

        return ret;
    }

    @Override
    public Point2D getRelationshipEnd(ApricotRelationship relationship) {
        Point2D ret = null;

        if (!hasRelationships()) {
            return null;
        }

        int idx = childRelationships.indexOf(relationship);
        if (idx >= 0) {
            idx += relationships.size();
            Point2D stackStart = getStackStartingPoint();
            double y = stackStart.getY() + (idx + 1) * STACK_PARTICLE_LENGTH;
            double x = 0;
            if (side == Side.LEFT) {
                x = stackStart.getX() - STACK_ENTITY_DISTANCE;
            } else {
                x = stackStart.getX() + STACK_ENTITY_DISTANCE;
            }

            ret = new Point2D(x, y);
        }

        return ret;
    }

    public int getPrimaryLinkSize() {
        return relationships.size();
    }

    private Point2D getStackStartingPoint() {
        double X = 0;
        if (side == Side.LEFT) {
            X = entityShape.getLayoutX() + entityShape.getTranslateX();
        } else {
            X = entityShape.getLayoutX() + entityShape.getTranslateX() + entityShape.getWidth();
        }
        Point2D ret = new Point2D(X, getPrimaryKeyY());

        return ret;
    }

    private double getPrimaryKeyY() {
        double ret = -1;

        ApricotEntity entity = (ApricotEntity) entityShape.getElement();
        for (FieldDetail fd : entity.getDetails()) {
            if (fd.isPrimaryKey()) {
                ret = entityShape.getLayoutY() + +entityShape.getTranslateY()
                        + entityShape.getFieldLocalY(fd.getName());
            }
        }

        if (ret == -1) {
            FieldDetail fd = entity.getDetails().get(0);
            ret = entityShape.getLayoutY() + +entityShape.getTranslateY() + entityShape.getFieldLocalY(fd.getName());
        }

        return ret;
    }

    @Override
    public void build() {
        this.getElements().clear();

        Point2D start = getStackStartingPoint();
        this.getElements().add(new MoveTo(start.getX(), start.getY()));
        if (side == Side.LEFT) {
            this.getElements().add(new HLineTo(start.getX() - STACK_ENTITY_DISTANCE));
        } else {
            this.getElements().add(new HLineTo(start.getX() + STACK_ENTITY_DISTANCE));
        }

        this.getElements().add(new VLineTo(
                start.getY() + (relationships.size() + childRelationships.size()) * STACK_PARTICLE_LENGTH + 5));

        sortRelationships();
    }
}
