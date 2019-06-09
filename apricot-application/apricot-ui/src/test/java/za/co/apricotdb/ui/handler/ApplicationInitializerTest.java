package za.co.apricotdb.ui.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.isp.lpt.JfxTestRunner;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.persistence.data.ViewManager;
import za.co.apricotdb.persistence.entity.ApricotProject;
import za.co.apricotdb.persistence.entity.ApricotProjectParameter;
import za.co.apricotdb.persistence.entity.ApricotSnapshot;
import za.co.apricotdb.persistence.entity.ApricotView;
import za.co.apricotdb.persistence.entity.ERDNotation;
import za.co.apricotdb.ui.ApplicationData;
import za.co.apricotdb.ui.ParentWindow;

/**
 * The unit tests for ApplicationInitializer.
 * 
 * @author Anton Nazarov
 * @since 08/06/2019
 */
@RunWith(JfxTestRunner.class)
public class ApplicationInitializerTest {
    ApplicationInitializer appInitializer = null;
    ProjectManager pm = null;
    SnapshotManager sm = null;
    ParentWindow pw = null;
    TreeViewHandler tvh = null;
    ViewManager vm = null;
    ComboBox<String> cb = null;
    ApricotViewHandler vh = null;
    TreeView<ProjectExplorerItem> ptv = null;

    @Before
    public void setUp() {
        pm = mock(ProjectManager.class);
        sm = mock(SnapshotManager.class);
        pw = mock(ParentWindow.class);
        tvh = mock(TreeViewHandler.class);
        vm = mock(ViewManager.class);
        vh = mock(ApricotViewHandler.class);
        when(pm.findCurrentProject()).thenReturn(getTestProject());
        when(sm.getDefaultSnapshot(any(ApricotProject.class))).thenReturn(getTestSnapshot());

        VBox b1 = new VBox();
        HBox b2 = new HBox();
        b1.getChildren().add(b2);
        Pane parentPane = new Pane();
        parentPane.getChildren().add(b1);
        pw.setParentPane(parentPane);
        ApplicationData appData = new ApplicationData();
        cb = new ComboBox<>();
        cb.setUserData("snapshotCombo.selectSnapshot");
        when(pw.getApplicationData()).thenReturn(appData);
        when(pw.getSnapshotCombo()).thenReturn(cb);
        ApricotView v = new ApricotView();
        v.setGeneral(true);
        when(vm.getCurrentView(any(ApricotProject.class))).thenReturn(v);
        TabPane tp = new TabPane();
        when(pw.getProjectTabPane()).thenReturn(tp);
        List<ApricotView> ws = new ArrayList<>();
        ws.add(v);
        when(vh.getAllViews(any(ApricotProject.class))).thenReturn(ws);
        ptv = new TreeView<>();
        when(pw.getProjectTreeView()).thenReturn(ptv);

        appInitializer = new ApplicationInitializer();
        appInitializer.projectManager = pm;
        appInitializer.snapshotManager = sm;
        appInitializer.parentWindow = pw;
        appInitializer.treeViewHandler = tvh;
        appInitializer.viewManager = vm;
        appInitializer.viewHandler = vh;
    }

    @Test
    public void testInitializeDefault() {
        appInitializer.initializeDefault();
        assertFalse(pw.isEmptyEnv());
    }

    @Test
    public void testInitCombo() {
        cb.setUserData("test");
        appInitializer.initializeDefault();
        assertNotNull(cb.getUserData());
        assertEquals(cb.getUserData(), "AppInitialize");
    }

    @Test
    public void testInitializeEmptyEnvironment() {
        when(pm.findCurrentProject()).thenReturn(null);
        appInitializer.initializeDefault();
        assertNotNull(ptv.getRoot());
        TreeItem<ProjectExplorerItem> itm = ptv.getRoot();
        assertEquals(itm.getValue().getItemName(), "<No current Project>");
    }

    @Test
    public void testInitializeForProject() {
        appInitializer.initializeForProject(getTestProject());
        assertNull(ptv.getRoot());
    }

    private ApricotProject getTestProject() {
        ApricotProject p = new ApricotProject("test_project", "the project description", "H2", true,
                new java.util.Date(), new ArrayList<ApricotSnapshot>(), new ArrayList<ApricotProjectParameter>(),
                new ArrayList<ApricotView>(), ERDNotation.CROWS_FOOT);

        return p;
    }

    private ApricotSnapshot getTestSnapshot() {
        ApricotSnapshot snapshot = new ApricotSnapshot();
        snapshot.setName("test_snapshot");

        return snapshot;
    }
}
