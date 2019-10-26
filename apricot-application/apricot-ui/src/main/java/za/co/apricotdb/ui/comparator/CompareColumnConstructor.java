package za.co.apricotdb.ui.comparator;

import javafx.scene.control.TreeTableColumn;

/**
 * The interface of the column constructor.
 * 
 * @author Anton Nazarov
 * @since 26/10/2019
 */
public interface CompareColumnConstructor<C, T> {

    void construct(TreeTableColumn<C, T> column);
}
