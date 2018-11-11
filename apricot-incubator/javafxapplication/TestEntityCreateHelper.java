package javafxapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to create the test entities for visual testing.
 * 
 * @author Anton Nazarov
 * @since 05/11/2018
 */
public class TestEntityCreateHelper {
    public static List<FieldDetail> getFpaEducationCourse() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("active_from", false, "date", false, null));
        fields.add(new FieldDetail("active_to", false, "date", false, null));
        fields.add(new FieldDetail("institute_name", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("course_advice_name_id", false, "bigint", false, "FK1"));
        fields.add(new FieldDetail("course_designation_name_id", false, "bigint", false, "FK2"));
        
        return fields;
    }
    
    public static List<FieldDetail> getIntermediaryAgreement() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK, FK, IDX"));
        fields.add(new FieldDetail("date_of_commencement", false, "datetime2", false, null));
        fields.add(new FieldDetail("contract_specification_id", true, "bigint", false, "FK1, IDX"));
        fields.add(new FieldDetail("reserve_data_id", false, "bigint", false, "FK2, FK3"));
        fields.add(new FieldDetail("fais_role", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("date_of_termination", false, "datetime2", false, null));
        fields.add(new FieldDetail("termination_reason_primary_id", false, "bigint", false, "FK4"));
        fields.add(new FieldDetail("termination_reason_secondary_id", false, "bigint", false, "FK5, FK6"));
        fields.add(new FieldDetail("status", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("sub_status", false, "varchar (255)", false, "IDX"));
        fields.add(new FieldDetail("reappointment_not_recommended", false, "bit", false, null));
        fields.add(new FieldDetail("possible_debarment", false, "bit", false, null));
        fields.add(new FieldDetail("open_for_new_business", false, "bit", false, null));
        fields.add(new FieldDetail("open_for_financing", false, "bit", false, null));
        fields.add(new FieldDetail("fica_compliance_id", false, "bigint", false, "FK7, FK8"));
        fields.add(new FieldDetail("termination_transaction_date", false, "date", false, null));
        fields.add(new FieldDetail("termination_timestamp", false, "datetime", false, null));
        fields.add(new FieldDetail("service_months_id", false, "bigint", false, "FK9"));
        fields.add(new FieldDetail("category_id", false, "bigint", false, "FK10"));
        fields.add(new FieldDetail("parent_entity_id", false, "bigint", false, "FK11"));
        fields.add(new FieldDetail("proof_of_intent_to", false, "bigint", false, "FK12"));
        fields.add(new FieldDetail("sign_away_rights", false, "bit", false, null));
        fields.add(new FieldDetail("succession_planning", false, "bit", false, null));
        fields.add(new FieldDetail("proof_of_intent_agreement_date", false, "datetime", false, null));
        fields.add(new FieldDetail("proof_of_intent", false, "bit", false, null));
        fields.add(new FieldDetail("date_of_activation", false, "datetime2", false, null));
        fields.add(new FieldDetail("reappointment", true, "bit", false, null));
        fields.add(new FieldDetail("date_of_contract_change", false, "datetime2", false, null));
        fields.add(new FieldDetail("contract_change_transaction_date", false, "datetime2", false, null));
        fields.add(new FieldDetail("redirect_balance_intermediary_agreement_id", false, "bigint", false, "FK13"));
        fields.add(new FieldDetail("redirect_balance_type_id", false, "bigint", false, null));
        fields.add(new FieldDetail("contract_changeover_same_intermediary_code", false, "bit", false, "IDX"));
        fields.add(new FieldDetail("open_for_new_business_accreditation_override", true, "bit", false, null));
        
        return fields;
    }
}
