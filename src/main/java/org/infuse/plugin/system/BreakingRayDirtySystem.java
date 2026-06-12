package org.infuse.plugin.system;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.infuse.plugin.Class.Emitter.EmitterStorage;
import org.infuse.plugin.Class.Ray.RayMapData;
import org.infuse.plugin.Class.Ray.RayStorage;
import org.infuse.plugin.components.MEConsumerComponent;
import org.infuse.plugin.components.MEEmitterComponent;

import javax.annotation.Nullable;
import java.util.UUID;

public class BreakingRayDirtySystem extends EntityEventSystem<EntityStore, PlaceBlockEvent> {

    public BreakingRayDirtySystem() {
        super(PlaceBlockEvent.class);
    }

    @Override
    public void handle(int i, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store,
                       CommandBuffer<EntityStore> buffer, PlaceBlockEvent event) {
        var pos = event.getTargetBlock();

        RayMapData ray = RayStorage.get(pos.x(), pos.y(), pos.z());

        if (ray != null) {
            UUID emitterUUID = ray.getRay();

            UUID otherEmitterUUID = ray.getOtherRay();

            var emitterPos = EmitterStorage.getPosFromUUID(emitterUUID);
            var emitter2Pos = EmitterStorage.getPosFromUUID(otherEmitterUUID);
            if (emitterPos != null) {
                var holder = store.getExternalData().getWorld().getBlockComponentHolder(emitterPos.x, emitterPos.y, emitterPos.z);
                if (holder != null) {
                    var emitter = holder.getComponent(MEEmitterComponent.getComponentType());
                    if (emitter != null) emitter.markDirty();
                    var emitter2 = holder.getComponent(MEConsumerComponent.getComponentType());
                    if (emitter2 != null) emitter2.markDirty();
                }
            }

            if (emitter2Pos != null) {
                var holder = store.getExternalData().getWorld().getBlockComponentHolder(emitter2Pos.x, emitter2Pos.y, emitter2Pos.z);
                if (holder != null) {
                    var emitter = holder.getComponent(MEEmitterComponent.getComponentType());
                    if (emitter != null) emitter.markDirty();
                    var emitter2 = holder.getComponent(MEConsumerComponent.getComponentType());
                    if (emitter2 != null) emitter2.markDirty();
                }
            }
        }

    }

    @Nullable
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
