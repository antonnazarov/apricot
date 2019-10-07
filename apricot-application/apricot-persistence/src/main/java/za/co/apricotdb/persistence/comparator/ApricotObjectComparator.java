package za.co.apricotdb.persistence.comparator;

import java.io.Serializable;

/**
 * The component - Apricot object comparator.
 * 
 * @author Anton Nazarov
 * @since 07/10/2019
 */
public interface ApricotObjectComparator<I extends Serializable, O extends ApricotObjectDifference<I>> {

    O compare(I source, I target);
}
