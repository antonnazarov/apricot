package za.co.apricotdb.ui;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import za.co.apricotdb.ui.model.EditRelationshipModel;

/**
 * This controller serves the following interface form:
 * apricot-relationship-editor.fxml
 * 
 * @author Anton Nazarov
 * @since 14/03/2019
 */
@Component
public class EditRelationshipController {

    @Resource
    ApplicationContext context;

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

    public void init(EditRelationshipModel model) {
        childForeignKey_1.getItems().clear();
        List<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");
        items.add("three");
        childForeignKey_1.getItems().addAll(items);
    }

    @FXML
    public void swap(ActionEvent event) {

    }

    @FXML
    public void cancel(ActionEvent event) {
        getStage().close();
    }

    @FXML
    public void save(ActionEvent event) {

    }
    
    @FXML
    public void foreignKeyEdited(ActionEvent event) {
        ComboBox<String> cb = (ComboBox<String>)event.getSource();
        System.out.println("Event::: " + event.toString() + "  " + ((ComboBox)event.getSource()).getId() + " value=[" + cb.getValue() + "]");
    }

    private Stage getStage() {
        return (Stage) mainPane.getScene().getWindow();
    }
}
