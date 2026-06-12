package org.infuse.plugin.system;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.infuse.plugin.Class.Emitter.EmitterMapData;
import org.infuse.plugin.Class.Emitter.EmitterStorage;
import org.infuse.plugin.Class.Ray.*;
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.Utils.RayUtil;
import org.infuse.plugin.components.MEConsumerComponent;
import org.infuse.plugin.components.MEEmitterComponent;
import org.joml.Vector3i;

import java.util.HashSet;
import java.util.UUID;
import java.util.Vector;

public class EmiteBreakSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {
    public EmiteBreakSystem() {
        super(BreakBlockEvent.class);
    }

    @Override
    public void handle(int i, @NonNullDecl ArchetypeChunk<EntityStore> chunk, @NonNullDecl Store<EntityStore> store,
                       @NonNullDecl CommandBuffer<EntityStore> buffer, @NonNullDecl BreakBlockEvent event) {

        var world = store.getExternalData().getWorld();

        var x = event.getTargetBlock().x();
        var y = event.getTargetBlock().y();
        var z = event.getTargetBlock().z();

        var holder = world.getBlockComponentHolder(x, y, z);

        if(holder != null){
            var component = holder.getComponent(MEEmitterComponent.getComponentType());
            var secondComponent = holder.getComponent(MEConsumerComponent.getComponentType());
            if(component != null){
                var rotation = world.getBlockRotationIndex(x, y, z);
                int resistance = component.getEmittedRay().getResistance();
                world.execute(() -> RayUtil.destroyRay(x, y, z, resistance, rotation, world, new HashSet<>()));
            }else{
                if(secondComponent != null){
                    int rotationXZ = 0;
                    if(world.getBlockRotationIndex(x, y, z) == 4 || world.getBlockRotationIndex(x, y, z) == 12){
                        rotationXZ = world.getBlockRotationIndex(x, y, z);
                    }else{
                        rotationXZ = world.getBlockRotationIndex(x, y, z)%4;
                    }

                    EmitterMapData emitterData = EmitterStorage.get(x, y, z);
                    if(emitterData != null){
                        UUID[] sources = {emitterData.URaySource, emitterData.DRaySource, emitterData.NRaySource,
                                emitterData.SRaySource, emitterData.ERaySource, emitterData.WRaySource};

                        for(UUID sourceUUID : sources){
                            if(sourceUUID == null) continue;
                            Vector3i emitterPos = EmitterStorage.getPosFromUUID(sourceUUID);
                            if(emitterPos == null) continue;
                            var emitterHolder = world.getBlockComponentHolder(emitterPos.x, emitterPos.y, emitterPos.z);
                            if(emitterHolder == null) continue;
                            var emitter = emitterHolder.getComponent(MEEmitterComponent.getComponentType());
                            if(emitter != null) emitter.markDirty();
                        }
                    }

                    EmitterStorage.remove(x, y, z, world);

                    final int finalRotationXZ = rotationXZ;
                    world.execute(() -> {
                        for(int j=0; j<secondComponent.getRayIOs().length; j++){
                            RayIO currentRayIO = secondComponent.getRayIOs()[j];
                            for(int k=0; k<currentRayIO.getOutputs().length; k++){
                                MEConsumerOutputRay currentOutput = currentRayIO.getOutputs()[k];
                                int strictDirection = currentOutput.getDirection().getValue();

                                int direction = RayUtil.getRayDirectionFromBlockRotation(strictDirection, finalRotationXZ);
                                secondComponent.setActivated(false);
                                RayUtil.destroyRay(x, y, z, currentRayIO.getLastFinalResistance(), direction, world, new HashSet<>());
                            }
                        }
                    });
                }
            }
        }

    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
