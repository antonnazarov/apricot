package za.co.apricotdb.persistence.comparator;

public interface ApricotObjectDifference<O> {

    O getSourceObject();

    O getTargetObject();

    boolean isDifferent();
}
