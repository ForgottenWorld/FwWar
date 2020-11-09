package me.kaotich00.fwwar.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.utils.NameUtil;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.commands.Command;
import me.kaotich00.fwwar.commands.admin.*;
import me.kaotich00.fwwar.commands.admin.arena.ArenaCommand;
import me.kaotich00.fwwar.commands.user.ChooseKitCommand;
import me.kaotich00.fwwar.commands.user.InfoCommand;
import me.kaotich00.fwwar.commands.user.PlotCommand;
import me.kaotich00.fwwar.utils.CommandUtils;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarCommandManager implements TabExecutor {

    private Map<String, Command> commandRegistry;
    private Fwwar plugin;

    public WarCommandManager(Fwwar plugin) {
        this.commandRegistry = new HashMap<>();
        this.plugin = plugin;
        setup();
    }

    private void setup() {
        this.commandRegistry.put(CommandUtils.WAR_PLOT_COMMAND, new PlotCommand());
        this.commandRegistry.put(CommandUtils.WAR_START_COMMAND, new StartCommand());
        this.commandRegistry.put(CommandUtils.WAR_RELOAD_COMMAND, new ReloadCommand());
        this.commandRegistry.put(CommandUtils.WAR_NEW_COMMAND, new NewCommand());
        this.commandRegistry.put(CommandUtils.WAR_KIT_COMMAND, new KitCommand());
        this.commandRegistry.put(CommandUtils.WAR_ADD_NATION_COMMAND, new AddCommand());
        this.commandRegistry.put(CommandUtils.WAR_REMOVE_NATION_COMMAND, new RemoveCommand());
        this.commandRegistry.put(CommandUtils.WAR_CONFIRM_COMMAND, new ConfirmCommand());
        this.commandRegistry.put(CommandUtils.WAR_CHOOSE_KIT_COMMAND, new ChooseKitCommand());
        this.commandRegistry.put(CommandUtils.WAR_STOP_COMMAND, new StopCommand());
        this.commandRegistry.put(CommandUtils.WAR_INFO_COMMAND, new InfoCommand());
        this.commandRegistry.put(CommandUtils.WAR_ARENA_COMMAND, new ArenaCommand());
    }

    private Command getCommand(String name) {
        return this.commandRegistry.get(name);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if( args.length == 0 ) {
            sender.sendMessage(MessageUtils.helpMessage());
            return CommandUtils.COMMAND_SUCCESS;
        }

        Command fwCommand = getCommand(args[0]);

        if( fwCommand != null ) {
            if(fwCommand.getRequiredArgs() > args.length) {
                sender.sendMessage(MessageUtils.formatErrorMessage("Not enough arguments"));
                sender.sendMessage(MessageUtils.formatErrorMessage(fwCommand.getUsage()));
                return true;
            }
            try {
                fwCommand.onCommand(sender, args);
            } catch (CommandException e) {
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        String argsIndex = "";

        /* Suggest child commands */
        if(args.length == 1) {
            argsIndex = args[0];

            for(String commandName: this.commandRegistry.keySet()) {
                suggestions.add(commandName);
            }
        }

        if(args.length == 2) {
            argsIndex = args[1];

            switch(args[0]) {
                case CommandUtils.WAR_ADD_NATION_COMMAND:
                    TownyAPI townyAPI = TownyAPI.getInstance();
                    for(Nation nation: townyAPI.getDataSource().getNations()) {
                        suggestions.add(nation.getName());
                    }
                    break;
            }
        }

        return NameUtil.filterByStart(suggestions, argsIndex);
    }

}
