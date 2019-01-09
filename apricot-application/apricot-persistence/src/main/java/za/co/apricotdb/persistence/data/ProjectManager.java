package za.co.apricotdb.persistence.data;

import java.util.List;

import javax.annotation.Resource;

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
}
