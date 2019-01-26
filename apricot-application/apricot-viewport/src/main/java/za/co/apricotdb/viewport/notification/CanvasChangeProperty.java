package za.co.apricotdb.viewport.notification;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The events procduced by the View Port.
 * 
 * @author Anton Nazarov
 * @since 25/01/2019
 */
public class CanvasChangeProperty {

    private boolean changed;

    private PropertyChangeSupport support;

    public CanvasChangeProperty() {
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
 
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
 
    public void setChanged(boolean value) {
        support.firePropertyChange("changed", this.changed, value);
        this.changed = value;
    }
    
    public boolean isChanged() {
        return changed;
    }
}
