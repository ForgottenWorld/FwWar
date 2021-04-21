package me.kaotich00.fwwar.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.KitWar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleScoreboardService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.MessageUtils;
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

import java.util.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        SimpleWarService simpleWarService = SimpleWarService.getInstance();
        Optional<War> optWar = simpleWarService.getWar();
        if(!optWar.isPresent()) {
            return;
        }

        try {
            TownyAPI townyAPI = TownyAPI.getInstance();
            Player player = event.getPlayer();

            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            Town town = resident.getTown();

            War war = optWar.get();

            if(!war.getWarStatus().equals(WarStatus.CONFIRMED)) {
                return;
            }

            if(!war.supportKits()) {
                return;
            }

            if(!war.hasResident(resident)) return;

            boolean hasKit = ((KitWar)war).getPlayerKit(player).isPresent();

            if(hasKit) return;

            player.sendTitle(Message.WAR_CONFIRM_TITLE.asString(), Message.WAR_CONFIRM_SUBTITLE.asString(), 15, 200, 15);
            String startMessage = MessageUtils.chatDelimiter() + "\n" + Message.WAR_CONFIRM_CHAT_MESSAGE.asString(player.getName());

            TextComponent clickMessage = new TextComponent("\n\n " + Message.WAR_CHOOSE_KIT_BUTTON.asString());
            clickMessage.setColor(ChatColor.YELLOW);
            clickMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/war chooseKit"));
            clickMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Message.WAR_CHOOSE_KIT_TOOLTIP.asString()).color(ChatColor.GREEN).italic(true).create()));

            ComponentBuilder message = new ComponentBuilder();
            message
                    .append(startMessage)
                    .append(clickMessage)
                    .append(MessageUtils.chatDelimiter());

            Bukkit.getScheduler().scheduleSyncDelayedTask(Fwwar.getPlugin(Fwwar.class), () -> {
                player.sendMessage(message.create());
            }, 40L);

        } catch (NotRegisteredException ignored) {
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){

        Player player = event.getEntity();

        SimpleWarService simpleWarService = SimpleWarService.getInstance();
        Optional<War> optWar = simpleWarService.getWar();
        if(!optWar.isPresent()) {
            return;
        }

        War war = optWar.get();

        if(!war.getWarStatus().equals(WarStatus.STARTED)) {
            return;
        }

        if(!(event.getEntity().getKiller() instanceof Player)) return;

        Player killer = event.getEntity().getKiller();

        try {
            TownyAPI townyAPI = TownyAPI.getInstance();

            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            if(!war.hasResident(resident)) return;

            Resident victimR = townyAPI.getDataSource().getResident(player.getName());
            Resident killerR = townyAPI.getDataSource().getResident(killer.getName());

            if(!victimR.getTown().getNation().equals(killerR.getTown().getNation())) {
                war.getKillCounter().incrementPlayerScore(killer, 1);
                SimpleScoreboardService.getInstance().updateScoreboards();
            }
        } catch (NotRegisteredException ignored) {
        }

        event.getDrops().clear();
        war.handlePlayerDeath(player);

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        SimpleWarService simpleWarService = SimpleWarService.getInstance();

        Optional<War> optCurrentWar = simpleWarService.getWar();
        if(!optCurrentWar.isPresent()) {
            return;
        }

        Player player = event.getPlayer();

        SimpleScoreboardService.getInstance().removeScoreboardForPlayer(player);

        War currentWar = optCurrentWar.get();

        if(!currentWar.getWarStatus().equals(WarStatus.STARTED)) {
            return;
        }

        currentWar.handlePlayerDeath(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){

        SimpleWarService simpleWarService = SimpleWarService.getInstance();

        Optional<War> optCurrentWar = simpleWarService.getWar();
        if(!optCurrentWar.isPresent()) {
            return;
        }

        Player player = event.getPlayer();

        War currentWar = optCurrentWar.get();

        if(!currentWar.getWarStatus().equals(WarStatus.STARTED) && !currentWar.getWarStatus().equals(WarStatus.ENDED)) {
            return;
        }

        if(!currentWar.getDeathQueue().hasPlayer(player)) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Fwwar.getPlugin(Fwwar.class), () -> {
            SimpleScoreboardService.getInstance().removeScoreboardForPlayer(player);

            player.getInventory().clear();
            player.getInventory().setArmorContents(null);

            try {
                TownyAPI townyAPI = TownyAPI.getInstance();

                Resident resident = townyAPI.getDataSource().getResident(player.getName());
                Town town = resident.getTown();

                player.teleport(town.getSpawn());
            } catch (TownyException ignored) {
            }
        }, 20L);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();

        SimpleWarService simpleWarService = SimpleWarService.getInstance();

        Optional<War> optCurrentWar = simpleWarService.getWar();
        if(!optCurrentWar.isPresent()) {
            return;
        }

        War currentWar = optCurrentWar.get();

        if(!currentWar.getWarStatus().equals(WarStatus.STARTED)) {
            return;
        }

        event.setCancelled(true);
        Message.WAR_CANNOT_DROP_ITEMS.send(player);
    }

}
