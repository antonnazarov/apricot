package za.co.apricotdb.ui.repository;

/**
 * This is the type of comparison operation which is relevant for the current
 * Repository comparison row.
 * 
 * @author Anton Nazarov
 * @since 18/04/2020
 */
public enum RepositoryEqualsType {
    NOT_EQUAL("not-equal-27.png"), EQUAL("equal-27.png");

    private String imageFile;

    RepositoryEqualsType(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getImageFile() {
        return imageFile;
    }
}
