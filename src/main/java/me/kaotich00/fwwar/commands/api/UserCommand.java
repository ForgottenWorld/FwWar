package me.kaotich00.fwwar.commands.api;

import me.kaotich00.fwwar.api.commands.Command;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserCommand implements Command {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.formatErrorMessage("Only players can run this command"));
            throw new CommandException();
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public Integer getRequiredArgs() {
        return null;
    }

}
