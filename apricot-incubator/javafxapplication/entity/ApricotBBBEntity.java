package javafxapplication.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafxapplication.event.EntityEventBuilder;
import javafxapplication.event.EventBuilder;
import javafxapplication.relationship.ApricotEntityLink;

/**
 * An extension of the class VBox, which represents an entity.
 *
 * @author Anton Nazarov
 * @since 20/11/2018
 */
public class ApricotBBBEntity extends VBox {

    public static final double BORDER_WIDTH_STANDARD = 1;
    public static final double BORDER_WIDTH_THICK = 4;
    public static final Insets STANDARD_PANEL_INSETS = new Insets(5, 10, 5, 10);
    public static final Insets REDUCED_PANEL_INSETS = new Insets(5, 7, 5, 7);
    public static final Insets REDUCED_PANEL_INSETS_PK = new Insets(2, 7, 2, 7);
    public static final Background MASTER_PANEL_BACKGROUND = new Background(
            new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    public static final Background SLAVE_PANEL_BACKGROUND_PK = new Background(
            new BackgroundFill(Color.WHITE, new CornerRadii(18, 18, 0, 0, false), Insets.EMPTY));
    public static final Background SLAVE_PANEL_BACKGROUND_NPK = new Background(
            new BackgroundFill(Color.WHITE, new CornerRadii(0, 0, 18, 18, false), Insets.EMPTY));
    public static final double FIELDS_HORIZONTAL_GAP = 10;
    public static final double FIELDS_VERTICAL_GAP = 3;
    public static final String STANDARD_FONT = "Helvetica";

    private List<FieldDetail> details;
    private String tableName;
    private Stage primaryStage;

    private boolean selected = false;
    private boolean slave;

    private Map<String, Text> fieldsMapping = new HashMap<>();

    private BorderStrokeHelper borderStrokeHelper = new BorderStrokeHelper();

    private List<ApricotEntityLink> entityLinks = new ArrayList<>();

    /**
     * Construct the Apricot Entity.
     */
    public ApricotBBBEntity(String tableName, List<FieldDetail> details, Stage primaryStage) {
        this.tableName = tableName;
        this.details = details;
        this.primaryStage = primaryStage;

        this.setId(tableName);

        init();
    }

    public Text getTextObjectForField(String fieldName) {
        return fieldsMapping.get(fieldName);
    }

    public void addEntityLink(ApricotEntityLink link) {
        entityLinks.add(link);
    }

    public double getHorizontalDistance(ApricotBBBEntity entity) {
        Point2D parentTopLeft = new Point2D(this.getLayoutX(), this.getLayoutY());
        Point2D parentBottomRight = new Point2D(this.getLayoutX() + this.getWidth(),
                this.getLayoutY() + this.getHeight());
        Point2D childTopLeft = new Point2D(entity.getLayoutX(), entity.getLayoutY());
        Point2D childBottomRight = new Point2D(entity.getLayoutX() + entity.getWidth(),
                entity.getLayoutY() + entity.getHeight());

        Point2D parentMidpoint = parentTopLeft.midpoint(parentBottomRight);
        Point2D childMidpoint = childTopLeft.midpoint(childBottomRight);

        return Math.abs(parentMidpoint.getX() - childMidpoint.getX()) - (this.getWidth() + entity.getWidth()) / 2;
    }

    private void init() {
        slave = isSlaveEntity();

        Node header = buildEntityHeader();
        Node primaryPanel = buildPrimaryKeyPanel();
        Node nonPrimaryPanel = buildNonPrimaryKeyPanel();

        DropShadow e = buildShadow();
        primaryPanel.setEffect(e);
        nonPrimaryPanel.setEffect(e);

        this.getChildren().addAll(header, primaryPanel, nonPrimaryPanel);
        EventBuilder eb = new EntityEventBuilder();
        this.setOnMousePressed(eb.getOnMousePressedEventHandler(tableName));
        this.setOnMouseDragged(eb.getOnMouseDraggedEventHandler(primaryStage, tableName));
        this.setOnMouseReleased(eb.getOnMouseReleasedEventHandler(primaryStage, tableName));
        this.setOnMouseMoved(eb.getOnMouseMovedEventHandler(primaryStage, tableName));
        this.setOnMouseExited(eb.getOnMouseExitedEventHandler(primaryStage));

        setBorderStrokeAndBackground();
    }

    private boolean isSlaveEntity() {
        for (FieldDetail fd : details) {
            if (fd.isPrimaryKey()) {
                if (fd.getConstraints().contains("FK")) {
                    return true;
                }
            }
        }

        return false;
    }

    private Node buildEntityHeader() {
        Text header = new Text(tableName);
        header.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        header.setId("entity_header");

        return header;
    }

    private Node buildPrimaryKeyPanel() {
        GridPane pkPanel = new GridPane();

        pkPanel.setPadding(STANDARD_PANEL_INSETS);
        pkPanel.setHgap(FIELDS_HORIZONTAL_GAP);
        pkPanel.setVgap(FIELDS_VERTICAL_GAP);
        pkPanel.setId("entity_primary_key");

        int cnt = 0;
        for (FieldDetail fd : details) {
            if (fd.isPrimaryKey()) {
                Text field = new Text(getShortField(fd));
                field.setFont(Font.font(STANDARD_FONT, FontWeight.BOLD, 12));
                Tooltip.install(field, getFieldTooltip(fd));
                pkPanel.add(field, 0, cnt);

                cnt++;

                fieldsMapping.put(fd.getName(), field);
            }
        }

        return pkPanel;
    }

    private Node buildNonPrimaryKeyPanel() {
        GridPane fPanel = new GridPane();

        fPanel.setPadding(STANDARD_PANEL_INSETS);
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

                fieldsMapping.put(fd.getName(), field);
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

    private DropShadow buildShadow() {
        DropShadow shadow = new DropShadow();
        shadow.setWidth(3);
        shadow.setHeight(3);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setRadius(4);

        return shadow;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;

        setBorderStrokeAndBackground();
    }

    private void setBorderStrokeAndBackground() {
        GridPane pk = (GridPane) this.getChildren().get(1);
        GridPane npk = (GridPane) this.getChildren().get(2);

        BorderStroke bs;
        Border b;

        if (!selected) {
            if (!slave) {
                bs = borderStrokeHelper.getPrimaryMasterBorderStroke(BORDER_WIDTH_STANDARD);
                b = new Border(bs);
                pk.setBorder(b);

                bs = borderStrokeHelper.getNonPrimaryMasterBorderStroke(BORDER_WIDTH_STANDARD);
                b = new Border(bs);
                npk.setBorder(b);
            } else {
                bs = borderStrokeHelper.getPrimarySlaveBorderStroke(BORDER_WIDTH_STANDARD);
                b = new Border(bs);
                pk.setBorder(b);

                bs = borderStrokeHelper.getNonPrimarySlaveBorderStroke(BORDER_WIDTH_STANDARD);
                b = new Border(bs);
                npk.setBorder(b);
            }

            pk.setPadding(STANDARD_PANEL_INSETS);
            npk.setPadding(STANDARD_PANEL_INSETS);
        } else {
            if (!slave) {
                bs = borderStrokeHelper.getPrimaryMasterBorderStroke(BORDER_WIDTH_THICK);
                b = new Border(bs);
                pk.setBorder(b);

                bs = borderStrokeHelper.getNonPrimaryMasterBorderStroke(BORDER_WIDTH_THICK);
                b = new Border(bs);
                npk.setBorder(b);

            } else {
                bs = borderStrokeHelper.getPrimarySlaveBorderStroke(BORDER_WIDTH_THICK);
                b = new Border(bs);
                pk.setBorder(b);

                bs = borderStrokeHelper.getNonPrimarySlaveBorderStroke(BORDER_WIDTH_THICK);
                b = new Border(bs);
                npk.setBorder(b);

            }

            pk.setPadding(REDUCED_PANEL_INSETS_PK);
            npk.setPadding(REDUCED_PANEL_INSETS);
        }
        if (!slave) {
            pk.setBackground(MASTER_PANEL_BACKGROUND);
            npk.setBackground(MASTER_PANEL_BACKGROUND);
        } else {
            pk.setBackground(SLAVE_PANEL_BACKGROUND_PK);
            npk.setBackground(SLAVE_PANEL_BACKGROUND_NPK);
        }
    }
}
