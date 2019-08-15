package za.co.apricotdb.metascan.oracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import za.co.apricotdb.metascan.MetaDataScannerBase;

public class DataLengthTest {

    @Test
    public void testRealLongDataType() {
        String dataType = calcDataType("TIMESTAMP(6) WITH LOCAL TIME ZONE");
        assertEquals("TIMESTAMP(6)", dataType);
        assertTrue(dataType.length() < MetaDataScannerBase.DATA_TYPE_LENGTH);
    }
    
    @Test
    public void testNonSplitDataType() {
        String dataType = calcDataType("TIMESTAMP(6)_WITH_LOCAL_TIME_ZONE");
        assertEquals("TIMESTAMP(6)_WITH_LOCAL_T", dataType);
        assertEquals(MetaDataScannerBase.DATA_TYPE_LENGTH, dataType.length());
    }
    
    @Test
    public void testNormalDataType() {
        String origDataType = "VARCHAR2";
        String calcDataType = calcDataType(origDataType);
        assertEquals(origDataType, calcDataType);
    }

    private String calcDataType(String dataType) {
        if (dataType.length() > MetaDataScannerBase.DATA_TYPE_LENGTH) {
            String[] split = dataType.split(" ");
            if (split != null && split.length > 0) {
                if (split[0].length() < MetaDataScannerBase.DATA_TYPE_LENGTH) {
                    dataType = split[0];
                } else {
                    dataType = dataType.substring(0, MetaDataScannerBase.DATA_TYPE_LENGTH);
                }
            }
        }

        return dataType;
    }
}
