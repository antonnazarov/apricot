package javafxapplication.canvas;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.Pane;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.entity.shape.ApricotEntityShape;
import javafxapplication.relationship.shape.ApricotLinkShape;

/**
 * The basic implementation of the Apricot- canvas.
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public class ApricotBasicCanvas extends Pane implements ApricotEntityRelationshipCanvas {

    private final Map<String, ApricotEntityShape> entities = new HashMap<>();
    // private List<ApricotEntityLink> links = new ArrayList<>();

    /**
     * Register new Entity Shape into the canvas.
     */
    @Override
    public void addEntity(ApricotEntityShape entityShape) {
        ApricotEntity e = entityShape.getEntity();
        entities.put(e.getTableName(), entityShape);

        // draw/redraw the element
        if (this.getChildren().contains(entityShape)) {
            this.getChildren().remove(entityShape);
        }
        this.getChildren().add(entityShape);
    }

    @Override
    public void addLink(ApricotLinkShape relationship) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void orderElements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ApricotEntity findEntityByName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteElement(ApricotERElement element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
