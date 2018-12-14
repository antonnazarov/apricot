package javafxapplication.align;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafxapplication.entity.ApricotBBBEntity;

/**
 * @deprecated
 *
 */
public class DiagramPanelManager {

    public static final double DIAGRAM_PANEL_OFFSET = 10;

    /**
     * Calculate and set the new dimensions of the diagram panel.
     */
    public void adjustDiagramPanel(Pane diagramPanel) {
        alignEntities(diagramPanel);
        alignPaneSize(diagramPanel);
    }

    /**
     * Get coordinates of the region which surrounds all entities in the diagram.
     */
    private Coordinates getRegion(Pane diagramPanel) {
        Coordinates c = new Coordinates(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
        List<Node> entities = diagramPanel.getChildren();
        for (Node n : entities) {
            if (n instanceof ApricotBBBEntity) {
                ApricotBBBEntity b = (ApricotBBBEntity) n;
                double left = b.getLayoutX() + b.getTranslateX();
                double right = left + b.getWidth();
                double top = b.getLayoutY() + b.getTranslateY();
                double bottom = top + b.getHeight();

                if (c.getLeft() > left) {
                    c.setLeft(left);
                }
                if (c.getRight() < right) {
                    c.setRight(right);
                }
                if (c.getTop() > top) {
                    c.setTop(top);
                }
                if (c.getBottom() < bottom) {
                    c.setBottom(bottom);
                }
            }
        }

        return c;
    }

    private Coordinates getPositiveDisp(Coordinates coord) {
        Coordinates disp = new Coordinates(0, 0, 0, 0);

        if (coord.getLeft() < DIAGRAM_PANEL_OFFSET) {
            disp.setLeft(DIAGRAM_PANEL_OFFSET - coord.getLeft());
        }
        if (coord.getTop() < DIAGRAM_PANEL_OFFSET) {
            disp.setTop(DIAGRAM_PANEL_OFFSET - coord.getTop());
        }

        return disp;
    }

    private void alignEntities(Pane diagramPanel) {
        Coordinates square = getRegion(diagramPanel);
        Coordinates disp = getPositiveDisp(square);

        System.out.println("square: " + square.toString());
        System.out.println("disp: " + disp.toString());

        List<Node> entities = diagramPanel.getChildren();
        for (Node n : entities) {
            if (n instanceof ApricotBBBEntity) {
                ApricotBBBEntity b = (ApricotBBBEntity) n;
                b.setLayoutX(b.getLayoutX() + disp.getLeft());
                b.setLayoutY(b.getLayoutY() + disp.getTop());
            }
        }
    }

    private void alignPaneSize(Pane diagramPanel) {
        Coordinates square = getRegion(diagramPanel);

        if (square.getRight() > diagramPanel.getPrefWidth()) {
            diagramPanel.setPrefWidth(square.getRight() + DIAGRAM_PANEL_OFFSET);
        }
        if (square.getBottom() > diagramPanel.getPrefHeight()) {
            diagramPanel.setPrefHeight(square.getBottom() + DIAGRAM_PANEL_OFFSET);
        }
    }
}
