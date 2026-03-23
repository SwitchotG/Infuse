package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.Codec;

import java.util.HashMap;
import java.util.Map;

public class ReactionRegistry {
    private static final Map<String, Codec<? extends IRayReaction>> ID_TO_CODEC = new HashMap<>();
    private static final Map<Class<?>, String> CLASS_TO_ID = new HashMap<>();

    public static <T extends IRayReaction> void register(
            String id,
            Class<T> clazz,
            Codec<T> codec
    ) {
        ID_TO_CODEC.put(id, codec);
        CLASS_TO_ID.put(clazz, id);
    }

    public static Codec<? extends IRayReaction> getCodec(String id) {
        return ID_TO_CODEC.get(id);
    }

    public static final Codec<IRayReaction> CODEC = new ReactionCodec();

    public static Map<String, Codec<? extends IRayReaction>> getAll() {
        return ID_TO_CODEC;
    }

    public static String getId(Class<?> clazz) {
        return CLASS_TO_ID.get(clazz);
    }
}
