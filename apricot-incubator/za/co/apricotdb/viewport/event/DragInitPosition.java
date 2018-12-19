package za.co.apricotdb.viewport.event;

import java.io.Serializable;

public class DragInitPosition implements Serializable {

    private static final long serialVersionUID = -1262439709050476344L;
    
    private double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
    private DraggingType draggingType;
    private double origWidth, origHeight;

    public DragInitPosition(double orgSceneX, double orgSceneY, double orgTranslateX, double orgTranslateY) {
        this.orgSceneX = orgSceneX;
        this.orgSceneY = orgSceneY;
        this.orgTranslateX = orgTranslateX;
        this.orgTranslateY = orgTranslateY;
    }

    public double getOrgSceneX() {
        return orgSceneX;
    }

    public double getOrgSceneY() {
        return orgSceneY;
    }

    public double getOrgTranslateX() {
        return orgTranslateX;
    }

    public double getOrgTranslateY() {
        return orgTranslateY;
    }

    public DraggingType getDraggingType() {
        return draggingType;
    }

    public void setDraggingType(DraggingType draggingType) {
        this.draggingType = draggingType;
    }

    public double getOrigWidth() {
        return origWidth;
    }

    public void setOrigWidth(double origWidth) {
        this.origWidth = origWidth;
    }

    public double getOrigHeight() {
        return origHeight;
    }

    public void setOrigHeight(double origHeight) {
        this.origHeight = origHeight;
    }
}
