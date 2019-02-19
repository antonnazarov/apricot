package za.co.apricotdb.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "apricot_project_parameter")
@NamedQuery(name="ApricotProjectParameter.getParametersWithPrefix", query="SELECT app FROM ApricotProjectParameter app WHERE app.project = :project AND app.name LIKE CONCAT(:prefix,'%') ORDER BY app.name DESC")
@NamedQuery(name="ApricotProjectParameter.getParameterByName", query="SELECT app FROM ApricotProjectParameter app WHERE app.project = :project AND app.name = :name")
public class ApricotProjectParameter implements Serializable {

    private static final long serialVersionUID = -2298570920305929909L;
    
    public ApricotProjectParameter() {}
    
    public ApricotProjectParameter(String name, String value, ApricotProject project) {
        this.name = name;
        this.value = value;
        this.project = project;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id")
    private long id;

    @Column(name = "parameter_name")
    private String name;

    @Column(name = "parameter_value")
    private String value;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ApricotProject project;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ApricotProject getProject() {
        return project;
    }

    public void setProject(ApricotProject project) {
        this.project = project;
    }
}
