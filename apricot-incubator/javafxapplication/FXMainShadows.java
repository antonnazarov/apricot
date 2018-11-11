package javafxapplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author g982675
 */
public class FXMainShadows extends Application {

    @Override
    public void start(Stage primaryStage) {
        Rectangle r1 = createShadowedBox(100, 10, 10, 5, 5, 10);
        Rectangle r2 = createShadowedBox(100, 20, 20, 10, 10, 10);
        Rectangle r3 = createShadowedBox(100, 30, 30, 15, 15, 10);
        Rectangle r4 = createShadowedBox(100, 20, 20, 0, 0, 10);
        Rectangle r5 = createShadowedBox(100, 20, 20, 0, 10, 10);
        Rectangle r6 = createShadowedBox(100, 20, 20, 10, 0, 10);
        Rectangle r7 = createShadowedBox(100, 20, 20, 10, 10, 0);
        Rectangle r8 = createShadowedBox(100, 20, 20, 10, 10, 20);
        Rectangle r9 = createShadowedBox(100, 20, 20, 10, 10, 50);
        Rectangle r10 = createShadowedBox(100, 10, 10, 5, 5, 10);
        r10.setStroke(Color.RED);
        FlowPane root = new FlowPane();
        root.setHgap(30);
        root.setVgap(30);
        root.setPrefWrapLength(200);
        root.getChildren().addAll(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);

        Scene scene = new Scene(root, 500, 500);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Rectangle createShadowedBox(double size,
            double shadowWidth, double shadowHeight,
            double offsetX, double offsetY,
            double radius) {
        Rectangle r = new Rectangle(size, size);
        r.setFill(Color.LIGHTGRAY);
        r.setStroke(Color.BLACK);
        r.setStrokeWidth(2);
        DropShadow e = new DropShadow();
        e.setWidth(shadowWidth);
        e.setHeight(shadowHeight);
        e.setOffsetX(offsetX);
        e.setOffsetY(offsetY);
        e.setRadius(radius);
        r.setEffect(e);
        return r;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
