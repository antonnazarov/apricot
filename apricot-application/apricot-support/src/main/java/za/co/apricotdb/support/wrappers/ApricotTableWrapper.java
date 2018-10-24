package za.co.apricotdb.support.wrappers;

import java.util.List;

public interface ApricotTableWrapper {
    String getTableName();
    ApricotColumnWrapper getColumnByName(String columnName);
    List<String> getChildTableNames();
    List<ApricotConstraintWrapper> getConstraints();
}
