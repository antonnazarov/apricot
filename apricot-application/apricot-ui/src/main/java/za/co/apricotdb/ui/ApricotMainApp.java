package za.co.apricotdb.ui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import za.co.apricotdb.ApricotLauncher;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.util.ImageHelper;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "za.co.apricotdb")
public class ApricotMainApp extends Application {

    Logger logger = LoggerFactory.getLogger(ApricotMainApp.class);

    private ConfigurableApplicationContext context;
    private Pane rootNode;
    private MainAppController controller;

    ApplicationInitializer initializer;

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(ApricotMainApp.class);
        initializer = context.getBean(ApplicationInitializer.class);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("apricot-main.fxml"));
        loader.setControllerFactory(context::getBean);
        rootNode = loader.load();

        controller = loader.<MainAppController>getController();
        controller.init();

        logger.info("Apricot DB has been successfully initialized");
    }

    @Override
    public void start(Stage primaryStage) {
        ParentWindow pw = context.getBean(ParentWindow.class);
        pw.setParentPane(rootNode);
        pw.init(controller);
        pw.setApplication(this);
        pw.setPrimaryStage(primaryStage);
        primaryStage.setOnShown(event -> {
            initializer.initializeDefault();
        });
        primaryStage.setScene(new Scene(rootNode));
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Apricot DB");
        primaryStage.getIcons().add(ImageHelper.getImage("favicon-32x32.png", getClass()));
        primaryStage.setMaximized(true);

        primaryStage.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    double ratio = 350.0 / rootNode.getWidth();
                    controller.splitPane.setDividerPositions(ratio);
                    observable.removeListener(this);
                }
            }
        });

        primaryStage.show();
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        ApricotLauncher.main(null);
        launch(args);
    }
}
