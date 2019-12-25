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

public class DefaultSlaveEntityShape extends DefaultEntityShape {

    public DefaultSlaveEntityShape(ApricotEntity entity, Text header, GridPane primaryPanel, GridPane nonPrimaryPanel) {
        super(entity, header, primaryPanel, nonPrimaryPanel);
    }

    @Override
    public void setDefault() {
        setDefault(Color.BLACK);
        setTextColor(Color.BLACK, true);
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
        
        setTextColor(Color.BLACK, true);
    }

    @Override
    public void setGrayed() {
        setDefault(Color.LIGHTGRAY);
        setTextColor(Color.LIGHTGREY, false);
    }

    @Override
    public void setHidden() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    private void setDefault(Color color) {
        primaryPanel.setPadding(STANDARD_PANEL_INSETS);
        nonPrimaryPanel.setPadding(STANDARD_PANEL_INSETS);

        BorderStroke primaryBs = new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(15, 15, 0, 0, false),
                new BorderWidths(STANDARD_BORDER_WIDTH));
        BorderStroke nonPrimaryBs = new BorderStroke(color, BorderStrokeStyle.SOLID,
                new CornerRadii(0, 0, 15, 15, false),
                new BorderWidths(0, STANDARD_BORDER_WIDTH, STANDARD_BORDER_WIDTH, STANDARD_BORDER_WIDTH), Insets.EMPTY);

        Border b = new Border(primaryBs);
        primaryPanel.setBorder(b);
        b = new Border(nonPrimaryBs);
        nonPrimaryPanel.setBorder(b);

        primaryPanel.setBackground(SLAVE_PANEL_BACKGROUND_PK);
        nonPrimaryPanel.setBackground(SLAVE_PANEL_BACKGROUND_NPK);

        header.setFill(color);
    }
}
