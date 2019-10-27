package za.co.apricotdb.ui.comparator;

/**
 * The types of the objects, which can be compared in Apricot DB "Compare"-
 * functionality.
 * 
 * @author Anton Nazarov
 * @since 22/10/2019
 */
public enum CompareRowType {
    SNAPSHOT("snapshot-source.png", "snapshot-target.png", "snapshot-source.png", "snapshot-target.png", null),
    TABLE("table-plain.png", "table-gray.png", "table-not-equal.png", "table-plus.png", "table-exclam.png"),
    COLUMN("field-plain.png", "field-gray.png", "field-not-equal.png", "field-plus.png", "field-exclam.png"),
    CONSTRAINT("constraint-plain.png", "constraint-gray.png", "constraint-not-equal.png", "constraint-plus.png",
            "constraint-exclam.png"),
    RELATIONSHIP("relationship-plain.png", "relationship-gray.png", "relationship-not-equal.png",
            "relationship-plus.png", "relationship-exclam.png"),
    CONSTRAINT_COLUMNS("field-plain.png", "field-plain.png", "field-plain.png", "field-plain.png", "field-plain.png");

    CompareRowType(String plain, String gray, String notEqual, String plus, String exclamation) {
        this.plain = plain;
        this.gray = gray;
        this.notEqual = notEqual;
        this.plus = plus;
        this.exclamation = exclamation;
    };

    private String plain;
    private String gray;
    private String notEqual;
    private String plus;
    private String exclamation;

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
}
