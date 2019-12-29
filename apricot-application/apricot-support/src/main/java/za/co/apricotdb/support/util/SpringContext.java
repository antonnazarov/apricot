package za.co.apricotdb.support.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * This class provides the static (and fully functional) Spring context for the
 * classes which are not the Spring beans and defined outside of the Spring
 * Context.
 * 
 * @author Anton Nazarov
 * @since 29/12/2019
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * Returns the Spring managed bean instance of the given class type if it
     * exists. Returns null otherwise.
     */
    public static <T extends Object> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        // store ApplicationContext reference to access required beans later on
        SpringContext.context = context;
    }
}