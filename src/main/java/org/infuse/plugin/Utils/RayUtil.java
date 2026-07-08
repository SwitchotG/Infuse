package org.infuse.plugin.Utils;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import org.infuse.plugin.InfusePlugin;
import org.joml.Vector3d;
import org.joml.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.ParticleUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.infuse.plugin.Class.Emitter.EmitterStorage;
import org.infuse.plugin.Class.Ray.*;
import org.infuse.plugin.components.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class RayUtil {

    public static boolean castRay(Ray emittedRay, int rotation, Vector3i vector3i, Store<EntityStore> store, World world, UUID uuid){
        int resistanceLeft = emittedRay.getResistance();
        int blockLeft = emittedRay.getResistance();
        int i = -1;

        while(blockLeft > 0){
            i++;
            int lastX = vector3i.x;
            int lastY = vector3i.y;
            int lastZ = vector3i.z;
            int subValue = 1 + i;
            switch(rotation){
                case 0:
                    lastZ -= subValue;
                    break;
                case 1:
                    lastX -= subValue;
                    break;
                case 2:
                    lastZ += subValue;
                    break;
                case 3:
                    lastX += subValue;
                    break;
                case 4:
                    lastY += subValue;
                    break;
                case 12:
                    lastY -= subValue;
                    break;
            }
            resistanceLeft = shootPartRay(world, new Vector3i(lastX, lastY, lastZ), resistanceLeft, store, uuid, rotation, emittedRay);
            blockLeft--;
            if(resistanceLeft <= 0){
                destroyRay(lastX, lastY, lastZ, blockLeft, rotation, world, new HashSet<>());
                blockLeft = 0;
            }
        }

        return true;
    }

    public static int shootPartRay(World world, Vector3i vector3i, int resistanceLeft, Store<EntityStore> store, UUID uuid, int direction, Ray emittedRay){
        BlockType blockType = world.getBlockType(vector3i.x, vector3i.y, vector3i.z);
        int returnedResistance = resistanceLeft;
        Holder<ChunkStore> holder = world.getBlockComponentHolder(vector3i.x, vector3i.y, vector3i.z);
        int x = vector3i.x;
        int y = vector3i.y;
        int z = vector3i.z;
        RayMapData ray = RayStorage.get(x, y, z);


        if(ray != null){
            if(!ray.canPropagate(uuid)){
                ray.collideWith(uuid, resistanceLeft, emittedRay.getPower(), emittedRay.getRayType());
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
            METransformableComponent transformableComponent = holder.getComponent(METransformableComponent.getComponentType());

            if(transformableComponent != null){
                world.setBlock(vector3i.x, vector3i.y, vector3i.z, transformableComponent.getBlockId());
            }

            MEConsumerComponent consumerComponent = holder.getComponent(MEConsumerComponent.getComponentType());

            if(consumerComponent != null){
                RayDirection rayDirection = getRayDirectionEnum(direction);

                Ray newRay = emittedRay.clone();

                newRay.setResistance(returnedResistance);

                UUID sourceUUID = ray != null ? ray.getRay() : uuid;

                EmitterStorage.putRay(x,y, z, newRay, rayDirection, sourceUUID, world);
            }

            METraversableComponent traversableComponent = holder.getComponent(METraversableComponent.getComponentType());

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
                ray.propagateAs(uuid, resistanceLeft, emittedRay.getPower(), emittedRay.getRayType());
            }else{
                ray.update(uuid, resistanceLeft);
            }
        }else{
            RayStorage.put(x, y, z, new RayMapData(uuid, resistanceLeft));
        }

        return returnedResistance;
    }

    public static RayDirection getRayDirectionEnum(int direction) {
        RayDirection rayDirection;

        switch(direction){
            case 0 -> rayDirection = RayDirection.West;
            case 1 -> rayDirection = RayDirection.South;
            case 2 -> rayDirection = RayDirection.East;
            case 3 -> rayDirection = RayDirection.North;
            case 4 -> rayDirection = RayDirection.Up;
            case 12 -> rayDirection = RayDirection.Down;
            default -> rayDirection = null;
        }
        return rayDirection;
    }

    public static int getRayDirectionFromBlockRotation(int i, int rotationXZ) {

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

    public static void destroyRay(int x, int y, int z, int numberOfBlock, int rotation, World world, Set<Long> visited){
        for(int i = 0; i < numberOfBlock; i++){
            int bx = x, by = y, bz = z;
            switch(rotation){
                case 0 -> { bz = z - 1 - i; }
                case 1 -> { bx = x - 1 - i; }
                case 2 -> { bz = z + 1 + i; }
                case 3 -> { bx = x + 1 + i; }
                case 4 -> { by = y + 1 + i; }
                case 12 -> { by = y - 1 - i; }
            }

            var holder = world.getBlockComponentHolder(bx, by, bz);
            if(holder != null){
                var consumer = holder.getComponent(MEConsumerComponent.getComponentType());
                if(consumer != null){
                    long key = ChunkUtil.indexBlockInColumn(bx, by, bz);
                    if (!visited.add(key)) continue;

                    InfusePlugin.get().getLOGGER().atInfo().log("ISACTIVATED? : (" + consumer.isActivated() + ")");
                    if(consumer.isActivated()){
                        int rotationXZ = world.getBlockRotationIndex(bx, by, bz) % 4;
                        EmitterStorage.removeRay(bx, by, bz, getRayDirectionEnum(rotation), world);
                        for(RayIO rayIO : consumer.getRayIOs()){
                            for(MEConsumerOutputRay output : rayIO.getOutputs()){
                                int direction = getRayDirectionFromBlockRotation(output.getDirection().getValue(), rotationXZ);
                                consumer.setActivated(false);
                                destroyRay(bx, by, bz, rayIO.getLastFinalResistance(), direction, world, visited);
                            }
                        }
                    }
                }
            }

            InfusePlugin.get().getLOGGER().atInfo().log("DELETED RAY AT : (" + bx + ", " + by + ", " + bz + ")");

            RayMapData ray = RayStorage.get(bx, by, bz);
            destroyPartRay(bx, by, bz, ray);
            EmitterStorage.remove(bx, by, bz, world);
        }
    }

    public static void destroyPartRay(int x, int y, int z, RayMapData ray){
        if(ray != null){
            InfusePlugin.get().getLOGGER().atInfo().log("DELETED RAYPART AT : (" + x + ", " + y + ", " + z + ")");
            RayStorage.remove(x, y, z);
        }
    }
}
