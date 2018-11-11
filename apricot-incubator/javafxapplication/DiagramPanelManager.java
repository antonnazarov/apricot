package javafxapplication;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DiagramPanelManager {

    public static final double DIAGRAM_PANEL_OFFSET = 10;

    /**
     * Calculate and set the new dimensions of the diagram panel.
     */
    public void adjusteDiagramPanel(Pane diagramPanel) {
        double minLeft = 0;
        double minTop = 0;
        double maxRight = 0;
        double maxBottom = 0;

        List<Node> entities = diagramPanel.getChildren();
        for (Node n : entities) {
            // n.getTranslateX()
        }

    }
    
    
    /**
     * Get coordinates of the region which surrounds all entities in the diagram.
     */
    private Coordinates getRegion(Pane diagramPanel) {
        Coordinates c = new Coordinates(0, 0, 0, 0);
        List<Node> entities = diagramPanel.getChildren();
        for (Node n : entities) {
            VBox b = (VBox) n;
            double left = b.getLayoutX() + b.getTranslateX();
            double right= left + b.getWidth();
        }
    }

//     private boolean allEntities
    class Coordinates {
        private double left = 0;
        private double top = 0;
        private double right = 0;
        private double bottom = 0;
        
        Coordinates(double left, double top, double right, double bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public double getLeft() {
            return left;
        }

        public void setLeft(double left) {
            this.left = left;
        }

        public double getTop() {
            return top;
        }

        public void setTop(double top) {
            this.top = top;
        }

        public double getRight() {
            return right;
        }

        public void setRight(double right) {
            this.right = right;
        }

        public double getBottom() {
            return bottom;
        }

        public void setBottom(double bottom) {
            this.bottom = bottom;
        }
    }
}
