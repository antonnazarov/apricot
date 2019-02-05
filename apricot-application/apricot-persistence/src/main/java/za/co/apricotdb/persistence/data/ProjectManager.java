package za.co.apricotdb.persistence.data;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.repository.ApricotProjectRepository;

/**
 * The "PROJECT"- related persistence operations.
 * 
 * @author Anton Nazarov
 * @since 09/01/2019
 */
@Component
public class ProjectManager {

    @Resource
    private ApricotProjectRepository projectRepository;
    
    @Resource
    EntityManager em;

    public void setProjectCurrent(ApricotProject project) {
        List<ApricotProject> projects = projectRepository.findAll();
        for (ApricotProject p : projects) {
            p.setCurrent(false);
            projectRepository.save(p);
        }
        project.setCurrent(true);
        projectRepository.save(project);
    }

    public ApricotProject findCurrentProject() {
        ApricotProject ret = null;
        
        List<ApricotProject> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            return null;
        }
        
        List<ApricotProject> cp = projectRepository.findByCurrent(true);
        if (cp != null) {
            if (cp.size() == 1) {
                ret = cp.get(0);
            } else if (cp.isEmpty()) {
                ret = projects.get(0);
                setProjectCurrent(ret);
            } else {
                //  more that one current project - fix it
                ret = cp.get(0);
                setProjectCurrent(ret);
            }
        }

        return ret;
    }
    
    public List<ApricotProject> getAllProjects() {
        TypedQuery<ApricotProject> query = em.createNamedQuery("ApricotProject.getAllProjects", ApricotProject.class);
        return query.getResultList();
    }
    
    public ApricotProject getProjectByName(String name) {
        ApricotProject ret = null;
        
        TypedQuery<ApricotProject> query = em.createNamedQuery("ApricotProject.getProjectByName", ApricotProject.class);
        query.setParameter("name", name);
        
        List<ApricotProject> p = query.getResultList();
        if (p != null && p.size() > 0) {
            ret = p.get(0);
        }
        
        return ret;
    }
    
    public ApricotProject saveApricotProject(ApricotProject project) {
        return projectRepository.saveAndFlush(project);
    }
    
    public ApricotProject getProject(long projectId) {
        return projectRepository.findOne(projectId);
    }
}
