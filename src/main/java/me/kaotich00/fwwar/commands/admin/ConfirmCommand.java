package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.command.CommandSender;

public class ConfirmCommand extends AdminCommand {

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

        if(currentWar.getWarStatus().equals(WarStatus.STARTED)) {
            Message.WAR_ALREADY_STARTED.send(sender);
            return;
        }

        if(currentWar.getParticipantsNations().size() < 1) {
            Message.WAR_AT_LEAST_TWO_NATION.send(sender);
            return;
        }

        if(currentWar.getWarType().equals(WarTypes.BOLT_WAR_FACTION)) {
            if(SimpleWarService.getInstance().getKits(currentWar.getWarType()).size() == 0) {
                Message.FACTION_WAR_NOT_ENOUGH_KITS.send(sender);
                return;
            }
        }

        currentWar.setWarStatus(WarStatus.CONFIRMED);
        Message.WAR_CONFIRMED.send(sender);

    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war confirm";
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
