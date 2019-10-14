package za.co.apricotdb.persistence.comparator;

/**
 * The object difference for different types of the objects.
 * 
 * @author Anton Nazarov
 * @since 07/10/2019
 */
public interface ApricotObjectDifference<O> {
    
    static String EMPTY = "[---]"; 

    O getSourceObject();

    O getTargetObject();

    boolean isDifferent();
    
    default void getDiffFlag(StringBuilder sb) {
        if (!isDifferent()) {
            sb.append(" [equal]");
        } else {
            sb.append(" [different]");
        }
        sb.append("\n");
    }
}
