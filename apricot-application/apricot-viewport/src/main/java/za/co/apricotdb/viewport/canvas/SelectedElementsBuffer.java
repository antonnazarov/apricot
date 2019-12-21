package za.co.apricotdb.viewport.canvas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * This class has been being a storage of previously selected elements (Entities
 * and Relationships). It allows to not to re-request a set of related elements
 * on every dragging/move of the Entity (Entities) in the current canvas.
 * 
 * @author Anton Nazarov
 * @since 21/12/2019
 */
public class SelectedElementsBuffer implements Serializable {

    private static final long serialVersionUID = 9096651105481387403L;
    Logger logger = LoggerFactory.getLogger(SelectedElementsBuffer.class);

    private ApricotCanvas canvas;
    private List<ApricotEntity> selectedEntityBuffer = new ArrayList<>();
    private List<ApricotEntity> entityBuffer = new ArrayList<>();
    private List<ApricotRelationship> relationshipBuffer = new ArrayList<>();

    public SelectedElementsBuffer(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Retrieve related objects, taking into account the buffered lists.
     */
    public void getRelatedSelectedObjects(Set<ApricotEntity> relatedEntities,
            Set<ApricotRelationship> relatedRelationships) {
        relatedEntities.clear();
        relatedRelationships.clear();

        if (isEqualSelectedEntityLists()) {
            relatedEntities.addAll(new ArrayList<>(entityBuffer));
            relatedRelationships.addAll(new ArrayList<>(relationshipBuffer));

            return;
        }

        for (ApricotEntity e : canvas.getSelectedEntities()) {
            findRelatedEntities(e, relatedEntities);
            relatedEntities.add(e);
        }

        for (ApricotEntity e : relatedEntities) {
            relatedRelationships.addAll(e.getPrimaryLinks());
            relatedRelationships.addAll(e.getForeignLinks());
        }

        selectedEntityBuffer.clear();
        entityBuffer.clear();
        relationshipBuffer.clear();
        selectedEntityBuffer.addAll(canvas.getSelectedEntities());
        entityBuffer.addAll(relatedEntities);
        relationshipBuffer.addAll(relatedRelationships);
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

    /**
     * Check if the selected and buffered lists are the same.
     */
    private boolean isEqualSelectedEntityLists() {
        if (selectedEntityBuffer.size() != canvas.getSelectedEntities().size()) {
            return false;
        }

        List<ApricotEntity> tmpSelected = new ArrayList<>(canvas.getSelectedEntities());
        tmpSelected.removeAll(selectedEntityBuffer);

        return tmpSelected.size() == 0;
    }
}
