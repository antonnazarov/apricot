package za.co.apricotdb.viewport.notification;

import org.springframework.context.ApplicationEvent;

/**
 * This type of event has been raised when the "crow foot" icon in clicked. 
 * 
 * @author Anton Nazarov
 * @since 13/04/2020
 */
public class AbsentRelatedClickedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -8653617795260260463L;
    
    private String tableName;

    public AbsentRelatedClickedEvent(String tableName) {
        super(tableName);
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
