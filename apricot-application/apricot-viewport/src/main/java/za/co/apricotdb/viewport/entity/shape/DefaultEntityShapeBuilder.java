package za.co.apricotdb.viewport.entity.shape;

import java.util.List;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.entity.ApricotEntity;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.modifiers.ElementVisualModifier;

/**
 * The implementation of the shape of the detailed entity.
 *
 * @author Anton Nazarov
 * @since 28/11/2018
 */
public class DefaultEntityShapeBuilder implements EntityShapeBuilder {

    public static final double FIELDS_HORIZONTAL_GAP = 10;
    public static final double FIELDS_VERTICAL_GAP = 3;

    private final ElementVisualModifier[] modifiers;
    private ApricotCanvas canvas;

    public DefaultEntityShapeBuilder(ApricotCanvas canvas, ElementVisualModifier... modifiers) {
        this.canvas = canvas;
        this.modifiers = modifiers;
    }

    @Override
    public ApricotEntityShape build(ApricotEntity entity) {

        ApricotEntityShape shape = null;

        EntityHeader header = new EntityHeader(entity.getTableName(), canvas);
        header.setAbsentParent(entity.isParentAbsent());
        header.setAbsentChild(entity.isChildAbsent());
        if (!entity.isSlave()) {
            shape = new DefaultMasterEntityShape(entity, header, buildPrimaryKeyPanel(entity.getDetails()),
                    buildNonPrimaryKeyPanel(entity.getDetails()));
        } else {
            shape = new DefaultSlaveEntityShape(entity, header, buildPrimaryKeyPanel(entity.getDetails()),
                    buildNonPrimaryKeyPanel(entity.getDetails()));
        }

        shape.setId(entity.getTableName());

        applyModifiers(shape);

        return shape;
    }

    public GridPane buildPrimaryKeyPanel(List<FieldDetail> details) {
        GridPane pkPanel = new GridPane();

        pkPanel.setHgap(FIELDS_HORIZONTAL_GAP);
        pkPanel.setVgap(FIELDS_VERTICAL_GAP);

        int cnt = 0;
        for (FieldDetail fd : details) {
            if (fd.isPrimaryKey()) {
                Text field = new Text(getFieldAsString(fd));
                field.setId(fd.getName());
                field.setFont(HEADER_FONT);
                Tooltip.install(field, getFieldTooltip(fd));
                pkPanel.add(field, 0, cnt);

                cnt++;
            }
        }

        return pkPanel;
    }

    public GridPane buildNonPrimaryKeyPanel(List<FieldDetail> details) {
        GridPane fPanel = new GridPane();

        fPanel.setHgap(FIELDS_HORIZONTAL_GAP);
        fPanel.setVgap(FIELDS_VERTICAL_GAP);

        int cnt = 0;
        for (FieldDetail fd : details) {
            if (!fd.isPrimaryKey()) {
                Text field = new Text(getFieldAsString(fd));
                field.setId(fd.getName());
                Tooltip.install(field, getFieldTooltip(fd));

                if (fd.getConstraints() != null && fd.getConstraints().contains("FK")) {
                    field.setFill(Color.BLUE);
                    field.setUserData(Boolean.valueOf(true));
                }

                fPanel.add(field, 0, cnt);

                cnt++;
            }
        }

        return fPanel;
    }

    protected String getFieldAsString(FieldDetail fd) {
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

    private void applyModifiers(ApricotEntityShape shape) {
        if (modifiers != null) {
            for (ElementVisualModifier modifier : modifiers) {
                modifier.modify(shape);
            }
        }
    }
}
