package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

import za.co.apricotdb.viewport.entity.ApricotEntity;

public class EntityContextMenuEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1920877189739918974L;

    private ApricotEntity entity;
    private double x;
    private double y;

    public EntityContextMenuEvent(ApricotEntity entity, double x, double y) {
        super(entity);

        this.entity = entity;
        this.x = x;
        this.y = y;
    }

    public ApricotEntity getEntity() {
        return entity;
    }

    public void setEntity(ApricotEntity entity) {
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
