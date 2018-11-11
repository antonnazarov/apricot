package javafxapplication;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Attempt to draw an entity.
 * 
 * @author Anton Nazarov
 */
public class FXMainEntity01 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Command Button");
        btn.setOnAction(event -> {System.out.println("Command has been performed");});
        
        //  the main pane
        Pane root = new Pane();
        root.getChildren().add(btn);
        Insets insets = new Insets(20, 20, 20, 20);
        root.setPadding(insets);
        
        VBox outerBox = new VBox();
        Text header = new Text("criminal_record_audit");
        header.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        outerBox.getChildren().add(header);
        outerBox.setLayoutX(100);
        outerBox.setLayoutY(100);
        
        BorderStroke bs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2));
        Border b = new Border(bs);
        GridPane pkBox = new GridPane();
        pkBox.setBorder(b);
        pkBox.setPadding(new Insets(10));
        pkBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        pkBox.setHgap(10);
        pkBox.setVgap(3);

        int i = 0;
        Text fieldName = new Text("id *");
        fieldName.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(fieldName, 0, i);
        Text type = new Text("bigint");
        type.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(type, 1, i);
        Text constr = new Text("PK");
        constr.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(constr, 2, i);
        
        i++;
        fieldName = new Text("audit_id *");
        fieldName.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(fieldName, 0, i);
        type = new Text("bigint");
        type.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(type, 1, i);
        constr = new Text("PK");
        constr.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(constr, 2, i);
        
        
        bs = new BorderStroke(Color.TRANSPARENT, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                new CornerRadii(0), new BorderWidths(2), Insets.EMPTY);
        b = new Border(bs);
        GridPane innerBox = new GridPane();
        innerBox.setBorder(b);
        innerBox.setPadding(new Insets(10));
        innerBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        innerBox.setHgap(10);
        innerBox.setVgap(3);
       
        i = 0;
        fieldName = new Text("action");
        innerBox.add(fieldName, 0, i);
        type = new Text("smallint");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("prev_audit_id");
        innerBox.add(fieldName, 0, i);
        type = new Text("bigint");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);
        
        i++;
        fieldName = new Text("prev_audit_timestamp");
        innerBox.add(fieldName, 0, i);
        type = new Text("datetime2");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("finger_prints_taken");
        innerBox.add(fieldName, 0, i);
        type = new Text("bit");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("date_finger_prints_taken");
        innerBox.add(fieldName, 0, i);
        type = new Text("date");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("record_type");
        innerBox.add(fieldName, 0, i);
        type = new Text("varchar (255)");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("criminal_record_result");
        innerBox.add(fieldName, 0, i);
        type = new Text("varchar (255)");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("date_indemnity_form_signed");
        innerBox.add(fieldName, 0, i);
        type = new Text("date");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("person_id *");
        innerBox.add(fieldName, 0, i);
        type = new Text("bigint");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);
        
        DropShadow e = new DropShadow();
        e.setWidth(3);
        e.setHeight(3);
        e.setOffsetX(3);
        e.setOffsetY(3);
        e.setRadius(4);
        pkBox.setEffect(e);
        innerBox.setEffect(e);
        
        outerBox.getChildren().add(pkBox);
        outerBox.getChildren().add(innerBox);
        
        // outerBox.setScaleX(1.5);
        // outerBox.setScaleY(1.5);
        
        root.getChildren().add(outerBox);
        
        Scene scene = new Scene(root, 1200, 800);
        
        primaryStage.setTitle("Entity Drawing, version 1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
