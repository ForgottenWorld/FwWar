package me.kaotich00.fwwar.war.assault;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.task.WarPlotConquestTask;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class SiegeWar extends AssaultWar {

    protected int warTaskId;

    public SiegeWar() {
        this.setWarStatus(WarStatus.CREATED);
        this.nations = new ArrayList<>();
        this.players = new HashMap<>();
        this.deathQueue = new ArrayList<>();
        this.killCount = new HashMap<>();
        this.townsForNation = new HashMap<>();
        this.warTaskId = 0;
    }

    @Override
    public String getDescription() {
        return "A plot conquest war. Conquer the other nation plot and win the war.";
    }

    @Override
    public WarTypes getWarType() {
        return WarTypes.ASSAULT_WAR_CONQUEST;
    }

    @Override
    public void startWar() {
        TownyAPI townyAPI = TownyAPI.getInstance();

        // Check if the required amount of Nations is present
        if(getParticipantsNations().size() < 2) {
            Message.NOT_ENOUGH_NATIONS.broadcast();
            return;
        }

        // Check if at least 2 Nations are considered enemies between each other
        boolean areThereEnemies = false;
        for(Nation nation: getParticipantsNations()) {
            for(Nation plausibleEnemy: getParticipantsNations()) {
                if(nation.hasEnemy(plausibleEnemy)) {
                    areThereEnemies = true;
                }
            }
        }

        if(!areThereEnemies) {
            Message.NO_ENEMY_NATION.broadcast();
            return;
        }

        setWarStatus(WarStatus.STARTED);
        Message.WAR_STARTED.broadcast();

        SimpleScoreboardService.getInstance().initScoreboards();

        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();
        Long seconds = defaultConfig.getLong("war.plot_check_time") * 20;

        SimpleWarService.getInstance().setWarTaskId(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Fwwar.getPlugin(Fwwar.class), new WarPlotConquestTask(Fwwar.getPlugin(Fwwar.class), this), seconds, seconds));
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

    @Override
    public boolean supportKits() {
        return false;
    }

    @Override
    public void handlePlayerDeath(Player player) {
        return;
    }

}
