package org.infuse.plugin.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.infuse.plugin.Class.Emitter.EmitterMapData;
import org.infuse.plugin.Class.Emitter.EmitterStorage;
import org.infuse.plugin.Class.Ray.MEConsumerInputRay;
import org.infuse.plugin.Class.Ray.Ray;
import org.infuse.plugin.Class.Ray.RayDirection;
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.Utils.RayUtil;
import org.infuse.plugin.components.MEConsumerComponent;
import org.infuse.plugin.components.MEEmitterComponent;

import java.util.ArrayList;
import java.util.List;

public class MEConsumerSystem extends EntityTickingSystem<ChunkStore> {
    @Override
    public Query<ChunkStore> getQuery() {
        return Query.and(
                MEConsumerComponent.getComponentType()
        );
    }

    @Override
    public void tick(float dt, int index, ArchetypeChunk<ChunkStore> chunk,
                     Store<ChunkStore> store, CommandBuffer<ChunkStore> cmd) {
        var block = chunk.getReferenceTo(index);


        var component = store.getComponent(block, MEConsumerComponent.getComponentType());

        if(component != null){
            ArrayList<RayDirection> rayDirections = new ArrayList<>();
            boolean hasAllRay = true;

            for(int i = 0; i < component.getInputs().length ; i++){
                if(component.getInputs()[i].isRequired()){
                    rayDirections.add(component.getInputs()[i].getDirection());
                }
            }

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

                if (worldChunk != null) {

                    int chunkWorldX = worldChunk.getX() * 32;
                    int chunkWorldZ = worldChunk.getZ() * 32;

                    int x = chunkWorldX + localX;
                    int z = chunkWorldZ + localZ;

                    EmitterMapData data = EmitterStorage.get(x, y, z);

                    if(data != null){
                        int rotationXZ = world.getBlockRotationIndex(x, y, z)%4;
                        for (int i = 0; i < rayDirections.toArray().length; i++) {
                            switch (getDirection(component, rayDirections.get(i).getValue(), rotationXZ)) {
                                case 4 -> hasAllRay = (data.URay != null);
                                case 12 -> hasAllRay = (data.DRay != null);
                                case 1 -> hasAllRay = (data.NRay != null);
                                case 2 -> hasAllRay = (data.ERay != null);
                                case 3 -> hasAllRay = (data.SRay != null);
                                case 0 -> hasAllRay = (data.WRay != null);
                            }
                            if(!hasAllRay){
                                break;
                            }
                        }
                        if(hasAllRay){
                            for(int i = 0; i < component.getReactions().length; i++){
                                component.getReactions()[i].react();
                            }

                            if(EmitterStorage.get(x, y, z) == null){
                                EmitterStorage.put(x, y, z, new EmitterMapData(component.getBlockId()));
                            }

                            if(Math.random() < 0.1) {
                                for (int i = 0; i < component.getOutputs().length; i++) {
                                    Ray ray = getRay(component, i, data, rotationXZ);

                                    if (ray == null) {
                                        break;
                                    }

                                    for (int j = 0; j < component.getOutputs()[i].getRayModification().getTypes().length; j++) {
                                        component.getOutputs()[i].getRayModification().getTypes()[j].modify(
                                                ray
                                                , component.getOutputs()[i].getRayModification().getModification()
                                        );
                                    }

                                    int strictDirection = component.getOutputs()[i].getDirection().getValue();
                                    int direction = getDirection(component, strictDirection, rotationXZ);

                                    if (store.isInThread() && !store.isShutdown()) {
                                        cmd.run(s ->
                                                {
                                                    RayUtil.castRay(ray, direction, new Vector3i(x, y, z), entityStore.getStore(), s.getExternalData().getWorld(), false, component.getBlockId());
                                                }
                                        );
                                    }
                                    data.SRay = null;
                                    data.WRay = null;
                                    data.NRay = null;
                                    data.DRay = null;
                                    data.ERay = null;
                                    data.URay = null;
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private static int getDirection(MEConsumerComponent component, int i, int rotationXZ) {

        int returnValue = i;

        if(returnValue == 12 || returnValue == 4){
            return returnValue;
        }

        if(rotationXZ == 1){
            switch (i) {
                case 1 -> returnValue = 2;
                case 2 -> returnValue = 3;
                case 3 -> returnValue = 0;
                case 0 -> returnValue = 1;
            }
        }else if(rotationXZ == 2){
            switch (i) {
                case 1 -> returnValue = 3;
                case 2 -> returnValue = 0;
                case 3 -> returnValue = 1;
                case 0 -> returnValue = 2;
            }
        }else if(rotationXZ == 3){
            switch (i) {
                case 1 -> returnValue = 0;
                case 2 -> returnValue = 1;
                case 3 -> returnValue = 2;
                case 0 -> returnValue = 3;
            }
        }
        return returnValue;
    }

    private static Ray getRay(MEConsumerComponent component, int i, EmitterMapData data, int rotationXZ) {
        MEConsumerInputRay inputRay = component.getInputs()[component.getOutputs()[i].getInputRay() - 1];
        Ray ray = null;



        if(rotationXZ == 0){
            switch (inputRay.getDirection()) {
                case Up -> ray = data.URay;
                case Down -> ray = data.DRay;
                case North -> ray = data.NRay;
                case East -> ray = data.ERay;
                case South -> ray = data.SRay;
                case West -> ray = data.WRay;
            }
        }else if(rotationXZ == 1){
            switch (inputRay.getDirection()) {
                case Up -> ray = data.URay;
                case Down -> ray = data.DRay;
                case North -> ray = data.ERay;
                case East -> ray = data.SRay;
                case South -> ray = data.WRay;
                case West -> ray = data.NRay;
            }
        }else if(rotationXZ == 2){
            switch (inputRay.getDirection()) {
                case Up -> ray = data.URay;
                case Down -> ray = data.DRay;
                case North -> ray = data.SRay;
                case East -> ray = data.WRay;
                case South -> ray = data.NRay;
                case West -> ray = data.ERay;
            }
        }else{
            switch (inputRay.getDirection()) {
                case Up -> ray = data.URay;
                case Down -> ray = data.DRay;
                case North -> ray = data.WRay;
                case East -> ray = data.NRay;
                case South -> ray = data.ERay;
                case West -> ray = data.SRay;
            }
        }

        return ray;
    }
}
