package com.evolgames.entities.serialization.serializers;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class FloatArraySerializer extends Serializer<float[]> {

    @Override
    public void write(Kryo kryo, Output output, float[] floats) {
        output.writeInt(floats.length); // Write the length of the array
        for (float f : floats) {
            output.writeFloat(f); // Write each float value
        }
    }

    @Override
    public float[] read(Kryo kryo, Input input, Class<? extends float[]> type) {
        int length = input.readInt(); // Read the length of the array
        float[] floats = new float[length];
        for (int i = 0; i < length; i++) {
            floats[i] = input.readFloat(); // Read each float value
        }
        return floats;
    }
}
