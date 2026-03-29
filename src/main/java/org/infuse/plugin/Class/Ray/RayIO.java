package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import org.infuse.plugin.components.MEEmitterComponent;

import java.util.UUID;

public class RayIO {
    public static final BuilderCodec<RayIO> CODEC = BuilderCodec.builder(RayIO.class, RayIO::new)
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

    public RayIO() {
        inputs = new MEConsumerInputRay[0];
        outputs = new MEConsumerOutputRay[0];
        reactions = new IRayReaction[0];
    }

    public RayIO(MEConsumerInputRay[] inputsNeeded, MEConsumerOutputRay[] outputs, IRayReaction[] reactions) {
        this.inputs = inputsNeeded;
        this.outputs = outputs;
        this.reactions = reactions;
    }

    private RayIO(MEConsumerInputRay[] inputsNeeded, MEConsumerOutputRay[] outputs, IRayReaction[] reactions, UUID blockId) {
        this.inputs = inputsNeeded;
        this.outputs = outputs;
        this.reactions = reactions;
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
}
