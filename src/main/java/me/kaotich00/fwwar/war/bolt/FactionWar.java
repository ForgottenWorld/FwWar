package me.kaotich00.fwwar.war.bolt;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.entity.Player;

import java.util.*;

public class FactionWar extends BoltWar {

    List<Nation> nations;
    Map<Town, List<UUID>> players;
    Map<String, Kit> kits;
    Map<UUID, Kit> playerKits;

    public FactionWar() {
        this.setWarStatus(WarStatus.CREATED);
        this.nations = new ArrayList<>();
        this.kits = new HashMap<>();
        this.playerKits = new HashMap<>();
        this.players = new HashMap<>();
    }

    public FactionWar(WarStatus warStatus) {
        this.setWarStatus(warStatus);
        this.nations = new ArrayList<>();
        this.kits = new HashMap<>();
        this.playerKits = new HashMap<>();
        this.players = new HashMap<>();
    }

    @Override
    public String getDescription() {
        return "The faction kit war consists of two nations facing each other in a merciless combat! Pick a class and kill every opponent.";
    }

    @Override
    public void addNation(Nation nation) {
        this.nations.add(nation);
    }

    @Override
    public void removeNation(Nation nation) {
        this.nations.remove(nation);
    }

    @Override
    public List<Nation> getParticipants() {
        return nations;
    }

    @Override
    public void addKit(Kit kit){
        this.kits.put(kit.getName(), kit);
    }


    @Override
    public void removeKit(String kitName) {
        this.kits.remove(kitName);
    }

    @Override
    public void updateKit(String kitName, Kit kit) {
        this.kits.put(kitName, kit);
    }

    @Override
    public Collection<Kit> getKits() {
        return this.kits.values();
    }

    @Override
    public Optional<Kit> getKitForName(String name) {
        return Optional.ofNullable(this.kits.get(name));
    }

    @Override
    public WarTypes getWarType() {
        return WarTypes.BOLT_WAR_FACTION;
    }

    @Override
    public void startWar() {

    }

    @Override
    public boolean supportKits() {
        return true;
    }

    @Override
    public void setPlayerKit(Player player, Kit kit) {
        this.playerKits.put(player.getUniqueId(), kit);
    }

    @Override
    public Optional<Kit> getPlayerKit(Player player) {
        return Optional.ofNullable(this.playerKits.get(player.getUniqueId()));
    }

    @Override
    public void addPlayerToWar(Town town, UUID playerUUID) {
        if(!this.players.containsKey(town)) {
            this.players.put(town, new ArrayList<>());
        }
        this.players.get(town).add(playerUUID);
    }

    @Override
    public void removePlayerFromWar(Town town, UUID playerUUID) {
        if(!this.players.containsKey(town)) {
            this.players.put(town, new ArrayList<>());
        }
        this.players.get(town).remove(playerUUID);
    }

    @Override
    public List<UUID> getParticipantsForTown(Town town) {
        return this.players.get(town);
    }

}
