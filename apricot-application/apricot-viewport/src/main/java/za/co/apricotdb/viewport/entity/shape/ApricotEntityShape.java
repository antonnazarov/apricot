package za.co.apricotdb.viewport.entity.shape;

import java.util.Properties;

import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.canvas.CanvasAllocationItem;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * The basic class for all possible Entity- shapes.
 *
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public abstract class ApricotEntityShape extends VBox implements ApricotShape {

    private ApricotEntity entity;
    protected ApricotEntityGroup entityGroup = new ApricotEntityGroup();

    public ApricotEntityShape(ApricotEntity entity) {
        this.entity = entity;
    }

    @Override
    public ApricotElement getElement() {
        return entity;
    }

    public abstract Text getFieldByName(String fieldName);

    public abstract double getFieldLocalY(String name);

    public abstract void resetAllStacks();

    public abstract Point2D getStackRelationshipStart(ApricotRelationship relationship);

    public abstract Point2D getStackRelationshipEnd(ApricotRelationship relationship);

    public ApricotEntityGroup getEntityGroup() {
        return entityGroup;
    }

    @Override
    public CanvasAllocationItem getAllocation() {
        return getAllocation(entity.getTableName(), this.getLayoutX(), this.getLayoutY(), this.getWidth());
    }

    public static CanvasAllocationItem getAllocation(String name, double layoutX, double layoutY, double width) {
        CanvasAllocationItem ret = new CanvasAllocationItem();
        ret.setName(name);
        ret.setType(ElementType.ENTITY);
        Properties props = new Properties();
        props.setProperty("layoutX", String.valueOf(layoutX));
        props.setProperty("layoutY", String.valueOf(layoutY));
        props.setProperty("width", String.valueOf(width));
        ret.setProperties(props);

        return ret;
    }

    @Override
    public void applyAllocation(CanvasAllocationItem item) {
        if (item.getName().equals(entity.getTableName()) && item.getType() == ElementType.ENTITY
                && item.getProperties() != null) {
            Properties p = item.getProperties();
            double layoutX = Double.parseDouble(p.getProperty("layoutX"));
            double layoutY = Double.parseDouble(p.getProperty("layoutY"));
            double width = Double.parseDouble(p.getProperty("width"));

            setLayoutX(layoutX);
            setLayoutY(layoutY);
            setPrefWidth(width);
            setWidth(width);
        }
    }
}
