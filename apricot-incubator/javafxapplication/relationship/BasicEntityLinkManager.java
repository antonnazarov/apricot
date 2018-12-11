package javafxapplication.relationship;

import javafx.scene.Group;
import javafxapplication.entity.ApricotBBBEntity;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * A basic implementation of the ApricotEntityLinkManager interface.
 *
 * @author Anton Nazaroc
 * @since 22/11/2018
 */
public class BasicEntityLinkManager implements ApricotEntityLinkManager {

    public static final double MINIMAL_SEGMENT_LENGTH = 20;
    public static final double VERTICAL_CORRECTION = -4;

    private ApricotBBBEntity parent;
    private ApricotBBBEntity child;
    Text primaryField;
    Text foreignField;

    @Override
    public void createEntityLink(String parentEntityName, String childEntityName, String primaryKeyName,
            String foreignKeyName, RelationshipType relationshipType, Pane entityCanvas) {

        findParentAndChild(parentEntityName, childEntityName, entityCanvas);
        if (parent == null || child == null) {
            System.out.println("Unable to find parent/child for the link");
            return;
        }

        findPrimaryForeignKeys(primaryKeyName, foreignKeyName);
        if (primaryField == null || foreignField == null) {
            System.out.println("Unable to find primaryField/foreignField for the link");
            return;
        }

        GridPane pk = (GridPane) parent.getChildren().get(1);
        GridPane npk = (GridPane) child.getChildren().get(2);
        double primaryFieldLayoutY = pk.getLayoutY() + primaryField.getLayoutY() + VERTICAL_CORRECTION;
        double foreignFieldLayoutY = npk.getLayoutY() + foreignField.getLayoutY() + VERTICAL_CORRECTION;

        ApricotEntityLink entityLink = null;
        EntityLinkBuilder builder = null;
        if (parent.getHorizontalDistance(child) >= MINIMAL_SEGMENT_LENGTH) {
            entityLink = new ApricotOrdinaryLink(parent, child, primaryFieldLayoutY, foreignFieldLayoutY,
                    relationshipType);
        }

        if (entityLink != null) {
            builder = entityLink.getLinkBuilder();
            Group g = builder.buildLink(entityLink);
            entityCanvas.getChildren().add(g);
        }
    }

    @Override
    public void handleEntityLink(ApricotEntityLink link) {

    }

    private void findParentAndChild(String parentEntityName, String childEntityName, Pane entityCanvas) {
        for (Node n : entityCanvas.getChildren()) {
            if (n instanceof ApricotBBBEntity) {
                if (n.getId().equals(parentEntityName)) {
                    parent = (ApricotBBBEntity) n;
                }

                if (n.getId().equals(childEntityName)) {
                    child = (ApricotBBBEntity) n;
                }
            }
        }
    }

    private void findPrimaryForeignKeys(String primaryKeyName, String foreignKeyName) {
        primaryField = parent.getTextObjectForField(primaryKeyName);
        foreignField = child.getTextObjectForField(foreignKeyName);
    }
}
