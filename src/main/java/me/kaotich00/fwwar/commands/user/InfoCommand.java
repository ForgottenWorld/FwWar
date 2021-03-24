package me.kaotich00.fwwar.commands.user;

import com.palmergames.bukkit.towny.object.Nation;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.UserCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.services.SimpleWarService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class InfoCommand extends UserCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War war = warService.getWar().get();

        StringBuilder promptMessage = new StringBuilder(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "" + String.join("", Collections.nCopies(45, "-")) + "\n" +
                ChatColor.GRAY + "" + " \n Info on the current war:\n" +
                ChatColor.YELLOW + "\n" + " War type: " + ChatColor.GREEN + "" + ChatColor.BOLD + war.getWarType().name() + "\n" +
                ChatColor.YELLOW + "\n" + " War status: " + ChatColor.GREEN + "" + ChatColor.BOLD + war.getWarStatus().name() + "\n" +
                ChatColor.GRAY + "\n" + " List of participating nations: \n");

        for(ParticipantNation nation: war.getParticipants()) {
           promptMessage.append("\n " + ChatColor.AQUA + " >> " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD).append(nation.getNation().getName());
        }

        promptMessage.append(ChatColor.YELLOW + "\n" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "\n").append(String.join("", Collections.nCopies(45, "-")));

        sender.sendMessage(promptMessage.toString());

    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war info";
    }

    @Override
    public String getName() {
        return "info";
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
