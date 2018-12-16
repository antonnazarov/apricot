package javafxapplication.testers;

import java.util.List;
import java.util.Map;

import javafx.scene.Node;
import javafxapplication.canvas.ApricotBasicCanvas;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.entity.ApricotEntityBuilder;
import javafxapplication.entity.FieldDetail;

public class BuildSomeEntites implements TestEntityBuilder {

    private ApricotBasicCanvas erCanvas = null;
    private Map<String, List<FieldDetail>> entities = null;
    private ApricotEntityBuilder builder = null;

    public BuildSomeEntites(ApricotBasicCanvas erCanvas) {
        this.erCanvas = erCanvas;
        builder = new ApricotEntityBuilder(erCanvas);
        TestEntityCreateHelper helper = new TestEntityCreateHelper();
        entities = helper.getAllEntities();
    }

    public void buildEntities() {
        
        build("fsb_adviser_registration", false, 30, 30);
        build("intermediary_agreement", true, 350, 30);
        build("role_player", false, 30, 450);
        build("party_role", true, 30, 550);
    }

    private void build(String tableName, boolean isSlave, double x, double y) {
        List<FieldDetail> fields = entities.get(tableName);
        ApricotEntity entity = builder.buildEntity(tableName, fields, isSlave);
        erCanvas.addElement(entity);
        Node n = entity.getShape();
        n.setLayoutX(x);
        n.setLayoutY(y);
    }
}
