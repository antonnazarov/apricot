package za.co.apricotdb.viewport.align;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.VBox;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ElementType;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.shape.ApricotEntityShape;

/**
 * This Align Command performs a simple allocation of the entities on the
 * canvas.
 *
 * @author Anton Nazarov
 * @since 19/11/2018
 */
public class SimpleGridEntityAllocator implements AlignCommand {

    public static final int COLUMNS_ON_CANVAS = 20;
    public static final double COLUMN_SPACING = 50;

    private ApricotCanvas canvas = null;

    public SimpleGridEntityAllocator(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void align() {
        List<ApricotEntityShape> entities = new ArrayList<>();
        double totalHeight = 0;
        for (ApricotElement elm : canvas.getElements()) {
            if (elm.getElementType() == ElementType.ENTITY) {
                ApricotEntity entity = (ApricotEntity) elm;
                ApricotEntityShape entityShape = entity.getEntityShape();
                entities.add(entityShape);
                totalHeight += entityShape.getHeight();
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
