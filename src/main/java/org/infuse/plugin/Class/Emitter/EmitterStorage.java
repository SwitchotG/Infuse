package org.infuse.plugin.Class.Emitter;

import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import org.infuse.plugin.Class.Emitter.EmitterMapData;
import org.infuse.plugin.Class.Ray.Ray;
import org.infuse.plugin.Class.Ray.RayDirection;
import org.infuse.plugin.InfusePlugin;
import org.infuse.plugin.components.MEConsumerComponent;
import org.joml.Vector3i;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class EmitterStorage {
    public static final Map<Long, Map<Integer, EmitterMapData>> DATA = new ConcurrentHashMap<>();
    private static final Map<UUID, Vector3i> UUID_TO_POS = new ConcurrentHashMap<>();

    private EmitterStorage() {}

    public static void put(int x, int y, int z, EmitterMapData data){
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        DATA.computeIfAbsent(id, k -> new ConcurrentHashMap<>())
                .put(blockIndex, data);

        UUID_TO_POS.put(data.uuid, new Vector3i(x, y, z));
    }

    public static void putRay(int x, int y, int z, Ray ray, RayDirection direction, UUID sourceUuid, World world){

        if(direction == null){
            return;
        }
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        Map<Integer, EmitterMapData> value = DATA.computeIfAbsent(id, k -> new ConcurrentHashMap<>());

        EmitterMapData data = value.computeIfAbsent(blockIndex, k -> new EmitterMapData());

        switch(direction){
            case Up -> {
                data.URay = ray;
                data.URaySource = sourceUuid;
            }
            case Down -> {
                data.DRay = ray;
                data.DRaySource = sourceUuid;
            }
            case East -> {
                data.ERay = ray;
                data.ERaySource = sourceUuid;
            }
            case West -> {
                data.WRay = ray;
                data.WRaySource = sourceUuid;
            }
            case North -> {
                data.NRay = ray;
                data.NRaySource = sourceUuid;
            }
            case South -> {
                data.SRay = ray;
                data.SRaySource = sourceUuid;
            }
        }

        var holder = world.getBlockComponentHolder(x, y, z);
        if(holder != null){
            var consumer = holder.getComponent(MEConsumerComponent.getComponentType());
            if(consumer != null) consumer.markDirty();
        }
    }

    public static void removeRay(int x, int y, int z, RayDirection direction, World world){
        if(direction == null){
            return;
        }
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        Map<Integer, EmitterMapData> value = DATA.computeIfAbsent(id, k -> new ConcurrentHashMap<>());

        EmitterMapData data = value.computeIfAbsent(blockIndex, k -> new EmitterMapData());

        switch(direction){
            case Up -> {
                data.URay = null;
                data.URaySource = null;
            }
            case Down -> {
                data.DRay = null;
                data.DRaySource = null;
            }
            case East -> {
                data.ERay = null;
                data.ERaySource = null;
            }
            case West -> {
                data.WRay = null;
                data.WRaySource = null;
            }
            case North -> {
                data.NRay = null;
                data.NRaySource = null;
            }
            case South -> {
                data.SRay = null;
                data.SRaySource = null;
            }
        }

        var holder = world.getBlockComponentHolder(x, y, z);
        if(holder != null){
            var consumer = holder.getComponent(MEConsumerComponent.getComponentType());
            if(consumer != null) consumer.markDirty();
        }
    }

    public static EmitterMapData get(int x, int y, int z) {

        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        Map<Integer, EmitterMapData> chunkMap = DATA.get(id);

        if (chunkMap == null) return null;

        return chunkMap.get(blockIndex);
    }

    public static Vector3i getPosFromUUID(UUID uuid) {
        if (uuid == null) return null;
        return UUID_TO_POS.get(uuid);
    }

    public static void remove(int x, int y, int z, World world) {

        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        Map<Integer, EmitterMapData> chunkMap = DATA.get(id);

        if (chunkMap == null) return;

        EmitterMapData data = chunkMap.get(blockIndex);
        if (data != null) {
            UUID_TO_POS.remove(data.uuid);
        }

        chunkMap.remove(blockIndex);

        if(chunkMap.isEmpty()){
            DATA.remove(id);
        }
        var holder = world.getBlockComponentHolder(x, y, z);
        if(holder != null){
            var consumer = holder.getComponent(MEConsumerComponent.getComponentType());
            if(consumer != null) consumer.markDirty();
        }

    }


    public static void clearChunk(long chunkId) {
        DATA.remove(chunkId);
    }

    public static void clearRay(int x, int y, int z){
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        Map<Integer, EmitterMapData> value = DATA.computeIfAbsent(id, k -> new ConcurrentHashMap<>());

        EmitterMapData data = value.computeIfAbsent(blockIndex, k -> new EmitterMapData());

        data.SRay = null;
        data.WRay = null;
        data.NRay = null;
        data.DRay = null;
        data.ERay = null;
        data.URay = null;
    }

}
