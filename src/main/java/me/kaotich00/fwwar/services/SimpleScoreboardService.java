package me.kaotich00.fwwar.services;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.objects.war.War;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class SimpleScoreboardService {

    private static SimpleScoreboardService instance;
    private Scoreboard warScoreBoard;
    private List<UUID> scoreboards;

    private SimpleScoreboardService() {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.scoreboards = new ArrayList<>();
    }

    public static SimpleScoreboardService getInstance() {
        if(instance == null) {
            instance = new SimpleScoreboardService();
        }
        return instance;
    }

    public void updateWarScoreBoard() {

        Scoreboard scoreboard = this.warScoreBoard;
        Objective objective = scoreboard.getObjective("trackWarStatus");
        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();

        for (String s : scoreboard.getEntries()) {
            scoreboard.resetScores(s);
        }

        int slot = 40;
        int spacesLenght = 1;

        Score score_header = objective.getScore(ChatColor.YELLOW + String.join("", Collections.nCopies(27, "-")));
        score_header.setScore(slot);
        slot--;

        Score score_blank = objective.getScore(String.join("", Collections.nCopies(spacesLenght++, " ")));
        score_blank.setScore(slot);
        slot--;

        Optional<War> optWar = SimpleWarService.getInstance().getCurrentWar();
        if (optWar.isPresent()) {
            War war = optWar.get();
            for (Nation participant : war.getParticipantNations()) {
                Score score_nation_name = objective.getScore(org.bukkit.ChatColor.YELLOW + "Nation: " + org.bukkit.ChatColor.GOLD + participant.getName());
                score_nation_name.setScore(slot);
                slot = slot - 2;
                for (Town town : war.getParticipantTownsForNation(participant)) {
                    int townHP = defaultConfig.getInt("war.town_max_hp");
                    Float remainingLife = townHP - SimplePlotService.getInstance().getCorePlotOfTown(town.getUuid()).get().getConquestPercentage();
                    Score score_town_name = objective.getScore(org.bukkit.ChatColor.DARK_AQUA + "Town: " + org.bukkit.ChatColor.AQUA + town.getName() + " - " + remainingLife + "%");
                    score_town_name.setScore(slot);
                    slot--;
                }

                score_blank = objective.getScore(String.join("", Collections.nCopies(spacesLenght++, " ")));
                score_blank.setScore(slot);
                slot--;
            }
        }

        Score score_footer = objective.getScore(ChatColor.YELLOW + String.join("", Collections.nCopies(26, "-")));
        score_footer.setScore(slot);

    }

    public void initWarScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("trackWarStatus","dummy", ChatColor.YELLOW + "Fw" + ChatColor.GOLD + ChatColor.BOLD + "War");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.warScoreBoard = scoreboard;

        War war = SimpleWarService.getInstance().getCurrentWar().get();
        for(Nation participant: war.getParticipantNations()) {
            for(Town town: war.getParticipantTownsForNation(participant)) {
                for(Resident resident: town.getResidents()) {
                    Player player = Bukkit.getPlayer(resident.getName());
                    if(player != null) {
                        player.setScoreboard(this.warScoreBoard);
                        this.scoreboards.add(player.getUniqueId());
                    }
                }
            }
        }

        updateWarScoreBoard();
    }

    public void destroyWarScoreboard() {
        for(UUID uuid : this.scoreboards) {
            Player player = Bukkit.getServer().getPlayer(uuid);
            if(player != null) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            }
        }
        this.scoreboards = new ArrayList<>();
    }

}
