package za.co.apricotdb.viewport.entity;

public class FieldDetail {
    private String name;
    private boolean mandatory;
    private String type;
    private boolean primaryKey;
    private String constraints;

    public FieldDetail(String name, boolean mandatory, String type, boolean primaryKey, String constraints) {
        this.name = name;
        this.mandatory = mandatory;
        this.type = type;
        this.primaryKey = primaryKey;
        this.constraints = constraints;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String getConstraints() {
        return constraints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name).append(", mandatory=[").append(mandatory).append("], type=[").append(type)
                .append("], primaryKey=[").append(primaryKey).append("], constraints=[").append(constraints)
                .append("]");

        return sb.toString();
    }
}
