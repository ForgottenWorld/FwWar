package me.kaotich00.fwwar.commands.admin;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.war.assault.SiegeWar;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class AddCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getCurrentWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War currentWar = SimpleWarService.getInstance().getCurrentWar().get();

        if(currentWar.getWarStatus().equals(WarStatus.CONFIRMED)) {
            Message.WAR_ALREADY_CONFIRMED.send(sender);
            return;
        }

        if(currentWar.hasParticipantsLimit()) {
            if (currentWar.getMaxAllowedParticipants() < currentWar.getParticipantsNations().size() + 1) {
                Message.CANNOT_ADD_MORE_NATIONS.send(sender);
                return;
            }
        }

        String nationName = args[1];

        if(currentWar.getParticipantsNations().stream().filter(nation -> nation.getName().equalsIgnoreCase(nationName)).findFirst().isPresent()) {
            Message.NATION_ALREADY_PRESENT.send(sender);
            return;
        }

        TownyAPI townyAPI = TownyAPI.getInstance();
        Nation nation;
        try{
            nation = townyAPI.getDataSource().getNation(nationName);
        } catch (NotRegisteredException e) {
            Message.NATION_DOES_NOT_EXISTS.send(sender);
            return;
        }

        if(SimpleWarService.getInstance().getCurrentWar().get() instanceof SiegeWar) {
            Set<Town> missingTowns = new HashSet<>();

            SimplePlotService plotService = SimplePlotService.getInstance();
            for (Town town : nation.getTowns()) {
                Optional<CorePlot> townCorePlot = plotService.getCorePlotOfTown(town.getUuid());
                if (!townCorePlot.isPresent()) {
                    missingTowns.add(town);
                }
            }

            if (missingTowns.size() > 0) {
                Message.WAR_CANNOT_START_NOT_ENOUGH_CORE_PLOTS.send(sender);
                for (Town town : missingTowns) {
                    sender.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "  >> " + ChatColor.AQUA + "" + ChatColor.BOLD + town.getName());
                }
                return;
            }

            FileConfiguration defaultConfig = Fwwar.getDefaultConfig();

            for(Town town: nation.getTowns()) {
                plotService.getCorePlotOfTown(town.getUuid()).ifPresent(corePlot -> {
                    corePlot.setConquestPercentage(0);
                });
            }
        }

        Double price = Double.parseDouble(args[2]);
        try {
            if(!nation.getAccount().canPayFromHoldings(price)) {
                Message.NATION_NOT_ENOUGH_MONEY.send(sender, nation.getName());
                return;
            }

            nation.getAccount().withdraw(price, "Joining the war");
            Message.NATION_PAYED_ENTRY.send(sender, nation.getName(), price);

            for(Resident resident: nation.getResidents()) {
                Player player = resident.getPlayer();
                if(player != null) {
                    Message.NATION_PAYED_ENTRY.send(player, nation.getName(), price);
                }
            }
        } catch (EconomyException e) {
            return;
        }

        currentWar.addNation(nation);

        for(Town town: nation.getTowns()) {
            for(Resident resident: town.getResidents()) {
                UUID residentUUID = resident.getUUID();
                currentWar.addPlayerToWar(town, residentUUID);
            }
        }

        Message.NATION_SUCCESSFULLY_ADDED.send(sender, nation.getName());

    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war add <nation> <price>";
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
