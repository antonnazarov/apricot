package za.co.apricotdb.viewport.notification;

/**
 * Set the current value of progress of the Progress Bar.
 *
 * @author Anton Nazarov
 * @since 14/05/2020
 */
public class SetProgressBarEvent extends ProgressBarEvent {

    private static final long serialVersionUID = 2845969437285449305L;

    public SetProgressBarEvent(Double progress) {
        super(progress);
    }

    public Double getProgress() {
        return (Double) getSource();
    }
}
