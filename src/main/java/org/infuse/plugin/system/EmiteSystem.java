package org.infuse.plugin.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.Utils.RayUtil;
import org.infuse.plugin.components.MEEmitterComponent;

public class EmiteSystem extends EntityTickingSystem<ChunkStore> {

    @Override
    public Query<ChunkStore> getQuery() {
        return Query.and(
                MEEmitterComponent.getComponentType()
        );
    }

    @Override
    public void tick(float dt, int index, ArchetypeChunk<ChunkStore> chunk,
                     Store<ChunkStore> store, CommandBuffer<ChunkStore> cmd) {
        if(Math.random() < 0.1){
            var block = chunk.getReferenceTo(index);


            var component = store.getComponent(block, MEEmitterComponent.getComponentType());


            if(component != null){

                var world = store.getExternalData().getWorld();

                var entityStore = world.getEntityStore();

                BlockModule.BlockStateInfo info =
                        (BlockModule.BlockStateInfo) cmd.getComponent(block, BlockModule.BlockStateInfo.getComponentType());

                if(info != null) {
                    int localX = ChunkUtil.xFromBlockInColumn(info.getIndex());
                    int y = ChunkUtil.yFromBlockInColumn(info.getIndex());
                    int localZ = ChunkUtil.zFromBlockInColumn(info.getIndex());

                    WorldChunk worldChunk =
                            (WorldChunk) store.getComponent(info.getChunkRef(), WorldChunk.getComponentType());

                    if(worldChunk != null){

                        int chunkWorldX = worldChunk.getX() * 32;
                        int chunkWorldZ = worldChunk.getZ() * 32;

                        int x = chunkWorldX + localX;
                        int z = chunkWorldZ + localZ;

                        if(component.isCreative()){
                            if(store.isInThread() && !store.isShutdown()){
                                cmd.run( s ->
                                        {
                                                RayUtil.castRay(component.getResistance(),world.getBlockRotationIndex(x, y, z),new Vector3i(x, y, z),entityStore.getStore(),s.getExternalData().getWorld(), false, component.getBlockId());
                                        }
                                );
                            }
                        }
                    }
                }
            }
        }
    }

}