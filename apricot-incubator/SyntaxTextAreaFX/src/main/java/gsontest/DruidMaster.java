package gsontest;

public class DruidMaster {

    private String druidName;
    private int druidNum;

    public DruidMaster(String druidName, int druidNum) {
        super();
        this.druidName = druidName;
        this.druidNum = druidNum;
    }

    public String getDruidName() {
        return druidName;
    }

    public int getDruidNum() {
        return druidNum;
    }

    @Override
    public String toString() {
        return "DruidMaster [druidName=" + druidName + ", druidNum=" + druidNum + "]";
    }
}
