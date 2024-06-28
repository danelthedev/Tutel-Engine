package org.tuiasi.engine.global.nodes.reflective;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Data
public class ReflectiveFieldAccesor {
    private String fieldName;

    private Field field;
    private Object obj;

    private Method getter;
    private Method setter;

    public ReflectiveFieldAccesor(Field field, Object obj, Method getter, Method setter) {
        this.fieldName = field.getName();
        this.field = field;
        this.obj = obj;
        this.getter = getter;
        this.setter = setter;
    }

    public void setValue(Object value) {
        try {
            setter.invoke(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getValue() {
        try {
            return getter.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}