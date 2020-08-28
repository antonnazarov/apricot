package za.co.apricotdb.ui.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.springframework.stereotype.Component;

/**
 * This component is mocking some background functionality.
 *
 * @author Anton Nazarov
 * @since 27/08/2020
 */
@Component
public class MockService extends Service<Boolean> {

    private StringProperty msg = new SimpleStringProperty(this, "msg");

    public void setMsg(String m) {
        msg.set(m);
    }

    public String getMsg() {
        return msg.getValue();
    }

    @Override
    protected Task<Boolean> createTask() {
        final String _msg = getMsg();
        return new Task<Boolean>() {

            @Override
            protected Boolean call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 99);
                    updateMessage("progress: " + i + " msg:" + _msg);
                    if (i % 10 == 0) {
                        System.out.println("progress: " + i);
                    }
                    Thread.sleep(100);
                }

                return true;
            }
        };
    }
}
