package me.kaotich00.fwwar.commands.user;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import me.kaotich00.fwwar.commands.api.UserCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.services.SimplePlotService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PlotCommand extends UserCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        TownyAPI townyAPI = TownyAPI.getInstance();
        Player player = (Player) sender;

        Resident resident = null;
        try {
            resident = townyAPI.getDataSource().getResident(player.getName());
        } catch (NotRegisteredException e) {
            Message.NOT_PART_OF_A_TOWN.send(sender);
            e.printStackTrace();
            return;
        }

        Town town = null;
        try {
            town = resident.getTown();
        } catch (NotRegisteredException e) {
            Message.NOT_PART_OF_A_TOWN.send(sender);
            e.printStackTrace();
            return;
        }

        if(!resident.equals(town.getMayor())) {
            Message.NOT_A_MAJOR.send(sender);
            return;
        }

        TownBlock currentBlock = townyAPI.getTownBlock(player.getLocation());
        if(currentBlock == null) {
            Message.NOT_A_TOWN_BLOCK.send(sender);
            return;
        }

        Town currentTown = null;
        try {
            currentTown = currentBlock.getTown();
        } catch (NotRegisteredException e) {
            Message.NOT_YOUR_TOWN_BLOCK.send(sender);
            e.printStackTrace();
        }

        if(!currentBlock.isOutpost()) {
            Message.NOT_AN_OUTPOST.send(sender);
            return;
        }

        UUID townUUID = town.getUuid();

        SimplePlotService plotService = SimplePlotService.getInstance();
        if(plotService.getCorePlotOfTown(townUUID).isPresent()) {
            Message.CORE_PLOT_ALREADY_PRESENT.send(sender);
            return;
        }

        CorePlot corePlot = new CorePlot(player.getWorld().getUID(), player.getLocation().getChunk().getChunkKey());
        plotService.setCorePlotOfTown(townUUID, corePlot);

        Message.CORE_PLOT_SUCCESSFULLY_SET.send(sender);
    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war plot";
    }

    @Override
    public String getName() {
        return "plot";
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
