package org.infuse.plugin.system;

import com.hypixel.hytale.component.Archetype;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.component.system.tick.TickableSystem;
import com.hypixel.hytale.server.core.modules.entity.system.EntitySystems;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.Class.Ray.RayStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class RayCleanupSystem implements TickableSystem<ChunkStore> {

    private int tickCounter = 0;

    @Override
    public void tick(float v, int i, @Nonnull Store<ChunkStore> store) {
        tickCounter++;
        if (tickCounter < 2400) return; // run every 2 minutes
        tickCounter = 0;

        World world = store.getExternalData().getWorld(); // get reference to the world
        for (Long chunkId : new ArrayList<>(RayStorage.DATA.keySet())) {
            RayStorage.clearChunk(chunkId);
        }
    }
}
