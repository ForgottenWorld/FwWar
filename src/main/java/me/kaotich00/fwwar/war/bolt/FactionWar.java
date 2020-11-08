package me.kaotich00.fwwar.war.bolt;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FactionWar extends BoltWar {

    private Map<UUID, Kit> playerKits;

    public FactionWar() {
        this.setWarStatus(WarStatus.CREATED);
        this.nations = new ArrayList<>();
        this.playerKits = new HashMap<>();
        this.players = new HashMap<>();
        this.deathQueue = new ArrayList<>();
        this.killCount = new HashMap<>();
    }

    public FactionWar(WarStatus warStatus) {
        this.setWarStatus(warStatus);
        this.nations = new ArrayList<>();
        this.playerKits = new HashMap<>();
        this.players = new HashMap<>();
        this.deathQueue = new ArrayList<>();
        this.killCount = new HashMap<>();
    }

    @Override
    public String getDescription() {
        return "The faction kit war consists of two nations facing each other in a merciless combat! Pick a class and kill every opponent.";
    }

    @Override
    public WarTypes getWarType() {
        return WarTypes.BOLT_WAR_FACTION;
    }

    @Override
    public void startWar() {
        List<UUID> playerWithNoSelectedKit = checkKits();
        if(playerWithNoSelectedKit.size() == 0) {
            setWarStatus(WarStatus.STARTED);

            Iterator<Town> iterator = this.players.keySet().iterator();

            while(iterator.hasNext()) {
                Town town = iterator.next();
                List<UUID> residents = this.players.get(town);

                residents.removeIf(resident -> Bukkit.getPlayer(resident) == null);

                for(UUID uuid: residents) {
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

            Message.WAR_STARTED.broadcast();
            SimpleScoreboardService.getInstance().initScoreboards();
        } else {
            Message.WAR_CANNOT_START_KIT_REQUIRED.broadcast();
            for(UUID uuid: playerWithNoSelectedKit) {
                Player player = Bukkit.getPlayer(uuid);
                if(player != null) {
                    Bukkit.broadcastMessage(ChatColor.AQUA + ">> " + ChatColor.GOLD + player.getName());
                }
            }
        }
    }

    @Override
    public void stopWar() {
        Iterator<Town> iterator = this.players.keySet().iterator();

        while(iterator.hasNext()) {
            Town town = iterator.next();
            List<UUID> residents = this.players.get(town);

            for(UUID uuid: residents) {
                Player player = Bukkit.getPlayer(uuid);
                if(player != null) {
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);

                    try {
                        player.teleport(town.getSpawn());
                    } catch (TownyException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        setWarStatus(WarStatus.ENDED);
    }

    private List<UUID> checkKits() {
        List<UUID> playerWithNoKits = new ArrayList<>();
        Iterator<Town> iterator = this.players.keySet().iterator();

        while(iterator.hasNext()) {
            Town town = iterator.next();
            List<UUID> residents = this.players.get(town);

            for(UUID uuid: residents) {
                Player player = Bukkit.getPlayer(uuid);
                if(player != null && !getPlayerKit(player).isPresent()) {
                    playerWithNoKits.add(uuid);
                }
            }
        }
        return playerWithNoKits;
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
    public void handlePlayerDeath(Player player) {

        addPlayerToDeathQueue(player);

        try {
            TownyAPI townyAPI = TownyAPI.getInstance();

            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            Town town = resident.getTown();

            List<UUID> participants = getParticipantsForTown(town);

            if(participants == null) {
                return;
            }

            if (!participants.contains(player.getUniqueId())) {
                return;
            }

            removePlayerFromWar(town, player.getUniqueId());
            Message.WAR_PLAYER_DEFEATED.send(player);

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
                SimpleScoreboardService.getInstance().removeScoreboards();
                SimpleWarService.getInstance().stopWar();
            }

        } catch (NotRegisteredException e) {

        } catch (TownyException e) {

        }
    }

}
