package za.co.apricotdb.ui.comparator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    ImageView getTargetView(CompareRowType type);

    String getSourceStyle(CompareRowType type);

    String getTargetStyle(CompareRowType type);

    default ImageView getImage(String image) {
        ImageView img = new ImageView();
        img.setImage(new Image(getClass().getResourceAsStream(image)));
        return img;
    }
}
