package me.kaotich00.fwwar.objects.plot;

import java.util.UUID;

public class CorePlot {

    private UUID worldUUID;
    private long chunkKey;
    private float conquestPercentage;

    public CorePlot(UUID worldUUID, long chunkKey) {
        this.worldUUID = worldUUID;
        this.chunkKey = chunkKey;
        this.conquestPercentage = 0;
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

}
