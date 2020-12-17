package za.co.apricotdb.viewport.notification;

/**
 * This event signals that the context menu of the Relationship was called.
 *
 * @author Anton Nazarov
 * @since 17/12/2020
 */
import org.springframework.context.ApplicationEvent;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

public class RelationshipContextMenuEvent extends ApplicationEvent {

    private ApricotRelationship relationship;
    private double x;
    private double y;

    public RelationshipContextMenuEvent(ApricotRelationship relationship, double x, double y) {
        super(relationship);

        this.relationship = relationship;
        this.x = x;
        this.y = y;
    }

    public ApricotRelationship getRelationship() {
        return relationship;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
