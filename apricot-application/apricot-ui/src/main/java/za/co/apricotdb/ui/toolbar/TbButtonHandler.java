package za.co.apricotdb.ui.toolbar;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import za.co.apricotdb.ui.util.ImageHelper;

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

    void enable();

    void disable();

    boolean isEnabled();

    void init(Button btn);

    default void setImage(Button btn, String imgName) {
        Image image = ImageHelper.getImage(imgName, getClass());
        btn.setGraphic(new ImageView(image));
    }
}
