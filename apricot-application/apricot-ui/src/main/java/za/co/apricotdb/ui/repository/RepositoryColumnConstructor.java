package za.co.apricotdb.ui.repository;

import org.springframework.stereotype.Component;

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import za.co.apricotdb.ui.comparator.CompareColumnConstructor;

/**
 * The constructor of the Repository form cell.
 * 
 * @author Anton Nazarov
 * @since 20/04/2020
 */
@Component
public class RepositoryColumnConstructor implements CompareColumnConstructor<RepositoryRow, RepositoryCell> {

    @Override
    public void construct(TreeTableColumn<RepositoryRow, RepositoryCell> column) {
        
        column.setCellFactory(clmn -> {
            return new TreeTableCell<RepositoryRow, RepositoryCell>() {
                @Override
                protected void updateItem(RepositoryCell item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    setText(null);
                    setGraphic(item);
                }
            };
        });
    }
}
