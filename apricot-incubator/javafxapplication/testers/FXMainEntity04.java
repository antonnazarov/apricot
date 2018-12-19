package javafxapplication.testers;

import java.util.Set;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
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
import za.co.apricotdb.viewport.align.AlignCommand;
import za.co.apricotdb.viewport.align.CanvasSizeAjustor;
import za.co.apricotdb.viewport.align.SetMaxEqualWidth;
import za.co.apricotdb.viewport.canvas.ApricotCanvasBuilder;
import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;
import za.co.apricotdb.viewport.canvas.CanvasBuilder;

public class FXMainEntity04 extends Application {
    ApricotCanvasImpl erCanvas = null;
    CanvasBuilder canvasBuilder = new ApricotCanvasBuilder();

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        HBox topBox = new HBox();
        topBox.setBorder(new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(1))));
        topBox.setSpacing(7);
        root.getChildren().add(topBox);

        erCanvas = (ApricotCanvasImpl) canvasBuilder.buildCanvas();
        erCanvas.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(1))));
        erCanvas.setPrefSize(600, 600);

        ScrollPane scroll = new ScrollPane();
        scroll.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
        
        //scroll.setStyle("-fx-font-size: 18px;");  // set the font size to something big.
        //erCanvas.setStyle("-fx-font-size: 12px;");
        
        scroll.setContent(erCanvas);
        root.getChildren().add(scroll);
        
        addButtons(topBox);

        //  build entities 
        TestEntityBuilder eb = new BuildSomeEntites(erCanvas);
        eb.buildEntities();
        
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Proper Class Hierarchy Test");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //  post creation aligns
        AlignCommand aligner = new CanvasSizeAjustor(erCanvas);
        aligner.align();
        
        aligner = new SetMaxEqualWidth(erCanvas);
        aligner.align();
        
        // aligner = new SimpleGridEntityAllocator(erCanvas);
        // aligner.align();
        
        Set<Node> nodes = scroll.lookupAll(".scroll-bar");
        for (final Node node : nodes) {
            if (node instanceof ScrollBar) {
                ScrollBar sb = (ScrollBar) node;
                if (sb.getOrientation() == Orientation.VERTICAL) { // HORIZONTAL is another option.
                    sb.setPrefWidth(18); // You can define your preferred width here.
                } else {
                    sb.setPrefHeight(18);
                }
            }
        }
        
        BuildSomeRelationships br = new BuildSomeRelationships(erCanvas);
        br.build();
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
