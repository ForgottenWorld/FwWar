package me.kaotich00.fwwar.objects.plot;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CorePlot implements ConfigurationSerializable {

    private UUID worldUUID;
    private long chunkKey;
    private float conquestPercentage;

    public CorePlot(UUID worldUUID, long chunkKey) {
        this.worldUUID = worldUUID;
        this.chunkKey = chunkKey;
        this.conquestPercentage = 0;
    }

    public CorePlot(Map<String, Object> serializedCorePlot) {
        this.worldUUID = UUID.fromString(String.valueOf(serializedCorePlot.get("world_uuid")));
        this.chunkKey = (long) serializedCorePlot.get("chunk_key");
        this.conquestPercentage = (float) ((double) serializedCorePlot.get("conquest_percentage"));
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public void setWorldUUID(UUID worldUUID) {
        this.worldUUID = worldUUID;
    }

    public long getChunkKey() {
        return chunkKey;
    }

    public void setChunkKey(long chunkKey) {
        this.chunkKey = chunkKey;
    }

    public float getConquestPercentage() {
        return conquestPercentage;
    }

    public void setConquestPercentage(float conquestPercentage) {
        this.conquestPercentage = conquestPercentage;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> mapSerializer = new HashMap<>();

        mapSerializer.put("world_uuid", this.worldUUID.toString());
        mapSerializer.put("chunk_key", this.chunkKey);
        mapSerializer.put("conquest_percentage", this.conquestPercentage);

        return mapSerializer;
    }
}
