package com.evolgames.entities.serialization.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.evolgames.entities.blocks.Polarity;

public class PolaritySerializer extends Serializer<Polarity> {

    @Override
    public void write(Kryo kryo, Output output, Polarity polarity) {
        output.writeInt(polarity.ordinal()); // Write the ordinal value of the enum
    }

    @Override
    public Polarity read(Kryo kryo, Input input, Class<? extends Polarity> type) {
        return Polarity.values()[input.readInt()]; // Read the ordinal value and retrieve the enum
    }
}
