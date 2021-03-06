package me.kaotich00.fwwar.services;

import me.kaotich00.fwwar.objects.plot.CorePlot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SimplePlotService {

    private static SimplePlotService instance;

    private final HashMap<UUID, CorePlot> plotMap;

    private SimplePlotService() {
        if(instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.plotMap = new HashMap<>();
    }

    public static SimplePlotService getInstance() {
        if(instance == null) {
            instance = new SimplePlotService();
        }
        return instance;
    }

    /**
     * Get the Core Plot for the given town
     * @param townUUID
     * @return
     */
    public Optional<CorePlot> getCorePlotOfTown(UUID townUUID) {
        return Optional.ofNullable(plotMap.get(townUUID));
    }

    /**
     * Set the Core Plot for the given town
     * @param townUUID
     * @param corePlot
     */
    public void setCorePlotOfTown(UUID townUUID, CorePlot corePlot) {
        this.plotMap.put(townUUID, corePlot);
    }

    /**
     * Return all the core plots present
     * @return
     */
    public Map<UUID,CorePlot> getCorePlots() {
        return this.plotMap;
    }

}
