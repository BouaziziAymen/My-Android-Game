package com.evolgames.dollmutilationgame.entities.serialization.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.Objects;

public class EnumSerializer<T extends Enum<T>> extends Serializer<T> {

    @Override
    public void write(Kryo kryo, Output output, T enumValue) {
        output.writeInt(enumValue.ordinal()); // Write the ordinal value of the enum
    }

    @Override
    public T read(Kryo kryo, Input input, Class<? extends T> type) {
        return Objects.requireNonNull(type.getEnumConstants())[input.readInt()]; // Read the ordinal value and retrieve the enum
    }
}
