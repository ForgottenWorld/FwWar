package me.kaotich00.fwwar.services;

import me.kaotich00.fwwar.api.services.KitService;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarTypes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleKitService implements KitService {

    private static SimpleKitService instance;
    private final Map<WarTypes, Map<String, Kit>> kits;

    private SimpleKitService() {
        if(instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.kits = new HashMap<>();
    }

    public static SimpleKitService getInstance() {
        if(instance == null) {
            instance = new SimpleKitService();
        }
        return instance;
    }

    public void addKit(WarTypes warType, Kit kit){
        if(!this.kits.containsKey(warType)) {
            this.kits.put(warType, new HashMap<>());
        }
        this.kits.get(warType).put(kit.getName(), kit);
    }

    public void removeKit(WarTypes warType, String kitName) {
        this.kits.get(warType).remove(kitName);
    }

    public void updateKit(WarTypes warType, String kitName, Kit kit) {
        this.kits.get(warType).put(kitName, kit);
    }

    public Collection<Kit> getKitsForType(WarTypes warType) {
        return this.kits.get(warType).values();
    }

    public Optional<Kit> getKitForName(WarTypes warType, String name) {
        if(!this.kits.containsKey(warType)) {
            this.kits.put(warType, new HashMap<>());
        }
        return Optional.ofNullable(this.kits.get(warType).get(name));
    }

    @Override
    public Map<Kit, WarTypes> getAllKits() {
        Map<Kit, WarTypes> kits = new HashMap<>();
        for(Map.Entry<WarTypes, Map<String,Kit>> entry : this.kits.entrySet()) {
            for(Map.Entry<String,Kit> entry2 : entry.getValue().entrySet()) {
                kits.put(entry2.getValue(), entry.getKey());
            }
        }
        return kits;
    }

}
