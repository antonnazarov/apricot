package za.co.apricotdb.viewport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import za.co.apricotdb.viewport.canvas.ApricotCanvas;
import za.co.apricotdb.viewport.canvas.ApricotCanvasImpl;

/**
 * The view port- specific Spring configuration class.
 * 
 * @author Anton Nazarov
 * @since 11/01/2019
 */
@Configuration
public class ViewPortConfig {
    
    @Bean
    @Scope("prototype")
    public ApricotCanvas apricotCanvas() {
        return new ApricotCanvasImpl();
    }
}
