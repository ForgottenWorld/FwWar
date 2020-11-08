package me.kaotich00.fwwar.services;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import fr.mrmicky.fastboard.FastBoard;
import me.kaotich00.fwwar.api.war.War;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class SimpleScoreboardService {

    private static SimpleScoreboardService instance;
    private Map<UUID, FastBoard> boards;

    private SimpleScoreboardService() {
        if (instance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.boards = new HashMap<>();
    }

    public static SimpleScoreboardService getInstance() {
        if(instance == null) {
            instance = new SimpleScoreboardService();
        }
        return instance;
    }

    public void initScoreboards() {
        SimpleWarService warService = SimpleWarService.getInstance();
        War currentWar = warService.getCurrentWar().get();

        for(Nation nation: currentWar.getParticipantsNations()) {
            for(Town town: nation.getTowns()) {
                for(Resident resident: town.getResidents()) {
                    Player player = Bukkit.getPlayer(resident.getUUID());
                    if(player != null) {
                        FastBoard board = new FastBoard(player);
                        board.updateTitle(org.bukkit.ChatColor.DARK_GRAY + "[" +
                                org.bukkit.ChatColor.YELLOW + "Fw" +
                                org.bukkit.ChatColor.GOLD + org.bukkit.ChatColor.BOLD + "War" +
                                ChatColor.DARK_GRAY + "]");

                        this.boards.put(player.getUniqueId(), board);
                    }
                }
            }
        }
        updateScoreboards();
    }

    public void updateScoreboards() {
        SimpleWarService warService = SimpleWarService.getInstance();
        War currentWar = warService.getCurrentWar().get();

        switch(currentWar.getWarType()) {
            case BOLT_WAR_FACTION:
                updateFactionKitScoreboard(currentWar);
                break;
            case BOLT_WAR_RANDOM:
                updateRandomFactionKitScoreboard(currentWar);
                break;
        }
    }

    public void removeScoreboards() {
        Iterator<UUID> boardsIterator = this.boards.keySet().iterator();
        List<UUID> boardsToRemove = new ArrayList<>();
        while(boardsIterator.hasNext()) {
            UUID currentUUID = boardsIterator.next();
            Player player = Bukkit.getPlayer(currentUUID);
            if(player != null) {
                FastBoard board = this.boards.get(currentUUID);
                board.delete();
                boardsIterator.remove();
                boardsToRemove.add(currentUUID);
            }
        }

        for(UUID boardToRemove: boardsToRemove) {
            this.boards.remove(boardToRemove);
        }
    }

    public void removeScoreboardForPlayer(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        if(player != null) {
            FastBoard board = this.boards.get(playerUUID);
            if(board != null) {
                board.delete();
                this.boards.remove(player.getUniqueId());
            }
        }
    }

    private void updateFactionKitScoreboard(War currentWar) {

        for(Nation nation: currentWar.getParticipantsNations()) {
            for(Town town: nation.getTowns()) {
                for(Resident resident: town.getResidents()) {
                    Player player = Bukkit.getPlayer(resident.getUUID());
                    if(player != null) {
                        FastBoard board = this.boards.get(player.getUniqueId());

                        if(board!= null) {
                            List<String> lines = new ArrayList<>();
                            lines.add("");
                            lines.add(ChatColor.GOLD + "" + ChatColor.BOLD + "  War type: " + ChatColor.YELLOW + "Faction War");
                            lines.add("");
                            lines.add(ChatColor.GOLD + "" + ChatColor.BOLD + "  Your class: " + ChatColor.YELLOW + currentWar.getPlayerKit(player).get().getName());
                            lines.add("");
                            lines.add(ChatColor.AQUA + "  Top players: ");
                            if (currentWar.getKillCountsLeaderboard().size() == 0) {
                                lines.add(ChatColor.GRAY + "  No records yet");
                            }
                            for (Map.Entry<UUID, Integer> entry : currentWar.getKillCountsLeaderboard().entrySet()) {
                                String playerName = "";
                                Player killer = Bukkit.getPlayer(entry.getKey());
                                if (killer == null) {
                                    OfflinePlayer killerOffline = Bukkit.getOfflinePlayer(entry.getKey());
                                    playerName = killerOffline.getName();
                                } else {
                                    playerName = killer.getName();
                                }

                                lines.add(ChatColor.DARK_AQUA + "  >> " + ChatColor.AQUA + "" + ChatColor.BOLD + playerName + ChatColor.GOLD + " - " + entry.getValue() + " kills");
                            }
                            lines.add("");

                            board.updateLines(
                                    lines
                            );
                        }
                    }
                }
            }
        }

    }

    private void updateRandomFactionKitScoreboard(War currentWar) {

        for (Nation nation : currentWar.getParticipantsNations()) {
            for (Town town : nation.getTowns()) {
                for (Resident resident : town.getResidents()) {
                    Player player = Bukkit.getPlayer(resident.getUUID());
                    if (player != null) {
                        FastBoard board = this.boards.get(player.getUniqueId());

                        if (board != null) {
                            List<String> lines = new ArrayList<>();
                            lines.add("");
                            lines.add(ChatColor.GOLD + "" + ChatColor.BOLD + "  War type: " + ChatColor.YELLOW + "Random kit");
                            lines.add("");
                            lines.add(ChatColor.AQUA + "  Top players: ");
                            if (currentWar.getKillCountsLeaderboard().size() == 0) {
                                lines.add(ChatColor.GRAY + "  No records yet");
                            }
                            for (Map.Entry<UUID, Integer> entry : currentWar.getKillCountsLeaderboard().entrySet()) {
                                String playerName = "";
                                Player killer = Bukkit.getPlayer(entry.getKey());
                                if (killer == null) {
                                    OfflinePlayer killerOffline = Bukkit.getOfflinePlayer(entry.getKey());
                                    playerName = killerOffline.getName();
                                } else {
                                    playerName = killer.getName();
                                }

                                lines.add(ChatColor.DARK_AQUA + "  >> " + ChatColor.AQUA + "" + ChatColor.BOLD + playerName + ChatColor.GOLD + " - " + entry.getValue() + " kills");
                            }
                            lines.add("");

                            board.updateLines(
                                    lines
                            );
                        }
                    }
                }
            }
        }

    }

    /*public void updateWarScoreBoard() {

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
            for (Nation participant : oldWar.getParticipantNations()) {
                Score score_nation_name = objective.getScore(org.bukkit.ChatColor.YELLOW + "Nation: " + org.bukkit.ChatColor.GOLD + participant.getName());
                score_nation_name.setScore(slot);
                slot = slot - 2;
                for (Town town : oldWar.getParticipantTownsForNation(participant)) {
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

    }*/

    /*public void initWarScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("trackWarStatus","dummy", ChatColor.YELLOW + "Fw" + ChatColor.GOLD + ChatColor.BOLD + "War");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.warScoreBoard = scoreboard;

        OldWar oldWar = SimpleWarService.getInstance().getCurrentWar().get();
        for(Nation participant: oldWar.getParticipantNations()) {
            for(Town town: oldWar.getParticipantTownsForNation(participant)) {
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
    }*/

}
