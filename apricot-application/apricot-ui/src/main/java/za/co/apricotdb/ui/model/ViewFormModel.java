package za.co.apricotdb.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import za.co.apricotdb.persistence.entity.ApricotSnapshot;

/**
 * The model of the New/Edit View Form.
 *  
 * @author Anton Nazarov
 * @since 23/01/2019
 */
public class ViewFormModel {

    private boolean newView;
    private String viewName;
    private String comment;
    private final List<String> availableTables = new ArrayList<>();
    private final List<String> viewTables = new ArrayList<>();
    private final List<String> fromViews = new ArrayList<>();
    private ApricotSnapshot snapshot = null; 

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getAvailableTables() {
        return availableTables;
    }

    public List<String> getViewTables() {
        return viewTables;
    }

    public List<String> getFromViews() {
        return fromViews;
    }

    public void addAvailableTables(List<String> availableTables) {
        this.availableTables.addAll(availableTables);
    }

    public void addViewTables(List<String> viewTables) {
        this.viewTables.addAll(viewTables);
    }

    public void addFromViews(List<String> fromViews) {
        this.fromViews.addAll(fromViews);
    }

    public boolean isNewView() {
        return newView;
    }

    public void setNewView(boolean newView) {
        this.newView = newView;
    }
    
    public ApricotSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(ApricotSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public void addAllItems() {
        viewTables.addAll(availableTables);
        Collections.sort(viewTables);
        availableTables.clear();
    }
    
    public void removeAllItems() {
        availableTables.addAll(viewTables);
        Collections.sort(availableTables);
        viewTables.clear();
    }
    
    public void addSelectedItems(List<String> items) {
        for (String s : items) {
            availableTables.remove(s);
        }
        viewTables.addAll(items);
        
        Collections.sort(availableTables);
        Collections.sort(viewTables);
    }
    
    public void removeSelectedItems(List<String> items) {
        for (String s : items) {
            viewTables.remove(s);
        }
        availableTables.addAll(items);
        
        Collections.sort(availableTables);
        Collections.sort(viewTables);
    }
}
