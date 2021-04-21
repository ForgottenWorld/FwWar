package me.kaotich00.fwwar.commands.admin;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RemoveCommand extends AdminCommand {

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

        if(war.getWarStatus().equals(WarStatus.STARTED)) {
            Message.WAR_ALREADY_STARTED.send(sender);
            return;
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

        if(war.getNation(nation.getUuid()) == null) {
            Message.NATION_NOT_PRESENT.send(sender);
            return;
        }

        war.removeNation(nation);
        Message.NATION_SUCCESSFULLY_REMOVED.send(sender, nation.getName());

    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war remove <nation>";
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Integer getRequiredArgs() {
        return 2;
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        List<String> suggestions = new ArrayList<>();
        Optional<War> optWar = SimpleWarService.getInstance().getWar();
        if(!optWar.isPresent()) {
            return null;
        }

        War currentWar = optWar.get();
        for(ParticipantNation nation: currentWar.getNations()) {
           suggestions.add(nation.getNation().getName());
        }
        return suggestions;
    }

}
