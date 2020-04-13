package za.co.apricotdb.viewport.entity.shape;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.modifiers.ElementVisualModifier;

/**
 * The builder of the entity shape with the extended field info.
 * 
 * @author Anton Nazarov
 * @since 24/05/2019
 */
public class ExtendedEntityShapeBuilder extends DefaultEntityShapeBuilder {

    public ExtendedEntityShapeBuilder(ApricotCanvas canvas, ElementVisualModifier... modifiers) {
        super(canvas, modifiers);
    }

    @Override
    public String getFieldAsString(FieldDetail fd) {
        StringBuilder sb = new StringBuilder(super.getFieldAsString(fd));
        sb.append(" ").append(fd.getType());

        return sb.toString();
    }
}
