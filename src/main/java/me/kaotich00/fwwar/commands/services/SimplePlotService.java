package me.kaotich00.fwwar.commands.services;

import me.kaotich00.fwwar.plot.CorePlot;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SimplePlotService {

    private static SimplePlotService instance;

    private HashMap<UUID, CorePlot> plotMap;

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

    public Optional<CorePlot> getCorePlotOfTown(UUID townUUID) {
        return Optional.ofNullable(plotMap.get(townUUID));
    }

    public void setCorePlotOfTown(UUID townUUID, CorePlot corePlot) {
        this.plotMap.put(townUUID, corePlot);
    }

}
