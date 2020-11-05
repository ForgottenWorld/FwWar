package me.kaotich00.fwwar.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
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
import org.bukkit.event.player.PlayerJoinEvent;

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

            player.sendTitle(ChatColor.YELLOW + "Hi, you are part of the upcoming war!", ChatColor.GOLD + "Check the chat right now", 15, 100, 15);
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
            e.printStackTrace();
        }

    }

}
