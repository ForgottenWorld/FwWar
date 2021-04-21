package me.kaotich00.fwwar.commands.user;

import com.github.stefvanschie.inventoryframework.Gui;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.UserCommand;
import me.kaotich00.fwwar.gui.kit.KitSelectionGui;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ChooseKitCommand extends UserCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War currentWar = warService.getWar().get();

        if(!currentWar.getWarStatus().equals(WarStatus.CONFIRMED)) {
            Message.WAR_CANNOT_CHOOSE_KIT.send(sender);
            return;
        }

        TownyAPI townyAPI = TownyAPI.getInstance();

        Player player = (Player) sender;

        Resident resident = null;
        try {
            resident = townyAPI.getDataSource().getResident(player.getName());
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }

        if(!currentWar.hasResident(resident)){
            Message.NO_LONGER_PART_OF_WAR.send(sender);
            return;
        }

        KitSelectionGui kitSelectionGui = new KitSelectionGui(player);
        Gui gui = kitSelectionGui.prepareGui();
        gui.show(player);

    }

    @Override
    public String getInfo() {
        return "";
    }

    @Override
    public String getUsage() {
        return "/war chooseKit";
    }

    @Override
    public String getName() {
        return "chooseKit";
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
