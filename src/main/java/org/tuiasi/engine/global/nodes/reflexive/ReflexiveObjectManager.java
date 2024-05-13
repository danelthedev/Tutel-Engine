package org.tuiasi.engine.global.nodes.reflexive;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflexiveObjectManager {

    private Map<String, ReflexiveFieldAccesor> rfas = new HashMap<String, ReflexiveFieldAccesor>();

    public ReflexiveObjectManager(Object obj) {
        // get all the fields of the object
        for (Field field : obj.getClass().getDeclaredFields()) {
            // get the name of the field
            String fieldName = field.getName();

            // get the getter method
            Method getter = null;
            try {
                getter = obj.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            } catch (Exception e) {
                // ignore
            }

            // get the setter method
            Method setter = null;
            try {
                setter = obj.getClass().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
            } catch (Exception e) {
                // ignore
            }

            // create a new ReflexiveFieldAccesor
            ReflexiveFieldAccesor rfa = new ReflexiveFieldAccesor(field, obj, getter, setter);

            // add the ReflexiveFieldAccesor to the map
            rfas.put(fieldName, rfa);
        }
    }

    public void setValue(String fieldName, Object value) {
        // set the value of the field in the object to the value
        rfas.get(fieldName).setValue(value);
    }

    public Object getValue(String fieldName) {
        // get the value of the field in the object
        return rfas.get(fieldName).getValue();
    }

    public List<String> getFields(){
        return rfas.keySet().stream().toList();
    }

}
