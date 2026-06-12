package org.infuse.plugin.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.component.system.tick.TickableSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.Class.Ray.RayMapData;
import org.infuse.plugin.Class.Ray.RayStorage;
import org.infuse.plugin.Class.Ray.RayType;
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.components.MEEmitterComponent;
import org.joml.Vector3d;
import org.joml.Vector3i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RayParticleSystem extends EntityTickingSystem<ChunkStore> {

    private int tickCounter = 0;

    private String getParticleName(RayType rayType) {
        if (rayType == null) return "Mana_Small_Explosion";
        return switch (rayType) {
            case Earth -> "Mana_Earth_Small_Explosion";
            case Fire -> "Mana_Fire_Small_Explosion";
            case Lightning -> "Mana_Lightning_Small_Explosion";
            case Void -> "Mana_Void_Small_Explosion";
            case Water -> "Mana_Water_Small_Explosion";
            case Wind -> "Mana_Wind_Small_Explosion";
            default -> "Mana_Small_Explosion";
        };
    }

    @Override
    public void tick(float v, int i, @Nonnull ArchetypeChunk<ChunkStore> archetypeChunk, @Nonnull Store<ChunkStore> store, @Nonnull CommandBuffer<ChunkStore> commandBuffer) {
        tickCounter++;
        if (tickCounter < 5) return;
        tickCounter = 0;

        World world = store.getExternalData().getWorld();
        var entityStore = world.getEntityStore();

        for (var chunkEntry : RayStorage.DATA.entrySet()) {
            long chunkId = chunkEntry.getKey();
            for (var blockEntry : chunkEntry.getValue().entrySet()) {
                RayMapData ray = blockEntry.getValue();
                int blockIndex = blockEntry.getKey();

                int chunkX = ChunkUtil.xOfChunkIndex(chunkId);
                int chunkZ = ChunkUtil.zOfChunkIndex(chunkId);
                int localX = ChunkUtil.xFromBlockInColumn(blockIndex);
                int localY = ChunkUtil.yFromBlockInColumn(blockIndex);
                int localZ = ChunkUtil.zFromBlockInColumn(blockIndex);

                int worldX = chunkX * 32 + localX;
                int worldZ = chunkZ * 32 + localZ;

                String particleName = getParticleName(ray.getType());
                ParticleUtil.spawnParticleEffect(
                        particleName,
                        new Vector3d(worldX + 0.5, localY, worldZ + 0.5),
                        entityStore.getStore()
                );
            }
        }
    }

    @Nullable
    @Override
    public Query<ChunkStore> getQuery() {
        return Query.and(
                MEEmitterComponent.getComponentType()
        );
    }
}
