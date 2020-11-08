package me.kaotich00.fwwar.commands.user;

import com.palmergames.bukkit.towny.object.Nation;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.UserCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.services.SimpleWarService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class InfoCommand extends UserCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getCurrentWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War currentWar = SimpleWarService.getInstance().getCurrentWar().get();

        String promptMessage = ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "" + String.join("", Collections.nCopies(45, "-")) + "\n" +
                ChatColor.GRAY + "" + " \n Info on the current war:\n" +
                ChatColor.YELLOW + "\n" + " War type: " + ChatColor.GREEN + "" + ChatColor.BOLD + currentWar.getWarType().name() + "\n" +
                ChatColor.YELLOW + "\n" + " War status: " + ChatColor.GREEN + "" + ChatColor.BOLD + currentWar.getWarStatus().name() + "\n" +
                ChatColor.GRAY + "\n" + " List of participating nations: \n";

        for(Nation nation: currentWar.getParticipantsNations()) {
           promptMessage += "\n " + ChatColor.AQUA + " >> " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + nation.getName();
        }

        promptMessage += ChatColor.YELLOW + "\n" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "\n" + String.join("", Collections.nCopies(45, "-"));

        sender.sendMessage(promptMessage);

    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war info";
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Integer getRequiredArgs() {
        return 1;
    }

}
