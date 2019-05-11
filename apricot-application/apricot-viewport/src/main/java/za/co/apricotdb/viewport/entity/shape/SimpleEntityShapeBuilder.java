package za.co.apricotdb.viewport.entity.shape;

import java.util.List;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import za.co.apricotdb.viewport.entity.FieldDetail;
import za.co.apricotdb.viewport.modifiers.ElementVisualModifier;

public class SimpleEntityShapeBuilder extends DefaultEntityShapeBuilder {

    public SimpleEntityShapeBuilder(ElementVisualModifier... modifiers) {
        super(modifiers);
    }

    @Override
    public GridPane buildNonPrimaryKeyPanel(List<FieldDetail> details) {
        GridPane fPanel = new GridPane();

        fPanel.setHgap(FIELDS_HORIZONTAL_GAP);
        fPanel.setVgap(FIELDS_VERTICAL_GAP);

        Text field = new Text("...");
        field.setId("ellipsis");
        fPanel.add(field, 0, 0);

        return fPanel;
    }
}
