package za.co.apricotdb.support.export;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.entity.ApricotProject;

/**
 * The Project- export functionality.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
@Component
public class ExportProjectProcessor {

    public static final String PROJECT_DIVIDER = "<-------------------------------------------------------->";
    private Logger logger = LoggerFactory.getLogger(ExportProjectProcessor.class);

    @Autowired
    ProjectManager projectManager;

    @Autowired
    RelationshipSerializer relationshipSerializer;

    @Autowired
    ConstraintColumnSerializer constraintColumnSerializer;

    @Transactional
    public String serializeProject() {
        StringBuilder sb = new StringBuilder();
        Gson gson = initGson();

        ApricotProject project = projectManager.findCurrentProject();
        sb.append(gson.toJson(project)).append(PROJECT_DIVIDER);

        ProjectHolder ph = new ProjectHolder();
        ph.setRelationships(relationshipSerializer.getRelationships(project));
        ph.setConstraintColumns(constraintColumnSerializer.getColumnConstraints(project));
        sb.append(gson.toJson(ph));

        logger.info("The Project " + project.getName() + " was successfully exported. Exported: " + sb.length()
                + " symbols");

        return sb.toString();
    }

    public static Gson initGson() {
        ExclusionStrategy strategy = new ApricotExclusionStrategy();
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(strategy);
        builder.setPrettyPrinting();

        return builder.create();
    }
}
