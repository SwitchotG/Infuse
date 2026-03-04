package org.infuse.plugin.Utils;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.infuse.plugin.Class.Ray.RayMapData;
import org.infuse.plugin.Class.Ray.RayStorage;
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.components.*;

import java.util.UUID;

public final class RayUtil {

    public static boolean castRay(int resistance, int rotation, Vector3i vector3i, Store<EntityStore> store, World world, boolean isDeleteMode, UUID uuid){

        int resistanceLeft = resistance;
        int blockLeft = resistance;
        int i = -1;
        boolean deleteMode = isDeleteMode;

        while(blockLeft > 0){
            i++;
            switch(rotation){
                case 0:
                    resistanceLeft = shootPartRay(world, new Vector3i(vector3i.x - 1 - i, vector3i.y, vector3i.z), resistanceLeft, store, deleteMode, uuid);
                    break;
                case 1:
                    resistanceLeft = shootPartRay(world, new Vector3i(vector3i.x, vector3i.y, vector3i.z + 1 + i), resistanceLeft, store, deleteMode, uuid);
                    break;
                case 2:
                    resistanceLeft = shootPartRay(world, new Vector3i(vector3i.x + 1 + i, vector3i.y, vector3i.z), resistanceLeft, store, deleteMode, uuid);
                    break;
                case 3:
                    resistanceLeft = shootPartRay(world, new Vector3i(vector3i.x, vector3i.y, vector3i.z - 1 - i), resistanceLeft, store, deleteMode, uuid);
                    break;
            }
            if(resistanceLeft <= 0){
                deleteMode = true;
            }
            blockLeft--;
        }

        return true;
    }

    public static int shootPartRay(World world, Vector3i vector3i, int resistanceLeft, Store<EntityStore> store, boolean deleteMode, UUID uuid){
        BlockType blockType = world.getBlockType(vector3i.x, vector3i.y, vector3i.z);
        int returnedResistance = resistanceLeft;
        Holder<ChunkStore> holder = world.getBlockComponentHolder(vector3i.x, vector3i.y, vector3i.z);
        int x = vector3i.x;
        int y = vector3i.y;
        int z = vector3i.z;
        RayMapData ray = RayStorage.get(x, y, z);

        if(deleteMode){
            if(ray != null){
                if(ray.canUnPropagateAs(uuid)){
                    RayStorage.remove(x, y, z);
                }else{
                    ray.deleteCollisionAs(uuid);
                }
            }
            return 0;
        }else{
            if(ray != null){
                if(!ray.canPropagate(uuid)){
                    ray.collideWith(uuid, resistanceLeft);
                    if(ray.needToUpdateCollision(uuid)){
                        ray.update(uuid, resistanceLeft);
                    }
                    return 0;
                }else{
                    if(ray.getOtherRay() != null){
                        returnedResistance -= (ray.getOtherResistance() - 1);
                    }
                }
            }
            if(blockType != null) {
                if (blockType.getId().equals("Empty")) {
                    returnedResistance--;
                }
            }
            if(holder != null){
                METransformableComponent transformableComponent = holder.getComponent(InfusePlugin.get().getMETransformableComponentType());

                if(transformableComponent != null){
                    world.setBlock(vector3i.x, vector3i.y, vector3i.z, transformableComponent.getBlockId());
                }

                METraversableComponent traversableComponent = holder.getComponent(InfusePlugin.get().getMETraversableComponentType());

                if(traversableComponent != null){
                    returnedResistance -= traversableComponent.getStoppingPower();
                }else{
                    return 0;
                }
            }else{
                if(blockType != null) {
                    if (!blockType.getId().equals("Empty")) {
                        return 0;
                    }
                }else{
                    return 0;
                }
            }
            if(ray != null){
                if(!ray.isPresent(uuid)){
                    ray.propagateAs(uuid, resistanceLeft);
                }else{
                    ray.update(uuid, resistanceLeft);
                }
            }else{
                RayStorage.put(x, y, z, new RayMapData(uuid, resistanceLeft));
            }
            ParticleUtil.spawnParticleEffect("Mana_Small_Explosion", new Vector3d(vector3i.x + 0.5, vector3i.y, vector3i.z + 0.5), store);
        }

        return returnedResistance;
    }

    public static void destroyRay(int x, int y, int z, MEEmitterComponent component, int rotation){
        for(int i=0; i < component.getResistance(); i++){
            switch(rotation){
                case 0:
                    RayMapData ray = RayStorage.get(x - 1 - i, y, z);
                    destroyPartRay(x - 1 - i, y, z, component.getBlockId(), ray);
                    break;
                case 1:
                    RayMapData ray2 = RayStorage.get(x, y, z + 1 + i);
                    destroyPartRay(x, y, z + 1 + i, component.getBlockId(), ray2);
                    break;
                case 2:
                    RayMapData ray3 = RayStorage.get(x + 1 + i, y, z);
                    destroyPartRay(x + 1 + i, y, z, component.getBlockId(), ray3);
                    break;
                case 3:
                    RayMapData ray4 = RayStorage.get(x, y, z - 1 - i);
                    destroyPartRay(x, y, z - 1 - i, component.getBlockId(), ray4);
                    break;
            }
        }
    }

    public static void destroyPartRay(int x, int y, int z, UUID uuid, RayMapData ray){
        if(ray != null){
            RayStorage.remove(x, y, z);
        }
    }
}
