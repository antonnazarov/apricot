package javafxapplication.testers;

import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafxapplication.canvas.ApricotBasicCanvas;
import javafxapplication.entity.ApricotEntity;
import javafxapplication.entity.ApricotEntityBuilder;
import javafxapplication.entity.FieldDetail;

public class FXMainEntity04 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        HBox topBox = new HBox();
        topBox.setBorder(new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(2))));
        topBox.setSpacing(7);
        root.getChildren().add(topBox);

        ApricotBasicCanvas erCanvas = new ApricotBasicCanvas();
        erCanvas.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(2))));
        erCanvas.setPrefSize(600, 600);

        ScrollPane scroll = new ScrollPane();
        scroll.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        scroll.setContent(erCanvas);
        root.getChildren().add(scroll);
        
        addButtons(topBox);

        TestEntityCreateHelper helper = new TestEntityCreateHelper();
        Map<String, List<FieldDetail>> entities = helper.getAllEntities();
        
        // String table = "intermediary_agreement";
        // List<FieldDetail> fields = entities.get(table);
        String table = "fsb_adviser_registration";
        List<FieldDetail> fields = entities.get(table);
        
        ApricotEntityBuilder b = new ApricotEntityBuilder(erCanvas);
        ApricotEntity entity = b.buildEntity(table, fields, false);
        erCanvas.addElement(entity);
        Node n = entity.getShape();
        n.setLayoutX(30);
        n.setLayoutY(30);

        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Proper Class Hierarchy Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addButtons(HBox topBox) {
        Button btn = new Button();
        btn.setText("Align Screen");
        btn.setOnAction(event -> {
            System.out.println("Test");
        });
        topBox.getChildren().add(btn);
    }
}
