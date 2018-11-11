package javafxapplication;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This builder class creates a basic graphical representation of the Entity.
 *
 * @author Anton Nazarov
 * @since 02/11/2018
 */
public class SimpleEntityBuilder implements EntityBuilder {

    public static final double BORDER_WIDTH_STANDARD = 1;
    public static final double BORDER_WIDTH_THICK = 4;
    public static final Insets STANDARD_PANEL_INSETS = new Insets(5, 10, 5, 10);
    public static final Background STANDARD_PANEL_BACKGROUND = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    public static final double FIELDS_HORIZONTAL_GAP = 10;
    public static final double FIELDS_VERTICAL_GAP = 3;
    public static final String STANDARD_FONT = "Helvetica";

    private List<FieldDetail> details;
    private String tableName;
    private Stage primaryStage;

    public SimpleEntityBuilder(String tableName, List<FieldDetail> details, Stage primaryStage) {
        this.tableName = tableName;
        this.details = details;
        this.primaryStage = primaryStage;
    }

    @Override
    public Node buildEntity() {
        VBox entity = new VBox();
        entity.setId(tableName);

        Node header = buildEntityHeader();
        Node primaryPanel = buildPrimaryKeyPanel();
        Node nonPrimaryPanel = buildNonPrimaryKeyPanel();

        DropShadow e = buildShadow();
        primaryPanel.setEffect(e);
        nonPrimaryPanel.setEffect(e);

        entity.getChildren().addAll(header, primaryPanel, nonPrimaryPanel);
        EventBuilder eb = new EntityEventBuilder();
        entity.setOnMousePressed(eb.getOnMousePressedEventHandler(tableName));
        entity.setOnMouseDragged(eb.getOnMouseDraggedEventHandler(primaryStage, tableName));
        entity.setOnMouseReleased(eb.getOnMouseReleasedEventHandler(primaryStage, tableName));
        entity.setOnMouseMoved(eb.getOnMouseMovedEventHandler(primaryStage, tableName));
        entity.setOnMouseExited(eb.getOnMouseExitedEventHandler(primaryStage));

        return entity;
    }

    @Override
    public void setFieldDetails(List<FieldDetail> details) {
        this.details = details;
    }

    private DropShadow buildShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setWidth(3);
        shadow.setHeight(3);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setRadius(4);

        return shadow;
    }

    private Node buildEntityHeader() {
        Text header = new Text(tableName);
        header.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        header.setId("entity_header");

        return header;
    }

    
    private Node buildPrimaryKeyPanel() {
        GridPane pkPanel = new GridPane();

        BorderStroke bs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(BORDER_WIDTH_STANDARD));
        Border b = new Border(bs);
        pkPanel.setBorder(b);
        pkPanel.setPadding(STANDARD_PANEL_INSETS);
        pkPanel.setBackground(STANDARD_PANEL_BACKGROUND);
        pkPanel.setHgap(FIELDS_HORIZONTAL_GAP);
        pkPanel.setVgap(FIELDS_VERTICAL_GAP);
        pkPanel.setId("entity_primary_key");

        for (FieldDetail fd : details) {
            int cnt = 0;
            if (fd.isPrimaryKey()) {
                Text field = new Text(getShortField(fd));
                field.setFont(Font.font(STANDARD_FONT, FontWeight.BOLD, 12));
                Tooltip.install(field, getFieldTooltip(fd));
                pkPanel.add(field, 0, cnt);

                cnt++;
            }
        }

        return pkPanel;
    }

    private Node buildNonPrimaryKeyPanel() {
        GridPane fPanel = new GridPane();

        BorderStroke bs = new BorderStroke(Color.TRANSPARENT, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                new CornerRadii(0), new BorderWidths(BORDER_WIDTH_STANDARD), Insets.EMPTY);
        Border b = new Border(bs);
        fPanel.setBorder(b);
        fPanel.setPadding(STANDARD_PANEL_INSETS);
        fPanel.setBackground(STANDARD_PANEL_BACKGROUND);
        fPanel.setHgap(FIELDS_HORIZONTAL_GAP);
        fPanel.setVgap(FIELDS_VERTICAL_GAP);
        fPanel.setId("entity_non_key_fields");

        int cnt = 0;
        for (FieldDetail fd : details) {
            if (!fd.isPrimaryKey()) {
                Text field = new Text(getShortField(fd));
                Tooltip.install(field, getFieldTooltip(fd));

                if (fd.getConstraints() != null && fd.getConstraints().contains("FK")) {
                    field.setFill(Color.BLUE);
                }

                fPanel.add(field, 0, cnt);

                cnt++;
            }
        }

        return fPanel;
    }

    private String getShortField(FieldDetail fd) {
        StringBuilder sb = new StringBuilder(fd.getName());
        if (fd.isMandatory()) {
            sb.append(" *");
        }

        if (fd.getConstraints() != null && fd.getConstraints().length() > 0) {
            sb.append(" (").append(fd.getConstraints()).append(")");
        }

        return sb.toString();
    }

    private Tooltip getFieldTooltip(FieldDetail fd) {
        StringBuilder sb = new StringBuilder(fd.getName());
        sb.append(" ").append(fd.getType());
        if (fd.isMandatory()) {
            sb.append(" not null");
        }
        if (fd.getConstraints() != null && fd.getConstraints().length() > 0) {
            sb.append(" (").append(fd.getConstraints()).append(")");
        }

        Tooltip t = new Tooltip(sb.toString());

        return t;
    }
}
