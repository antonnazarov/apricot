package gsontest;

public class DruidMaster {

    private String druidName;
    private int druidNum;
    private DruidHolder holder;

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

    public DruidHolder getHolder() {
        return holder;
    }

    public void setHolder(DruidHolder holder) {
        this.holder = holder;
    }

    @Override
    public String toString() {
        return "DruidMaster [druidName=" + druidName + ", druidNum=" + druidNum + "]";
    }
}
