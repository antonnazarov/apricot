package javafxapplication;

import java.util.List;
import javafx.scene.Node;

public interface EntityBuilder {
    Node buildEntity();
    void setFieldDetails(List<FieldDetail> details);
}
