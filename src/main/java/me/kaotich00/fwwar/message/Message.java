package me.kaotich00.fwwar.message;

import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public enum Message {

    PREFIX(ChatColor.DARK_GRAY + "[" +
            ChatColor.YELLOW + "Fw" +
            ChatColor.GOLD + ChatColor.BOLD + "War" +
            ChatColor.DARK_GRAY + "]", false),

    NOT_REGISTERED(MessageUtils.formatErrorMessage("You are not a citizen"), true),
    NOT_PART_OF_A_TOWN(MessageUtils.formatErrorMessage("You are not part of a town!"), true),
    NOT_A_MAJOR(MessageUtils.formatErrorMessage("You are not the major of the town!"), true),

    CORE_PLOT_ALREADY_PRESENT(MessageUtils.formatErrorMessage("Your town already has a Core Block!"), true),
    CORE_PLOT_SUCCESSFULLY_SET(MessageUtils.formatSuccessMessage("Successfully set town core block!"), true),
    NOT_A_TOWN_BLOCK(MessageUtils.formatErrorMessage("You are not in a town block"), true),
    NOT_YOUR_TOWN_BLOCK(MessageUtils.formatErrorMessage("This is not your town"), true),
    NOT_AN_OUTPOST(MessageUtils.formatErrorMessage("The core plot must be in an outpost"), true);


    private final String message;
    private final boolean showPrefix;

    Message(String message, boolean showPrefix) {
        this.message = MessageUtils.rewritePlaceholders(message);
        this.showPrefix = showPrefix;
    }

    public void send(CommandSender sender, Object... objects) {
        sender.sendMessage(asString(objects));
    }

    public String asString(Object... objects) {
        return format(objects);
    }

    private String format(Object... objects) {
        String string = this.message;
        if(this.showPrefix) {
            string = PREFIX.message + " " + this.message;
        }
        for (int i = 0; i < objects.length; i++) {
            Object o = objects[i];
            string = string.replace("{" + i + "}", String.valueOf(o));
        }
        return string;
    }

}