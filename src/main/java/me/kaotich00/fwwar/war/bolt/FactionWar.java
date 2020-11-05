package me.kaotich00.fwwar.war.bolt;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    public List<Nation> getParticipantsNations() {
        return nations;
    }

    @Override
    public Set<Town> getParticipantsTowns() {
        return this.players.keySet();
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
        setWarStatus(WarStatus.STARTED);

        for(List<UUID> uuidList: players.values()) {
            for(UUID uuid: uuidList) {
                Player player = Bukkit.getPlayer(uuid);
                if(player != null) {
                    player.getInventory().clear();

                    Kit kit = this.playerKits.get(uuid);
                    for(ItemStack item: kit.getItemsList()) {
                        player.getInventory().addItem(item);
                    }
                }
            }
        }
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

        if(this.players.get(town).size() == 0) {
            this.players.remove(town);
        }
    }

    @Override
    public List<UUID> getParticipantsForTown(Town town) {
        return this.players.get(town);
    }

    @Override
    public void handlePlayerDeath(Player player) {
        try {
            TownyAPI townyAPI = TownyAPI.getInstance();

            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            Town town = resident.getTown();

            List<UUID> participants = getParticipantsForTown(town);
            if (!participants.contains(player.getUniqueId())) {
                return;
            }

            removePlayerFromWar(town, player.getUniqueId());

            boolean shouldRemoveNation = true;
            for(Town t: town.getNation().getTowns()) {
                if(getParticipantsTowns().contains(t)){
                    shouldRemoveNation = false;
                }
            }

            if(shouldRemoveNation) {
                removeNation(town.getNation());
                Message.NATION_DEFEATED.broadcast(town.getNation().getName());
            }

            if(getParticipantsNations().size() < 2) {
                Message.WAR_ENDED.broadcast();
                SimpleScoreboardService.getInstance().removeScoreboards();
                return;
            }

        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
    }

}
