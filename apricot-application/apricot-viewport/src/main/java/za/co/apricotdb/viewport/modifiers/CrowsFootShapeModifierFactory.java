package za.co.apricotdb.viewport.modifiers;

import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * The factory of the shape modifiers specific for the "Crow Foot" notation.
 * 
 * @author Anton Nazarov
 * @since 25/05/2019
 */
public class CrowsFootShapeModifierFactory implements ShapeModifierFactory {

    @Override
    public ElementVisualModifier[] getDirectShapeModifiers(ApricotCanvas canvas, AlignCommand aligner) {
        return new ElementVisualModifier[] { new DirectRelationshipEventModifier(canvas, aligner) };
    }

    @Override
    public ElementVisualModifier[] getHatShapeModifiers(ApricotCanvas canvas, AlignCommand aligner) {
        return new ElementVisualModifier[] { new HatRelationshipEventModifier(canvas, aligner) };
    }

    @Override
    public ElementVisualModifier[] getDadsHandShapeModifiers(ApricotCanvas canvas, AlignCommand aligner) {
        return new ElementVisualModifier[] { new DadsHandRelationshipEventModifier(canvas, aligner) };
    }

    @Override
    public ElementVisualModifier[] getRoofShapeModifiers(ApricotCanvas canvas, AlignCommand aligner) {
        return new ElementVisualModifier[] { new RoofRelationshipEventModifier(canvas, aligner) };
    }
}
