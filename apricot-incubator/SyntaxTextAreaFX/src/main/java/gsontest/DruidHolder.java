package gsontest;

import java.util.ArrayList;
import java.util.List;

public class DruidHolder {
    
    private String holderId;
    private java.util.Date hDate;
    private List<DruidMaster> masters;
    
    public DruidHolder(String holderId) {
        this.holderId = holderId;
        this.hDate = new java.util.Date();
        this.masters = new ArrayList<>();
    }
    
    public String getHolderId() {
        return holderId;
    }
    public void setHolderId(String holderId) {
        this.holderId = holderId;
    }
    public java.util.Date gethDate() {
        return hDate;
    }
    public void sethDate(java.util.Date hDate) {
        this.hDate = hDate;
    }
    public List<DruidMaster> getMasters() {
        return masters;
    }

    @Override
    public String toString() {
        return "DruidHolder [holderId=" + holderId + ", hDate=" + hDate + ", masters=" + masters + "]";
    }
}
