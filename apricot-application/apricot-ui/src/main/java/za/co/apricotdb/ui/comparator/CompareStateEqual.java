package za.co.apricotdb.ui.comparator;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * The compare state = EQUAL.
 * 
 * @author Anton Nazarov
 * @since 27/10/2019
 */
public class CompareStateEqual implements CompareState {

    @Override
    public ImageView getSourceImage(CompareRowType type) {
        if (type == CompareRowType.SNAPSHOT) {
            return getImage(ICON_PATH + type.getPlain());
        }

        return getImage(ICON_PATH + type.getGray());
    }

    @Override
    public ImageView getTargetImage(CompareRowType type) {
        if (type == CompareRowType.SNAPSHOT) {
            return getImage(ICON_PATH + type.getGray());
        }

        return getImage(ICON_PATH + type.getGray());
    }

    @Override
    public String getSourceStyle(CompareRowType type) {
        String style = null;
        switch (type) {
        case SNAPSHOT:
            style = 
            "-fx-padding: 3;" +
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-insets: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-color: green;";            
            break;
        case TABLE:
            style = "-fx-font-weight: bold;";
            break;
        default:
            style = "-fx-font-weight: normal;";
            break;
        }

        return style;
    }

    @Override
    public String getTargetStyle(CompareRowType type) {
        return getSourceStyle(type);
    }

    @Override
    public Color getSourceColor(CompareRowType type) {
        return Color.GRAY;
    }

    @Override
    public Color getTargetColor(CompareRowType type) {
        return Color.GRAY;
    }

    @Override
    public String toString() {
        return "EQUAL";
    }
}
