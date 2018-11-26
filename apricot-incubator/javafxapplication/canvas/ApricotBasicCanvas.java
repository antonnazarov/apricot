package javafxapplication.canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.relationship.ApricotEntityLink;

/**
 * The basic implementation of the Apricot- canvas.
 *
 * @author Anton Nazarov
 * @since 26/11/2018
 */
public class ApricotBasicCanvas extends Pane implements ApricotEntityRelationshipCanvas {

    private Map<String, ApricotEntity> entities = new HashMap<>();
    private List<ApricotEntityLink> links = new ArrayList<>();

    @Override
    public void addEntity(ApricotEntity entity) {
        entities.put(entity.getId(), entity);
        if (!this.getChildren().contains(entity)) {
            this.getChildren().add(entity);
        }
    }

    @Override
    public void addLink(ApricotEntityLink relationship) {
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
