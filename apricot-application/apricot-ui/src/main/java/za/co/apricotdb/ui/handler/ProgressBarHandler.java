package za.co.apricotdb.ui.handler;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.ParentWindow;

/**
 * This component contains the business logic required for the Progress Bar operations.
 */
@Component
public class ProgressBarHandler {

    @Autowired
    ParentWindow parentWindow;

    public void initProgressBar() {
        Platform.runLater(() -> {
            parentWindow.getProgressBarModel().setProgress(0.0d);
            parentWindow.getProgressBarModel().setVisible(true);
            setProgressBarSize();
        });
    }

    public void finalizeProgressBar() {
        Platform.runLater(() -> {
            parentWindow.getProgressBarModel().setProgress(1.0d);
            parentWindow.getProgressBarModel().setVisible(false);
            ProgressBar pb = parentWindow.getProgressBarModel().getProgressBar();
            pb.setPrefHeight(0);
        });
    }

    public void setProgress(double progress) {
        Platform.runLater(() -> {
            parentWindow.getProgressBarModel().setProgress(progress);
        });
    }

    private void setProgressBarSize() {
        ProgressBar pb = parentWindow.getProgressBarModel().getProgressBar();
        HBox p = (HBox) pb.getParent();
        double width = p.getWidth();
        p.setPrefHeight(20);

        pb.setPrefWidth(width);
    }
}
