package me.kaotich00.fwwar.commands.admin;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.objects.war.ParticipantTown;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.war.assault.SiegeWar;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class AddCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War war = warService.getWar().get();

        if(war.getWarStatus().equals(WarStatus.CONFIRMED)) {
            Message.WAR_ALREADY_CONFIRMED.send(sender);
            return;
        }

        if(war.hasParticipantsLimit()) {
            if (war.getMaxAllowedParticipants() < war.getNations().size() + 1) {
                Message.CANNOT_ADD_MORE_NATIONS.send(sender);
                return;
            }
        }

        String nationName = args[1];

        TownyAPI townyAPI = TownyAPI.getInstance();
        Nation nation;
        try{
            nation = townyAPI.getDataSource().getNation(nationName);
        } catch (NotRegisteredException e) {
            Message.NATION_DOES_NOT_EXISTS.send(sender);
            return;
        }

        if(war.getNation(nation.getUuid()) != null) {
            Message.NATION_ALREADY_PRESENT.send(sender);
            return;
        }

        if(war instanceof SiegeWar) {
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

        war.addNation(nation);

        for(Town town: nation.getTowns()) {
            ParticipantNation participantNation = war.getNation(nation.getUuid());
            participantNation.addTown(town);
            war.addTown(town);

            for(Resident resident: town.getResidents()) {
                ParticipantTown participantTown = participantNation.getTown(town.getUuid());
                UUID residentUUID = resident.getUUID();

                participantTown.addPlayer(residentUUID);
                war.addPlayer(resident.getPlayer());
            }
        }

        Message.NATION_SUCCESSFULLY_ADDED.send(sender, nation.getName());

    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war add <nation> <price>";
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public Integer getRequiredArgs() {
        return 3;
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        TownyAPI townyAPI = TownyAPI.getInstance();
        for(Nation nation: townyAPI.getDataSource().getNations()) {
            suggestions.add(nation.getName());
        }
        return suggestions;
    }

}
