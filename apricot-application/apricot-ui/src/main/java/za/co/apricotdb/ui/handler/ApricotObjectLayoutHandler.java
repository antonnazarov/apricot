package za.co.apricotdb.ui.handler;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.ObjectLayoutManager;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.LayoutObjectType;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;

/**
 * This component implements the business operations related to the Object
 * Layout.
 * 
 * @author Anton Nazarov
 * @since 12/03/2019
 */
@Component
public class ApricotObjectLayoutHandler {

    public static final double PASTE_BIAS = 50;

    @Autowired
    ObjectLayoutManager layoutManager;

    @Autowired
    ProjectManager projectManager;
    
    @Autowired
    ViewManager viewManager;

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

    /**
     * Clone the layout for the give table either into the new or the current view.
     */
    public void cloneTableLayout(ApricotView sourceView, ApricotView currentView, String tableName, String clonedName) {
        ApricotProject project = projectManager.findCurrentProject();
        ApricotObjectLayout layout = null;
        ApricotObjectLayout clonedLayout = null;
        if (sourceView != null) {
            layout = layoutManager.findLayoutByName(sourceView, tableName);
            clonedLayout = getClonedLayout(clonedName, layout.getObjectLayout(), currentView);
        } else {
            // copy inside the current view
            layout = layoutManager.findLayoutByName(currentView, tableName);
            clonedLayout = getClonedLayout(clonedName, getBiasedAllocationItem(layout.getObjectLayout()), currentView);
        }

        layoutManager.saveObjectLayout(clonedLayout);
        
        //  for the non Main View, copy the layout in the main view too
        if (!currentView.getName().equals(ApricotView.MAIN_VIEW)) {
            List<ApricotView> vws = viewManager.getViewByName(project, ApricotView.MAIN_VIEW);
            if (vws != null && vws.size() == 1) {
                ApricotView mainView = vws.get(0);
                layout = layoutManager.findLayoutByName(mainView, tableName);
                clonedLayout = getClonedLayout(clonedName, getBiasedAllocationItem(layout.getObjectLayout()), mainView);
                layoutManager.saveObjectLayout(clonedLayout);
            }
        }
    }

    private ApricotObjectLayout getClonedLayout(String clonedName, String clonedProperties, ApricotView view) {
        ApricotObjectLayout layout = layoutManager.findLayoutByName(view, clonedName);
        if (layout == null) {
            layout = new ApricotObjectLayout(LayoutObjectType.TABLE, clonedName, clonedProperties, view);
        } else {
            layout.setObjectType(LayoutObjectType.TABLE);
            layout.setObjectLayout(clonedProperties);
        }

        return layout;
    }

    private String getBiasedAllocationItem(String sLayout) {
        CanvasAllocationItem alloc = new CanvasAllocationItem();
        alloc.setPropertiesFromString(sLayout);
        Properties props = alloc.getProperties();
        double layoutX = Double.parseDouble(props.getProperty("layoutX"));
        double layoutY = Double.parseDouble(props.getProperty("layoutY"));

        layoutX += PASTE_BIAS;
        layoutY += PASTE_BIAS;
        props.setProperty("layoutX", String.valueOf(layoutX));
        props.setProperty("layoutY", String.valueOf(layoutY));
        alloc.setProperties(props);

        return alloc.getPropertiesAsString();
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
