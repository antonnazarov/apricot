package za.co.apricotdb.support.util;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import za.co.apricotdb.persistence.data.DataReader;
import za.co.apricotdb.persistence.data.MetaData;

/**
 * The command line utility which reads the current repository data.
 *
 * @author Anton Nazarov
 * @since 05/10/2018
 */
@Component
public class CmdMetaReader implements CommandLineRunner {
    
    @Autowired
    DataReader dataReader;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        long snapshotId = -1;
        boolean isRead = false;
        for (String c : args) {
            if (c.equals("read")) {
                isRead = true;
            } else if (c.startsWith("snapshot=")) {
                snapshotId = Long.parseLong(c.substring(9));
            }
        }
        
        if (isRead && snapshotId != -1) {
            List<String> tbls = new ArrayList<>();
            tbls.add("*");
            MetaData metaData = dataReader.readTablesByList(tbls, snapshotId);
            System.out.println(metaData.toString());
        }

    }
}
