package me.kaotich00.fwwar.commands.admin;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import it.forgottenworld.fwechelonapi.FWEchelonApi;
import it.forgottenworld.fwechelonapi.discourse.DiscoursePost;
import it.forgottenworld.fwechelonapi.services.DiscourseService;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.integration.DiscourseImporter;
import me.kaotich00.fwwar.integration.DiscourseParser;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import me.kaotich00.fwwar.war.WarFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ImportCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        String topicId = args[1];

        if(SimpleWarService.getInstance().getCurrentWar().isPresent()) {
            War currentWar = SimpleWarService.getInstance().getCurrentWar().get();
            WarStatus warStatus = currentWar.getWarStatus();

            if(!warStatus.equals(WarStatus.ENDED)) {
                Message.WAR_ALREADY_PRESENT.send(sender);
                return;
            }
        }

        if(Bukkit.getPluginManager().getPlugin("FWEchelon") == null) {
            Message.ECHELON_MISSING.send(sender);
            return;
        }

        if(Bukkit.getPluginManager().getPlugin("FWEchelon") != null) {
            DiscourseImporter importer = new DiscourseImporter(topicId, sender);
            importer.importData();
        }

    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war import <topic_id>";
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
