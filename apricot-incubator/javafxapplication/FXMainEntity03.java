package javafxapplication;

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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The advances test.
 *
 * @author Anton Nazarov
 * @since 10/11/2018
 */
public class FXMainEntity03 extends Application {

    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        root.setPrefSize(400, 400);
        root.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(4))));

        EntityBuilder eBuilder = new SimpleEntityBuilder("fpa_education_course", TestEntityCreateHelper.getFpaEducationCourse(), primaryStage);
        Node entity = eBuilder.buildEntity();
        entity.setLayoutX(30);
        entity.setLayoutY(30);
        root.getChildren().add(entity);

        eBuilder = new SimpleEntityBuilder("intermediary_agreement", TestEntityCreateHelper.getIntermediaryAgreement(), primaryStage);
        entity = eBuilder.buildEntity();
        entity.setLayoutX(130);
        entity.setLayoutY(130);
        root.getChildren().add(entity);

        ScrollPane s1 = new ScrollPane();
        s1.setPrefSize(120, 120);
        s1.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(4))));
        s1.setContent(root);

        /*
        entity.setOnMouseMoved(event -> {
            System.out.println("mouse moved " + event.getX() + " " + event.getY());
        });
         */
        
        final Node entity1 = entity;
        Button btn = new Button();
        btn.setText("Get layouts");
        btn.setOnAction(event -> {
            System.out.println("LayoutX=" + entity1.getLayoutX() + " LayoutY=" + entity1.getLayoutY()
                    + " TranslateX=" + entity1.getTranslateX() + " TranslateY=" + entity1.getTranslateY());
        });
        root.getChildren().add(btn);

        Scene scene = new Scene(s1, 1200, 800);

        primaryStage.setTitle("Many Entities Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
