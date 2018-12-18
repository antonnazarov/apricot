package javafxapplication.relationship.shape;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafxapplication.canvas.ApricotElement;
import javafxapplication.canvas.ApricotShape;
import javafxapplication.relationship.ApricotRelationship;

/**
 * The common features of all link shapes.
 * 
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public abstract class ApricotRelationshipShape extends Group implements ApricotShape {

    private final ApricotRelationship relationship;
    private RelationshipShapeType shapeType;    
    private Path path;
    private Shape startElement;
    private Shape endElement;


    public ApricotRelationshipShape(ApricotRelationship relationship, RelationshipShapeType shapeType) {
        this.relationship = relationship;
        this.shapeType = shapeType;
    }
    
    public RelationshipShapeType getShapeType() {
        return shapeType;
    }
    
    @Override
    public void setDefault() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSelected() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setGrayed() {
        // TODO Auto-generated method stub
        
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
    
    public Shape getStartElement() {
        return startElement;
    }

    public void setStartElement(Shape startElement) {
        if (this.startElement != null) {
            this.getChildren().remove(this.startElement);
        }
        this.getChildren().add(startElement);
        this.startElement = startElement;
    }

    public Shape getEndElement() {
        return endElement;
    }

    public void setEndElement(Shape endElement) {
        if (this.endElement != null) {
            this.getChildren().remove(this.endElement);
        }
        this.getChildren().add(endElement);
        this.endElement = endElement;
    }
}
