package org.tuiasi.engine.misc.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joml.Vector3f;

import java.io.IOException;

public class Vector3fSerializer extends StdSerializer<Vector3f> {

    public Vector3fSerializer() {
        this(null);
    }

    public Vector3fSerializer(Class<Vector3f> t) {
        super(t);
    }

    @Override
    public void serialize(Vector3f value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("x", value.x);
        gen.writeNumberField("y", value.y);
        gen.writeNumberField("z", value.z);
        gen.writeEndObject();
    }
}