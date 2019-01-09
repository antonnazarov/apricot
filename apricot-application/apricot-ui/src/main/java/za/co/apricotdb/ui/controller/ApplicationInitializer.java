package za.co.apricotdb.ui.controller;

import org.springframework.stereotype.Component;

/**
 * This controller is responsible for initialization of the application on startup.
 * 
 * @author Anton Nazarov
 * @since 09/01/2019
 */
@Component
public class ApplicationInitializer {
    
    public void initialize() {
        System.out.println("The application was started");
    }
}
