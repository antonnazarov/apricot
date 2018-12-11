package javafxapplication.testers;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PointHandler {
    public void setForeignKeyDot(VBox entity) {
        System.out.println(entity.getId() + " " + entity.getTranslateX() + " " + entity.getTranslateY());
        GridPane p = (GridPane) entity.getChildren().get(2);
        for (Node n : p.getChildren()) {
            if (n instanceof Text) {
                Text t = (Text) n;
                System.out.println(
                        entity.getId() + " ---- " + t.getText() + "->X:" + t.getLayoutX() + " Y:" + t.getLayoutY());
            }
        }
    }
}
