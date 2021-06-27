package za.co.apricotdb.ui.handler;

import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PrettyFormatterTest {

    @Test
    public void testSqlFormat() {
        String formattedSQL = new BasicFormatterImpl().format("CREATE VIEW vw_financial_transaction_all AS    SELECT      a.intermediary_code,      ft.financial_transaction_id,      a.sub_account_number,       CASE       WHEN ft.description NOT LIKE 'VAT%' THEN  ae.amount_in_cents * -1      ELSE 0        END as amount_in_cents,      ft.cut_off_date AS cut_off_period,       ft.transaction_date,      ft.run_date,      ft.description,      be.source_system,      CASE      WHEN (ft.program_reference = 516 AND be.intermediary_code = be.source_intermediary_code) THEN v.redirect_balance_intermediary_code       WHEN ft.program_reference = 516 THEN source_intermediary_code      ELSE be.source_system_reference     END AS source_system_reference,     ft.transaction_category_id AS transaction_category,       be.calculated_vat_applicable_indicator as vat_applicable_indicator,       CASE        WHEN ae.amount_in_cents < 0 THEN be.calculated_vat_amount_in_cents        ELSE be.calculated_vat_amount_in_cents * -1       END as vat_amount_in_cents,       be.retroactive_vat_indicator,      ber.is_manual_transaction_event,       be.business_event_type,      be.capture_timestamp     FROM account a      JOIN account_entry ae ON ae.account_id = a.account_id      JOIN financial_transaction ft on ft.financial_transaction_id = ae.financial_transaction_id      JOIN account_entry ae2 on ae2.financial_transaction_id = ft.financial_transaction_id and ae2.account_id <> a.account_id     JOIN account a2 on a2.account_id = ae2.account_id     JOIN business_event be on be.business_event_id = ft.business_event_id      JOIN business_event_reference ber on be.business_event_type = ber.business_event_type      JOIN tmp_intermediary_view v on v.intermediary_code = be.intermediary_code      WHERE       (a.sub_account_number = '101' OR (a.sub_account_number = '301' AND a2.sub_account_number is null) OR (a.sub_account_number = '301' AND a2.sub_account_number <> '101')  OR (a.sub_account_number = '102' AND a2.sub_account_number <> '101'))          AND ae.entry_type = 'NORMAL'           AND (ft.description NOT LIKE 'VAT%' or (be.amount_in_cents = 0 and vat_amount_in_cents <> 0 and ft.description LIKE 'VAT%'))");
        System.out.println(formattedSQL);
        assertNotNull(formattedSQL);
    }
}
