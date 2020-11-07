package me.kaotich00.fwwar.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        try {
            TownyAPI townyAPI = TownyAPI.getInstance();
            Player player = event.getPlayer();

            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            Town town = resident.getTown();

            SimpleWarService simpleWarService = SimpleWarService.getInstance();

            Optional<War> optCurrentWar = simpleWarService.getCurrentWar();
            if(!optCurrentWar.isPresent()) {
                return;
            }

            War currentWar = optCurrentWar.get();

            if(!currentWar.getWarStatus().equals(WarStatus.CONFIRMED)) {
                return;
            }

            if(!currentWar.supportKits()) {
                return;
            }

            List<UUID> participants = currentWar.getParticipantsForTown(town);
            if(!participants.contains(player.getUniqueId())) {
                return;
            }

            boolean hasKit = currentWar.getPlayerKit(player).isPresent();

            if(hasKit) {
                return;
            }

            player.sendTitle(ChatColor.YELLOW + "Hi, you are part of the upcoming war!", ChatColor.GOLD + "Check the chat right now", 15, 200, 15);
            String startMessage = ChatColor.GREEN + String.join("", Collections.nCopies(53, "-")) +
                    ChatColor.DARK_AQUA + "Hi " + player.getName() + ", you will be part of the next war." +
                    ChatColor.AQUA + " Therefore you must choose a kit to use during the battle.";


            TextComponent clickMessage = new TextComponent("\n\n [CLICK HERE TO CHOOSE A KIT]\n");
            clickMessage.setColor(ChatColor.GREEN);
            clickMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/war chooseKit"));
            clickMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose a kit").color(ChatColor.GREEN).italic(true).create()));

            String endMessage = ChatColor.GREEN + String.join("", Collections.nCopies(53, "-"));

            ComponentBuilder message = new ComponentBuilder();
            message
                    .append(startMessage)
                    .append(clickMessage)
                    .append(endMessage);

            Bukkit.getScheduler().scheduleSyncDelayedTask(Fwwar.getPlugin(Fwwar.class), () -> {
                player.sendMessage(message.create());
            }, 40L);

        } catch (NotRegisteredException e) {

        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){

        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = event.getEntity();

        SimpleWarService simpleWarService = SimpleWarService.getInstance();

        Optional<War> optCurrentWar = simpleWarService.getCurrentWar();
        if(!optCurrentWar.isPresent()) {
            return;
        }

        War currentWar = optCurrentWar.get();

        if(!currentWar.getWarStatus().equals(WarStatus.STARTED)) {
            return;
        }

        event.getDrops().clear();

        if(event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            currentWar.incrementPlayerKillCount(killer, 1);
            SimpleScoreboardService.getInstance().updateScoreboards();
        }

        currentWar.handlePlayerDeath(player);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){

        SimpleScoreboardService.getInstance().removeScoreboardForPlayer(event.getPlayer().getUniqueId());

        Player player = event.getPlayer();

        SimpleWarService simpleWarService = SimpleWarService.getInstance();

        Optional<War> optCurrentWar = simpleWarService.getCurrentWar();
        if(!optCurrentWar.isPresent()) {
            return;
        }

        War currentWar = optCurrentWar.get();

        if(!currentWar.getWarStatus().equals(WarStatus.STARTED)) {
            return;
        }

        currentWar.handlePlayerDeath(player);

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();

        SimpleWarService simpleWarService = SimpleWarService.getInstance();

        Optional<War> optCurrentWar = simpleWarService.getCurrentWar();
        if(!optCurrentWar.isPresent()) {
            return;
        }

        War currentWar = optCurrentWar.get();

        if(!currentWar.getWarStatus().equals(WarStatus.STARTED) && !currentWar.getWarStatus().equals(WarStatus.ENDED)) {
            return;
        }

        if(!currentWar.isPlayerInDeathQueue(player)) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Fwwar.getPlugin(Fwwar.class), () -> {
            SimpleScoreboardService.getInstance().removeScoreboardForPlayer(player.getUniqueId());

            try {
                TownyAPI townyAPI = TownyAPI.getInstance();

                Resident resident = townyAPI.getDataSource().getResident(player.getName());
                Town town = resident.getTown();

                player.teleport(town.getSpawn());
            } catch (NotRegisteredException e) {
            } catch (TownyException e) {
            }
        }, 20L);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        SimpleWarService simpleWarService = SimpleWarService.getInstance();

        Optional<War> optCurrentWar = simpleWarService.getCurrentWar();
        if(!optCurrentWar.isPresent()) {
            return;
        }

        War currentWar = optCurrentWar.get();

        if(!currentWar.getWarStatus().equals(WarStatus.STARTED) && !currentWar.getWarStatus().equals(WarStatus.ENDED)) {
            return;
        }

        event.setCancelled(true);
        Message.WAR_CANNOT_DROP_ITEMS.send(player);
    }

}
