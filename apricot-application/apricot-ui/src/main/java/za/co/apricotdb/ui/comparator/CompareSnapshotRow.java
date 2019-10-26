package za.co.apricotdb.ui.comparator;

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
    private CompareRowType type;

    public CompareSnapshotRow(String source, boolean diff, String target, CompareRowType type) {
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

    public CompareRowType getType() {
        return type;
    }

    public void setType(CompareRowType type) {
        this.type = type;
    }
}
