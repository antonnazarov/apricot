package javafxapplication.entity.shape;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafxapplication.canvas.ApricotElement;
import javafxapplication.canvas.ApricotShape;
import javafxapplication.entity.ApricotEntity;

/**
 * The basic class for all possible Entity- shapes.
 *
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public abstract class ApricotEntityShape extends VBox implements ApricotShape {

    public static final double VERTICAL_CORRECTION = -4;

    private ApricotEntity entity;

    public ApricotEntityShape(ApricotEntity entity) {
        this.entity = entity;
    }

    @Override
    public ApricotElement getElement() {
        return entity;
    }

    public abstract Text getFieldByName(String fieldName);

    public double getFieldLocalY(String name) {
        Text field = getFieldByName(name);

        return field.getLayoutY() + VERTICAL_CORRECTION;
    }
}
