package me.kaotich00.fwwar.commands.admin;

import it.forgottenworld.fwechelonapi.FWEchelonApi;
import it.forgottenworld.fwechelonapi.services.DiscourseService;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.integration.DiscourseParser;
import me.kaotich00.fwwar.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class DetectCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        String topicId = args[1];

        /*if(Bukkit.getPluginManager().getPlugin("FWEchelon") == null) {
            Message.ECHELON_MISSING.send(sender);
            return;
        }

        if(Bukkit.getPluginManager().getPlugin("FWEchelon") != null) {
            FWEchelonApi api = (FWEchelonApi) Bukkit.getPluginManager().getPlugin("FWEchelon");
            DiscourseService discourseIntegrationService = api.getDiscourseService();

            discourseIntegrationService.getDiscoursePostsWithNoticeTypeInThread(Integer.parseInt(topicId), (postList) -> {

            }, (error) -> {
                sender.sendMessage(error);
            });
        }*/

        DiscourseParser discourseParser = new DiscourseParser(null);
        Map<String,String> parsedValues = discourseParser.getParsedData();

        sender.sendMessage(ChatColor.GREEN + "Importing data from Discourse...");
        sender.sendMessage(ChatColor.GREEN + "Found the following data: ");
        for(Map.Entry<String,String> entry: parsedValues.entrySet()) {
            sender.sendMessage(ChatColor.GRAY + entry.getKey() + ": " + ChatColor.GOLD + entry.getValue());
        }
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war detect <topic_id>";
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
