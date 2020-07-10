package za.co.apricotdb.ui.util;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

/**
 * This helper reads an image on the given resource name and
 * properly closes the resource.
 *
 * @author Anton Nazarov
 * @sinse 10/07/2020
 */
public class ImageHelper {

    /**
     * Get the image from Resource.
     */
    public static Image getImage(String name, Class clazz) {
        try (InputStream stream = clazz.getResourceAsStream(name)) {
            return new Image(stream);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
