package za.co.apricotdb.ui.handler;

import java.io.Serializable;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * This bean contains property, stored in the Tab's UserData.
 * 
 * @author Anton Nazarov
 * @since 20/01/2019
 */
public class TabInfoObject implements Serializable {

    private static final long serialVersionUID = -3689232180222019259L;

    private ApricotCanvas canvas = null;
    private ApricotView view = null;
    private ApricotSnapshot snapshot = null;
    private ScrollPane scroll;

    public ApricotCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(ApricotCanvas canvas) {
        this.canvas = canvas;
    }

    public ApricotView getView() {
        return view;
    }

    public void setView(ApricotView view) {
        this.view = view;
    }

    public ApricotSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(ApricotSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public ScrollPane getScroll() {
        return scroll;
    }

    public void setScroll(ScrollPane scroll) {
        this.scroll = scroll;
    }

    public static TabInfoObject getTabInfo(Tab tab) {
        if (tab.getUserData() != null && tab.getUserData() instanceof TabInfoObject) {
            return (TabInfoObject) tab.getUserData();
        }

        return null;
    }
}
