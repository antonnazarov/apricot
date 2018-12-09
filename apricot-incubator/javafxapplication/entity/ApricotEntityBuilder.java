package javafxapplication.entity;

import java.util.List;

import javafxapplication.canvas.ApricotEntityRelationshipCanvas;
import javafxapplication.entity.shape.DetailedEntityShapeBuilder;
import javafxapplication.entity.shape.EntityShapeBuilder;
import javafxapplication.modifiers.ElementVisualModifier;
import javafxapplication.modifiers.EntitySetDetailedEntityShadowModifier;
import javafxapplication.modifiers.SetEntityEventsModifier;

/**
 * The basic implementation of EntityBuilder interface.
 * 
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class ApricotEntityBuilder implements EntityBuilder {
    
    private EntityShapeBuilder shapeBuilder = null;
    
    public ApricotEntityBuilder(ApricotEntityRelationshipCanvas canvas) {
        
        //  collect all necessary modifiers
        ElementVisualModifier[] modifiers = new ElementVisualModifier[] { 
                new EntitySetDetailedEntityShadowModifier(),
                new SetEntityEventsModifier(canvas) 
        };
        
        shapeBuilder = new DetailedEntityShapeBuilder(modifiers);        
    }

    @Override
    public ApricotEntity buildEntity(String tableName, List<FieldDetail> details, boolean slave) {
        ApricotEntity entity = new ApricotEntityImpl(tableName, details, slave, shapeBuilder);

        return entity;
    }
}
