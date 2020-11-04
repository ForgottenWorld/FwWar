package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.war.cui.WarCreationPrompt;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NewCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        WarCreationPrompt prompt = new WarCreationPrompt(Fwwar.getPlugin(Fwwar.class));
        prompt.startConversationForPlayer((Player) sender);
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war new";
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
