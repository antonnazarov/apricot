package za.co.apricotdb.viewport.canvas;

import java.beans.PropertyChangeListener;

public interface CanvasBuilder {
    
    ApricotCanvas buildCanvas(PropertyChangeListener canvasChangeListener);
}
