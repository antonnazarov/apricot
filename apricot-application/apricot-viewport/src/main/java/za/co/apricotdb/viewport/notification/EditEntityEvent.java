package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;

/**
 * This event is generated when some entity was double clicked inside the current canvas.
 * 
 * @author Anton Nazarov
 * @since 06/03/2019
 */
public class EditEntityEvent extends ApplicationEvent {

    private static final long serialVersionUID = 3948546285430849097L;
    
    private String tableName;

    public EditEntityEvent(ApricotCanvas source, String tableName) {
        super(source);
        this.tableName = tableName;
    }
    
    public String getTableName() {
        return tableName;
    }
}
