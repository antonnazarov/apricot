package javafxapplication.relationship;

import javafxapplication.entity.ApricotEntity;
import javafx.scene.Group;

/**
 * This class contains attributes common for all types of links.
 *
 * @author Anton Nazarov
 * @since 22/11/2018
 */
public abstract class ApricotGenericLink implements ApricotEntityLink {

    private final ApricotEntity parent;
    private final ApricotEntity child;
    private final double primaryFieldLayoutY;
    private final double foreignFieldLayoutY;
    RelationshipType type;
    boolean selected;
    Group link;

    public ApricotGenericLink(ApricotEntity parent, ApricotEntity child,
            double primaryFieldLayoutY, double foreignFieldLayoutY,
            RelationshipType type) {
        this.parent = parent;
        this.child = child;
        this.primaryFieldLayoutY = primaryFieldLayoutY;
        this.foreignFieldLayoutY = foreignFieldLayoutY;
        this.type = type;
    }

    @Override
    public ApricotEntity getParent() {
        return parent;
    }

    @Override
    public ApricotEntity getChild() {
        return child;
    }

    @Override
    public double getPrimaryFieldLayoutY() {
        return primaryFieldLayoutY;
    }

    @Override
    public double getForeignFieldLayoutY() {
        return foreignFieldLayoutY;
    }

    public RelationshipType getType() {
        return type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Group getLink() {
        return link;
    }

    public void setLink(Group link) {
        this.link = link;
    }
}
