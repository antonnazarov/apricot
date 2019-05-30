package za.co.apricotdb.ui.handler;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * An item of the Project Explorer which represents one Entity.
 * 
 * @author Anton Nazarov
 * @since 29/05/2019
 */
public class ProjectExplorerItem extends HBox {

    private String itemName = null;
    private ItemType itemType = null;
    private boolean included;
    private Label label = null;
    private ImageView img = null;

    public enum ItemType {
        PROJECT, ENTITY
    }

    public ProjectExplorerItem(String itemName, ItemType itemType, boolean included) {
        super();

        this.itemName = itemName;
        this.itemType = itemType;

        img = new ImageView();
        if (itemType == ItemType.PROJECT) {
            img.setImage(new Image(getClass().getResourceAsStream("project-2-s1.JPG")));
        }

        label = new Label(itemName);
        if (itemType == ItemType.PROJECT) {
            label.setPadding(new Insets(5, 0, 0, 5));
        } else {
            label.setPadding(new Insets(0, 0, 0, 5));
        }
        label.setPrefWidth(250);
        if (itemType == ItemType.PROJECT) {
            label.setStyle("-fx-font-weight: bold;");
        } else {
            setIncluded(included);
        }

        getChildren().addAll(img, label);
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
        if (included) {
            label.setStyle("-fx-font-weight: bold;");
            img.setImage(new Image(getClass().getResourceAsStream("table-small-black.jpg")));
        } else {
            label.setStyle("-fx-font-weight: normal;");
            img.setImage(new Image(getClass().getResourceAsStream("table-small-gray.jpg")));
        }
    }

    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((itemName == null) ? 0 : itemName.hashCode());
        result = prime * result + ((itemType == null) ? 0 : itemType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProjectExplorerItem other = (ProjectExplorerItem) obj;
        if (itemName == null) {
            if (other.itemName != null)
                return false;
        } else if (!itemName.equals(other.itemName))
            return false;
        if (itemType != other.itemType)
            return false;
        return true;
    }
}
