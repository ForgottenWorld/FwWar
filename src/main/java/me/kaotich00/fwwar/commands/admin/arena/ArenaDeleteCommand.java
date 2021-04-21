package me.kaotich00.fwwar.commands.admin.arena;

import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.services.SimpleArenaService;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArenaDeleteCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        String arenaName = args[2];

        Optional<Arena> optArena = SimpleArenaService.getInstance().getArenaByName(arenaName);
        if (!optArena.isPresent()) {
            Message.ARENA_NOT_FOUND.send(sender, arenaName);
            return;
        }

        Arena arena = optArena.get();
        SimpleArenaService.getInstance().deleteArena(arena);

        Message.ARENA_DELETED.send(sender, arena.getName());
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war arena delete <name>";
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public Integer getRequiredArgs() {
        return 3;
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        for(Arena arena: SimpleArenaService.getInstance().getArenas()) {
            suggestions.add(arena.getName());
        }
        return suggestions;
    }

}
