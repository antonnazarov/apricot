package javafxapplication.entity;

import java.util.List;
import javafxapplication.canvas.ApricotERElement;
import javafxapplication.entity.shape.ApricotEntityShape;

/**
 * This interface is for a graphical representation of an entity.
 *
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public interface ApricotEntity extends ApricotERElement {

    ApricotEntityShape getEntityShape();

    String getTableName();

    List<FieldDetail> getDetails();

    boolean isSlave();
}
