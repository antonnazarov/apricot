package javafxapplication.testers;

import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;
import za.co.apricotdb.viewport.relationship.ApricotRelationshipBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipBuilder;
import za.co.apricotdb.viewport.relationship.RelationshipType;

public class BuildSomeRelationships {
    
    private RelationshipBuilder rBuilder = null;
    private ApricotCanvasImpl canvas = null;
    
    public BuildSomeRelationships(ApricotCanvasImpl canvas) {
        this.canvas = canvas;
        rBuilder = new ApricotRelationshipBuilder(canvas); 
    }
    
    public void build() {
        ApricotRelationship r = rBuilder.buildRelationship("agreement", "intermediary_agreement", "id", "id", RelationshipType.IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("fsp_registration", "fsb_adviser_registration", "id", "fsp_registration_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("role_player", "party_role", "id", "role_player_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
        r = rBuilder.buildRelationship("role_player", "fsb_adviser_registration", "id", "role_player_id", RelationshipType.OPTIONAL_NON_IDENTIFYING);
        canvas.addElement(r);
    }
}
