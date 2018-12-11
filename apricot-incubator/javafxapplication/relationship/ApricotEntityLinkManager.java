package javafxapplication.relationship;

import javafx.scene.layout.Pane;

/**
 * This is the manager interface, which incapsulate the algorithms of making
 * decisions which type of the link to be drawn ("Ordinary", "Dad's Hand", "A
 * Hat").
 *
 * @author Anton Nazarov
 * @since 21/11/2018
 */
public interface ApricotEntityLinkManager {

    /**
     * Create a new link between entities using the mnemonic parameters
     */
    void createEntityLink(String parentEntityName, String childEntityName, String primaryKeyName, String foreignKeyName,
            RelationshipType relationshipType, Pane entityCanvas);

    /**
     * handle an existing link between entities.
     */
    void handleEntityLink(ApricotEntityLink link);
}
