package za.co.apricotdb.ui.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The holder of the collection of Project Items.
 *
 * @author Anton Nazarov
 * @since 26/04/2020
 */
public class ProjectItems {

    private final Map<String, ProjectItem> items = new HashMap<>();

    /**
     * Add the project item to the list.
     */
    public void add(ProjectItem item) {
        // check if item with the same name exists and substitute only of the file name is newer than the existing one
        ProjectItem itm = items.get(item.getProjectName());
        if (itm != null) {
            if (item.getFileName().compareTo(itm.getFileName()) > 0) {
                items.put(item.getProjectName(), item);
            }
        } else {
            items.put(item.getProjectName(), item);
        }
    }

    public List<ProjectItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public ProjectItem findProjectItem(String projectName) {
        return items.get(projectName);
    }

    @Override
    public String toString() {
        return "ProjectItems{" +
                "items=" + items +
                '}';
    }
}
