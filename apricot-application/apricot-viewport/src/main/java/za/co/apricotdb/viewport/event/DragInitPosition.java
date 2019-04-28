package za.co.apricotdb.viewport.event;

import java.io.Serializable;

public class DragInitPosition implements Serializable {

    private static final long serialVersionUID = -1262439709050476344L;
    
    private double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
    private DraggingType draggingType;
    private double origWidth, origHeight;
    private double initRuler;

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

    public double getInitRuler() {
        return initRuler;
    }

    public void setInitRuler(double initRuler) {
        this.initRuler = initRuler;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("DragInitPosition: ");
        sb.append("orgSceneX=[").append(orgSceneX).append("], ");
        sb.append("orgSceneY=[").append(orgSceneY).append("], ");
        sb.append("orgTranslateX=[").append(orgTranslateX).append("], ");
        sb.append("orgTranslateY=[").append(orgTranslateY).append("], ");
        sb.append("draggingType=[").append(draggingType).append("], ");
        sb.append("origWidth=[").append(origWidth).append("], ");
        sb.append("origHeight=[").append(origHeight).append("], ");
        sb.append("initRuler=[").append(initRuler).append("]");
        
        return sb.toString();
    }
}
