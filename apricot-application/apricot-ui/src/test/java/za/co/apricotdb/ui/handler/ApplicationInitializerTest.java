package za.co.apricotdb.ui.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.ERDNotation;
import za.co.apricotdb.ui.ParentWindow;

/**
 * The unit tests for ApplicationInitializer.
 * 
 * @author Anton Nazarov
 * @since 08/06/2019
 */
public class ApplicationInitializerTest {
    ProjectManager pm = null;
    SnapshotManager sm = null;
    ParentWindow pw = new ParentWindow();

    @Before
    public void setUp() {
        pm = mock(ProjectManager.class);
        sm = mock(SnapshotManager.class);
        
    }

    @Test
    public void testInitializeDefault() {
        when(pm.findCurrentProject()).thenReturn(getTestProject());

    }

    private ApricotProject getTestProject() {
        ApricotProject p = new ApricotProject("test_project", "the project description", "H2", true,
                new java.util.Date(), new ArrayList<ApricotSnapshot>(), new ArrayList<ApricotProjectParameter>(),
                new ArrayList<ApricotView>(), ERDNotation.CROWS_FOOT);

        return p;
    }
}
