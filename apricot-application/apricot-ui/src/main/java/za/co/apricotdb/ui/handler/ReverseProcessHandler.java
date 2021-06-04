package za.co.apricotdb.ui.handler;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.metascan.ApricotTargetDatabase;
import za.co.apricotdb.metascan.MetaDataScannerFactory;
import za.co.apricotdb.metascan.oracle.OracleServiceType;
import za.co.apricotdb.persistence.data.ProjectManager;
import za.co.apricotdb.persistence.data.SnapshotManager;
import za.co.apricotdb.ui.error.ApricotErrorHandler;
import za.co.apricotdb.ui.service.ReverseEngineService;

import javax.transaction.Transactional;

/**
 * Test
 *
 * @author Anton Nazarov
 * @since 29/05/2021
 */
@Component
public class ReverseProcessHandler {

    @Autowired
    ReverseEngineHandler reverseEngineHandler;

    @Autowired
    MetaDataScannerFactory scannerFactory;

    @Autowired
    ReverseEngineService reverseEngineService;

    @Autowired
    SnapshotManager snapshotManager;

    @Autowired
    ProjectManager projectManager;

    @Autowired
    BlackListHandler blackListHandler;

    @Autowired
    ApricotErrorHandler errorHandler;

    @Transactional
    public void doReverseProcess(String server, String port, String database, String schema, String user,
                                 String password, ApricotTargetDatabase targetDb, boolean useWindowsUserFlag,
                                 OracleServiceType serviceType, String tnsNamesOraPath, Stage stage, String connectionSummary) {
        // check the connection firstly
        reverseEngineHandler.testConnection(server, port, database, schema, user, password, targetDb,
                useWindowsUserFlag, serviceType, tnsNamesOraPath);

        String driverClass = scannerFactory.getDriverClass(targetDb);
        String url = scannerFactory.getUrl(targetDb, server, port, database, useWindowsUserFlag, serviceType, tnsNamesOraPath);

        reverseEngineService.initService(targetDb, driverClass, url, schema, user, password, snapshotManager.getDefaultSnapshot());

        //  success
        reverseEngineService.setOnSucceeded(e -> {
            stage.close();
            String[] blackList = blackListHandler.getBlackListTables(projectManager.findCurrentProject());
            if (snapshotManager.isCurrentSnapshotEmpty()) {
                reverseEngineHandler.openScanResultForm(reverseEngineService.getValue(), blackList, connectionSummary);
            } else {
                //  the current snapshot contains Entities
                reverseEngineHandler.reverseInCurrentSnapshot(reverseEngineService.getValue(), blackList,
                        connectionSummary);
            }
        });

        //  failure
        reverseEngineService.setOnFailed(e -> {
            errorHandler.showErrorInfo("Unable to reverse engineer the target database", "ReverseEngineering Error", reverseEngineService.getException());
        });
        reverseEngineService.start();
    }
}
