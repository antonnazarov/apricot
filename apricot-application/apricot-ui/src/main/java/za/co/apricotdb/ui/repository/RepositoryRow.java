package za.co.apricotdb.ui.repository;

import javafx.beans.property.SimpleObjectProperty;

/**
 * This bean represents a row of the left-buttons-right panel of Apricot Repository comparison form.
 * 
 * @author Anton Nazarov
 * @since 16/04/2020
 */
public class RepositoryRow {
    
    private SimpleObjectProperty localOboject;
    private SimpleObjectProperty buttons;
    private SimpleObjectProperty remoteObject;
    private RepositoryRowType rowType;
    
    public enum RepositoryRowType {
        PROJECT, SNAPSHOT
    }
    
    
}
