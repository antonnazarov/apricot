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
    private CompareState state;

    public CompareSnapshotRow(String source, boolean diff, String target, CompareRowType type, CompareState state) {
        this.source = new SimpleStringProperty(source);
        this.diff = new SimpleBooleanProperty(diff);
        this.target = new SimpleStringProperty(target);
        this.type = type;
        this.state = state;
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

    public CompareState getState() {
        return state;
    }

    public void setState(CompareState state) {
        this.state = state;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Source=[").append(source.getValue()).append("], ");
        sb.append("Target=[").append(target.getValue()).append("], ");
        sb.append("is different=[").append(diff.getValue()).append("], ");
        sb.append("type=[").append(type).append("], ");
        sb.append("state=[").append(state).append("]");
        
        return sb.toString();
    }
}
