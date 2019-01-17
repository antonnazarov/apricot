package za.co.apricotdb.viewport.canvas;

import java.util.Properties;

/**
 * The individual canvas allocation item.
 * 
 * @author Anton Nazarov
 * @since 17/01/2019
 */
public class CanvasAllocationItem {
    private String name;
    private ElementType type;
    private Properties properties;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ElementType getType() {
        return type;
    }
    
    public void setType(ElementType type) {
        this.type = type;
    }
    
    public Properties getProperties() {
        return properties;
    }
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    public String getPropertiesAsString() {
        // @TODO implement!
        return null;
    }
    
    public void setPropertiesFromString(String props) {
        // @TODO implement!
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof CanvasAllocationItem) {
            return this.name.equals(((CanvasAllocationItem)o).getName());
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("CanvasAllocationItem: ");
        sb.append("name=[").append(name).append("], ");
        sb.append("type=[").append(type).append("], ");
        sb.append("properties=[").append(properties).append("]");
        
        return sb.toString();
    }
}
