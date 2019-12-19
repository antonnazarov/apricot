package za.co.apricotdb.viewport.relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import javafx.geometry.Side;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.DefaultEntityShape;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopology;
import za.co.apricotdb.viewport.relationship.shape.RelationshipTopologyImpl;

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
     * Draw/re-draw the relationships for the given entities and the detailLevel.
     */
    public void buildRelationships(List<ApricotEntity> entities, String detailLevel) {
        Set<ApricotEntity> relatedEntities = new HashSet<>();
        Set<ApricotRelationship> relatedRelationships = new HashSet<>();
        getRelatedObjects(entities, relatedEntities, relatedRelationships);
        
        buildRelationships(new ArrayList<ApricotEntity>(relatedEntities), new ArrayList<ApricotRelationship>(relatedRelationships), detailLevel);
    }
    
    /**
     * Draw/re-draw the relationships for the given entities, relationships and the detailLevel.
     */
    public void buildRelationships(List<ApricotEntity> entities, List<ApricotRelationship> relationships, String detailLevel) {
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
                                    break;

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

    /**
     * Collect all related entities and relationships, eligible for the redrawing on
     * the canvas.
     */
    private void getRelatedObjects(List<ApricotEntity> entities, Set<ApricotEntity> relatedEntities,
            Set<ApricotRelationship> relatedRelationships) {

        relatedEntities.clear();
        relatedRelationships.clear();

        for (ApricotEntity e : entities) {
            findRelatedEntities(e, relatedEntities);
            relatedEntities.add(e);
        }

        for (ApricotEntity e : relatedEntities) {
            relatedRelationships.addAll(e.getPrimaryLinks());
            relatedRelationships.addAll(e.getForeignLinks());
        }
    }

    private void findRelatedEntities(ApricotEntity e, Set<ApricotEntity> relatedEntities) {
        // primary links
        for (ApricotRelationship r : e.getPrimaryLinks()) {
            relatedEntities.add(r.getChild());
        }
        // foreign links
        for (ApricotRelationship r : e.getForeignLinks()) {
            relatedEntities.add(r.getParent());
        }
    }
}
