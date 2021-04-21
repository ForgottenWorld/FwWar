package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.objects.war.ParticipantTown;
import me.kaotich00.fwwar.services.SimpleKitService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.MessageUtils;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ConfirmCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War currentWar = SimpleWarService.getInstance().getWar().get();

        if(currentWar.getWarStatus().equals(WarStatus.CONFIRMED)) {
            Message.WAR_ALREADY_CONFIRMED.send(sender);
            return;
        }

        if(currentWar.getWarStatus().equals(WarStatus.STARTED)) {
            Message.WAR_ALREADY_STARTED.send(sender);
            return;
        }

        if(currentWar.getNations().size() < 1) {
            Message.WAR_AT_LEAST_TWO_NATION.send(sender);
            return;
        }

        if(currentWar.getWarType().equals(WarTypes.BOLT_WAR_FACTION)) {
            if(SimpleKitService.getInstance().getKitsForType(currentWar.getWarType()).size() == 0) {
                Message.FACTION_WAR_NOT_ENOUGH_KITS.send(sender);
                return;
            }
        }

        currentWar.setWarStatus(WarStatus.CONFIRMED);
        Message.WAR_CONFIRMED.send(sender);

        if(!currentWar.getWarType().equals(WarTypes.BOLT_WAR_FACTION)) {
            return;
        }

        for(ParticipantNation participantNation: currentWar.getNations()) {
            for(ParticipantTown participantTown: participantNation.getTowns()) {
                for(UUID playerUUID: participantTown.getPlayers()) {
                    Player participantPlayer = Bukkit.getPlayer(playerUUID);

                    if(participantPlayer != null) {
                        participantPlayer.sendTitle(Message.WAR_CONFIRM_TITLE.asString(), Message.WAR_CONFIRM_SUBTITLE.asString(), 15, 200, 15);
                        String startMessage = MessageUtils.chatDelimiter() + "\n" + Message.WAR_CONFIRM_CHAT_MESSAGE.asString(participantPlayer.getName());

                        TextComponent clickMessage = new TextComponent("\n\n " + Message.WAR_CHOOSE_KIT_BUTTON.asString());
                        clickMessage.setColor(ChatColor.YELLOW);
                        clickMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/war chooseKit"));
                        clickMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Message.WAR_CHOOSE_KIT_TOOLTIP.asString()).color(ChatColor.GREEN).italic(true).create()));

                        ComponentBuilder message = new ComponentBuilder();
                        message
                                .append(startMessage)
                                .append(clickMessage)
                                .append(MessageUtils.chatDelimiter());

                        participantPlayer.sendMessage(message.create());
                    }
                }
            }
        }

    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war confirm";
    }

    @Override
    public String getName() {
        return "confirm";
    }

    @Override
    public Integer getRequiredArgs() {
        return 1;
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        return null;
    }

}
