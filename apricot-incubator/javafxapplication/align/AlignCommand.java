package javafxapplication.align;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * All alignment operations have to be implemented, using this interface.
 * 
 * @author Anton Nazarov
 * @since 19/11/2018
 */
public interface AlignCommand {
    void execute(Stage primaryStage, Pane entityCanvas);
}
