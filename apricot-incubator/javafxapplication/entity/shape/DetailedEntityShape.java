package javafxapplication.entity.shape;

import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafxapplication.entity.ApricotEntity;

/**
 * An implementation of the entity- shape with reflection of all the fields in
 * the entity.
 *
 * @author Anton Nazarov
 * @since 27/11/2018
 */
public abstract class DetailedEntityShape extends ApricotEntityShape {

    public static final Insets STANDARD_PANEL_INSETS = new Insets(5, 10, 5, 10);
    public static final Insets REDUCED_PANEL_INSETS_PK = new Insets(2, 7, 2, 7);
    public static final Insets REDUCED_PANEL_INSETS_NPK = new Insets(5, 7, 5, 7);
    public static final double STANDARD_BORDER_WIDTH = 1;
    public static final double THICK_BORDER_WIDTH = 4;

    protected final Text header;
    protected final GridPane primaryPanel;
    protected final GridPane nonPrimaryPanel;
    private final Map<String, Text> fieldsMapping = new HashMap<>();

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
}
