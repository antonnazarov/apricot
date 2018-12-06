package javafxapplication.entity.shape;

import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafxapplication.entity.ApricotEntity;

public class DetailedMasterEntityShape extends DetailedEntityShape {

    public DetailedMasterEntityShape(ApricotEntity entity, Text header, GridPane primaryPanel, GridPane nonPrimaryPanel) {
        super(entity, header, primaryPanel, nonPrimaryPanel);
    }

    @Override
    public void setDefault() {
        primaryPanel.setPadding(STANDARD_PANEL_INSETS);
        nonPrimaryPanel.setPadding(STANDARD_PANEL_INSETS);
        
        BorderStroke primaryBs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(STANDARD_BORDER_WIDTH));
        BorderStroke nonPrimaryBs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(0, STANDARD_BORDER_WIDTH, STANDARD_BORDER_WIDTH, STANDARD_BORDER_WIDTH), Insets.EMPTY);
        Border b = new Border(primaryBs);
        primaryPanel.setBorder(b);
        b = new Border(nonPrimaryBs);
        nonPrimaryPanel.setBorder(b);
    }

    @Override
    public void setSelected() {
        primaryPanel.setPadding(REDUCED_PANEL_INSETS_PK);
        nonPrimaryPanel.setPadding(REDUCED_PANEL_INSETS_NPK);
        
        BorderStroke primaryBs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(THICK_BORDER_WIDTH));
        BorderStroke nonPrimaryBs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(0, THICK_BORDER_WIDTH, THICK_BORDER_WIDTH, THICK_BORDER_WIDTH), Insets.EMPTY);
        Border b = new Border(primaryBs);
        primaryPanel.setBorder(b);
        b = new Border(nonPrimaryBs);
        nonPrimaryPanel.setBorder(b);
    }

    @Override
    public void setGrayed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHidden() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
