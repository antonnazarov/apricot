package za.co.apricotdb.viewport.relationship;

import javafx.geometry.Side;
import org.springframework.stereotype.Component;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.SelectedElementsBuffer;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.DefaultEntityShape;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopology;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopologyImpl;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This component selectively builds the relationships for the provided list of
 * entities.
 * 
 * @author Anton Nazarov
 * @since 19/12/2019
 */
@Component
public class RelationshipBatchBuilder {

    private RelationshipTopology topology = new RelationshipTopologyImpl();

    @PostConstruct
    public void init() {
        topology = new RelationshipTopologyImpl();
    }

    /**
     * Rebuild relationships, using the buffer of the currently
     */
    public void buildRelationships(ApricotCanvas canvas) {
        Set<ApricotEntity> relatedEntities = new HashSet<>();
        Set<ApricotRelationship> relatedRelationships = new HashSet<>();
        SelectedElementsBuffer buffer = canvas.getSelectedElementsBuffer();
        buffer.getRelatedSelectedObjects(relatedEntities, relatedRelationships);

        buildRelationships(new ArrayList(relatedEntities),
                new ArrayList(relatedRelationships), canvas.getDetailLevel());
    }

    /**
     * Draw/re-draw the relationships for the given entities, relationships and the
     * detailLevel.
     */
    public void buildRelationships(List<ApricotEntity> entities, List<ApricotRelationship> relationships,
            String detailLevel) {
        for (ApricotEntity entity : entities) {
            if (entity.getEntityShape() != null) {
                if (entity.getEntityShape() instanceof DefaultEntityShape) {
                    DefaultEntityShape entityShape = (DefaultEntityShape) entity.getEntityShape();
                    entityShape.resetAllStacks();
                    entityShape.getEntityGroup().getChildren().remove(entityShape.getLeftStack());
                    entityShape.getEntityGroup().getChildren().remove(entityShape.getRightStack());
                    entityShape.getEntityGroup().getChildren().remove(entityShape.getTopStack());

                    for (ApricotRelationship r : entity.getPrimaryLinks()) {
                        Side side = topology.getRelationshipSide(r, true);
                        switch (side) {
                        case LEFT:
                            entityShape.getLeftStack().addRelationship(r);
                            break;
                        case RIGHT:
                            entityShape.getRightStack().addRelationship(r);
                            break;
                        case TOP:
                            entityShape.getTopStack().addRelationship(r);
                            break;
                        default:
                            break;
                        }
                    }

                    // the simplified view requires
                    if (detailLevel.equals("SIMPLE")) {
                        for (ApricotRelationship r : entity.getForeignLinks()) {
                            Side side = topology.getRelationshipSide(r, false);
                            switch (side) {
                            case LEFT:
                                entityShape.getLeftStack().addChildRelationship(r);
                                break;
                            case RIGHT:
                                entityShape.getRightStack().addChildRelationship(r);
                                break;
                            case TOP:
                                entityShape.getTopStack().addChildRelationship(r);
                                break;
                            default:
                                break;
                            }
                        }
                    } else {
                        // check if the normal and extended views have the primary and foreign key
                        // in the same time
                        for (ApricotRelationship r : entity.getForeignLinks()) {
                            if (r.getRelationshipType() == RelationshipType.IDENTIFYING) {
                                Side side = topology.getRelationshipSide(r, false);
                                switch (side) {
                                case LEFT:
                                    if (entityShape.getLeftStack().getPrimaryLinkSize() > 0) {
                                        entityShape.getLeftStack().addChildRelationship(r);
                                    }
                                    break;
                                case RIGHT:
                                    if (entityShape.getRightStack().getPrimaryLinkSize() > 0) {
                                        entityShape.getRightStack().addChildRelationship(r);
                                    }
                                    break;
                                case TOP:
                                default:
                                    break;
                                }
                            }
                        }
                    }

                    if (entityShape.getLeftStack().hasRelationships()) {
                        entityShape.getLeftStack().build();
                        entityShape.getEntityGroup().getChildren().add(entityShape.getLeftStack());
                    }
                    if (entityShape.getRightStack().hasRelationships()) {
                        entityShape.getRightStack().build();
                        entityShape.getEntityGroup().getChildren().add(entityShape.getRightStack());
                    }
                    if (entityShape.getTopStack().hasRelationships()) {
                        entityShape.getTopStack().build();
                        entityShape.getEntityGroup().getChildren().add(entityShape.getTopStack());
                    }
                }
            }
        }

        for (ApricotRelationship r : relationships) {
            r.buildShape();
        }
    }
}
