package za.co.apricotdb.viewport.modifiers;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * The factory of the shape modifiers specific for IDEF1x notation.
 * 
 * @author Anton Nazarov
 * @since 25/05/2019
 */
public class Idef1xShapeModifierFactory implements ShapeModifierFactory {

    @Override
    public ElementVisualModifier[] getDirectShapeModifiers(ApricotCanvas canvas) {
        return new ElementVisualModifier[] { new NonIdentifyingRelationshipShapeModifier(),
                new DirectRelationshipEventModifier(canvas) };
    }

    @Override
    public ElementVisualModifier[] getHatShapeModifiers(ApricotCanvas canvas) {
        return new ElementVisualModifier[] { new NonIdentifyingRelationshipShapeModifier(),
                new HatRelationshipEventModifier(canvas) };
    }

    @Override
    public ElementVisualModifier[] getDadsHandShapeModifiers(ApricotCanvas canvas) {
        return new ElementVisualModifier[] { new NonIdentifyingRelationshipShapeModifier(),
                new DadsHandRelationshipEventModifier(canvas) };
    }

    @Override
    public ElementVisualModifier[] getRoofShapeModifiers(ApricotCanvas canvas) {
        return new ElementVisualModifier[] { new RoofRelationshipEventModifier(canvas) };
    }
}
