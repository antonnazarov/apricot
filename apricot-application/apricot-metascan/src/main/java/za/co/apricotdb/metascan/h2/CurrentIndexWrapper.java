package za.co.apricotdb.metascan.h2;

import za.co.apricotdb.persistence.entity.ApricotConstraint;

/**
 * A non modifiable wrapper around the ApricotConstraint class.
 * 
 * @author Anton Nazarov
 * @since 07/04/2019
 *
 */
public class CurrentIndexWrapper {

    private ApricotConstraint currentIndex;

    public ApricotConstraint getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(ApricotConstraint currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getIndexName() {
        if (currentIndex != null) {
            return currentIndex.getName();
        }

        return "";
    }
}
