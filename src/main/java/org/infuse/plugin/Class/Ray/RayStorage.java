package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.math.util.ChunkUtil;
import org.joml.Vector3i;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RayStorage {
    public static final Map<Long, Map<Integer, RayMapData>> DATA = new ConcurrentHashMap<>();
    private static final Map<UUID, Vector3i> UUID_TO_POS = new ConcurrentHashMap<>();

    private RayStorage() {}

    public static void put(int x, int y, int z, RayMapData data){
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        DATA.computeIfAbsent(id, k -> new ConcurrentHashMap<>())
                .put(blockIndex, data);

        UUID_TO_POS.put(data.getRay(), new Vector3i(x, y, z));
    }

    public static RayMapData get(int x, int y, int z) {

        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        Map<Integer, RayMapData> chunkMap = DATA.get(id);

        if (chunkMap == null) return null;

        return chunkMap.get(blockIndex);
    }

    public static Vector3i getPosFromUUID(UUID uuid) {
        return UUID_TO_POS.get(uuid);
    }

    public static void remove(int x, int y, int z) {

        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlockInColumn(x, y, z);

        Map<Integer, RayMapData> chunkMap = DATA.get(id);

        if (chunkMap == null) return;

        RayMapData data = chunkMap.get(blockIndex);
        if (data != null) {
            UUID_TO_POS.remove(data.getRay());
        }

        chunkMap.remove(blockIndex);

        if(chunkMap.isEmpty()){
            DATA.remove(id);
        }
    }


    public static void clearChunk(long chunkId) {
        DATA.remove(chunkId);
    }

}
