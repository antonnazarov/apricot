package za.co.apricotdb.support.wrappers;

import java.util.List;

/**
 * The general interface, which represents the ApricotColumn wrapper.
 * 
 * @author Anton Nazarov
 * @since 24/10/2018
 */
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
