package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;

public class MEConsumerInputRay {
    public static final BuilderCodec<MEConsumerInputRay> CODEC = BuilderCodec.builder(MEConsumerInputRay.class, MEConsumerInputRay::new)
            .append(new KeyedCodec<>("Direction", RayDirection.CODEC),
                    (comp, direction) -> comp.direction = direction,
                    comp -> comp.direction)
            .add()
            .append(new KeyedCodec<>("Required", BuilderCodec.BOOLEAN),
                    (comp, required) -> comp.required = required,
                    comp -> comp.required)
            .add()
            .build();


    private RayDirection direction;

    private boolean required;

    public MEConsumerInputRay() {
        this.direction = RayDirection.North;
        this.required = true;
    }

    public MEConsumerInputRay(RayDirection direction, boolean required) {
        this.direction = direction;
        this.required = required;
    }

    public RayDirection getDirection() {
        return direction;
    }

    public void setDirection(RayDirection direction) {
        this.direction = direction;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
