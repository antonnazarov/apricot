package za.co.apricotdb.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ProgressBar;

/**
 * The dynamic model of the Progress Bar.
 *
 * @author Anton Nazarov
 * @since 14/05/2020
 */
public class ProgressBarModel {

    private ProgressBar progressBar;
    private final DoubleProperty progress = new SimpleDoubleProperty();
    private final BooleanProperty visible = new SimpleBooleanProperty();

    public ProgressBarModel(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public DoubleProperty getProgressProperty() {
        return progress;
    }

    public BooleanProperty getVisibleProperty() {
        return visible;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }

    public double getProgress() {
        return this.progress.get();
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
