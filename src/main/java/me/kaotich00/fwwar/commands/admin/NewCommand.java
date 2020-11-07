package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.cui.WarCreationPrompt;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NewCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        if(SimpleWarService.getInstance().getCurrentWar().isPresent()) {
            War currentWar = SimpleWarService.getInstance().getCurrentWar().get();
            WarStatus warStatus = currentWar.getWarStatus();

            if(!warStatus.equals(WarStatus.ENDED)) {
                Message.WAR_ALREADY_PRESENT.send(sender);
                return;
            }
        }

        WarCreationPrompt prompt = new WarCreationPrompt(Fwwar.getPlugin(Fwwar.class));
        prompt.startConversationForPlayer((Player) sender);
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war new";
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
