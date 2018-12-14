package javafxapplication.testers;

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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafxapplication.align.AlignCommand;
import javafxapplication.align.DiagramPanelManager;
import javafxapplication.align.SimpleGridEntityAllocator;
import javafxapplication.relationship.ApricotEntityLinkManager;
import javafxapplication.relationship.BasicEntityLinkManager;
import javafxapplication.relationship.RelationshipType;

/**
 * The advances test.
 *
 * @author Anton Nazarov
 * @since 10/11/2018
 */
public class FXMainEntity03 extends Application {

    @Override
    public void start(Stage primaryStage) {

        VBox root = new VBox();
        HBox topBox = new HBox();
        topBox.setBorder(new Border(
                new BorderStroke(Color.GREEN, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(4))));
        topBox.setSpacing(7);

        Pane entityCanvas = new Pane();
        entityCanvas.setPrefSize(400, 400);
        entityCanvas.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(0), new BorderWidths(4))));

        TestEntityCreateHelper helper = new TestEntityCreateHelper();
        // helper.buildAllEntities(primaryStage, entityCanvas, new String[] {"party",
        // "role_player", "award", "party_role", "work_permit",
        // "fsb_adviser_registration", "fsp_registration"});
        // helper.buildAllEntities(primaryStage, entityCanvas, new String[] {"party",
        // "fpa_education_course"});
        // helper.buildAllEntities(primaryStage, entityCanvas);
        helper.buildAllEntities(primaryStage, entityCanvas,
                new String[] { "fsp_registration", "fsb_adviser_registration", "party" });

        root.getChildren().add(topBox);

        ScrollPane s1 = new ScrollPane();
        s1.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(4))));
        s1.setContent(entityCanvas);

        root.getChildren().add(s1);

        Button btn = new Button();
        btn.setText("Align Screen");
        btn.setOnAction(event -> {
            DiagramPanelManager m = new DiagramPanelManager();
            m.adjustDiagramPanel(entityCanvas);
        });
        topBox.getChildren().add(btn);

        btn = new Button();
        btn.setText("Get Pos");
        btn.setOnAction(event -> {
            for (Node n : entityCanvas.getChildren()) {
                if (n instanceof VBox) {
                    VBox ent = (VBox) n;
                    System.out.println(n.getId() + "-> width=" + ent.getWidth() + ", height=" + ent.getHeight()
                            + ", LayoutX=" + ent.getLayoutX() + ", LayoutY=" + ent.getLayoutY() + ", TranslateX="
                            + ent.getTranslateX() + ", TranslateY=" + ent.getTranslateY());
                }
            }
        });
        topBox.getChildren().add(btn);

        btn = new Button();
        btn.setText("Scale Down/Up");
        btn.setOnAction(event -> {
            if (entityCanvas.getScaleX() == 0.5 && entityCanvas.getScaleY() == 0.5) {
                entityCanvas.setScaleX(1);
                entityCanvas.setScaleY(1);
            } else {
                entityCanvas.setScaleX(0.5);
                entityCanvas.setScaleY(0.5);
            }
        });
        topBox.getChildren().add(btn);

        btn = new Button();
        btn.setText("FKs");
        btn.setOnAction(event -> {
            PointHandler p = new PointHandler();

            for (Node n : entityCanvas.getChildren()) {
                if (n instanceof VBox) {
                    VBox ent = (VBox) n;
                    p.setForeignKeyDot(ent);
                }
            }
        });
        topBox.getChildren().add(btn);

        btn = new Button();
        btn.setText("Grid Align");
        btn.setOnAction(event -> {
            AlignCommand command = new SimpleGridEntityAllocator();
            command.execute(primaryStage, entityCanvas);

            DiagramPanelManager m = new DiagramPanelManager();
            m.adjustDiagramPanel(entityCanvas);
        });
        topBox.getChildren().add(btn);

        btn = new Button();
        btn.setText("Create Link");
        btn.setOnAction(event -> {
            ApricotEntityLinkManager manager = new BasicEntityLinkManager();
            manager.createEntityLink("fsp_registration", "fsb_adviser_registration", "id", "fsp_registration_id",
                    RelationshipType.IDENTIFYING, entityCanvas);

//            ApricotEntityLinkDeprecated r = new ApricotEntityLinkDeprecated("fsp_registration", "fsb_adviser_registration", 
//                    "id", "fsp_registration_id", RelationshipType.IDENTIFYING, entityCanvas);
//            r.draw();
        });
        topBox.getChildren().add(btn);

        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setTitle("Many Entities Test");
        primaryStage.setScene(scene);
        primaryStage.show();

        postProcess(entityCanvas);
    }

    private void postProcess(Pane entityCanvas) {
        DiagramPanelManager m = new DiagramPanelManager();
        m.adjustDiagramPanel(entityCanvas);
        ApricotEntityLinkManager manager = new BasicEntityLinkManager();
        manager.createEntityLink("fsp_registration", "fsb_adviser_registration", "id", "fsp_registration_id",
                RelationshipType.IDENTIFYING, entityCanvas);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
