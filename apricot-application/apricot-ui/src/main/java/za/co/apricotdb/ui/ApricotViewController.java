package za.co.apricotdb.ui;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;

/**
 * This controller add new view.
 * 
 * @author Anton Nazarov
 * @since 23/01/2019
 *
 */
@Component
public class ApricotViewController {
    
    @FXML
    RadioButton initEmptyOption;

    @FXML
    RadioButton initSelectedOption;

    @FXML
    RadioButton initFromViewOption;
    
    @FXML
    ComboBox<String> fromViewList;
    
    @PostConstruct
    public void init() {
        
    }
}
