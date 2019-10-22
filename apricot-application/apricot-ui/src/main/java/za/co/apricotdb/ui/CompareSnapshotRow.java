package za.co.apricotdb.ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This bean is a model for the Compare Snapshots TreeTableView.
 * 
 * @author Anton Nazarov
 * @since 22/10/2019
 */
public class CompareSnapshotRow {

    private SimpleStringProperty source;
    private SimpleBooleanProperty diff;
    private SimpleStringProperty target;
    private CompareObjectType type;

    public CompareSnapshotRow(String source, boolean diff, String target, CompareObjectType type) {
        this.source = new SimpleStringProperty(source);
        this.diff = new SimpleBooleanProperty(diff);
        this.target = new SimpleStringProperty(target);
        this.type = type;
    }

    public SimpleStringProperty getSource() {
        return source;
    }

    public SimpleBooleanProperty getDiff() {
        return diff;
    }

    public SimpleStringProperty getTarget() {
        return target;
    }

    public CompareObjectType getType() {
        return type;
    }

    public void setType(CompareObjectType type) {
        this.type = type;
    }
}
