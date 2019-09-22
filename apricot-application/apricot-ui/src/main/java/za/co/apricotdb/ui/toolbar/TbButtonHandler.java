package za.co.apricotdb.ui.toolbar;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The handler of the tool bar button interface.
 *  
 * @author Anton Nazarov
 * @since 21/09/2019
 */
public interface TbButtonHandler {

    void initButton(Button btn);

    String getEnabledImageName();

    String getDisabledImageName();

    Button getButton();

    String getToolpitText();

    default void enable() {
        Button btn = getButton();
        btn.setDisable(false);
        setImage(btn, getEnabledImageName());
    }

    default void disable() {
        Button btn = getButton();
        // btn.setDisable(true);
        setImage(btn, getDisabledImageName());
    }

    default void setImage(Button btn, String imgName) {
        Image image = new Image(getClass().getResourceAsStream(imgName));
        btn.setGraphic(new ImageView(image));
    }

    default void init(Button btn) {
        btn.setText(null);
        enable();

        Tooltip tt = new Tooltip();
        tt.setText(getToolpitText());
        tt.setStyle("-fx-font: normal bold 12 Langdon; " + "-fx-base: #AE3522; " + "-fx-text-fill: orange;");
        btn.setTooltip(tt);
    }
}
