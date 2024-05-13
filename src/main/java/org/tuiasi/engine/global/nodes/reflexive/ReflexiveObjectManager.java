package org.tuiasi.engine.global.nodes.reflexive;

import org.tuiasi.engine.global.nodes.EditorVisible;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflexiveObjectManager {

    private Map<String, ReflexiveFieldAccesor> rfas = new HashMap<String, ReflexiveFieldAccesor>();

    public ReflexiveObjectManager(Object obj) {
        processFields(obj, "");
    }

    private void processFields(Object obj, String prefix) {
        List<Field> fields = List.of(obj.getClass().getDeclaredFields());

        Class objClass = obj.getClass();
        while(objClass.getSuperclass() != null && !objClass.getSuperclass().getName().equals("Object")) {
            fields = new ArrayList<>(fields);
            fields.addAll(List.of(objClass.getSuperclass().getDeclaredFields()));

            objClass = objClass.getSuperclass();
        }

        for (Field field : fields) {
            if (!field.isAnnotationPresent(EditorVisible.class)) {
                continue;
            }

            String fieldName = field.getName();

            Method getter = null;
            try {
                getter = obj.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            } catch (Exception e) {
                // ignore
            }

            Method setter = null;
            try {
                setter = obj.getClass().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), field.getType());
            } catch (Exception e) {
                // ignore
            }

            ReflexiveFieldAccesor rfa = new ReflexiveFieldAccesor(field, obj, getter, setter);

            rfas.put(prefix + fieldName, rfa);

            // If the field is a user-defined class, recursively process its fields
            if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.")) {
                try {
                    field.setAccessible(true);
                    Object nestedObj = field.get(obj);
                    if (nestedObj != null) {
                        processFields(nestedObj, prefix + fieldName + " ");
                        // After processing nested fields, check if any were added to the rfas map
                        // If so, remove the entry for the current field
                        if (rfas.keySet().stream().anyMatch(key -> key.startsWith(prefix + fieldName + " "))) {
                            rfas.remove(prefix + fieldName);
                        }
                    }
                } catch (IllegalAccessException e) {
                    // ignore
                }
            }

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
