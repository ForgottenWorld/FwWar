package me.kaotich00.fwwar.plot;

import java.util.UUID;

public class CorePlot {

    private UUID worldUUID;
    private long chunkKey;

    public CorePlot(UUID worldUUID, long chunkKey) {
        this.worldUUID = worldUUID;
        this.chunkKey = chunkKey;
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

}
