package za.co.apricotdb.persistence.data;

import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.persistence.entity.ViewDetailLevel;
import za.co.apricotdb.persistence.repository.ApricotObjectLayoutRepository;
import za.co.apricotdb.persistence.repository.ApricotViewRepository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ViewManager {

    @Resource
    EntityManager em;

    @Resource
    ApricotViewRepository viewRepository;

    @Resource
    ApricotObjectLayoutRepository objectLayoutRepository;

    public ApricotView getGeneralView(ApricotProject project) {
        ApricotView ret = null;

        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getGeneralView", ApricotView.class);
        query.setParameter("project", project);

        List<ApricotView> res = query.getResultList();
        if (res == null || res.size() == 0) {
            ret = createGeneralView(project);
        } else {
            ret = res.get(0);
        }

        return ret;
    }

    /**
     * Get all views of the given project ordered by the ordinal position.
     */
    public List<ApricotView> getAllViews(ApricotProject project) {
        List<ApricotView> ret = new ArrayList<>();

        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getAllViewsOrdered", ApricotView.class);
        query.setParameter("project", project);
        List<ApricotView> res = query.getResultList();

        if (res != null && res.size() > 0) {
            return res;
        }

        return ret;
    }

    /**
     * Get all views which contains layout definition for the specific object.
     */
    public List<ApricotView> getViewsByObjectName(ApricotProject project, LayoutObjectType objectType,
            String objectName) {
        List<ApricotView> ret = new ArrayList<>();

        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getViewsByObjectName", ApricotView.class);
        query.setParameter("project", project);
        query.setParameter("objectType", objectType);
        query.setParameter("objectName", objectName);

        List<ApricotView> res = query.getResultList();

        if (res != null && res.size() > 0) {
            return res;
        }

        return ret;
    }

    public void removeGeneralView(ApricotProject project) {
        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getGeneralView", ApricotView.class);
        query.setParameter("project", project);

        List<ApricotView> res = query.getResultList();
        if (res != null && res.size() > 0) {
            for (ApricotView v : res) {
                for (ApricotObjectLayout l : v.getObjectLayouts()) {
                    objectLayoutRepository.delete(l);
                }
                viewRepository.delete(v);
            }
        }
    }

    @Transactional
    public void removeView(ApricotView view) {
        ApricotView v = viewRepository.getOne(view.getId());
        for (ApricotObjectLayout l : v.getObjectLayouts()) {
            objectLayoutRepository.delete(l);
        }

        viewRepository.delete(v);
    }

    public List<ApricotObjectLayout> getLayouts(ApricotView view) {
        TypedQuery<ApricotObjectLayout> query = em.createNamedQuery("ApricotView.getLayouts", ApricotObjectLayout.class);
        query.setParameter("view", view);

        return query.getResultList();
    }

    public ApricotView saveView(ApricotView view) {
        return viewRepository.saveAndFlush(view);
    }

    public ApricotView findViewById(long id) {
        Optional<ApricotView> o = viewRepository.findById(id);

        return o.get();
    }

    public ApricotView getViewById(long id) {
        ApricotView ret = null;
        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getViewById", ApricotView.class);
        query.setParameter("viewId", id);
        List<ApricotView> res = query.getResultList();
        if (res != null && res.size() == 1) {
            ret = res.get(0);
        }

        return ret;
    }

    public List<ApricotView> getViewByName(ApricotProject project, String name) {
        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getViewByName", ApricotView.class);
        query.setParameter("project", project);
        query.setParameter("name", name);

        return query.getResultList();
    }

    public int getMaxOrdinalPosition(ApricotProject project) {
        TypedQuery<Integer> query = em.createNamedQuery("ApricotView.getViewMaxOrdinalPosition", Integer.class);
        query.setParameter("project", project);

        return query.getSingleResult();
    }

    public ApricotView getCurrentView(ApricotProject project) {
        ApricotView ret = null;
        TypedQuery<ApricotView> query = em.createNamedQuery("ApricotView.getCurrentView", ApricotView.class);
        query.setParameter("project", project);
        query.setParameter("current", true);

        List<ApricotView> res = query.getResultList();
        if (res == null || res.isEmpty()) {
            ret = getGeneralView(project);
        } else {
            ret = res.get(0);
        }

        return ret;
    }

    public ApricotView setCurrentView(ApricotView currentView) {
        ApricotView view = getCurrentView(currentView.getProject());

        if (view == null) {
            view = getGeneralView(currentView.getProject());
        }

        view.setCurrent(false);
        saveView(view);
        currentView.setCurrent(true);
        return saveView(currentView);
    }

    public ApricotView createGeneralView(ApricotProject project) {
        ApricotView generalView = new ApricotView(ApricotView.MAIN_VIEW,
                "The main (general) view of the project " + project.getName(), new java.util.Date(), null, true, 0,
                project, null, true, ViewDetailLevel.DEFAULT);
        return saveView(generalView);
    }
}
