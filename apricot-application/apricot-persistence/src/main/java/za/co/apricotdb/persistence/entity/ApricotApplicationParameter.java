package za.co.apricotdb.persistence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Representation of the table apricot_app_parameter.
 * 
 * @author Anton Nazarov
 * @since 27/02/2019
 */
@Entity
@Table(name = "apricot_app_parameter")
@NamedQuery(name="ApricotApplicationParameter.getParameterByName", query="SELECT aap FROM ApricotApplicationParameter aap WHERE aap.name = :name")
public class ApricotApplicationParameter implements Serializable {
    
    private static final long serialVersionUID = -8315836623519351216L;

    private ApricotApplicationParameter() {}
    
    public ApricotApplicationParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_parameter_id")
    private long id;
    
    @Column(name = "app_parameter_name")
    private String name;
    
    @Column(name = "app_parameter_value")
    private String value;

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
}
