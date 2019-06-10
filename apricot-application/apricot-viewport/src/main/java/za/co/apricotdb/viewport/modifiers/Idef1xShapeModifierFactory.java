package za.co.apricotdb.viewport.modifiers;

import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * The factory of the shape modifiers specific for IDEF1x notation.
 * 
 * @author Anton Nazarov
 * @since 25/05/2019
 */
public class Idef1xShapeModifierFactory implements ShapeModifierFactory {

    @Override
    public ElementVisualModifier[] getDirectShapeModifiers(ApricotCanvas canvas, AlignCommand aligner) {
        return new ElementVisualModifier[] { new NonIdentifyingRelationshipShapeModifier(),
                new DirectRelationshipEventModifier(canvas, aligner) };
    }

    @Override
    public ElementVisualModifier[] getHatShapeModifiers(ApricotCanvas canvas, AlignCommand aligner) {
        return new ElementVisualModifier[] { new NonIdentifyingRelationshipShapeModifier(),
                new HatRelationshipEventModifier(canvas, aligner) };
    }

    @Override
    public ElementVisualModifier[] getDadsHandShapeModifiers(ApricotCanvas canvas, AlignCommand aligner) {
        return new ElementVisualModifier[] { new NonIdentifyingRelationshipShapeModifier(),
                new DadsHandRelationshipEventModifier(canvas, aligner) };
    }

    @Override
    public ElementVisualModifier[] getRoofShapeModifiers(ApricotCanvas canvas, AlignCommand aligner) {
        return new ElementVisualModifier[] { new RoofRelationshipEventModifier(canvas, aligner) };
    }
}
