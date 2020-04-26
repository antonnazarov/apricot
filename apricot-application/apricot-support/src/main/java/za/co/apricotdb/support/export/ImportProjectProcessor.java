package za.co.apricotdb.support.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import za.co.apricotdb.persistence.entity.ApricotColumn;
import za.co.apricotdb.persistence.entity.ApricotColumnConstraint;
import za.co.apricotdb.persistence.entity.ApricotConstraint;
import za.co.apricotdb.persistence.entity.ApricotObjectLayout;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotTable;
import za.co.apricotdb.persistence.entity.ApricotView;

/**
 * This component imports the project from the file.
 * 
 * @author Anton Nazarov
 * @since 19/03/2020
 */
@Component
public class ImportProjectProcessor {

    Logger LOGGER = LoggerFactory.getLogger(ImportProjectProcessor.class);

    @Resource
    EntityManager em;

    public ApricotProject deserializeProject(String sProject) {
        String[] splt = parseProject(sProject);
        Gson gson = ExportProjectProcessor.initGson();
        ApricotProject project = gson.fromJson(splt[0], ApricotProject.class);
        if (project == null) {
            throw new IllegalArgumentException("Unable to import the Project: the main project part is empty");
        }

        return project;
    }

    @Transactional
    public ApricotProject importProject(String sProject, boolean serialize) {
        String[] splt = parseProject(sProject);

        ApricotProject project = deserializeProject(sProject);

        Gson gson = ExportProjectProcessor.initGson();
        ProjectHolder prjHolder = gson.fromJson(splt[1], ProjectHolder.class);
        if (prjHolder == null) {
            throw new IllegalArgumentException("Unable to import the Project: the links part are empty");
        }

        Map<Long, ApricotConstraint> constraintsMap = new HashMap<>();
        Map<Long, ApricotColumn> columnsMap = new HashMap<>();
        List<ApricotRelationship> relationships = new ArrayList<>();
        List<ApricotColumnConstraint> colConstraints = new ArrayList<>();
        project.setId(0);
        List<ApricotSnapshot> snapshots = project.getSnapshots();
        if (snapshots != null) {
            for (ApricotSnapshot snap : snapshots) {
                snap.setProject(project);
                snap.setId(0);

                List<ApricotTable> tables = snap.getTables();
                if (tables != null) {
                    for (ApricotTable table : tables) {
                        table.setSnapshot(snap);
                        table.setId(0);

                        List<ApricotConstraint> constraints = table.getConstraints();
                        if (constraints != null) {
                            for (ApricotConstraint c : constraints) {
                                c.setTable(table);
                                constraintsMap.put(c.getId(), c);
                            }
                        }

                        List<ApricotColumn> columns = table.getColumns();
                        if (columns != null) {
                            for (ApricotColumn col : columns) {
                                col.setTable(table);
                                columnsMap.put(col.getId(), col);
                            }
                        }
                    }
                }
            }
        }

        for (RelationshipHolder rh : prjHolder.getRelationships()) {
            ApricotConstraint parentConstr = constraintsMap.get(rh.getParentConstraintId());
            if (parentConstr == null) {
                throw new IllegalArgumentException(
                        "Unable to find the parent constraint with ID=" + rh.getParentConstraintId());
            }
            ApricotConstraint childConstr = constraintsMap.get(rh.getChildConstraintId());
            if (childConstr == null) {
                throw new IllegalArgumentException(
                        "Unable to find the child constraint with ID=" + rh.getChildConstraintId());
            }

            ApricotRelationship relationship = new ApricotRelationship(parentConstr, childConstr);
            relationships.add(relationship);
        }

        for (ConstraintColumnHolder cc : prjHolder.getConstraintColumns()) {
            ApricotConstraint constr = constraintsMap.get(cc.getConstraintId());
            if (constr == null) {
                throw new IllegalArgumentException("Unable to find the constraint with ID=" + cc.getConstraintId());
            }

            ApricotColumn clmn = columnsMap.get(cc.getColumnId());
            if (clmn == null) {
                throw new IllegalArgumentException("Unable to find the column with ID=" + cc.getColumnId());
            }

            ApricotColumnConstraint acc = new ApricotColumnConstraint(constr, clmn);
            acc.setOrdinalPosition(cc.getOrdinalPosition());
            colConstraints.add(acc);
        }

        for (long id : constraintsMap.keySet()) {
            ApricotConstraint constr = constraintsMap.get(id);
            constr.setId(0);
        }
        for (long id : columnsMap.keySet()) {
            ApricotColumn clnm = columnsMap.get(id);
            clnm.setId(0);
        }

        List<ApricotView> views = project.getViews();
        if (views != null) {
            for (ApricotView view : views) {
                view.setProject(project);
                view.setId(0);
                List<ApricotObjectLayout> layouts = view.getObjectLayouts();
                if (layouts != null) {
                    for (ApricotObjectLayout layout : layouts) {
                        layout.setView(view);
                        layout.setId(0);
                    }
                }
            }
        }

        if (serialize) {
            serializeProject(project, relationships, colConstraints);
        }
        
        return project;
    }

    private String[] parseProject(String sProject) {
        String[] splt = sProject.split(ExportProjectProcessor.PROJECT_DIVIDER);
        if (splt.length != 2) {
            throw new IllegalArgumentException("The import file has wrong structure");
        }

        return splt;
    }

    private void serializeProject(ApricotProject project, List<ApricotRelationship> relationships,
            List<ApricotColumnConstraint> colConstraints) {
        em.persist(project);
        for (ApricotRelationship r : relationships) {
            em.persist(r);
        }
        for (ApricotColumnConstraint acc : colConstraints) {
            em.persist(acc);
        }
    }
}
