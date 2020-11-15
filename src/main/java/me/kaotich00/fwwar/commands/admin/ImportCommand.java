package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.integration.DiscourseImporter;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;


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
