package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.codecs.EnumCodec;

public enum RayType {
    Fire,
    Water,
    Lightning,
    Wind,
    Earth,
    Void;

    public static final Codec<RayType> CODEC =
            new EnumCodec<>(RayType.class);
}
