package za.co.apricotdb.persistence.comparator;

/**
 * The object difference for different types of the objects.
 * 
 * @author Anton Nazarov
 * @since 07/10/2019
 */
public interface ApricotObjectDifference<O> {

    O getSourceObject();

    O getTargetObject();

    boolean isDifferent();
}
