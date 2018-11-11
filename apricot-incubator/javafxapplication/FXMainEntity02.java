package javafxapplication;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
public class FXMainEntity02 extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        
        this.primaryStage = primaryStage;

        //  the main pane
        Pane root = new Pane();

        Insets insets = new Insets(5, 0, 5, 20);
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
        pkBox.setPadding(new Insets(5, 0, 5, 10));
        pkBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        pkBox.setHgap(10);
        pkBox.setVgap(3);

        int i = 0;
        Text fieldName = new Text("id *");
        fieldName.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        Tooltip.install(fieldName,
                new Tooltip("id, not null, bigint, PK"));
        pkBox.add(fieldName, 0, i);
        Text type = new Text(" ");
        type.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(type, 1, i);
        Text constr = new Text(" ");
        constr.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(constr, 2, i);

        i++;
        fieldName = new Text("audit_id *");
        fieldName.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(fieldName, 0, i);
        type = new Text(" ");
        type.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(type, 1, i);
        constr = new Text(" ");
        constr.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
        pkBox.add(constr, 2, i);

        bs = new BorderStroke(Color.TRANSPARENT, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                new CornerRadii(0), new BorderWidths(2), Insets.EMPTY);
        b = new Border(bs);
        GridPane innerBox = new GridPane();
        innerBox.setBorder(b);
        innerBox.setPadding(new Insets(10, 0, 10, 10));
        innerBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        innerBox.setHgap(10);
        innerBox.setVgap(3);

        i = 0;
        fieldName = new Text("action");
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("prev_audit_id");
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("prev_audit_timestamp");
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("finger_prints_taken");
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("date_finger_prints_taken");
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("record_type");
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("criminal_record_result");
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("date_indemnity_form_signed");
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
        innerBox.add(type, 1, i);
        constr = new Text("");
        innerBox.add(constr, 2, i);

        i++;
        fieldName = new Text("person_id * (FK)");
        fieldName.setFill(Color.BLUE);
        innerBox.add(fieldName, 0, i);
        type = new Text(" ");
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

        outerBox.setOnMousePressed(getOnMousePressedEventHandler());
        outerBox.setOnMouseDragged(getOnMouseDraggedEventHandler());
        outerBox.setOnMouseReleased(event -> {
            if (primaryStage.getScene().getCursor() != Cursor.DEFAULT) {
                primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
            System.out.println("The mouse is released");
        });

        /*
         AtomicBoolean focused = new AtomicBoolean(false);
         outerBox.setOnMouseClicked(event -> {
         BorderStroke pbs = null;
         if (!focused.get()) {
         pbs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(3));
         focused.getAndSet(true);
         } else {
         pbs = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1));
         focused.getAndSet(false);
         }
         Border pb = new Border(pbs);
         pkBox.setBorder(pb);
         innerBox.setBorder(pb);

         System.out.println("Mouse clicked");
         });
         */
        // outerBox.setScaleX(1.5);
        // outerBox.setScaleY(1.5);
        root.getChildren().add(outerBox);

        Button btn = new Button();
        btn.setText("Command Button");
        btn.setOnAction(event -> {
            System.out.println("Command has been performed");
            System.out.println("Width=" + outerBox.getWidth());
            System.out.println("Highth=" + outerBox.getHeight());
        });
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Entity Drawing, version 2");
        primaryStage.setScene(scene);
        primaryStage.setOnShowing(event -> {
            // outerBox.setPrefWidth(400);
            System.out.println("On Showing was called");
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.PRIMARY) {
                    Node sourceNode = (Node) t.getSource();
                    DragInitPosition pos = new DragInitPosition(t.getSceneX(),
                            t.getSceneY(), sourceNode.getTranslateX(),
                            sourceNode.getTranslateY());
                    sourceNode.setUserData(pos);
                }
            }
        };

        return onMousePressedEventHandler;
    }

    private EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.PRIMARY) {
                    Node sourceNode = (Node) t.getSource();
                    if (sourceNode.getUserData() != null && sourceNode.getUserData() instanceof DragInitPosition) {
                        DragInitPosition pos = (DragInitPosition) sourceNode.getUserData();

                        double offsetX = t.getSceneX() - pos.getOrgSceneX();
                        double offsetY = t.getSceneY() - pos.getOrgSceneY();
                        double newTranslateX = pos.getOrgTranslateX() + offsetX;
                        double newTranslateY = pos.getOrgTranslateY() + offsetY;

                        sourceNode.setTranslateX(newTranslateX);
                        sourceNode.setTranslateY(newTranslateY);

                        primaryStage.getScene().setCursor(Cursor.HAND);
                    }
                }
            }
        };

        return onMouseDraggedEventHandler;
    }
}
