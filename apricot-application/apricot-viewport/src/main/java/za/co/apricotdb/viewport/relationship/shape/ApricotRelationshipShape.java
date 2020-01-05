package za.co.apricotdb.viewport.relationship.shape;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import za.co.apricotdb.viewport.canvas.ApricotElement;
import za.co.apricotdb.viewport.canvas.ApricotShape;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

/**
 * The common features of all link shapes.
 * 
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public abstract class ApricotRelationshipShape extends Group implements ApricotShape {

    public static final double RELATIONSHIP_DEFAULT_STROKE_WIDTH = 1;
    public static final double RELATIONSHIP_SELECTED_STROKE_WIDTH = 2;

    private final ApricotRelationship relationship;
    private RelationshipShapeType shapeType;
    private Path path;
    private Group startElement;
    private Group endElement;

    public ApricotRelationshipShape(ApricotRelationship relationship, RelationshipShapeType shapeType) {
        this.relationship = relationship;
        this.shapeType = shapeType;
    }

    public RelationshipShapeType getShapeType() {
        return shapeType;
    }

    @Override
    public void setDefault() {
        if (path != null) {
            path.setStrokeWidth(RELATIONSHIP_DEFAULT_STROKE_WIDTH);
            path.setStroke(Color.BLACK);
        }

        if (startElement != null) {
            setGroupStroke(startElement, RELATIONSHIP_DEFAULT_STROKE_WIDTH, Color.BLACK);
        }
        if (endElement != null) {
            setGroupStroke(endElement, RELATIONSHIP_DEFAULT_STROKE_WIDTH, Color.BLACK);
        }
    }

    @Override
    public void setSelected() {
        if (path != null) {
            path.setStrokeWidth(RELATIONSHIP_SELECTED_STROKE_WIDTH);
            path.setStroke(Color.BLACK);
        }

        if (startElement != null) {
            setGroupStroke(startElement, RELATIONSHIP_SELECTED_STROKE_WIDTH, Color.BLACK);
        }
        if (endElement != null) {
            setGroupStroke(endElement, RELATIONSHIP_SELECTED_STROKE_WIDTH, Color.BLACK);
        }
    }

    private void setGroupStroke(Group element, double strokeWidth, Color color) {
        for (Node n : element.getChildren()) {
            if (n instanceof Shape) {
                ((Shape) n).setStrokeWidth(strokeWidth);
                ((Shape) n).setStroke(color);
            }
        }
    }

    @Override
    public void setGrayed() {
        if (path != null) {
            path.setStrokeWidth(RELATIONSHIP_DEFAULT_STROKE_WIDTH);
            path.setStroke(Color.LIGHTGRAY);
        }

        if (startElement != null) {
            setGroupStroke(startElement, RELATIONSHIP_DEFAULT_STROKE_WIDTH, Color.LIGHTGRAY);
        }
        if (endElement != null) {
            setGroupStroke(endElement, RELATIONSHIP_DEFAULT_STROKE_WIDTH, Color.LIGHTGRAY);
        }
    }

    @Override
    public void setHidden() {
        // TODO Auto-generated method stub

    }

    @Override
    public ApricotElement getElement() {
        return relationship;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        if (this.path != null) {
            this.getChildren().remove(this.path);
        }
        this.getChildren().add(path);
        this.path = path;
    }

    public Group getStartElement() {
        return startElement;
    }

    public void setStartElement(Group startElement) {
        if (this.startElement != null) {
            this.getChildren().remove(this.startElement);
        }
        this.getChildren().add(startElement);
        this.startElement = startElement;
    }

    public Group getEndElement() {
        return endElement;
    }

    public void setEndElement(Group endElement) {
        if (this.endElement != null) {
            this.getChildren().remove(this.endElement);
        }
        this.getChildren().add(endElement);
        this.endElement = endElement;
    }

    public abstract void translateRelationshipRulers(double translateX, double translateY);

    public abstract void resetRelationshipRulers();
}
