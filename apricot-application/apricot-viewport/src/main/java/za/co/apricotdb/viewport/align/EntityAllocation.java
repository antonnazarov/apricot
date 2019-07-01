package za.co.apricotdb.viewport.align;

import java.util.List;

import javafx.geometry.Point2D;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * The Entity Allocation interface - the wrapper interface for ApricotEntity.
 * 
 * @author Anton Nazarov
 * @since 01/07/2019
 */
public interface EntityAllocation {

    ApricotEntityShape getEntityShape();

    String getTableName();

    List<FieldDetail> getDetails();

    List<ApricotRelationship> getPrimaryLinks();

    List<ApricotRelationship> getForeignLinks();
    
    void setLayout(double x, double y);
    
    void setWidth(double width);
    
    Point2D getLayout();
    
    double getWidth();
    
    double getHeight();
}
