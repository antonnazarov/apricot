package za.co.apricotdb.viewport.entity;

import java.util.List;

import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.align.CanvasSizeAdjustor;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ElementStatus;
import za.co.apricotdb.viewport.entity.shape.DefaultEntityShapeBuilder;
import za.co.apricotdb.viewport.entity.shape.EntityShapeBuilder;
import za.co.apricotdb.viewport.entity.shape.ExtendedEntityShapeBuilder;
import za.co.apricotdb.viewport.entity.shape.SimpleEntityShapeBuilder;
import za.co.apricotdb.viewport.event.GroupOperationHandler;
import za.co.apricotdb.viewport.modifiers.ElementVisualModifier;
import za.co.apricotdb.viewport.modifiers.EntitySetDetailedEntityShadowModifier;
import za.co.apricotdb.viewport.modifiers.SetEntityEventsModifier;
import za.co.apricotdb.viewport.relationship.RelationshipBatchBuilder;

/**
 * The basic implementation of EntityBuilder interface.
 * 
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public class DefaultEntityBuilder implements EntityBuilder {

    private EntityShapeBuilder shapeBuilder = null;
    private ApricotCanvas canvas = null;

    public DefaultEntityBuilder(ApricotCanvas canvas, String detailLevel) {
        this.canvas = canvas;

        GroupOperationHandler groupHandler = new GroupOperationHandler();
        AlignCommand aligner = new CanvasSizeAdjustor(canvas);
        RelationshipBatchBuilder relationshipBuilder = new RelationshipBatchBuilder();

        // collect all necessary modifiers
        ElementVisualModifier[] modifiers = new ElementVisualModifier[] { new EntitySetDetailedEntityShadowModifier(),
                new SetEntityEventsModifier(canvas, groupHandler, aligner, relationshipBuilder) };

        if (detailLevel.equals("DEFAULT")) {
            shapeBuilder = new DefaultEntityShapeBuilder(canvas, modifiers);
        } else if (detailLevel.equals("SIMPLE")) {
            shapeBuilder = new SimpleEntityShapeBuilder(canvas, modifiers);
        } else if (detailLevel.equals("EXTENDED")) {
            shapeBuilder = new ExtendedEntityShapeBuilder(canvas, modifiers);
        }
    }

    @Override
    public ApricotEntity buildEntity(String tableName, List<FieldDetail> details, boolean slave, boolean parentAbsent,
            boolean childAbsent) {
        ApricotEntity entity = new ApricotEntityImpl(tableName, details, slave, shapeBuilder, canvas, parentAbsent,
                childAbsent);
        entity.buildShape();
        entity.setElementStatus(ElementStatus.DEFAULT);

        return entity;
    }
}
