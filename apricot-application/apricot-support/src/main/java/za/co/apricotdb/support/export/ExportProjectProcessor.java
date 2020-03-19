package za.co.apricotdb.support.export;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
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

    Logger logger = LoggerFactory.getLogger(ExportProjectProcessor.class);

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
        String jsonProject = gson.toJson(project);

        ProjectHolder ph = new ProjectHolder();
        ph.setJsonProject(jsonProject);
        ph.setRelationships(relationshipSerializer.getRelationships(project));
        ph.setConstraintColumns(constraintColumnSerializer.getColumnConstraints(project));

        sb.append(gson.toJson(ph));

        try {
            File file = new File("C:/Anton Nazarov/tmp/project-export.txt");
            FileUtils.write(file, sb.toString(), Charset.defaultCharset());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        logger.info("The serialized Project: " + project.getName() + "\n" + sb.toString());

        return sb.toString();
    }

    private Gson initGson() {
        ExclusionStrategy strategy = new ApricotExclusionStrategy();
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(strategy);
        // builder.setPrettyPrinting();

        return builder.create();
    }
}
