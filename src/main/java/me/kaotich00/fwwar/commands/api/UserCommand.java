package me.kaotich00.fwwar.commands.api;

import me.kaotich00.fwwar.api.commands.Command;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class UserCommand implements Command {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.formatErrorMessage("Only players can run this command"));
            throw new CommandException();
        }
    }

    @Override
    public abstract String getName();

    @Override
    public abstract String getUsage();

    @Override
    public abstract String getInfo();

    @Override
    public abstract Integer getRequiredArgs();

    @Override
    public abstract List<String> getSuggestions(String[] args);

}
