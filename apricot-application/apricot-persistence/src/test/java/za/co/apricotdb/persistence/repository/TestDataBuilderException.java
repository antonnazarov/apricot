package za.co.apricotdb.persistence.repository;

/**
 * The DataBuilder exception.
 *
 * @author Anton Nazarov
 * @since 26/09/2018
 */
public class TestDataBuilderException extends Exception {

    public TestDataBuilderException(String msg) {
        super(msg);
    }

    public TestDataBuilderException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
