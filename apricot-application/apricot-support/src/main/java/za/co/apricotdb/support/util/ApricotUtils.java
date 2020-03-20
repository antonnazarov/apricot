package za.co.apricotdb.support.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The Apricot- utilities to be run as a standalone app.
 *
 * @author Anton Nazarov
 * @since 29/09/2018
 */
@Configuration
@ComponentScan(basePackages = "za.co.apricotdb")
@EnableAutoConfiguration
public class ApricotUtils {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("no arguments were found\n\n" + "Usage:\n"
                    + "za.co.apricotdb.support.util.ApricotUtils <arguments>\n" + "arguments:\n"
                    + "clean - to clean the Apricot project database\n" + "read - get the current database content\n"
                    + "scan - scan the target database\n");
        }

        SpringApplication.run(ApricotUtils.class, args);
    }

    /**
     * Convert the given file name (no extension should be provided) into a "good" name.  
     */
    public static String convertToFileName(String name) {
        String ret = name.replaceAll("\\W+", " ");
        ret = ret.trim().replaceAll(" +", " ");

        return ret;
    }
}
