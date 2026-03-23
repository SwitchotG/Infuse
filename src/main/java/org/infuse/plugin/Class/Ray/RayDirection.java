package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.codecs.EnumCodec;

public enum RayDirection {
    North(1),
    West(2),
    South(3),
    East(0),
    Up(4),
    Down(12);

    private final int value;

    RayDirection(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static final Codec<RayDirection> CODEC =
            new EnumCodec<>(RayDirection.class);
}
