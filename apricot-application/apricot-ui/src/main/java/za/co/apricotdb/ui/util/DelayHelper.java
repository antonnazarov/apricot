package za.co.apricotdb.ui.util;

import java.util.concurrent.TimeUnit;

public class DelayHelper {

    public static void delay(long delay) {
        try {
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
