package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.math.util.ChunkUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RayStorage {
    public static final Map<Long, Map<Integer, RayMapData>> DATA = new ConcurrentHashMap<>();

    private RayStorage() {}

    public static void put(int x, int y, int z, RayMapData data){
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlock(x, y, z);

        DATA.computeIfAbsent(id, k -> new ConcurrentHashMap<>())
                .put(blockIndex, data);
    }

    public static RayMapData get(int x, int y, int z) {

        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlock(x, y, z);

        Map<Integer, RayMapData> chunkMap = DATA.get(id);

        if (chunkMap == null) return null;

        return chunkMap.get(blockIndex);
    }

    public static void remove(int x, int y, int z) {

        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlock(x, y, z);

        Map<Integer, RayMapData> chunkMap = DATA.get(id);

        if (chunkMap == null) return;

        chunkMap.remove(blockIndex);

        if(chunkMap.isEmpty()){
            DATA.remove(id);
        }
    }


    public static void clearChunk(long chunkId) {
        DATA.remove(chunkId);
    }

}
