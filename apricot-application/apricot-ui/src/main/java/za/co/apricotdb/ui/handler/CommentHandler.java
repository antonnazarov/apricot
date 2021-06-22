package za.co.apricotdb.ui.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This handler serves the comments, added to the columns of Entity.
 *
 * @author Anton Nazarov
 * @since 22/06/2021
 */
@Component
public class CommentHandler {

    @Resource
    EntityManager em;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    ViewManager viewManager;

    public Map<String, String> retrieveComments(String entityName) {
        Map<String, String> comments = new HashMap<>();

        Map<String, ApricotObjectLayout> cmt = readCommentsForEntity(entityName);
        for (String column : cmt.keySet()) {
            ApricotObjectLayout l = cmt.get(column);
            if (l != null && StringUtils.isNotEmpty(l.getObjectLayout())) {
                comments.put(column, l.getObjectLayout());
            }
        }

        return comments;
    }

    public void saveComments(String entityName, Map<String, String> comments) {
        Map<String, ApricotObjectLayout> cmt = readCommentsForEntity(entityName);
        ApricotView mainView = getMainView();

        for (String column : comments.keySet()) {
            ApricotObjectLayout l = cmt.get(column);
            String comment = comments.get(column);
            if (l != null) {
                if (StringUtils.isNotBlank(comment)) {
                    l.setObjectLayout(comment);
                    em.merge(l);
                } else {
                    em.remove(l);
                }
            } else {
                if (StringUtils.isNotBlank(comment)) {
                    ApricotObjectLayout newComment = new ApricotObjectLayout(LayoutObjectType.COLUMN_COMMENT, entityName+"::"+column, comment, mainView);
                    em.persist(newComment);
                }
            }
        }
    }

    private Map<String, ApricotObjectLayout> readCommentsForEntity(String entityName) {
        Map<String, ApricotObjectLayout> ret = new HashMap<>();

        ApricotView mainView = getMainView();
        if (mainView != null) {
            TypedQuery<ApricotObjectLayout> query = em.createNamedQuery("ApricotObjectLayout.getLayoutsForViewAndNamePrefix", ApricotObjectLayout.class);
            query.setParameter("view", mainView);
            query.setParameter("objectType", LayoutObjectType.COLUMN_COMMENT);
            query.setParameter("objectName", entityName);

            List<ApricotObjectLayout> layouts = query.getResultList();
            for (ApricotObjectLayout l : layouts) {
                if (StringUtils.isNotEmpty(l.getObjectName())) {
                    String[] splt = l.getObjectName().split("::");
                    if (splt.length == 2) {
                        ret.put(splt[1], l);
                    }
                }
            }
        }

        return ret;
    }

    private ApricotView getMainView() {
        return viewManager.getGeneralView(projectManager.findCurrentProject());
    }
}
