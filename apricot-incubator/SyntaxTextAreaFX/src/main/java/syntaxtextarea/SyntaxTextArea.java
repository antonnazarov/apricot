/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntaxtextarea;

import in.co.s13.syntaxtextareafx.SyntaxTextAreaFX;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author nika
 */
public class SyntaxTextArea extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        SyntaxTextAreaFX ta= new SyntaxTextAreaFX("test/sample.sql");
        
        StackPane root = new StackPane();
        root.getChildren().add(ta.getNode());
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
