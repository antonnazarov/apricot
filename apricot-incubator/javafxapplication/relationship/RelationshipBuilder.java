package javafxapplication.relationship;

/**
 * Classes of this type are responsible for drawing of graphical representation
 * of the entity link.
 * 
 * @author Anton Nazarov
 * @since 22/11/2018
 */
public interface RelationshipBuilder {

    ApricotRelationship buildRelationship(String parentEntityName, String childEntityName, 
            String primaryKeyName, String foreignKeyName, RelationshipType type);
}
