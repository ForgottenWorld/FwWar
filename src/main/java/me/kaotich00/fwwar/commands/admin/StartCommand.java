package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleArenaService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StartCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War currentWar = SimpleWarService.getInstance().getWar().get();

        if(!currentWar.getWarStatus().equals(WarStatus.CONFIRMED)) {
            Message.WAR_MUST_BE_CONFIRMED.send(sender);
            return;
        }

        if(currentWar.supportKits() && SimpleArenaService.getInstance().getArenas().size() == 0) {
            Message.WAR_CANNOT_START_ARENA_REQUIRED.send(sender);
            return;
        }

        currentWar.startWar();
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
        return "start";
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
