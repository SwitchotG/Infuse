package org.infuse.plugin.components;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.Class.Ray.*;
import org.infuse.plugin.InfusePlugin;

import javax.annotation.Nullable;
import java.util.UUID;

public class MEConsumerComponent implements Component<ChunkStore> {

    public static final BuilderCodec<MEConsumerComponent> CODEC = BuilderCodec.builder(MEConsumerComponent.class, MEConsumerComponent::new)
            .append(new KeyedCodec<>("Inputs", new ArrayCodec<>(MEConsumerInputRay.CODEC, MEConsumerInputRay[]::new)),
                    (comp, inputsNeeded) -> comp.inputs = inputsNeeded,
                    comp -> comp.inputs)
            .add()
            .append(new KeyedCodec<>("Outputs", new ArrayCodec<>(MEConsumerOutputRay.CODEC, MEConsumerOutputRay[]::new)),
                    (comp, outputs) -> comp.outputs = outputs,
                    comp -> comp.outputs)
            .add()
            .append(new KeyedCodec<>("Reactions", new ArrayCodec<>(ReactionRegistry.CODEC, IRayReaction[]::new)),
                    (comp, reactions) -> comp.reactions = reactions,
                    comp -> comp.reactions)
            .add()
            .build();

    private MEConsumerInputRay[] inputs;

    private MEConsumerOutputRay[] outputs;

    private IRayReaction[] reactions;

    private final UUID blockId;

    public MEConsumerComponent() {
        this.blockId = UUID.randomUUID();
        inputs = new MEConsumerInputRay[0];
        outputs = new MEConsumerOutputRay[0];
        reactions = new IRayReaction[0];
    }

    public MEConsumerComponent(MEConsumerInputRay[] inputsNeeded, MEConsumerOutputRay[] outputs, IRayReaction[] reactions) {
        this.inputs = inputsNeeded;
        this.outputs = outputs;
        this.reactions = reactions;
        this.blockId = UUID.randomUUID();
    }

    private MEConsumerComponent(MEConsumerInputRay[] inputsNeeded, MEConsumerOutputRay[] outputs, IRayReaction[] reactions, UUID blockId) {
        this.inputs = inputsNeeded;
        this.outputs = outputs;
        this.reactions = reactions;
        this.blockId = blockId;
    }

    public UUID getBlockId() {
        return blockId;
    }

    public MEConsumerInputRay[] getInputs() {
        return inputs;
    }

    public void setInputs(MEConsumerInputRay[] inputs) {
        this.inputs = inputs;
    }

    public MEConsumerOutputRay[] getOutputs() {
        return outputs;
    }

    public void setOutputs(MEConsumerOutputRay[] outputs) {
        this.outputs = outputs;
    }

    public IRayReaction[] getReactions() {
        return reactions;
    }

    public void setReactions(IRayReaction[] reactions) {
        this.reactions = reactions;
    }

    public static ComponentType<ChunkStore, MEConsumerComponent> getComponentType() {
        return InfusePlugin.get().getMEConsumerComponentType();
    }

    @Nullable
    @Override
    public Component<ChunkStore> clone() {
        return new MEConsumerComponent(this.inputs, this.outputs, this.reactions, this.blockId);
    }
}
