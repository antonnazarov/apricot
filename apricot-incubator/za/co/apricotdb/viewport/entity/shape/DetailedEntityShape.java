package za.co.apricotdb.viewport.entity.shape;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import za.co.apricotdb.viewport.entity.ApricotEntity;

/**
 * An implementation of the entity- shape with reflection of all the fields in
 * the entity.
 *
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public abstract class DetailedEntityShape extends ApricotEntityShape {
    
    public static final double VERTICAL_PRIMARY_ENDPOINT_CORRECTION = 12;
    public static final double VERTICAL_NON_PRIMARY_ENDPOINT_CORRECTION = -3;

    public static final Insets STANDARD_PANEL_INSETS = new Insets(5, 10, 5, 10);
    public static final Insets REDUCED_PANEL_INSETS_PK = new Insets(3, 8, 3, 8);
    public static final Insets REDUCED_PANEL_INSETS_NPK = new Insets(5, 8, 5, 8);
    public static final double STANDARD_BORDER_WIDTH = 1;
    public static final double THICK_BORDER_WIDTH = 3;
    public static final Background MASTER_PANEL_BACKGROUND = new Background(
            new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    public static final Background SLAVE_PANEL_BACKGROUND_PK = new Background(
            new BackgroundFill(Color.WHITE, new CornerRadii(18, 18, 0, 0, false), Insets.EMPTY));
    public static final Background SLAVE_PANEL_BACKGROUND_NPK = new Background(
            new BackgroundFill(Color.WHITE, new CornerRadii(0, 0, 18, 18, false), Insets.EMPTY));

    protected final Text header;
    protected final GridPane primaryPanel;
    protected final GridPane nonPrimaryPanel;
    private final Map<String, Text> fieldsMapping = new HashMap<>();
    
    private final NonIdentifyingStack leftStack = new NonIdentifyingStack(this, Side.LEFT);
    private final NonIdentifyingStack rightStack = new NonIdentifyingStack(this, Side.RIGHT);
    private final IdentifyingStack topStack = new IdentifyingStack(this);

    public DetailedEntityShape(ApricotEntity entity, Text header, GridPane primaryPanel, GridPane nonPrimaryPanel) {
        super(entity);

        this.header = header;
        this.primaryPanel = primaryPanel;
        this.nonPrimaryPanel = nonPrimaryPanel;

        buildShape();
        mapFields();
    }

    @Override
    public Text getFieldByName(String fieldName) {
        return fieldsMapping.get(fieldName);
    }
    
    public double getFieldLocalY(String name) {
        double localY = 0;
        Text field = getFieldByName(name);
        
        if (nonPrimaryPanel.getChildren().contains(field)) {
            localY = field.getLayoutY() + nonPrimaryPanel.getLayoutY() + VERTICAL_NON_PRIMARY_ENDPOINT_CORRECTION;
        } else {
            localY = field.getLayoutY() + VERTICAL_PRIMARY_ENDPOINT_CORRECTION;
        }

        return localY;
    }

    private void buildShape() {
        this.getChildren().addAll(header, primaryPanel, nonPrimaryPanel);
    }

    private void mapFields() {
        for (Node n : primaryPanel.getChildren()) {
            if (n instanceof Text) {
                fieldsMapping.put(n.getId(), (Text) n);
            }
        }
        for (Node n : nonPrimaryPanel.getChildren()) {
            if (n instanceof Text) {
                fieldsMapping.put(n.getId(), (Text) n);
            }
        }
    }

    public NonIdentifyingStack getLeftStack() {
        return leftStack;
    }

    public NonIdentifyingStack getRightStack() {
        return rightStack;
    }

    public IdentifyingStack getTopStack() {
        return topStack;
    }
}
