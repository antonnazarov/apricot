package za.co.apricotdb.support.wrappers;

import java.util.List;

/**
 * The general interface, which represents the ApricotTable wrapper.
 * 
 * @author Anton Nazarov
 * @since 24/10/2018
 */
public interface ApricotTableWrapper {

    String getTableName();

    ApricotColumnWrapper getColumnByName(String columnName);

    List<String> getChildTableNames();

    List<ApricotConstraintWrapper> getConstraints();
}
