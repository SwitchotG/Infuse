package org.infuse.plugin.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.Class.Ray.*;
import org.infuse.plugin.InfusePlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class MEConsumerComponent implements Component<ChunkStore> {

    public static final BuilderCodec<MEConsumerComponent> CODEC = BuilderCodec.builder(MEConsumerComponent.class, MEConsumerComponent::new)
            .append(new KeyedCodec<>("Ray Inputs Outputs :", new ArrayCodec<>(RayIO.CODEC, RayIO[]::new)),
                    (comp, rayIOs) -> comp.rayIOs = rayIOs,
                    comp -> comp.rayIOs)
            .add()
            .build();

    private final UUID blockId;

    private RayIO[] rayIOs;

    private transient boolean isDirty = true;

    private transient boolean isActivated = false;

    public MEConsumerComponent(){
        rayIOs = new RayIO[0];
        this.blockId = UUID.randomUUID();
    }

    public MEConsumerComponent(RayIO[] rayIOs, boolean isActivated){
        this.rayIOs = rayIOs;
        this.blockId = UUID.randomUUID();
        this.isActivated = isActivated;
    }
    public UUID getBlockId() {
        return blockId;
    }

    public RayIO[] getRayIOs() {
        return rayIOs;
    }

    public void setRayIOs(RayIO[] rayIOs) {
        this.rayIOs = rayIOs;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void markDirty() {
        isDirty = true;
    }

    public void clearDirty(){
        isDirty = false;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public static ComponentType<ChunkStore, MEConsumerComponent> getComponentType() {
        return InfusePlugin.get().getMEConsumerComponentType();
    }

    @Nonnull
    public MEConsumerComponent clone() {
        return new MEConsumerComponent(this.rayIOs, this.isActivated);
    }
}
