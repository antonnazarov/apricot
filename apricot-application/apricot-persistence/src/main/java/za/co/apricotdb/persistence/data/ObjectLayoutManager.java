package za.co.apricotdb.persistence.data;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.persistence.repository.ApricotObjectLayoutRepository;

/**
 * Object Layout related low level- DB- operations are implemented in this
 * class.
 * 
 * @author Anton Nazarov
 * @since 17/01/2019
 */
@Component
public class ObjectLayoutManager {

    @Resource
    EntityManager em;

    @Resource
    ApricotObjectLayoutRepository layoutRepository;

    @Autowired
    ViewManager viewManager;

    public List<ApricotObjectLayout> getObjectLayoutsByType(ApricotView view, LayoutObjectType type) {
        TypedQuery<ApricotObjectLayout> query = em.createNamedQuery("ApricotObjectLayout.getLayoutsByType",
                ApricotObjectLayout.class);
        query.setParameter("view", view);
        query.setParameter("objectType", type);

        return query.getResultList();
    }

    public ApricotObjectLayout findLayoutByName(ApricotView view, String objectName) {
        ApricotObjectLayout ret = null;

        TypedQuery<ApricotObjectLayout> query = em.createNamedQuery("ApricotObjectLayout.getLayoutByName",
                ApricotObjectLayout.class);
        query.setParameter("view", view);
        query.setParameter("objectName", objectName);
        List<ApricotObjectLayout> layouts = query.getResultList();
        if (layouts != null && layouts.size() > 0) {
            ret = layouts.get(0);
        }

        return ret;
    }

    /**
     * Get layout objects of all types for all views in the project.
     */
    public List<ApricotObjectLayout> getLayoutsForProject(ApricotProject project, String objectName) {
        TypedQuery<ApricotObjectLayout> query = em.createNamedQuery("ApricotObjectLayout.getLayoutsForProject",
                ApricotObjectLayout.class);
        query.setParameter("project", project);
        query.setParameter("objectName", objectName);

        return query.getResultList();
    }

    /**
     * Get relationship layout objects for all views in the project.
     */
    public List<ApricotObjectLayout> getRelationshipLayoutsForProject(ApricotProject project, String objectName) {
        TypedQuery<ApricotObjectLayout> query = em
                .createNamedQuery("ApricotObjectLayout.getRelationshipLayoutsForProject", ApricotObjectLayout.class);
        query.setParameter("project", project);
        query.setParameter("objectType", LayoutObjectType.RELATIONSHIP);
        query.setParameter("objectName", objectName);

        return query.getResultList();
    }

    public void saveObjectLayout(ApricotObjectLayout layout) {
        layoutRepository.saveAndFlush(layout);
    }

    public void deleteObjectLayout(ApricotObjectLayout layout) {
        layoutRepository.delete(layout);
    }

    public List<ApricotObjectLayout> getObjectLayoutsByView(ApricotView view) {
        TypedQuery<ApricotObjectLayout> query = em.createNamedQuery("ApricotObjectLayout.getLayoutsForView",
                ApricotObjectLayout.class);
        query.setParameter("view", view);

        return query.getResultList();
    }

    /**
     * Delete all Object Layouts in the given view.
     */
    @Transactional
    public void deleteViewObjectLayouts(ApricotView view) {
        ApricotView managedView = viewManager.getViewById(view.getId());
        managedView.getObjectLayouts().clear();
        viewManager.saveView(managedView);
    }

    /**
     * Calculate the rate of the coverage of entities (tables) included in the given
     * collection by the corresponding layout objects of the project.
     */
    public double calcCoverageRate(List<ApricotTable> tables, ApricotView view) {
        double rate = 0.0;

        int cnt = 0;
        List<ApricotObjectLayout> layouts = getObjectLayoutsByType(view, LayoutObjectType.TABLE);
        for (ApricotTable table : tables) {
            for (ApricotObjectLayout l : layouts) {
                if (l.getObjectName().equals(table.getName())) {
                    cnt++;
                }
            }
        }
        rate = (double) cnt / tables.size();

        return rate;
    }
}
