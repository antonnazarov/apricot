package za.co.apricotdb.ui.util;

import javafx.scene.control.Tooltip;

/**
 * This helper provides a static method to create a standard Tooltip object.
 *
 * @author Anton Nazarov
 * @since 14/12/2020
 */
public class TooltipHelper {

    public static Tooltip getApricotTooltip(String tooltipText) {
        Tooltip tt = new Tooltip();
        tt.setText(tooltipText);
        tt.setStyle("-fx-font: normal bold 12 Langdon; " + "-fx-base: #AE3522; " + "-fx-text-fill: orange;");

        return tt;
    }
}
