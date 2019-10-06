package za.co.apricotdb.persistence.comparator;

import java.io.Serializable;

public interface ApricotObjectComparator<I extends Serializable, O extends ApricotObjectDifference<I>> {
    
    O compare(I source, I target);
}
