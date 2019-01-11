package za.co.apricotdb.viewport.topology;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.VLineTo;
import javafx.stage.Stage;

public class TopologyTests extends Application {

    @Override
    public void start(Stage primaryStage) {

        TopologyManager m = new TopologyManager();
        Path pa = getPathA();
        Path pb = getPathB();
        VBox root = new VBox();
        Pane pane = new Pane();

        Button btn = new Button();
        btn.setText("Run Test");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                List<Rectangle2D> rectangles = getRectangles();

                Path tildasA = new Path();
                tildasA.setStrokeWidth(2);
                Path recalcPathA = m.recalculatePath(pa, rectangles, tildasA);
                recalcPathA.setStrokeWidth(2);
                recalcPathA.setStroke(Color.LIGHTGRAY);

                pane.getChildren().add(recalcPathA);
                pane.getChildren().add(tildasA);

                Path tildasB = new Path();
                tildasA.setStrokeWidth(2);
                Path recalcPathB = m.recalculatePath(pb, rectangles, tildasB);
                recalcPathB.setStrokeWidth(2);
                recalcPathB.setStroke(Color.LIGHTGRAY);

                pane.getChildren().add(recalcPathB);
                pane.getChildren().add(tildasB);

                List<Path> lines = new ArrayList<>();
                lines.add(pa);
                lines.add(pb);
                Path masks = new Path();
                masks.setStrokeWidth(2);
                masks.setStroke(Color.LIGHTGRAY);
                Path detours = new Path();
                detours.setStrokeWidth(2);
                LineMutualIntersectionManager intman = new LineMutualIntersectionManager();
                intman.getIntersectionMask(lines, rectangles, masks, detours);
                pane.getChildren().add(masks);
                pane.getChildren().add(detours);
            }
        });

        root.getChildren().add(btn);

        pane.setBorder(new Border(
                new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, new BorderWidths(2))));

        // rectangles
        List<Rectangle2D> rectangles = getRectangles();
        for (Rectangle2D rect : rectangles) {
            Rectangle r = getReclangle(rect);
            r.setStrokeWidth(4);
            pane.getChildren().add(r);
        }

        // paths
        pane.getChildren().add(pa);
        pane.getChildren().add(pb);

        /*
         * Path tilda1 = m.getTilda(new Point2D(310, 310), true);
         * tilda1.setStrokeWidth(2); pane.getChildren().add(tilda1); Path tilda2 =
         * m.getTilda(new Point2D(380, 380), false); tilda2.setStrokeWidth(2);
         * pane.getChildren().add(tilda2);
         */

        root.getChildren().add(pane);

        Scene scene = new Scene(root, 1400, 800);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void printSegments(Path p) {
        TopologyManager m = new TopologyManager();
        List<LineSegment> segments = m.getSegments(p);
        for (LineSegment s : segments) {
            System.out.println(s);
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application. main()
     * serves only as fallback in case the application can not be launched through
     * deployment artifacts, e.g., in IDEs with limited FX support. NetBeans ignores
     * main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Path getPathA() {
        Path p = new Path();

        p.getElements().add(new MoveTo(100, 100));
        p.getElements().add(new HLineTo(400));
        p.getElements().add(new VLineTo(200));
        p.getElements().add(new HLineTo(300));
        p.getElements().add(new VLineTo(150));
        p.getElements().add(new HLineTo(200));
        p.getElements().add(new VLineTo(450));
        p.getElements().add(new HLineTo(100));
        p.getElements().add(new VLineTo(550));
        p.getElements().add(new HLineTo(600));
        p.getElements().add(new VLineTo(650));
        p.getElements().add(new HLineTo(250));
        p.getElements().add(new VLineTo(80));
        p.getElements().add(new HLineTo(320));
        p.getElements().add(new VLineTo(750));

        p.setStrokeWidth(2);

        return p;
    }

    private Path getPathB() {
        Path p = new Path();

        p.getElements().add(new MoveTo(100, 250));
        p.getElements().add(new HLineTo(800));
        p.getElements().add(new VLineTo(450));
        p.getElements().add(new HLineTo(350));
        p.getElements().add(new VLineTo(130));
        p.getElements().add(new HLineTo(650));
        p.getElements().add(new VLineTo(650));
        p.getElements().add(new HLineTo(700));
        p.getElements().add(new VLineTo(50));
        p.getElements().add(new HLineTo(450));
        p.getElements().add(new VLineTo(800));

        p.setStrokeWidth(2);

        return p;
    }

    private Rectangle getReclangle(Rectangle2D rect) {

        Rectangle r = new Rectangle(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.BLUE);

        return r;
    }

    private List<Rectangle2D> getRectangles() {
        List<Rectangle2D> ret = new ArrayList<>();

        Rectangle2D r = new Rectangle2D(170, 170, 250, 450);
        ret.add(r);

        r = new Rectangle2D(290, 300, 120, 420);
        ret.add(r);

        r = new Rectangle2D(500, 300, 120, 420);
        ret.add(r);

        r = new Rectangle2D(680, 100, 180, 210);
        ret.add(r);

        r = new Rectangle2D(750, 400, 420, 120);
        ret.add(r);

        r = new Rectangle2D(850, 120, 180, 560);
        ret.add(r);

        return ret;
    }

}
