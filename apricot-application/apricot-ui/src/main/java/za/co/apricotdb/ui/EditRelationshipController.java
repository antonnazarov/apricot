package za.co.apricotdb.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.ui.handler.NonTransactionalPort;
import za.co.apricotdb.ui.model.EditRelationshipModel;
import za.co.apricotdb.ui.model.EditRelationshipModelBuilder;
import za.co.apricotdb.ui.model.ParentChildKeyHolder;

import javax.annotation.Resource;

/**
 * This controller serves the following interface form:
 * apricot-relationship-editor.fxml
 * 
 * @author Anton Nazarov
 * @since 14/03/2019
 */
@Component
public class EditRelationshipController {

    public static final String NEW_RELATIONSHIP = "<New Relationship>";

    @Resource
    ApplicationContext context;

    @Autowired
    EditRelationshipModelBuilder modelBuilder;

    @Autowired
    NonTransactionalPort port;

    @FXML
    Pane mainPane;

    @FXML
    TextField relationshipName;

    @FXML
    CheckBox autoRelated;

    @FXML
    TextField parentEntity;

    @FXML
    TextField childEntity;

    @FXML
    TextField primaryKeyField_1;
    @FXML
    TextField primaryKeyField_2;
    @FXML
    TextField primaryKeyField_3;
    @FXML
    TextField primaryKeyField_4;
    @FXML
    TextField primaryKeyField_5;

    @FXML
    ComboBox<String> childForeignKey_1;
    @FXML
    ComboBox<String> childForeignKey_2;
    @FXML
    ComboBox<String> childForeignKey_3;
    @FXML
    ComboBox<String> childForeignKey_4;
    @FXML
    ComboBox<String> childForeignKey_5;

    @FXML
    CheckBox pk_1;
    @FXML
    CheckBox pk_2;
    @FXML
    CheckBox pk_3;
    @FXML
    CheckBox pk_4;
    @FXML
    CheckBox pk_5;

    @FXML
    CheckBox notNull_1;
    @FXML
    CheckBox notNull_2;
    @FXML
    CheckBox notNull_3;
    @FXML
    CheckBox notNull_4;
    @FXML
    CheckBox notNull_5;

    private EditRelationshipModel model;

    public void init(EditRelationshipModel model) {
        this.model = model;

        String rName;
        if (StringUtils.isNotEmpty(model.getRelationshipNameProperty().getValue())) {
            rName = model.getRelationshipNameProperty().getValue();
        } else {
            rName = NEW_RELATIONSHIP;
        }
        relationshipName.textProperty().bindBidirectional(model.getRelationshipNameProperty());
        relationshipName.setText(rName);

        parentEntity.setText(model.getParentTable().getName());
        childEntity.setText(model.getChildTable().getName());
        autoRelated.setSelected(model.isAutoRelationship());

        addKeyHolders(model);
        model.resetKeys();
    }

    /**
     * Initialize the controller for the existing relationships.
     */
    public void init(EditRelationshipModel model, ApricotRelationship relationship) {
        init(model);
    }

    public EditRelationshipModel getModel() {
        return model;
    }

    @FXML
    public void swap(ActionEvent event) {
        port.swapEntities(this);
    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void save(ActionEvent event) {
        if (port.saveRelationship(this)) {
            getStage().close();
        }
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void foreignKeyEdited(ActionEvent event) {
        ComboBox<String> source = (ComboBox<String>) event.getSource();

        port.handleForeignKeyChanged(model, source.getId(), source.getValue());
    }

    @FXML
    public void primaryKeyFlagChanged(ActionEvent event) {
        CheckBox source = (CheckBox) event.getSource();
        model.setPrimaryKeyFlag(source.getId(), source.isSelected());
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }

    private void addKeyHolders(EditRelationshipModel model) {
        ParentChildKeyHolder keyHolder = new ParentChildKeyHolder(primaryKeyField_1, childForeignKey_1, pk_1,
                notNull_1);
        model.addKey(keyHolder);
        keyHolder = new ParentChildKeyHolder(primaryKeyField_2, childForeignKey_2, pk_2, notNull_2);
        model.addKey(keyHolder);
        keyHolder = new ParentChildKeyHolder(primaryKeyField_3, childForeignKey_3, pk_3, notNull_3);
        model.addKey(keyHolder);
        keyHolder = new ParentChildKeyHolder(primaryKeyField_4, childForeignKey_4, pk_4, notNull_4);
        model.addKey(keyHolder);
        keyHolder = new ParentChildKeyHolder(primaryKeyField_5, childForeignKey_5, pk_5, notNull_5);
        model.addKey(keyHolder);
    }
}
