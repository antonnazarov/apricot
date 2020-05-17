package za.co.apricotdb.ui.repository;

import za.co.apricotdb.persistence.entity.ApricotProject;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * The holder of the Apricot Project repository item (the remote side).
 *
 * @author Anton Nazarov
 * @since 26/04/2020
 */
public class ProjectItem implements Serializable {

    private String projectName;
    private File file;
    private ApricotProject project;

    public ProjectItem(String projectName, File file, ApricotProject project) {
        this.projectName = projectName;
        this.file = file;
        this.project = project;
    }

    public String getProjectName() {
        return projectName;
    }

    public File getFile() {
        return file;
    }

    public ApricotProject getProject() {
        return project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectItem that = (ProjectItem) o;
        return projectName.equals(that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectName);
    }

    @Override
    public String toString() {
        return "ProjectItem{" +
                "projectName='" + projectName + '\'' +
                ", fileName='" + file.getName() + '\'' +
                ", project=" + project +
                '}';
    }
}
