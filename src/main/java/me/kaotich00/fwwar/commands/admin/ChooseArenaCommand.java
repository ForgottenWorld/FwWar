package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.services.SimpleArenaService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class ChooseArenaCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if (!warService.getCurrentWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War currentWar = SimpleWarService.getInstance().getCurrentWar().get();

        if (currentWar.getWarStatus().equals(WarStatus.CONFIRMED)) {
            Message.WAR_ALREADY_CONFIRMED.send(sender);
            return;
        }

        if (currentWar.getWarStatus().equals(WarStatus.STARTED)) {
            Message.WAR_ALREADY_STARTED.send(sender);
            return;
        }

        String arenaName = args[1];

        Optional<Arena> arena = SimpleArenaService.getInstance().getArenaByName(arenaName);
        if(!arena.isPresent()) {
            Message.ARENA_NOT_FOUND.send(sender, arenaName);
            return;
        }

        currentWar.setArena(arena.get());
        Message.ARENA_SELECTED.send(sender, arenaName);
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war chooseArena <name>";
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
