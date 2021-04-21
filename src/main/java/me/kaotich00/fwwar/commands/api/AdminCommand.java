package me.kaotich00.fwwar.commands.api;

import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public abstract class AdminCommand extends UserCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("fwwar.admin")) {
            sender.sendMessage(MessageUtils.formatErrorMessage("You don't have permissions to run this command"));
            throw new CommandException();
        }
    }

}
