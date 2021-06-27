package za.co.apricotdb.persistence.data;

import za.co.apricotdb.persistence.entity.ApricotDatabaseView;
import za.co.apricotdb.persistence.entity.ApricotRelationship;
import za.co.apricotdb.persistence.entity.ApricotTable;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of the final scanned database structure.
 * 
 * @author Anton Nazarov
 * @since 26/09/2018
 */
public class MetaData {
    
    private List<ApricotTable> tables = new ArrayList<>();
    private List<ApricotRelationship> relationships = new ArrayList<>();
    private List<ApricotDatabaseView> views = new ArrayList<>();
    
    public void addTable(ApricotTable table) {
        tables.add(table);
    }
    
    public void addRelationship(ApricotRelationship relationship) {
        relationships.add(relationship);
    }

    public List<ApricotTable> getTables() {
        return tables;
    }

    public void setTables(List<ApricotTable> tables) {
        this.tables = tables;
    }

    public List<ApricotRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<ApricotRelationship> relationships) {
        this.relationships = relationships;
    }

    public List<ApricotDatabaseView> getViews() {
        return views;
    }

    public void setViews(List<ApricotDatabaseView> views) {
        this.views = views;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tables);
        sb.append(relationships);
        sb.append(views);

        return sb.toString();
    }
}
