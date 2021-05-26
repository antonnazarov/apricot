package za.co.apricotdb.ui.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.apricotdb.persistence.data.ApplicationParameterManager;
import za.co.apricotdb.persistence.entity.ApricotApplicationParameter;
import za.co.apricotdb.ui.model.ConnectionAppParameterModel;
import za.co.apricotdb.ui.model.ConnectionParametersModel;
import za.co.apricotdb.ui.util.GsonFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The handler of the DB connection parameters.
 * This component stores the parameters, successfully passed the connection test into the application for
 * re-usage.
 *
 * @author Anton Nazarov
 * @version 08/07/2020
 * @since unknown
 */
@Component
public class SqlServerParametersHandler implements ConnectionParametersHandler {

    @Autowired
    ApplicationParameterManager appParameterManager;

    /**
     * Save the given connection parameters of the successful connection.
     */
    @Override
    public void saveConnectionParameters(String dbType, String server, String port, String database, String schema,
                                         String user, String password, String serviceType, String tnsNamesOraPath) {

        ConnectionAppParameterModel model = getModel();
        ConnectionParametersModel connModel = new ConnectionParametersModel(dbType, server, port, database, schema,
                user, password, serviceType, tnsNamesOraPath);
        if (model.addConnection(connModel)) {
            //  the model was altered
            saveModel(model);
        }
    }

    /**
     * Retrieve the current version of ConnectionAppParameterModel stored in the database.
     */
    public ConnectionAppParameterModel getModel() {
        ConnectionAppParameterModel model = null;
        ApricotApplicationParameter param =
                appParameterManager.getParameterByName(ApplicationParameterManager.DATABASE_CONNECTIONS);
        if (param != null && StringUtils.isNotEmpty(param.getValue())) {
            Gson gson = GsonFactory.initGson();
            try {
                model = gson.fromJson(param.getValue(), ConnectionAppParameterModel.class);
            } catch (JsonSyntaxException e) {
                //  the value in the database is not recognizable
                model = new ConnectionAppParameterModel();
            }
        } else {
            model = new ConnectionAppParameterModel();
        }

        return model;
    }

    /**
     * Get the saved connections for the given database type.
     */
    public ConnectionAppParameterModel getModel(String dbType) {
        ConnectionAppParameterModel model = getModel();

        List<ConnectionParametersModel> filteredByDbType = new ArrayList<>();
        for (ConnectionParametersModel cm : model.getConnectionParameters()) {
            if (cm.getDbType().equals(dbType)) {
                filteredByDbType.add(cm);
            }
        }

        model.setConnectionParameters(filteredByDbType);
        return model;
    }

    private void saveModel(ConnectionAppParameterModel model) {
        if (model != null) {
            Gson gson = GsonFactory.initGson();
            String gModel = gson.toJson(model);
            appParameterManager.saveParameter(ApplicationParameterManager.DATABASE_CONNECTIONS, gModel);
        }
    }

    public Properties getLatestConnectionProperties() {
        ConnectionAppParameterModel model = getModel();
        if (model != null) {
            ConnectionParametersModel paramModel = model.getLatestSuccessfulConnection();
            if (paramModel != null) {
                return paramModel.getAsProperties();
            }
        }

        return null;
    }
}
