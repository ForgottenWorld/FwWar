package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StopCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War currentWar = SimpleWarService.getInstance().getWar().get();

        if(!currentWar.getWarStatus().equals(WarStatus.STARTED)) {
            Message.WAR_MUST_BE_STARTED.send(sender);
            return;
        }

        SimpleWarService.getInstance().stopWar();
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war start";
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public Integer getRequiredArgs() {
        return 1;
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        return null;
    }

}
