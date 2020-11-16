package me.kaotich00.fwwar.services;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import fr.mrmicky.fastboard.FastBoard;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.KitWar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.war.assault.AssaultWar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

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
            case ASSAULT_WAR_CLASSIC:
                genericWarScoreboard(currentWar);
                break;
            case ASSAULT_WAR_CONQUEST:
                siegeWarScoreboard();
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
                            lines.add(ChatColor.GOLD + "" + ChatColor.BOLD + "  Your class: " + ChatColor.YELLOW + ((KitWar)currentWar).getPlayerKit(player).get().getName());
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

    private void genericWarScoreboard(War currentWar) {

        for (Nation nation : currentWar.getParticipantsNations()) {
            for (Town town : nation.getTowns()) {
                for (Resident resident : town.getResidents()) {
                    Player player = Bukkit.getPlayer(resident.getUUID());
                    if (player != null) {
                        FastBoard board = this.boards.get(player.getUniqueId());

                        if (board != null) {
                            List<String> lines = new ArrayList<>();
                            lines.add("");
                            lines.add(ChatColor.GOLD + "" + ChatColor.BOLD + "  War type: " + ChatColor.YELLOW + currentWar.getWarType().name());
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

    public void siegeWarScoreboard() {

        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();
        AssaultWar currentWar = (AssaultWar) SimpleWarService.getInstance().getCurrentWar().get();

        for (Nation nation : currentWar.getParticipantsNations()) {
            for (Town town : nation.getTowns()) {
                for (Resident resident : town.getResidents()) {
                    Player player = Bukkit.getPlayer(resident.getUUID());
                    if (player != null) {
                        FastBoard board = this.boards.get(player.getUniqueId());

                        if (board != null) {
                            List<String> lines = new ArrayList<>();
                            lines.add(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.STRIKETHROUGH + String.join("", Collections.nCopies(35, "-")));
                            lines.add("");
                            lines.add(ChatColor.GOLD + "" + ChatColor.BOLD + "  War type: " + ChatColor.YELLOW + currentWar.getWarType().name());
                            lines.add("");
                            for (Nation participant : currentWar.getParticipantsNations()) {
                                lines.add(org.bukkit.ChatColor.YELLOW + "  >> Nation: " + org.bukkit.ChatColor.GOLD + participant.getName());
                                for (Town town2 : currentWar.getTownsForNation(participant)) {
                                    int townHP = defaultConfig.getInt("war.town_max_hp");
                                    Float remainingLife = townHP - SimplePlotService.getInstance().getCorePlotOfTown(town2.getUuid()).get().getConquestPercentage();
                                    lines.add(org.bukkit.ChatColor.DARK_AQUA + "    >> Town: " + org.bukkit.ChatColor.AQUA + town2.getName() + " - " + remainingLife + "%");
                                }
                                lines.add("");
                            }

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
                            lines.add(ChatColor.YELLOW + "" + ChatColor.BOLD + ChatColor.STRIKETHROUGH + String.join("", Collections.nCopies(35, "-")));

                            board.updateLines(
                                    lines
                            );
                        }
                    }
                }
            }
        }
    }

}
