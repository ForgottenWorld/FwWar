package me.kaotich00.fwwar.commands.admin.arena;

import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.services.SimpleArenaService;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class ArenaNewCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        String arenaName = args[2];

        Optional<Arena> optArena = SimpleArenaService.getInstance().getArenaByName(arenaName);
        if(optArena.isPresent()) {
            Message.ARENA_ALREADY_EXISTS.send(sender, arenaName);
            return;
        }

        SimpleArenaService arenaManager = SimpleArenaService.getInstance();
        arenaManager.newArena(arenaName);

        Message.ARENA_CREATED.send(sender, arenaName, arenaName);
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war arena new <name>";
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Integer getRequiredArgs() {
        return 3;
    }

}
