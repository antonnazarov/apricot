package za.co.apricotdb.viewport.entity;

import java.util.List;

/**
 * The generic entity builder interface.
 *
 * @author Anton Nazarov
 * @since 04/12/2018
 */
public interface EntityBuilder {

    ApricotEntity buildEntity(String tableName, List<FieldDetail> details, boolean slave);

}
