package javafxapplication.relationship;

import javafxapplication.entity.ApricotEntity;
import javafxapplication.relationship.shape.ApricotLinkShape;

/**
 * This class is a generic implementation of the ApricotEntityLink interface.
 *
 * @author Anton Nazarov
 * @since 22/11/2018
 */
public class ApricotBasicEntityLink implements ApricotEntityLink {

    private final ApricotEntity parent;
    private final ApricotEntity child;
    private final String primaryFieldName;
    private final String foreignFieldName;
    private final RelationshipType type;
    ApricotLinkShape linkShape;
    private boolean selected;

    /**
     * Construct the EntityLink bean.
     */
    public ApricotBasicEntityLink(ApricotEntity parent, ApricotEntity child, String primaryFieldName,
            String foreignFieldName, RelationshipType type) {
        this.parent = parent;
        this.child = child;
        this.primaryFieldName = primaryFieldName;
        this.foreignFieldName = foreignFieldName;
        this.type = type;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public ApricotLinkShape getLinkShape() {
        return linkShape;
    }

    @Override
    public void hide() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public void show() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public void grayDown(boolean grayed) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }
}
