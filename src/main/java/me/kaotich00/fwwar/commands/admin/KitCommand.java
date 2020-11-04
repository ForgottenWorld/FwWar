package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.cui.KitEditingPrompt;
import me.kaotich00.fwwar.cui.WarCreationPrompt;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleWarService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        SimpleWarService warService = SimpleWarService.getInstance();

        if(!warService.getCurrentWar().isPresent()) {
            Message.WAR_NOT_FOUND.send(sender);
            return;
        }

        War currentWar = SimpleWarService.getInstance().getCurrentWar().get();
        if(!currentWar.supportKits()) {
            Message.WAR_DOES_NOT_SUPPORT_KIT.send(sender);
            return;
        }

        KitEditingPrompt prompt = new KitEditingPrompt(Fwwar.getPlugin(Fwwar.class));
        prompt.startConversationForPlayer((Player) sender);

    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war kit";
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Integer getRequiredArgs() {
        return 1;
    }

}
