package za.co.apricotdb.viewport.entity.shape;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * The basic class for all possible Entity- shapes.
 *
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public abstract class ApricotEntityShape extends VBox implements ApricotShape {

    private ApricotEntity entity;

    public ApricotEntityShape(ApricotEntity entity) {
        this.entity = entity;
    }

    @Override
    public ApricotElement getElement() {
        return entity;
    }

    public abstract Text getFieldByName(String fieldName);

    public abstract double getFieldLocalY(String name);
}
