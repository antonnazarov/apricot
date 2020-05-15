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
            HBox p = getParentBox();
            p.setPrefHeight(0);
        });
    }

    public void setProgress(double progress) {
        Platform.runLater(() -> {
            parentWindow.getProgressBarModel().setProgress(progress);
        });
    }

    private void setProgressBarSize() {
        HBox p = getParentBox();
        double width = p.getWidth();
        p.setPrefHeight(20);

        parentWindow.getProgressBarModel().getProgressBar().setPrefWidth(width);
    }

    private HBox getParentBox() {
        ProgressBar pb = parentWindow.getProgressBarModel().getProgressBar();
        return (HBox) pb.getParent();
    }
}
