package za.co.apricotdb.viewport.canvas;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
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
    private Properties properties = new Properties();
    
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
    
    /**
     * Save the allocation properties into a String  
     */
    public String getPropertiesAsString() {
        String ret = null;
        Writer w = new StringWriter();
        try {
            properties.store(w, "CanvasAllocationItem");
            ret = w.toString();
        } catch (IOException ex) {
            ret = "error: " + ex.getMessage();
        }

        return ret;
    }
    
    public void setPropertiesFromString(String props) {
        if (!props.startsWith("error:")) {
            Reader r = new StringReader(props);
            try {
                properties.load(r);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
