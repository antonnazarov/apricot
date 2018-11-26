package javafxapplication.entity;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BorderStrokeHelper {

    public BorderStroke getPrimaryMasterBorderStroke(double borderWidth) {
        return new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(borderWidth));
    }

    public BorderStroke getPrimarySlaveBorderStroke(double borderWidth) {
        return new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(15, 15, 0, 0, false), new BorderWidths(borderWidth));
    }

    public BorderStroke getNonPrimaryMasterBorderStroke(double borderWidth) {
        return new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(0, borderWidth, borderWidth, borderWidth), Insets.EMPTY);
    }

    public BorderStroke getNonPrimarySlaveBorderStroke(double borderWidth) {
        return new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                new CornerRadii(0, 0, 15, 15, false), new BorderWidths(0, borderWidth, borderWidth, borderWidth), Insets.EMPTY);
    }
}
