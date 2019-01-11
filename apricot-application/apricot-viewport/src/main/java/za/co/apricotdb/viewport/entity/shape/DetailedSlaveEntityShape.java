package za.co.apricotdb.viewport.entity.shape;

import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import za.co.apricotdb.viewport.entity.ApricotEntity;

public class DetailedSlaveEntityShape extends DetailedEntityShape {

    public DetailedSlaveEntityShape(ApricotEntity entity, Text header, GridPane primaryPanel,
            GridPane nonPrimaryPanel) {
        super(entity, header, primaryPanel, nonPrimaryPanel);
    }

    @Override
    public void setDefault() {
        primaryPanel.setPadding(STANDARD_PANEL_INSETS);
        nonPrimaryPanel.setPadding(STANDARD_PANEL_INSETS);

        BorderStroke primaryBs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                new CornerRadii(15, 15, 0, 0, false), new BorderWidths(STANDARD_BORDER_WIDTH));
        BorderStroke nonPrimaryBs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                new CornerRadii(0, 0, 15, 15, false),
                new BorderWidths(0, STANDARD_BORDER_WIDTH, STANDARD_BORDER_WIDTH, STANDARD_BORDER_WIDTH), Insets.EMPTY);

        Border b = new Border(primaryBs);
        primaryPanel.setBorder(b);
        b = new Border(nonPrimaryBs);
        nonPrimaryPanel.setBorder(b);
        
        primaryPanel.setBackground(SLAVE_PANEL_BACKGROUND_PK);
        nonPrimaryPanel.setBackground(SLAVE_PANEL_BACKGROUND_NPK);
    }

    @Override
    public void setSelected() {
        primaryPanel.setPadding(REDUCED_PANEL_INSETS_PK);
        nonPrimaryPanel.setPadding(REDUCED_PANEL_INSETS_NPK);

        BorderStroke primaryBs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                new CornerRadii(15, 15, 0, 0, false), new BorderWidths(THICK_BORDER_WIDTH));
        BorderStroke nonPrimaryBs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                new CornerRadii(0, 0, 15, 15, false),
                new BorderWidths(0, THICK_BORDER_WIDTH, THICK_BORDER_WIDTH, THICK_BORDER_WIDTH), Insets.EMPTY);

        Border b = new Border(primaryBs);
        primaryPanel.setBorder(b);
        b = new Border(nonPrimaryBs);
        nonPrimaryPanel.setBorder(b);
        
        primaryPanel.setBackground(SLAVE_PANEL_BACKGROUND_PK);
        nonPrimaryPanel.setBackground(SLAVE_PANEL_BACKGROUND_NPK);
    }

    @Override
    public void setGrayed() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public void setHidden() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }
}
