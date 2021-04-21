package me.kaotich00.fwwar.commands.admin.arena;

import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.utils.CommandUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ArenaCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        String subcommand = args[1];
        dispatchSubCommand(subcommand, sender, args);
    }

    private void dispatchSubCommand(String subcommand, CommandSender sender, String[] args) {
        switch(subcommand) {
            case CommandUtils.ARENA_NEW_COMMAND:
                ArenaNewCommand newCommand = new ArenaNewCommand();
                newCommand.onCommand(sender, args);
                break;
            case CommandUtils.ARENA_EDIT_COMMAND:
                ArenaEditCommand editCommand = new ArenaEditCommand();
                editCommand.onCommand(sender, args);
                break;
            case CommandUtils.ARENA_DELETE_COMMAND:
                ArenaDeleteCommand deleteCommand = new ArenaDeleteCommand();
                deleteCommand.onCommand(sender, args);
                break;
        }
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war arena <new/edit/delete> <name>";
    }

    @Override
    public String getName() {
        return "arena";
    }

    @Override
    public Integer getRequiredArgs() {
        return 2;
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();

        String argsIndex = "";
        if(args.length == 2) {
            suggestions.add("new");
            suggestions.add("edit");
            suggestions.add("delete");

            return suggestions;
        }

        argsIndex = args[1];
        if(args.length == 3) {
            switch (argsIndex) {
                case CommandUtils.ARENA_NEW_COMMAND:
                    ArenaNewCommand newCommand = new ArenaNewCommand();
                    suggestions = newCommand.getSuggestions(args);
                    break;
                case CommandUtils.ARENA_EDIT_COMMAND:
                    ArenaEditCommand editCommand = new ArenaEditCommand();
                    suggestions = editCommand.getSuggestions(args);
                    break;
                case CommandUtils.ARENA_DELETE_COMMAND:
                    ArenaDeleteCommand deleteCommand = new ArenaDeleteCommand();
                    suggestions = deleteCommand.getSuggestions(args);
                    break;
            }
        }
        return suggestions;
    }

}
