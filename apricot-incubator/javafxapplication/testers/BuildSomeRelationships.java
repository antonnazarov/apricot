package javafxapplication.testers;

import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.ApricotRelationshipBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipType;

public class BuildSomeRelationships {
    
    private RelationshipBuilder rBuilder = null;
    private ApricotCanvasImpl canvas = null;
    
    public BuildSomeRelationships(ApricotCanvasImpl canvas) {
        this.canvas = canvas;
        rBuilder = new ApricotRelationshipBuilder(canvas); 
    }
    
    public void build() {
        ApricotRelationship r = rBuilder.buildRelationship("agreement", "intermediary_agreement", "id", "id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("fsp_registration", "fsb_adviser_registration", "id", "fsp_registration_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("role_player", "party_role", "id", "role_player_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("role_player", "fsb_adviser_registration", "id", "role_player_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("role_player", "award", "id", "role_player_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);  
        r = rBuilder.buildRelationship("role_player", "insurance_educational_credits", "id", "person_id", RelationshipType.MANDATORY_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("role_player", "party", "id", "id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("role_player", "work_permit", "id", "person_id", RelationshipType.MANDATORY_NON_IDENTIFYING);
        canvas.addElement(r); 
        r = rBuilder.buildRelationship("party", "person", "id", "id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);  
        r = rBuilder.buildRelationship("person", "accreditation_registration", "id", "person_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("person", "award", "id", "person_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);   
        r = rBuilder.buildRelationship("person", "award_eagle_data", "id", "person_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);  
        r = rBuilder.buildRelationship("person", "criminal_record", "id", "person_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("person", "education_certificate", "id", "person_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);     
        
        r = rBuilder.buildRelationship("category_scheme", "category", "id", "category_scheme_id", RelationshipType.MANDATORY_NON_IDENTIFYING);
        canvas.addElement(r); 
        r = rBuilder.buildRelationship("category", "contract_specification", "id", "category_id", RelationshipType.MANDATORY_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "contract_specification", "id", "parent", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("translation", "contract_specification", "id", "title_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("reserve_specification", "contract_specification", "id", "default_reserve_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("fsb_registration_specification", "contract_specification", "id", "fsb_registration_specification_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("category", "contract_specification", "id", "rem_stmnt_tmpl_cat_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("sub_status_specification", "contract_specification", "id", "sub_status_specification_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("termination_reason_specification", "contract_specification", "id", "termination_reason_specification_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("graduation_phase", "contract_specification", "id", "default_graduation_phase_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("graduation_phase", "graduation_phase", "id", "next_graduation_phase_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "allowed_buy_and_sell_contract_specification", "id", "allowed_buy_and_sell_contract_specification_id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "allowed_change_contract_specification", "id", "allowed_change_contract_specification_id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "commission_entitlement_specification", "id", "contract_specification_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "contract_specification_additional_product_category", "id", "contract_specification_id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "contract_specification_award_type", "id", "contract_specification_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "contract_specification_product_category", "id", "contract_specification_id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "intermediary_agreement", "id", "contract_specification_id", RelationshipType.MANDATORY_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "redirect_balance_contract_specification", "id", "contract_specification_id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "redirect_balance_contract_specification", "id", "redirect_balance_contract_specification_id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "relationship_specification", "id", "played_by_contract_spec_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("contract_specification", "relationship_specification", "id", "assigned_to_contract_spec_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
    }
}
