package za.co.apricotdb.ui.comparator;

/**
 * The types of the objects, which can be compared in Apricot DB "Compare"-
 * functionality.
 * 
 * @author Anton Nazarov
 * @since 22/10/2019
 */
public enum CompareRowType {
    SNAPSHOT("snapshot-source.png", "snapshot-target.png", "snapshot-source.png", "snapshot-target.png", null,
            "Snapshot"),
    TABLE("table-plain.png", "table-gray.png", "table-not-equal.png", "table-plus.png", "table-exclam.png", "Table"),
    COLUMN("field-plain.png", "field-gray.png", "field-not-equal.png", "field-plus.png", "field-exclam.png", "Column"),
    CONSTRAINT("constraint-plain.png", "constraint-gray.png", "constraint-not-equal.png", "constraint-plus.png",
            "constraint-exclam.png", "Constraint"),
    RELATIONSHIP("relationship-plain.png", "relationship-gray.png", "relationship-not-equal.png",
            "relationship-plus.png", "relationship-exclam.png", "Relationship"),
    CONSTRAINT_COLUMNS("field-plain.png", "field-plain.png", "field-plain.png", "field-plain.png", "field-plain.png",
            "Constraint columns");

    CompareRowType(String plain, String gray, String notEqual, String plus, String exclamation, String name) {
        this.plain = plain;
        this.gray = gray;
        this.notEqual = notEqual;
        this.plus = plus;
        this.exclamation = exclamation;
        this.name = name;
    };

    private String plain;
    private String gray;
    private String notEqual;
    private String plus;
    private String exclamation;
    private String name;

    public String getPlain() {
        return plain;
    }

    public String getGray() {
        return gray;
    }

    public String getNotEqual() {
        return notEqual;
    }

    public String getPlus() {
        return plus;
    }

    public String getExclamation() {
        return exclamation;
    }

    public String getName() {
        return name;
    }
}
