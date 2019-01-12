package za.co.apricotdb.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import za.co.apricotdb.ui.controller.ApplicationInitializer;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "za.co.apricotdb")
public class ApricotMainApp extends Application {

    private ConfigurableApplicationContext context;
    private Parent rootNode;

    ApplicationInitializer initializer;

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(ApricotMainApp.class);
        initializer = context.getBean(ApplicationInitializer.class);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("apricot-main.fxml"));
        loader.setControllerFactory(context::getBean);
        rootNode = loader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ParentWindow pw = new ParentWindow(rootNode);
        primaryStage.setOnShown(event -> {
            initializer.initializeDefault(pw);
        });

        primaryStage.setScene(new Scene(rootNode));
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Apricot DB");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("favicon-32x32.png")));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @deprecated
     */
    private void setOnStageResize(Stage stage, ParentWindow pw) {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
//            paneAdjustor.adjustSplitPaneWidth(pw.getCenterNode(), stage.getWidth());
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
    }
}
