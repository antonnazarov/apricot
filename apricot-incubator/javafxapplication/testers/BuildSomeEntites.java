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
        build("award", false, 1000, 30);
        build("insurance_educational_credits", false, 1000, 200);
        build("party", true, 1000, 450);
        build("work_permit", false, 1000, 650);
        build("person", true, 1300, 450);
        build("accreditation_registration", false, 1550, 30);
        build("award_eagle_data", false, 1550, 200);
        build("criminal_record", false, 1550, 350);
        build("education_certificate", false, 1550, 530);
        
        build("contract_specification", false, 700, 950);
        build("category", false, 400, 950);
        build("category_scheme", false, 250, 950);
        build("translation", false, 250, 1050);
        build("reserve_specification", false, 400, 1150);
        build("fsb_registration_specification", false, 400, 1380);
        build("sub_status_specification", false, 400, 1480);
        build("termination_reason_specification", false, 400, 1580);
        build("graduation_phase", false, 400, 1660);
        
        build("allowed_buy_and_sell_contract_specification", true, 1000, 850);
        build("allowed_change_contract_specification", true, 1000, 950);
        build("commission_entitlement_specification", false, 1000, 1050);
        build("contract_specification_additional_product_category", true, 1000, 1285);
        build("contract_specification_award_type", false, 1000, 1450);
        build("contract_specification_product_category", true, 1000, 1550);
        build("redirect_balance_contract_specification", true, 1000, 1650);
        build("relationship_specification", false, 1000, 1750);
    }

    private void build(String tableName, boolean isSlave, double x, double y) {
        List<FieldDetail> fields = entities.get(tableName);
        ApricotEntity entity = builder.buildEntity(tableName, fields, isSlave);
        erCanvas.addElement(entity);
        Node n = entity.getEntityShape();
        n.setLayoutX(x);
        n.setLayoutY(y);
    }
}
