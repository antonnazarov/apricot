package za.co.apricotdb.ui.repository;

public enum RowType {
    PROJECT("proj-clean-27.png"), SNAPSHOT("snap-clean-27.png");

    private String imageFile;

    RowType(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getImageFile() {
        return imageFile;
    }
}
