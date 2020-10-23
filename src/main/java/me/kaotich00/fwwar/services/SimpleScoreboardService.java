package me.kaotich00.fwwar.services;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.objects.war.War;
import me.kaotich00.fwwar.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class SimpleScoreboardService {

    private static SimpleScoreboardService instance;
    private Scoreboard warScoreBoard;

    private SimpleScoreboardService() {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
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

        for (String s : scoreboard.getEntries()) {
            scoreboard.resetScores(s);
        }

        int slot = 40;

        Score score_header = objective.getScore(ChatColor.YELLOW + String.join("", Collections.nCopies(27, "-")));
        score_header.setScore(slot);
        slot--;

        Optional<War> optWar = SimpleWarService.getInstance().getCurrentWar();
        if (optWar.isPresent()) {
            War war = optWar.get();
            for (Nation participant : war.getParticipantNations()) {
                Score score_nation_name = objective.getScore(org.bukkit.ChatColor.YELLOW + "Nation: " + org.bukkit.ChatColor.GOLD + participant.getName());
                score_nation_name.setScore(slot);
                slot = slot - 2;
                for (Town town : war.getParticipantTownsForNation(participant)) {
                    Float remainingLife = 100 - SimplePlotService.getInstance().getCorePlotOfTown(town.getUuid()).get().getConquestPercentage();
                    Score score_town_name = objective.getScore(org.bukkit.ChatColor.DARK_AQUA + "Town: " + org.bukkit.ChatColor.AQUA + town.getName() + " - " + remainingLife + "%");
                    score_town_name.setScore(slot);
                    slot--;
                }
            }
        }

        Score score_footer = objective.getScore(ChatColor.YELLOW + String.join("", Collections.nCopies(27, "-")));
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
                    }
                }
            }
        }

        updateWarScoreBoard();
    }

    public void destroyWarScoreboard() {
        War war = SimpleWarService.getInstance().getCurrentWar().get();
        for(Nation participant: war.getParticipantNations()) {
            for(Town town: war.getParticipantTownsForNation(participant)) {
                for(Resident resident: town.getResidents()) {
                    Player player = Bukkit.getPlayer(resident.getName());
                    if(player != null) {
                        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                    }
                }
            }
        }
    }

}
