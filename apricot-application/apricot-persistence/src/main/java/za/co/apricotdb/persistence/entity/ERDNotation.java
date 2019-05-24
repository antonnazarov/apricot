package za.co.apricotdb.persistence.entity;

/**
 * The ERD notations supported by Apricot DB.
 * 
 * @author Anton Nazarov
 * @since 24/05/2019
 */
public enum ERDNotation {

    IDEF1x("IDEF1x"), CROWS_FOOT("Crow's Foot");

    private String definition;

    private ERDNotation(String definition) {
        this.definition = definition;
    }

    public String getDefinition() {
        return definition;
    }

    public static ERDNotation parseNotation(String notation) {
        ERDNotation[] values = values();
        for (ERDNotation n : values) {
            if (n.getDefinition().equals(notation)) {
                return n;
            }
        }

        return null;
    }
}
