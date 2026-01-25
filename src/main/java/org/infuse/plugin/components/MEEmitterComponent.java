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
            .build();

    private int resistance;

    private int cost;

    public MEEmitterComponent() {

    }

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

    public MEEmitterComponent(int resistance, int cost) {
        this.resistance = resistance;
        this.cost = cost;
    }

    public static ComponentType<ChunkStore, MEEmitterComponent> getComponentType() {
        return InfusePlugin.get().getMEEmitterComponentType();
    }

    @Nonnull
    public MEEmitterComponent clone() {
        MEEmitterComponent component = new MEEmitterComponent(this.resistance, this.cost);
        return component;
    }
}
