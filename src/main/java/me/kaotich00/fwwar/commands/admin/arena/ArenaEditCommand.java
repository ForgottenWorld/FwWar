package me.kaotich00.fwwar.commands.admin.arena;

import com.github.stefvanschie.inventoryframework.Gui;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.gui.arena.ArenaEditGui;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.services.SimpleArenaService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArenaEditCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        String arenaName = args[2];

        Optional<Arena> optArena = SimpleArenaService.getInstance().getArenaByName(arenaName);
        if(!optArena.isPresent()) {
            Message.ARENA_NOT_FOUND.send(sender, arenaName);
            return;
        }

        ArenaEditGui arenaEditGui = new ArenaEditGui(optArena.get(), (Player) sender);
        Gui gui = arenaEditGui.prepareGui();
        gui.show((Player) sender);

    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/planetwar arena edit <name>";
    }

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public Integer getRequiredArgs() {
        return 2;
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
