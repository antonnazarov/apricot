package javafxapplication.testers;

import java.util.List;
import java.util.Map;

import javafx.scene.Node;
import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.ApricotEntityBuilder;
import za.co.apricotdb.viewport.entity.FieldDetail;

public class BuildSomeEntites implements TestEntityBuilder {

    private ApricotCanvasImpl erCanvas = null;
    private Map<String, List<FieldDetail>> entities = null;
    private ApricotEntityBuilder builder = null;

    public BuildSomeEntites(ApricotCanvasImpl erCanvas) {
        this.erCanvas = erCanvas;
        builder = new ApricotEntityBuilder(erCanvas);
        TestEntityCreateHelper helper = new TestEntityCreateHelper();
        entities = helper.getAllEntities();
    }

    public void buildEntities() {
        
        build("fsb_adviser_registration", false, 700, 500);
        build("fsp_registration", false, 30, 30);
        build("intermediary_agreement", true, 350, 30);
        build("role_player", false, 780, 380);
        build("party_role", true, 30, 550);
        build("agreement", false, 800, 200);
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
