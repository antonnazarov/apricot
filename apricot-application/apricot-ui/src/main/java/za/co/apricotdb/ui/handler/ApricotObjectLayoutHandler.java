package za.co.apricotdb.ui.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;

/**
 * This component implements the business operations related to the Object
 * Layout.
 * 
 * @author Anton Nazarov
 * @since 12/03/2019
 */
@Component
public class ApricotObjectLayoutHandler {

    @Autowired
    ObjectLayoutManager layoutManager;

    @Autowired
    ProjectManager projectManager;

    public void duplicateObjectLayoutsForNewEntityName(String oldName, String newName) {
        ApricotProject project = projectManager.findCurrentProject();

        List<ApricotObjectLayout> layouts = layoutManager.getLayoutsForProject(project, oldName);
        for (ApricotObjectLayout l : layouts) {
            if (layoutManager.findLayoutByName(l.getView(), newName) == null) {
                ApricotObjectLayout clone = l.clone();
                clone.setObjectName(newName);
                layoutManager.saveObjectLayout(clone);
            }
        }

        layouts = layoutManager.getRelationshipLayoutsForProject(project, oldName);
        for (ApricotObjectLayout l : layouts) {
            ApricotObjectLayout clone = cloneRelationship(l, oldName, newName);
            if (clone != null && layoutManager.findLayoutByName(l.getView(), clone.getObjectName()) == null) {
                layoutManager.saveObjectLayout(clone);
            }
        }
    }

    private ApricotObjectLayout cloneRelationship(ApricotObjectLayout relationship, String oldName, String newName) {
        ApricotObjectLayout ret = null;
        String[] s = relationship.getObjectName().split("\\|");
        boolean updated = false;
        if (s.length >= 4) {
            if (s[0].equals(oldName)) {
                s[0] = newName;
                updated = true;
            }
            if (s[1].equals(oldName)) {
                s[1] = newName;
                updated = true;
            }

            if (updated) {
                StringBuilder sb = new StringBuilder();
                sb.append(s[0]).append("|").append(s[1]).append("|").append(s[2]).append("|").append(s[3]).append("|");
                ret = relationship.clone();
                ret.setObjectName(sb.toString());
            }
        }

        return ret;
    }

}
