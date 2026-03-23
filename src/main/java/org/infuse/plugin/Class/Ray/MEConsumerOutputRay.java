package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MEConsumerOutputRay {
    public static final BuilderCodec<MEConsumerOutputRay> CODEC = BuilderCodec.builder(MEConsumerOutputRay.class, MEConsumerOutputRay::new)
            .append(new KeyedCodec<>("Direction", RayDirection.CODEC),
                    (comp, direction) -> comp.direction = direction,
                    comp -> comp.direction)
            .add()
            .append(new KeyedCodec<>("Modification", RayModification.CODEC),
                    (comp, rayModification) -> comp.rayModification = rayModification,
                    comp -> comp.rayModification)
            .add()
            .append(new KeyedCodec<>("Input to copy and modify", BuilderCodec.INTEGER),
                    (comp, inputRay) -> comp.inputRay = inputRay,
                    comp -> comp.inputRay)
            .add()
            .append(new KeyedCodec<>("Is Activated", BuilderCodec.BOOLEAN),
                    (comp, activated) -> comp.activated = activated,
                    comp -> comp.activated)
            .add()
            .build();


    private RayDirection direction;

    private RayModification rayModification;

    private int inputRay;

    private boolean activated;

    public MEConsumerOutputRay() {
        this.direction = RayDirection.North;
        this.rayModification = new RayModification();
        this.inputRay = 0;
        this.activated = true;
    }

    public MEConsumerOutputRay(RayDirection direction, RayModification rayModification, int inputRay, boolean activated) {
        this.direction = direction;
        this.rayModification = rayModification;
        this.inputRay = inputRay;
        this.activated = activated;
    }

    public RayDirection getDirection() {
        return direction;
    }

    public void setDirection(RayDirection direction) {
        this.direction = direction;
    }

    public RayModification getRayModification() {
        return rayModification;
    }

    public void setRayModification(RayModification rayModification) {
        this.rayModification = rayModification;
    }

    public int getInputRay() {
        return inputRay;
    }

    public void setInputRay(int inputRay) {
        this.inputRay = inputRay;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
