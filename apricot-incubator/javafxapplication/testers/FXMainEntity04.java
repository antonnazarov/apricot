package javafxapplication.testers;

import javafx.application.Application;
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
import javafxapplication.align.AlignCommand;
import javafxapplication.align.CanvasSizeAjustor;
import javafxapplication.canvas.ApricotBasicCanvas;

public class FXMainEntity04 extends Application {
    ApricotBasicCanvas erCanvas = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        HBox topBox = new HBox();
        topBox.setBorder(new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(2))));
        topBox.setSpacing(7);
        root.getChildren().add(topBox);

        erCanvas = new ApricotBasicCanvas();
        erCanvas.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(2))));
        erCanvas.setPrefSize(600, 600);

        ScrollPane scroll = new ScrollPane();
        scroll.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        scroll.setContent(erCanvas);
        root.getChildren().add(scroll);
        
        addButtons(topBox);

        //  build entities 
        TestEntityBuilder eb = new BuildTwoEntites(erCanvas);
        eb.buildEntities();
        
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Proper Class Hierarchy Test");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        AlignCommand aligner = new CanvasSizeAjustor(erCanvas);
        aligner.align();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addButtons(HBox topBox) {
        Button btn = new Button();
        btn.setText("Align Screen");
        btn.setOnAction(event -> {
            AlignCommand aligner = new CanvasSizeAjustor(erCanvas);
            aligner.align();
        });
        topBox.getChildren().add(btn);
    }
}
