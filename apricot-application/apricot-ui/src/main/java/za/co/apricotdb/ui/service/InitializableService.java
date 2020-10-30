package za.co.apricotdb.ui.service;

/**
 * This interface has to be in=mplemented by all asynchronous services of Apricot, which need to initialize the ProgressBar form.
 *
 * @author Anton Nazarov
 * @since 30/10/2020
 */
public interface InitializableService {

    void init(String title, String headerText);
}
