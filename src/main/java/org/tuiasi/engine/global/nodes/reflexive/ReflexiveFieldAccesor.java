package org.tuiasi.engine.global.nodes.reflexive;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Data
public class ReflexiveFieldAccesor {
    private String fieldName;

    private Field field;
    private Object obj;

    private Method getter;
    private Method setter;

    public ReflexiveFieldAccesor(Field field, Object obj,  Method getter, Method setter) {
        this.fieldName = field.getName();
        this.field = field;
        this.obj = obj;
        this.getter = getter;
        this.setter = setter;
    }

    public void setValue(Object value) {
        // set the value of the field in the object to the value
        try {
            setter.invoke(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getValue() {
        // get the value of the field in the object
        try {
            return getter.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
