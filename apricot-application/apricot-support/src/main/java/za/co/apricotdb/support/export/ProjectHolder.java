package za.co.apricotdb.support.export;

import java.util.List;

/**
 * This plain bean hold all parts of the project, necessary for serialization.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
public class ProjectHolder {

    private String jsonProject;
    private List<RelationshipHolder> relationships;
    private List<ConstraintColumnHolder> constraintColumns;

    public String getJsonProject() {
        return jsonProject;
    }

    public void setJsonProject(String jsonProject) {
        this.jsonProject = jsonProject;
    }

    public List<RelationshipHolder> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<RelationshipHolder> relationships) {
        this.relationships = relationships;
    }

    public List<ConstraintColumnHolder> getConstraintColumns() {
        return constraintColumns;
    }

    public void setConstraintColumns(List<ConstraintColumnHolder> constraintColumns) {
        this.constraintColumns = constraintColumns;
    }

    @Override
    public String toString() {
        return "ProjectHolder [project=" + jsonProject + ", relationships=" + relationships + ", constraintColumns="
                + constraintColumns + "]";
    }
}
