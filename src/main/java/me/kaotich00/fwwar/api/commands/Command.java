package me.kaotich00.fwwar.api.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Command {

    void onCommand(CommandSender sender, String args[]);

    String getName();

    String getUsage();

    String getInfo();

    Integer getRequiredArgs();

    List<String> getSuggestions(String[] args);

}
