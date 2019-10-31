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
    private boolean diff;
    private SimpleBooleanProperty equalize;
    private SimpleStringProperty target;
    private CompareRowType type;
    private CompareState state;
    private SimpleBooleanProperty checkBoxDisabled;
    private String objectName;

    public CompareSnapshotRow(String source, boolean diff, String target, CompareRowType type, CompareState state,
            String objectName) {
        this.source = new SimpleStringProperty(source);
        this.diff = diff;
        this.target = new SimpleStringProperty(target);
        this.equalize = new SimpleBooleanProperty(false);
        this.checkBoxDisabled = new SimpleBooleanProperty(false); // all the check boxes have been initialized editable

        this.type = type;
        this.state = state;

        this.objectName = objectName;
    }

    public SimpleStringProperty getSource() {
        return source;
    }

    public boolean getDiff() {
        return diff;
    }

    public SimpleBooleanProperty getEqualize() {
        return equalize;
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

    public SimpleBooleanProperty getCheckBoxDisabled() {
        return checkBoxDisabled;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Source=[").append(source.getValue()).append("], ");
        sb.append("Target=[").append(target.getValue()).append("], ");
        sb.append("is different=[").append(diff).append("], ");
        sb.append("type=[").append(type).append("], ");
        sb.append("state=[").append(state).append("]");

        return sb.toString();
    }
}
