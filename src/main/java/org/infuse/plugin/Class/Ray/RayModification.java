package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;

import java.sql.Array;

public class RayModification {

    public static final BuilderCodec<RayModification> CODEC = BuilderCodec.builder(RayModification.class, RayModification::new)
            .append(new KeyedCodec<>("Types", new ArrayCodec<RayModificationType>(RayModificationType.CODEC, RayModificationType[]::new)),
                    (comp, types) -> comp.types = types,
                    comp -> comp.types)
            .add()
            .append(new KeyedCodec<>("Modifications values", Ray.CODEC),
                    (comp, ray) -> comp.modification = ray,
                    comp -> comp.modification)
            .add()
            .build();

    private RayModificationType[] types;

    private Ray modification;

    public RayModification() {
        this.types = new RayModificationType[0];
        this.modification = new Ray();
    }

    public RayModification(Ray modification, RayModificationType[] type) {
        this.modification = modification;
        this.types = type;
    }

    public RayModificationType[] getTypes() {
        return types;
    }

    public void setTypes(RayModificationType[] types) {
        this.types = types;
    }

    public Ray getModification() {
        return modification;
    }

    public void setModification(Ray modification) {
        this.modification = modification;
    }
}
