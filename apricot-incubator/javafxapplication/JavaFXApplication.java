package javafxapplication;

import javafx.application.Application;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class JavaFXApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(12);
        Button myButton = new Button("press me!");
        myButton.setId("myButton");
        myButton.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        myButton.setOnAction(e -> changeNodeVisibility(myButton.getScene(),
                "myCheckbox"));
        CheckBox myCheckbox = new CheckBox("Hello!");
        myCheckbox.setId("myCheckbox");
        myCheckbox.setOpacity(0.7d);
        myCheckbox.setEffect(new Reflection());
        hBox.getChildren().addAll(myButton, myCheckbox);
        Scene mySceneGraph = new Scene(hBox, 800, 600);
        primaryStage.setScene(mySceneGraph);
        primaryStage.show();
    }

    private void changeNodeVisibility(Scene scene, String id) {
        Node node = scene.lookup("#" + id);
        if (node != null) {
            node.setVisible(!node.isVisible());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
