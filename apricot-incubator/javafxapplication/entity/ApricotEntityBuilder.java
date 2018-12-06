package javafxapplication.entity;

import java.util.List;
import javafxapplication.entity.shape.DetailedEntityShapeBuilder;
import javafxapplication.entity.shape.EntityShapeBuilder;
import javafxapplication.modifiers.EntitySetDetailedEntityShadowModifier;

/**
 * The basic implementation of EntityBuilder interface.
 * 
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class ApricotEntityBuilder implements EntityBuilder {
    
    private final EntitySetDetailedEntityShadowModifier[] modifiers = {new EntitySetDetailedEntityShadowModifier()};
    private final EntityShapeBuilder shapeBuilder = new DetailedEntityShapeBuilder(modifiers);

    @Override
    public ApricotEntity buildEntity(String tableName, List<FieldDetail> details, boolean slave) {
        ApricotEntity entity = new ApricotEntityImpl(tableName, details, slave, shapeBuilder);
        
        return entity;
    }
}
