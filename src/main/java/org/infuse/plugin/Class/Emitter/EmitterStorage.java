package org.infuse.plugin.Class.Emitter;

import com.hypixel.hytale.math.util.ChunkUtil;
import org.infuse.plugin.Class.Emitter.EmitterMapData;
import org.infuse.plugin.Class.Ray.Ray;
import org.infuse.plugin.Class.Ray.RayDirection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EmitterStorage {
    public static final Map<Long, Map<Integer, EmitterMapData>> DATA = new ConcurrentHashMap<>();

    private EmitterStorage() {}

    public static void put(int x, int y, int z, EmitterMapData data){
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlock(x, y, z);

        DATA.computeIfAbsent(id, k -> new ConcurrentHashMap<>())
                .put(blockIndex, data);
    }

    public static void putRay(int x, int y, int z, Ray ray, RayDirection direction){

        if(direction == null){
            return;
        }
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlock(x, y, z);

        Map<Integer, EmitterMapData> value = DATA.computeIfAbsent(id, k -> new ConcurrentHashMap<>());

        EmitterMapData data = value.computeIfAbsent(blockIndex, k -> new EmitterMapData());

        switch(direction){
            case Up -> data.URay = ray;
            case Down -> data.DRay = ray;
            case East -> data.ERay = ray;
            case West -> data.WRay = ray;
            case North -> data.NRay = ray;
            case South -> data.SRay = ray;
        }
    }

    public static EmitterMapData get(int x, int y, int z) {

        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlock(x, y, z);

        Map<Integer, EmitterMapData> chunkMap = DATA.get(id);

        if (chunkMap == null) return null;

        return chunkMap.get(blockIndex);
    }

    public static void remove(int x, int y, int z) {

        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlock(x, y, z);

        Map<Integer, EmitterMapData> chunkMap = DATA.get(id);

        if (chunkMap == null) return;

        chunkMap.remove(blockIndex);

        if(chunkMap.isEmpty()){
            DATA.remove(id);
        }
    }


    public static void clearChunk(long chunkId) {
        DATA.remove(chunkId);
    }

    public static void clearRay(int x, int y, int z){
        long id = ChunkUtil.indexChunk(x >> 5, z >> 5);
        int blockIndex = ChunkUtil.indexBlock(x, y, z);

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
