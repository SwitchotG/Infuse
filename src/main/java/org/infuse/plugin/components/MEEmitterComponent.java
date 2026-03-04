package org.infuse.plugin.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.infuse.plugin.InfusePlugin;

import javax.annotation.Nonnull;
import java.util.UUID;

public class MEEmitterComponent implements Component<ChunkStore> {

    public static final BuilderCodec<MEEmitterComponent> CODEC = BuilderCodec.builder(MEEmitterComponent.class, MEEmitterComponent::new)
            .append(new KeyedCodec<>("Resistance", Codec.INTEGER),
                    (comp, resistance) -> comp.resistance = resistance,
                    comp -> comp.resistance)
            .add()
            .append(new KeyedCodec<>("Cost", Codec.INTEGER),
                    (comp, cost) -> comp.cost = cost,
                    comp -> comp.cost)
            .add()
            .append(new KeyedCodec<>("Creative", Codec.BOOLEAN),
                    (comp, creative) -> comp.creative = creative,
                    comp -> comp.creative)
            .add()
            .build();

    private int resistance;

    private int cost;

    private boolean creative;

    private final UUID blockId;

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isCreative() {
        return creative;
    }

    public void setCreative(boolean creative) {
        this.creative = creative;
    }

    public UUID getBlockId() {
        return blockId;
    }

    public MEEmitterComponent() {
        this.blockId = UUID.randomUUID();
    }

    public MEEmitterComponent(int resistance, int cost, boolean isCreative) {
        this.resistance = resistance;
        this.cost = cost;
        this.creative = isCreative;
        this.blockId = UUID.randomUUID();
    }

    public static ComponentType<ChunkStore, MEEmitterComponent> getComponentType() {
        return InfusePlugin.get().getMEEmitterComponentType();
    }

    @Nonnull
    public MEEmitterComponent clone() {
        MEEmitterComponent component = new MEEmitterComponent(this.resistance, this.cost, this.creative);
        return component;
    }
}
