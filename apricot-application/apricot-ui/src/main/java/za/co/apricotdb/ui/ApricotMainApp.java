package za.co.apricotdb.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.handler.ApplicationInitializer;
import za.co.apricotdb.ui.handler.OnKeyPressedEventHandler;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "za.co.apricotdb")
public class ApricotMainApp extends Application {

    private ConfigurableApplicationContext context;
    private Pane rootNode;

    ApplicationInitializer initializer;

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(ApricotMainApp.class);
        initializer = context.getBean(ApplicationInitializer.class);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("apricot-main.fxml"));
        loader.setControllerFactory(context::getBean);
        rootNode = loader.load();
        
        MainAppController controller = loader.<MainAppController>getController();
        System.out.println("ApricotMainApp: the main app controller was instantiated: " + controller.toString());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ParentWindow pw = context.getBean(ParentWindow.class);
        pw.setParentPane(rootNode);
        primaryStage.setOnShown(event -> {
            initializer.initializeDefault();
        });
        primaryStage.setScene(new Scene(rootNode));
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Apricot DB");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("favicon-32x32.png")));
        primaryStage.setMaximized(true);
        primaryStage.show();
        
        //  handling key pressed in the main scene
        primaryStage.getScene().setOnKeyPressed(context.getBean(OnKeyPressedEventHandler.class));
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
