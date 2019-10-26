package za.co.apricotdb.ui.comparator;

import javafx.scene.image.ImageView;

public class CompareStateEqual implements CompareState {

    @Override
    public ImageView getSourceImage(CompareRowType type) {
        return getImage(ICON_PATH + type.getGray());
    }

    @Override
    public ImageView getTargetView(CompareRowType type) {
        return getImage(ICON_PATH + type.getGray());
    }

    @Override
    public String getSourceStyle(CompareRowType type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTargetStyle(CompareRowType type) {
        // TODO Auto-generated method stub
        return null;
    }

}
