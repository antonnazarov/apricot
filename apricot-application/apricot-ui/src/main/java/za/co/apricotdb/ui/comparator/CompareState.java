package za.co.apricotdb.ui.comparator;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import za.co.apricotdb.ui.util.ImageHelper;

/**
 * The state of the comparison results. There might be several possible states:
 * EQUAL, DIFFERENT, REMOVED, CREATED
 *
 * @author Anton Nazarov
 * @since 26/10/2019
 */
public interface CompareState {

    public static final String ICON_PATH = "/za/co/apricotdb/ui/comparator/";

    ImageView getSourceImage(CompareRowType type);

    ImageView getTargetImage(CompareRowType type);

    String getSourceStyle(CompareRowType type);

    String getTargetStyle(CompareRowType type);

    Color getSourceColor(CompareRowType type);

    Color getTargetColor(CompareRowType type);

    default ImageView getImage(String image) {
        ImageView img = new ImageView();
        img.setImage(ImageHelper.getImage(image, getClass()));

        return img;
    }
}
