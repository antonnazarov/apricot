package javafxapplication.entity.shape;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafxapplication.entity.ApricotEntity;

/**
 * The builder of the entity shape.
 *
 * @author Anton Nazarov
 * @since 28/11/2018
 */
public interface EntityShapeBuilder {

    public static final Font HEADER_FONT = Font.font("Helvetica", FontWeight.BOLD, 12);

    ApricotEntityShape build(ApricotEntity entity);
}
