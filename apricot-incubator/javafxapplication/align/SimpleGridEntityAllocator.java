package javafxapplication.align;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafxapplication.canvas.ApricotCanvas;
import javafxapplication.canvas.ApricotElement;
import javafxapplication.canvas.ElementType;

/**
 * This Align Command performs a simple allocation of the entities on the
 * canvas.
 *
 * @author Anton Nazarov
 * @since 19/11/2018
 */
public class SimpleGridEntityAllocator implements AlignCommand {

    public static final int COLUMNS_ON_CANVAS = 5;
    public static final double COLUMN_SPACING = 50;

    private ApricotCanvas canvas = null;

    public SimpleGridEntityAllocator(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void align() {
        List<VBox> entities = new ArrayList<>();
        double totalHeight = 0;
        for (ApricotElement elm : canvas.getElements()) {
            if (elm.getElementType() == ElementType.ENTITY) {
                Node n = elm.getShape();
                if (n instanceof VBox) {
                    VBox entity = (VBox) n;
                    entities.add(entity);
                    totalHeight += entity.getHeight();
                }

            }
        }

        entities.sort((VBox v1, VBox v2) -> Double.compare(v2.getHeight(), v1.getHeight()));

        double maxColumnHeight = totalHeight / COLUMNS_ON_CANVAS;
        double columnMaxWidth = 0;
        double currentColumnHeight = COLUMN_SPACING;
        double currentColumnTransX = COLUMN_SPACING;
        for (VBox entity : entities) {
            if (currentColumnHeight > maxColumnHeight) {
                // start a new column
                currentColumnHeight = COLUMN_SPACING;
                currentColumnTransX += columnMaxWidth + COLUMN_SPACING;
                columnMaxWidth = 0;
            }

            entity.setLayoutX(currentColumnTransX);
            entity.setLayoutY(currentColumnHeight);

            currentColumnHeight += entity.getHeight() + COLUMN_SPACING;
            if (entity.getWidth() > columnMaxWidth) {
                columnMaxWidth = entity.getWidth();
            }
        }
    }
}
