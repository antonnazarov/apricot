package javafxapplication.relationship;

import javafxapplication.canvas.ApricotERElement;
import javafxapplication.relationship.shape.ApricotLinkShape;

public interface ApricotEntityLink extends ApricotERElement {
    
    ApricotLinkShape getLinkShape();
}
