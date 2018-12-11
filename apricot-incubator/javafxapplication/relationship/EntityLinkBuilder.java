package javafxapplication.relationship;

import javafx.scene.Group;

/**
 * Classes of this type are responsible for drawing of graphical representation
 * of the entity link.
 * 
 * @author Anton Nazarov
 * @since 22/11/2018
 */
public interface EntityLinkBuilder {

    Group buildLink(ApricotEntityLink entityLink);
}
