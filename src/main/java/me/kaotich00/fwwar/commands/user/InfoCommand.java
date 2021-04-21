package me.kaotich00.fwwar.commands.user;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.UserCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.services.SimpleWarService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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

        StringBuilder participantsList = new StringBuilder();
        for(ParticipantNation nation: war.getNations()) {
            participantsList.append("\n " + ChatColor.AQUA + " >> " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD).append(nation.getNation().getName());
        }
        Message.WAR_INFO_COMMAND.send(sender, war.getWarType().name(), war.getWarStatus().name(), participantsList.toString());
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
