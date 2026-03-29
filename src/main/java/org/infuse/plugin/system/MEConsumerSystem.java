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
                        for(int i = 0; i < component.getRayIOs().length; i++){
                            ArrayList<RayDirection> rayDirections = new ArrayList<>();
                            boolean hasAllRay = true;

                            for(int j = 0; j < component.getRayIOs()[i].getInputs().length ; j++){
                                if(component.getRayIOs()[i].getInputs()[j].isRequired()){
                                    rayDirections.add(component.getRayIOs()[i].getInputs()[j].getDirection());
                                }
                            }

                            int rotationXZ = world.getBlockRotationIndex(x, y, z)%4;
                            for (int j = 0; j < rayDirections.toArray().length; j++) {
                                switch (getDirection(component, rayDirections.get(j).getValue(), rotationXZ)) {
                                    case 4 -> hasAllRay = (data.URay != null);
                                    case 12 -> hasAllRay = (data.DRay != null);
                                    case 1 -> hasAllRay = (data.NRay != null);
                                    case 2 -> hasAllRay = (data.WRay != null);
                                    case 3 -> hasAllRay = (data.SRay != null);
                                    case 0 -> hasAllRay = (data.ERay != null);
                                }
                                if(!hasAllRay){
                                    break;
                                }else{
                                    InfusePlugin.get().getLOGGER().atInfo().log("Input : " + getDirection(component, rayDirections.get(j).getValue(), rotationXZ));
                                }

                            }
                            if(hasAllRay) {
                                for (int j = 0; j < component.getRayIOs()[i].getReactions().length; j++) {
                                    component.getRayIOs()[i].getReactions()[j].react();
                                }

                                if (EmitterStorage.get(x, y, z) == null) {
                                    EmitterStorage.put(x, y, z, new EmitterMapData(component.getBlockId()));
                                }

                                if (Math.random() < 0.2) {
                                    for (int j = 0; j < component.getRayIOs()[i].getOutputs().length; j++) {
                                        Ray ray = getRay(component, i, j, data, rotationXZ);

                                        if (ray == null) {
                                            InfusePlugin.get().getLOGGER().atInfo().log("Le rayon est nulle...");
                                            break;
                                        }

                                        for (int k = 0; k < component.getRayIOs()[i].getOutputs()[k].getRayModification().getTypes().length; k++) {
                                            component.getRayIOs()[i].getOutputs()[j].getRayModification().getTypes()[k].modify(
                                                    ray
                                                    , component.getRayIOs()[i].getOutputs()[j].getRayModification().getModification()
                                            );
                                        }

                                        int strictDirection = component.getRayIOs()[i].getOutputs()[j].getDirection().getValue();
                                        int direction = getDirection(component, strictDirection, rotationXZ);

                                        InfusePlugin.get().getLOGGER().atInfo().log("Output : " + direction);
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

    private static Ray getRay(MEConsumerComponent component, int i, int j, EmitterMapData data, int rotationXZ) {
        if(component.getRayIOs()[i].getOutputs()[j].getInputRay() == 0){
            InfusePlugin.get().getLOGGER().atInfo().log("La valeur Input to copy ne peut pas être 0 !");
            return null;
        }
        MEConsumerInputRay inputRay = component.getRayIOs()[i].getInputs()[component.getRayIOs()[i].getOutputs()[j].getInputRay() - 1];
        Ray ray = null;

        InfusePlugin.get().getLOGGER().atInfo().log("Infos : " + rotationXZ + " Direction : " + inputRay.getDirection());


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
                case North -> ray = data.WRay;
                case East -> ray = data.NRay;
                case South -> ray = data.ERay;
                case West -> ray = data.SRay;
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
                case North -> ray = data.ERay;
                case East -> ray = data.SRay;
                case South -> ray = data.WRay;
                case West -> ray = data.NRay;
            }
        }

        return ray;
    }
}
