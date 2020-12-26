package za.co.apricotdb.ui.handler;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.RelationshipManager;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.error.ApricotErrorLogger;
import za.co.apricotdb.viewport.relationship.ApricotRelationship;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * The handler/builder of the Context Menu of the Relationship.
 */
@Component
public class RelationshipContextMenuHandler {

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    DeleteSelectedHandler deleteSelectedHandler;

    @Autowired
    NonTransactionalPort nonTransactionalPort;

    @Autowired
    RelationshipManager relationshipManager;

    @ApricotErrorLogger(title = "Unable to create the Relationship context menu")
    public void createRelationshipContextMenu(ApricotRelationship relationship, double x, double y) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(buildMenuHeader(relationship), new SeparatorMenuItem(),
                buildEditRelationshipItem(relationship), buildDeleteRelationshipItem(relationship));

        contextMenu.setAutoHide(true);
        contextMenu.show(parentWindow.getWindow(), x, y);
    }

    private za.co.apricotdb.persistence.entity.ApricotRelationship getRelationshipEntity(ApricotRelationship relationship) {
        return relationshipManager.findRelationshipById(relationship.getRelationshipId());
    }

    public CustomMenuItem buildMenuHeader(ApricotRelationship relationship) {
        CustomMenuItem item = new CustomMenuItem();
        String parent = relationship.getParent().getTableName();
        String child = relationship.getChild().getTableName();
        String text;
        if (!parent.equals(child)) {
            text = "From: " + parent + "\nTo: " + child;
        } else {
            text = parent + " (auto)";
        }
        text += "\nConstraint: " + relationship.getConstraintName();
        Text txNode = new Text();
        if (!relationship.isValid()) {
            txNode.setFill(Color.RED);
            text += "\nthis relationship is invalid: \n" + relationship.getValidationMessage();
        }
        txNode.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
        txNode.setText(text);

        item.setContent(txNode);

        return item;
    }

    public MenuItem buildRelationshipInfoItem() {
        MenuItem item = new MenuItem("Relationship Info");
        item.setOnAction(e -> {

        });

        return item;
    }

    @Transactional
    public MenuItem buildEditRelationshipItem(ApricotRelationship relationship) {
        MenuItem item = new MenuItem("Edit Relationship");
        item.setOnAction(e -> {
            za.co.apricotdb.persistence.entity.ApricotRelationship r =
                    getRelationshipEntity(relationship);
            nonTransactionalPort.openRelationshipEditorForm(r);
        });

        return item;
    }

    public MenuItem buildDeleteRelationshipItem(ApricotRelationship relationship) {
        MenuItem item = new MenuItem("Delete Relationship");
        item.setOnAction(e -> {
            List<ApricotRelationship> list = new ArrayList<>();
            list.add(relationship);
            deleteSelectedHandler.deleteRelationships(list);
        });

        return item;
    }
}
