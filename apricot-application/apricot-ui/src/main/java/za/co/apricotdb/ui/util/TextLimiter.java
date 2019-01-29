package za.co.apricotdb.ui.util;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;

/**
 * This is a workaround to limit length of the text field.
 * 
 * @author Anton Nazarov
 * @since 22/01/2019
 */
public class TextLimiter {

    public static void addTextLimiter(final TextInputControl tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue,
                    final String newValue) {
                if (tf != null && tf.getText() != null && tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}
