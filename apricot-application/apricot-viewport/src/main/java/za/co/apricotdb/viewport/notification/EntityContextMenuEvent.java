package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

import javafx.scene.Node;

public class EntityContextMenuEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1920877189739918974L;

    private Node entity;
    private double x;
    private double y;

    public EntityContextMenuEvent(Node entity, double x, double y) {
        super(entity);

        this.entity = entity;
        this.x = x;
        this.y = y;
    }

    public Node getEntity() {
        return entity;
    }

    public void setEntity(Node entity) {
        this.entity = entity;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
