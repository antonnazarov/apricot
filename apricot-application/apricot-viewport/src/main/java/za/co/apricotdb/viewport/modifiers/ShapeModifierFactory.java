package za.co.apricotdb.viewport.modifiers;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * The factory of shape modifiers for different types of relationships,
 * depending on the ERD notation.
 * 
 * @author Anton Nazarov
 * @since 25/05/2019
 */
public interface ShapeModifierFactory {

    ElementVisualModifier[] getDirectShapeModifiers(ApricotCanvas canvas);

    ElementVisualModifier[] getHatShapeModifiers(ApricotCanvas canvas);

    ElementVisualModifier[] getDadsHandShapeModifiers(ApricotCanvas canvas);

    ElementVisualModifier[] getRoofShapeModifiers(ApricotCanvas canvas);

    public static ShapeModifierFactory instantiateFactory(ApricotCanvas canvas) {
        ShapeModifierFactory factory = null;
        switch (canvas.getErdNotation()) {
        case "IDEF1x":
            factory = new Idef1xShapeModifierFactory();
            break;
        case "CROWS_FOOT":
            factory = new CrowsFootShapeModifierFactory();
            break;
        }

        return factory;
    }
}
