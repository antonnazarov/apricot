package za.co.apricotdb.ui.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import za.co.apricotdb.ui.ParentWindow;
import za.co.apricotdb.ui.handler.ProgressBarHandler;
import za.co.apricotdb.viewport.notification.FinalizeProgressBarEvent;
import za.co.apricotdb.viewport.notification.InitProgressBarEvent;
import za.co.apricotdb.viewport.notification.ProgressBarEvent;
import za.co.apricotdb.viewport.notification.SetProgressBarEvent;

/**
 * A listener of the ProgressBar Events of all types.
 *
 * @author Anton Nazarov
 * @since 14/05/2020
 */
@Component
public class ProgressBarListener implements ApplicationListener<ProgressBarEvent> {

    @Autowired
    ParentWindow parentWindow;

    @Autowired
    ProgressBarHandler handler;

    @Override
    public void onApplicationEvent(ProgressBarEvent event) {
        if (event instanceof InitProgressBarEvent) {
            handler.initProgressBar();
        } else if (event instanceof SetProgressBarEvent) {
            SetProgressBarEvent e = (SetProgressBarEvent) event;
            handler.setProgress(e.getProgress());
        } else if (event instanceof FinalizeProgressBarEvent) {
            handler.finalizeProgressBar();
        }
    }
}
