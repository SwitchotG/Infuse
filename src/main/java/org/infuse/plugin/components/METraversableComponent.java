package org.infuse.plugin.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.InfusePlugin;

import javax.annotation.Nullable;

public class METraversableComponent implements Component<ChunkStore> {
    public static final BuilderCodec<METraversableComponent> CODEC = BuilderCodec.builder(METraversableComponent.class, METraversableComponent::new)
            .append(new KeyedCodec<>("StoppingPower", Codec.INTEGER),
                    (comp, stoppingPower) -> comp.stoppingPower = stoppingPower,
                    comp -> comp.stoppingPower)
            .add()
            .build();


    private int stoppingPower;

    public METraversableComponent() {
    }

    public METraversableComponent(int stoppingPower) {
        this.stoppingPower = stoppingPower;
    }

    public int getStoppingPower() {
        return stoppingPower;
    }

    public void setStoppingPower(int stoppingPower) {
        this.stoppingPower = stoppingPower;
    }

    public static ComponentType<ChunkStore, METraversableComponent> getComponentType() {
        return InfusePlugin.get().getMETraversableComponentType();
    }

    @Nullable
    @Override
    public METraversableComponent clone() {
        return new METraversableComponent(this.stoppingPower);
    }
}
