package za.co.apricotdb.ui.repository;

/**
 * The Apricot Repository specific checked exception.
 * 
 * @author Anton Nazarov
 * @since 07/04/2020
 */
public class ApricotRepositoryException extends Exception {

    private static final long serialVersionUID = -1151985794668207632L;

    public ApricotRepositoryException(String message) {
        super(message);
    }

    public ApricotRepositoryException(String message, Throwable ex) {
        super(message, ex);
    }
    
    public ApricotRepositoryException(Throwable ex) {
        super(ex);
    }
}
