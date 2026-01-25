package org.infuse.plugin.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.InfusePlugin;

import javax.annotation.Nullable;

public class METransformableComponent implements Component<ChunkStore> {
    public static final BuilderCodec<METransformableComponent> CODEC = BuilderCodec.builder(METransformableComponent.class, METransformableComponent::new)
            .append(new KeyedCodec<>("TransformationId", Codec.STRING),
                    (comp, blockId) -> comp.blockId = blockId,
                    comp -> comp.blockId)
            .add()
            .build();


    private String blockId;

    public METransformableComponent() {
    }

    public METransformableComponent(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public static ComponentType<ChunkStore, METransformableComponent> getComponentType() {
        return InfusePlugin.get().getMETransformableComponentType();
    }

    @Nullable
    @Override
    public METransformableComponent clone() {
        return new METransformableComponent(this.blockId);
    }
}
