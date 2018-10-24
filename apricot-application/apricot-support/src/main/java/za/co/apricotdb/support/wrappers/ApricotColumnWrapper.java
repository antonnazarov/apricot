package za.co.apricotdb.support.wrappers;

import java.util.List;

public interface ApricotColumnWrapper {
    String getColumnName();
    String isMandatory();
    int getOrdinalPosition();
    String getColumnType();
    boolean isForeignKey();
    String getForeignTableName();
    String getConstraintAliases();
    List<ApricotConstraintWrapper> getConstraints();
    String getConstraintsAsString();
}
