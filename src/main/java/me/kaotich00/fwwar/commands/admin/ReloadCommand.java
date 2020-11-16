package me.kaotich00.fwwar.commands.admin;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.commands.api.AdminCommand;
import me.kaotich00.fwwar.config.ConfigurationManager;
import me.kaotich00.fwwar.locale.LocalizationManager;
import me.kaotich00.fwwar.message.Message;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends AdminCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        super.onCommand(sender, args);

        ConfigurationManager configManager = ConfigurationManager.getInstance();
        configManager.reloadDefaultConfig();
        Message.CONFIG_RELOADED.send(sender);

        Fwwar.getLocalizationManager().reload();
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }

    @Override
    public String getUsage() {
        return "/war reload";
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
