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
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.Utils.RayUtil;
import org.infuse.plugin.components.MEEmitterComponent;

public class EmiteBreakSystem extends EntityEventSystem<EntityStore, BreakBlockEvent> {
    public EmiteBreakSystem() {
        super(BreakBlockEvent.class);
    }

    @Override
    public void handle(int i, @NonNullDecl ArchetypeChunk<EntityStore> chunk, @NonNullDecl Store<EntityStore> store,
                       @NonNullDecl CommandBuffer<EntityStore> buffer, @NonNullDecl BreakBlockEvent event) {

        var world = store.getExternalData().getWorld();

        var x = event.getTargetBlock().getX();
        var y = event.getTargetBlock().getY();
        var z = event.getTargetBlock().getZ();

        var holder = world.getBlockComponentHolder(x, y, z);

        if(holder != null){
            var component = holder.getComponent(MEEmitterComponent.getComponentType());
            if(component != null){
                var rotation = world.getBlockRotationIndex(x, y, z);
                RayUtil.destroyRay(x, y, z, component, rotation);
            }
        }

    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
}
