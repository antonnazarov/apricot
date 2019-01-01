package javafxapplication.testers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.viewport.entity.FieldDetail;

/**
 * This class helps to create the test entities for visual testing.
 *
 * @author Anton Nazarov
 * @since 05/11/2018
 */
public class TestEntityCreateHelper {

    public List<FieldDetail> getFpaEducationCourse() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("active_from", false, "date", false, null));
        fields.add(new FieldDetail("active_to", false, "date", false, null));
        fields.add(new FieldDetail("institute_name", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("course_advice_name_id", false, "bigint", false, "FK1"));
        fields.add(new FieldDetail("course_designation_name_id", false, "bigint", false, "FK2"));

        return fields;
    }

    public List<FieldDetail> getIntermediaryAgreement() {
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

    public List<FieldDetail> getCategory() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("description", false, "varchar (250)", false, null));
        fields.add(new FieldDetail("category_name", true, "varchar (50)", false, null));
        fields.add(new FieldDetail("category_scheme_id", true, "bigint", false, "FK"));
        fields.add(new FieldDetail("category_system_name", true, "varchar (50)", false, null));
        fields.add(new FieldDetail("meta_data", false, "varchar (250)", false, null));
        fields.add(new FieldDetail("order_weight", false, "int", false, null));

        return fields;
    }

    /**
     * Table education_qualification
     */
    public List<FieldDetail> getEducationQualification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("name_id", false, "bigint", false, "FK"));
        fields.add(new FieldDetail("nqf_level", false, "varchar (10)", false, null));
        fields.add(new FieldDetail("education_type", true, "varchar (100)", false, null));

        return fields;
    }

    /**
     * Table translation
     */
    public List<FieldDetail> getTranslation() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("class_name", true, "varchar (25)", false, null));

        return fields;
    }

    /**
     * Table fsb_recognized_education
     */
    public List<FieldDetail> getFsbRecognizedEducation() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("education_qualification_id", false, "bigint", false, "FK"));
        fields.add(new FieldDetail("fsb_institute", false, "bigint", false, "FK1"));
        fields.add(new FieldDetail("fsb_number", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("saqa_credits", false, "int", false, null));
        fields.add(new FieldDetail("saqa_id", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("start_date", false, "datetime2", false, null));
        fields.add(new FieldDetail("end_date", false, "datetime2", false, null));

        return fields;
    }

    /**
     * Table fsb_qualification_product_category
     */
    public List<FieldDetail> getFsbQualificationProductCategory() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("qualification_id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("product_category", true, "bigint", false, "PK"));
        fields.add(new FieldDetail("product_category_type", false, "bigint", false, null));

        return fields;
    }

    /**
     * Table fsb_recognized_institute
     */
    public List<FieldDetail> getFsbRecognizedInstitute() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("fsb_institute_number", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("name", false, "bigint", false, null));

        return fields;
    }

    /**
     * Table organisation
     */
    public List<FieldDetail> getOrganisation() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("translation_id", true, "bigint", false, "FK"));
        fields.add(new FieldDetail("organisation_type_id", false, "bigint", false, null));
        fields.add(new FieldDetail("legacy_status_id", false, "bigint", false, null));
        fields.add(new FieldDetail("legacy_category_id", false, "bigint", false, null));
        fields.add(new FieldDetail("manager_party_id", false, "bigint", false, null));
        fields.add(new FieldDetail("legacy_code", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("is_deleted", true, "bit", false, null));
        fields.add(new FieldDetail("cost_center_code", true, "varchar (255)", false, null));

        return fields;
    }

    /**
     * Table party
     */
    public List<FieldDetail> getParty() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("description", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("language", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("language_preference", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("organisation_membership", false, "bigint", false, null));
        fields.add(new FieldDetail("organisation_membership_end_date", false, "datetime2", false, null));
        fields.add(new FieldDetail("organisation_membership_start_date", false, "datetime2", false, null));

        return fields;
    }

    /**
     * Table company_registration
     */
    public List<FieldDetail> getCompanyRegistration() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("end_date", false, "bigint", false, null));
        fields.add(new FieldDetail("external_reference", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("issue_date", false, "date", false, null));
        fields.add(new FieldDetail("organisation_id", true, "date", false, "FK"));

        return fields;
    }

    /**
     * Table organisation_registration
     */
    public List<FieldDetail> getOrganisationRegistration() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("organisation_id", true, "bigint", true, "PK, FK, UC"));
        fields.add(new FieldDetail("registration_id", true, "bigint", true, "PK, FK1"));

        return fields;
    }

    /**
     * Table fsp_registration
     */
    public List<FieldDetail> getFspRegistration() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("fsp_number", true, "varchar (10)", false, "UC"));
        fields.add(new FieldDetail("fsp_name", false, "varchar (100)", false, null));
        fields.add(new FieldDetail("start_date", true, "date", false, null));
        fields.add(new FieldDetail("end_date", false, "date", false, null));

        return fields;
    }

    /**
     * Table translation_locale
     */
    public List<FieldDetail> getTranslationLocale() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("text", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("locale", true, "varchar (255)", true, "PK"));

        return fields;
    }

    /**
     * Table fsb_adviser_registration
     */
    public List<FieldDetail> getFsbAdviserRegistration() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("end_date", false, "date", false, null));
        fields.add(new FieldDetail("external_reference", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("issue_date", false, "date", false, null));
        fields.add(new FieldDetail("date_of_first_appointment_in_industry", false, "date", false, null));
        fields.add(new FieldDetail("dofa_group", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("fais_level", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("fsb_registration_status", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("fsb_registration_status_timestamp", false, "datetime2", false, null));
        fields.add(new FieldDetail("fsb_registration_update_request_timestamp", false, "datetime2", false, null));
        fields.add(new FieldDetail("last_fais_updated_date", false, "datetime2", false, null));
        fields.add(new FieldDetail("last_run_fais_test_date", false, "datetime2", false, null));
        fields.add(new FieldDetail("re_cutoff_date", false, "date", false, null));
        fields.add(new FieldDetail("re_status", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("role_player_id", false, "bigint", false, "FK"));
        fields.add(new FieldDetail("date_of_commencement", false, "date", false, null));
        fields.add(new FieldDetail("fsp_registration_id", false, "bigint", false, "FK1"));
        fields.add(new FieldDetail("fsb_id_reference_number", false, "varchar (50)", false, null));
        fields.add(new FieldDetail("fsb_id_reference_type", false, "varchar (50)", false, null));

        return fields;
    }

    /**
     * Table insurance_educational_credits
     */
    public List<FieldDetail> getInsuranceEducationalCredits() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("end_date", false, "date", false, null));
        fields.add(new FieldDetail("external_reference", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("issue_date", false, "date", false, null));
        fields.add(new FieldDetail("date_obtained", false, "date", false, null));
        fields.add(new FieldDetail("insurance_type", false, "varchar (3)", false, null));
        fields.add(new FieldDetail("nqf_level", false, "varchar (10)", false, null));
        fields.add(new FieldDetail("number_of_credits", false, "int", false, null));
        fields.add(new FieldDetail("person_id", true, "bigint", false, "FK"));

        return fields;
    }

    /**
     * Table award
     */
    public List<FieldDetail> getAward() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("end_date", false, "date", false, null));
        fields.add(new FieldDetail("external_reference", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("role_player_id", false, "bigint", false, "FK"));
        fields.add(new FieldDetail("award_type_id", false, "bigint", false, "FK1"));
        fields.add(new FieldDetail("person_id", false, "bigint", false, "FK2"));

        return fields;
    }

    /**
     * Table party_role
     */
    public List<FieldDetail> getPartyRole() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("fsb_registration_id", false, "bigint", false, null));
        fields.add(new FieldDetail("role_player_id", false, "bigint", false, "FK1"));
        fields.add(new FieldDetail("channel_type", false, "varchar (30)", false, null));
        fields.add(new FieldDetail("start_date", true, "datetime", false, null));
        fields.add(new FieldDetail("end_date", false, "datetime", false, null));
        fields.add(new FieldDetail("contract_specification", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("institution_org_linking", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("institution_agent_code", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("party_role_code", false, "varchar (50)", false, null));
        fields.add(new FieldDetail("party_role_type", false, "varchar (50)", false, null));
        fields.add(new FieldDetail("fsb_broker_registration_id", false, "bigint", false, null));
        fields.add(new FieldDetail("historic_linking_group_id", false, "bigint", false, "FK2"));

        return fields;
    }

    /**
     * Table work_permit
     */
    public List<FieldDetail> getWorkPermit() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("end_date", false, "date", false, null));
        fields.add(new FieldDetail("external_reference", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("issue_date", false, "date", false, null));
        fields.add(new FieldDetail("person_id", true, "bigint", false, "FK"));

        return fields;
    }

    /**
     * Table award_type
     */
    public List<FieldDetail> getAwardType() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("active_from", false, "date", false, null));
        fields.add(new FieldDetail("active_to", false, "date", false, null));
        fields.add(new FieldDetail("annual", false, "bit", false, null));
        fields.add(new FieldDetail("award_code", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("award_name", true, "varchar (255)", false, null));
        fields.add(new FieldDetail("category_id", true, "bigint", false, "FK"));

        return fields;
    }

    /**
     * Table category_scheme
     */
    public List<FieldDetail> getCategoryScheme() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("description", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("scheme_name", false, "varchar (255)", false, null));

        return fields;
    }

    /**
     * Table historic_linking_group
     */
    public List<FieldDetail> getHistoricLinkingGroup() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("historic_linking_group_id", true, "bigint", true, "PK"));

        return fields;
    }

    /**
     * Table role_player
     */
    public List<FieldDetail> getRolePlayer() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));

        return fields;
    }
    
    /**
     * Table agreement
     */
    public List<FieldDetail> getAgreement() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("payment_agreement_id", false, "bigint", false, null));
        fields.add(new FieldDetail("start_date", true, "date", false, null));
        fields.add(new FieldDetail("end_date", false, "date", false, null));
        fields.add(new FieldDetail("created_date", false, "datetime2", false, null));
        fields.add(new FieldDetail("channel_type", true, "varchar (50)", false, null));

        return fields;
    }
    
    /**
     * Person agreement
     */
    public List<FieldDetail> getPerson() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("broker_type", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("date_of_birth", true, "datetime2", false, null));
        fields.add(new FieldDetail("ethnicity", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("gender", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("marital_status", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("religion", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("in_community_of_property", false, "varchar (255)", false, null));

        return fields;
    } 

    /**
     * accreditation_registration
     */
    public List<FieldDetail> getAccreditationRegistration() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("accrediation_id", false, "bigint", false, null));
        fields.add(new FieldDetail("person_id", false, "bigint", false, "FK"));
        fields.add(new FieldDetail("start_date", false, "datetime2", false, null));
        fields.add(new FieldDetail("end_date", false, "datetime2", false, null));

        return fields;
    }
    
    /**
     * award eagle data
     */
    public List<FieldDetail> getAwardEagleData() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("cumulative_eagle_score", false, "int", false, null));
        fields.add(new FieldDetail("last_updated_date_time", false, "datetime2", false, null));
        fields.add(new FieldDetail("next_eagle_limit", false, "int", false, null));
        fields.add(new FieldDetail("person_id", false, "datetime2", false, "FK"));

        return fields;
    }
    
    /**
     * award eagle data
     */
    public List<FieldDetail> getCriminalRecord() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("finger_prints_taken", false, "bit", false, null));
        fields.add(new FieldDetail("date_finger_prints_taken", false, "date", false, null));
        fields.add(new FieldDetail("record_type", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("criminal_record_result", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("date_indemnity_form_signed", false, "date", false, null));
        fields.add(new FieldDetail("person_id", false, "datetime2", false, "FK"));

        return fields;
    }  
    
    /**
     * award education certificate
     */
    public List<FieldDetail> getEducationCertificate() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("end_date", false, "date", false, null));
        fields.add(new FieldDetail("external_reference", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("issue_date", false, "date", false, null));
        fields.add(new FieldDetail("date_obtained", false, "date", false, null));
        fields.add(new FieldDetail("person_id", false, "datetime2", false, "FK"));
        fields.add(new FieldDetail("education_qualification_id", false, "bigint", false, null));        

        return fields;
    }
    
    //  contract_specification
    public List<FieldDetail> getContractSpecification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("active_from", true, "datetime2", false, null));
        fields.add(new FieldDetail("channel_type", true, "varchar (50)", false, null));
        fields.add(new FieldDetail("contract_code", true, "varchar (50)", false, null));
        fields.add(new FieldDetail("description", false, "varchar (250)", false, null));
        fields.add(new FieldDetail("active_from", false, "datetime2", false, null));
        fields.add(new FieldDetail("category_id", true, "bigint", false, "FK"));
        fields.add(new FieldDetail("parent", false, "bigint", false, "FK1"));
        fields.add(new FieldDetail("title_id", false, "bigint", false, "FK2"));
        fields.add(new FieldDetail("mis_role", false, "varchar (50)", false, null));
        fields.add(new FieldDetail("default_reserve_id", false, "bigint", false, "FK3"));
        fields.add(new FieldDetail("payment_specification_id", false, "bigint", false, null));
        fields.add(new FieldDetail("fsb_registration_specification_id", false, "bigint", false, "FK4"));
        fields.add(new FieldDetail("party_type", false, "varchar (50)", false, null));
        fields.add(new FieldDetail("rem_stmnt_tmpl_cat_id", false, "bigint", false, "FK5"));
        fields.add(new FieldDetail("default_open_for_new_business", false, "bit", false, null));
        fields.add(new FieldDetail("open_for_new_business_editable", false, "bit", false, null));
        fields.add(new FieldDetail("organisation_manager", false, "bit", false, null));
        fields.add(new FieldDetail("sub_status_specification_id", false, "bigint", false, "FK6"));
        fields.add(new FieldDetail("termination_reason_specification_id", false, "bigint", false, "FK7"));
        fields.add(new FieldDetail("default_graduation_phase_id", false, "bigint", false, "FK8"));
        fields.add(new FieldDetail("sign_away_rights_enabled", false, "bit", false, null));
        fields.add(new FieldDetail("succession_planning_enabled", false, "bit", false, null));
        fields.add(new FieldDetail("proof_of_intent_enabled", false, "bit", false, null));
        fields.add(new FieldDetail("late_activation_allowed", false, "bit", false, null));
        fields.add(new FieldDetail("max_person_age_allowed_at_activation", false, "bigint", false, null));
        fields.add(new FieldDetail("late_termination_allowed", false, "bit", false, null));
        fields.add(new FieldDetail("contract_allowed_at_activation", false, "bit", false, null));
        fields.add(new FieldDetail("contract_changeover_allowed", false, "bit", false, null));
        fields.add(new FieldDetail("late_contract_changeover_allowed", false, "bit", false, null));
        fields.add(new FieldDetail("vat_registration_allowed", false, "bit", false, null));
        fields.add(new FieldDetail("historic_linking_allowed", false, "bit", false, null));
        fields.add(new FieldDetail("buy_and_sell_allowed", false, "bit", false, null));
        
        return fields;
    }
    
    //  category_scheme
    public List<FieldDetail> getCategorySheme() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("description", false, "varchar (250)", false, null));
        fields.add(new FieldDetail("scheme_name", true, "varchar (50)", false, "UC"));

        return fields;
    }
    
    //  reserve_specification
    public List<FieldDetail> getReserveSpecification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("additional_reserve_release_default", false, "int", false, null));
        fields.add(new FieldDetail("has_additional_reserve_release", false, "bit", false, null));
        fields.add(new FieldDetail("has_lapse", false, "bit", false, null));
        fields.add(new FieldDetail("has_reserve_end_date", false, "bit", false, null));
        fields.add(new FieldDetail("has_reserve_retention", false, "bit", false, null));
        fields.add(new FieldDetail("reserve_type", true, "varchar (5)", false, "UC"));
        fields.add(new FieldDetail("apply_lapse_percentage", false, "bit", false, null));
        fields.add(new FieldDetail("reserve_type_description", true, "varchar (255)", false, null));

        return fields;
    }
    
    // fsb_registration_specification
    public List<FieldDetail> getFsbRegistrationSpecification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("fsp_number", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("default_fais_role", false, "varchar (255)", false, null));

        return fields;
    }
    
    // sub_status_specification
    public List<FieldDetail> getSubStatusSpecification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));

        return fields;
    }
    
    // termination_reason_specification
    public List<FieldDetail> getTerminationReasonSpecification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));

        return fields;
    }
    
    // graduation_phase
    public List<FieldDetail> getGraduationPhase() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("graduation_phase", false, "varchar (255)", false, null));
        fields.add(new FieldDetail("graduation_year", false, "int", false, null));
        fields.add(new FieldDetail("next_graduation_phase_id", false, "bigint", false, "FK"));

        return fields;
    }
    
    // allowed_buy_and_sell_contract_specification
    public List<FieldDetail> getAllowBuyAndSell() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("contract_specification_id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("allowed_buy_and_sell_contract_specification_id", true, "bigint", true, "PK, FK"));

        return fields;
    }
    
    //  allowed_change_contract_specification
    public List<FieldDetail> getAllowedChangeContract() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("contract_specification_id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("allowed_change_contract_specification_id", true, "bigint", true, "PK, FK"));

        return fields;
    }
    
    //  commission_entitlement_specification
    public List<FieldDetail> getCommissionEntitlementSpecification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("commission_entitlement_active_default", false, "bit", false, null));
        fields.add(new FieldDetail("commission_entitlement_inactive_default", false, "bit", false, null));
        fields.add(new FieldDetail("commission_entitlement_redirect_default", false, "bit", false, null));
        fields.add(new FieldDetail("user_can_edit_active", false, "bit", false, null));
        fields.add(new FieldDetail("user_can_edit_inactive", false, "bit", false, null));
        fields.add(new FieldDetail("redirect_enabled", false, "bit", false, null));
        fields.add(new FieldDetail("commission_entitlement_type_id", false, "bigint", false, "FK"));
        fields.add(new FieldDetail("contract_specification_id", false, "bigint", false, "FK1"));

        return fields;
    }
    
    //  contract_specification_additional_product_category
    public List<FieldDetail> getContractSpecificationAdditionalProductCategory() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("contract_specification_id", true, "bigint", true, "PK, FK, FK1"));
        fields.add(new FieldDetail("fsb_product_category_reg_map_key", true, "varchar (255)", true, "PK"));
        fields.add(new FieldDetail("fsb_product_category_reg_id", true, "bigint", false, "FK2, UC"));

        return fields;
    }
    
    // contract_specification_award_type
    public List<FieldDetail> getContractSpecificationAwardType() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("contract_specification_id", true, "bigint", false, "FK, UIDX"));
        fields.add(new FieldDetail("award_type_id", true, "bigint", false, "UIDX"));

        return fields;
    }
    
    // contract_specification_product_category
    public List<FieldDetail> getContractSpecificationProductCategory() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("contract_specification_id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("fsb_product_category_reg_map_key", true, "varchar (255)", true, "PK"));
        fields.add(new FieldDetail("fsb_product_category_reg_id", true, "bigint", false, "FK1, UC"));

        return fields;
    }
    
    // redirect_balance_contract_specification
    public List<FieldDetail> getRedirectBalanceContractSpecification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("contract_specification_id", true, "bigint", true, "PK, FK"));
        fields.add(new FieldDetail("redirect_balance_contract_specification_id", true, "bigint", true, "PK, FK1"));

        return fields;
    }
    
    // relationship_specification
    public List<FieldDetail> getRelationshipSpecification() {
        List<FieldDetail> fields = new ArrayList<>();
        fields.add(new FieldDetail("id", true, "bigint", true, "PK"));
        fields.add(new FieldDetail("played_by_contract_spec_id", false, "bigint", false, "FK"));
        fields.add(new FieldDetail("assigned_to_contract_spec_id", false, "bigint", false, "FK1"));
        fields.add(new FieldDetail("relationship_system_name", false, "varchar (255)", false, null));

        return fields;
    }
     
    public Map<String, List<FieldDetail>> getAllEntities() {
        Map<String, List<FieldDetail>> ret = new HashMap<>();

        List<FieldDetail> l = getFpaEducationCourse();
        ret.put("fpa_education_course", l);

        l = getIntermediaryAgreement();
        ret.put("intermediary_agreement", l);
        
        l = getRelationshipSpecification();
        ret.put("relationship_specification", l);
        
        l = getRedirectBalanceContractSpecification();
        ret.put("redirect_balance_contract_specification", l);
        
        l = getContractSpecificationProductCategory();
        ret.put("contract_specification_product_category", l);
        
        l = getContractSpecificationAwardType();
        ret.put("contract_specification_award_type", l);
        
        l = getContractSpecificationAdditionalProductCategory();
        ret.put("contract_specification_additional_product_category", l);
        
        l = getCommissionEntitlementSpecification();
        ret.put("commission_entitlement_specification", l);
        
        l = getAllowedChangeContract();
        ret.put("allowed_change_contract_specification", l);
        
        l = getAllowBuyAndSell();
        ret.put("allowed_buy_and_sell_contract_specification", l);
        
        l = getGraduationPhase();
        ret.put("graduation_phase", l);
        
        l = getTerminationReasonSpecification();
        ret.put("termination_reason_specification", l);
        
        l = getSubStatusSpecification();
        ret.put("sub_status_specification", l);
        
        l = getFsbRegistrationSpecification();
        ret.put("fsb_registration_specification", l);
        
        l = getReserveSpecification();
        ret.put("reserve_specification", l);

        l = getCategory();
        ret.put("category", l);
        
        l = getCategorySheme();
        ret.put("category_scheme", l);

        l = getEducationQualification();
        ret.put("education_qualification", l);

        l = getTranslation();
        ret.put("translation", l);

        l = getFsbRecognizedEducation();
        ret.put("fsb_recognized_education", l);

        l = getFsbQualificationProductCategory();
        ret.put("fsb_qualification_product_category", l);

        l = getFsbRecognizedInstitute();
        ret.put("fsb_recognized_institute", l);

        l = getOrganisation();
        ret.put("organisation", l);

        l = getParty();
        ret.put("party", l);

        l = getCompanyRegistration();
        ret.put("company_registration", l);

        l = getOrganisationRegistration();
        ret.put("organisation_registration", l);

        l = getFspRegistration();
        ret.put("fsp_registration", l);

        l = getTranslationLocale();
        ret.put("translation_locale", l);

        l = getFsbAdviserRegistration();
        ret.put("fsb_adviser_registration", l);

        l = getInsuranceEducationalCredits();
        ret.put("insurance_educational_credits", l);

        l = getAward();
        ret.put("award", l);

        l = getPartyRole();
        ret.put("party_role", l);

        l = getWorkPermit();
        ret.put("work_permit", l);

        l = getAwardType();
        ret.put("award_type", l);

        l = getCategoryScheme();
        ret.put("category_scheme", l);

        l = getHistoricLinkingGroup();
        ret.put("historic_linking_group", l);

        l = getRolePlayer();
        ret.put("role_player", l);
        
        l = getAgreement();
        ret.put("agreement", l);
        
        l = getPerson();
        ret.put("person", l);
        
        l = getAccreditationRegistration();
        ret.put("accreditation_registration", l);
        
        l = getAwardEagleData();
        ret.put("award_eagle_data", l);
        
        l = getCriminalRecord();
        ret.put("criminal_record", l);
        
        l = getEducationCertificate();
        ret.put("education_certificate", l);
        
        l = getContractSpecification();
        ret.put("contract_specification", l);
        
        return ret;
    }

    public void buildAllEntities(Stage primaryStage, Pane entityCanvas) {
        buildAllEntities(primaryStage, entityCanvas, null);
    }

    public void buildAllEntities(Stage primaryStage, Pane entityCanvas, String[] entitiesToCreate) {
        Map<String, List<FieldDetail>> entities = getAllEntities();

        double layoutX = 20;
        double layoutY = 20;
        for (String entityName : entities.keySet()) {
            if ((entitiesToCreate != null && Arrays.stream(entitiesToCreate).anyMatch(entityName::equals))
                    || entitiesToCreate == null) {
                List<FieldDetail> fields = entities.get(entityName);


                layoutX += 320;
                layoutY += 320;
            }
        }
    }
}
