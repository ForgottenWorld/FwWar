package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.services.SimpleWarService;
import org.bukkit.command.CommandSender;

public class StartCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        //SimpleWarService.getInstance().startWar(sender);
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war start";
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
