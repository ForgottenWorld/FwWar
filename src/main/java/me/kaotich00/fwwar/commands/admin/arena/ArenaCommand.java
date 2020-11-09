package me.kaotich00.fwwar.commands.admin.arena;

import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.utils.CommandUtils;
import org.bukkit.command.CommandSender;

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
        }
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war arena <new/edit/delete> <name>";
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Integer getRequiredArgs() {
        return 2;
    }

}
