package za.co.apricotdb.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Component;

/**
 * This is a "model"- representation of the Constraint to be reflected in
 * apricpt-entity-editor.fxml form.
 * 
 * @author Anton Nazarov
 * @since 28/02/2019
 *
 */
@Component
public class ApricotConstraintData implements Serializable {

    private static final long serialVersionUID = 5624231358883188078L;
    
    private String constraintType;
    private String constraintName;
    private List<ApricotColumnData> columns = new ArrayList<>();
    private long id;
    private boolean added;

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public String getConstraintColumns() {
        StringBuilder sb = new StringBuilder();
        for (ApricotColumnData cd : columns) {
            if (sb.length() != 0) {
                sb.append("; ");
            }
            sb.append(cd.getName().getValue());
        }
        
        String res = WordUtils.wrap(sb.toString(), 45);
        return res;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public List<ApricotColumnData> getColumns() {
        return columns;
    }

    public void setColumns(List<ApricotColumnData> columns) {
        this.columns = columns;
    }
}
