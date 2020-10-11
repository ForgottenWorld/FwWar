package me.kaotich00.fwwar.utils;

import org.bukkit.ChatColor;

public class MessageUtils {

    public static String getPluginPrefix() {
        return ChatColor.DARK_GRAY + "[" +
                ChatColor.YELLOW + "Fw" +
                ChatColor.GOLD + ChatColor.BOLD + "War" +
                ChatColor.DARK_GRAY + "]";
    }

    public static String chatHeader() {
        return  ChatColor.YELLOW + "oOo----------------[ " +
                ChatColor.GOLD + "FwTournament " +
                ChatColor.YELLOW + " ]----------------oOo ";
    }

    public static String formatSuccessMessage(String message) {
        message = ChatColor.GREEN + message;
        return message;
    }

    public static String formatErrorMessage(String message) {
        message = ChatColor.RED + message;
        return message;
    }

    public static String helpMessage() {
        String message = chatHeader();
        message = message.concat(
                "\n" + ChatColor.GRAY + ">> " + ChatColor.DARK_AQUA + "/war " + ChatColor.AQUA + "start " +
                        "\n" + ChatColor.GRAY + ">> " + ChatColor.DARK_AQUA + "/war " + ChatColor.AQUA + "plot "
        );
        return message;
    }

    public static String rewritePlaceholders(String input) {
        int i = 0;
        while (input.contains("{}")) {
            input = input.replaceFirst("\\{\\}", "{" + i++ + "}");
        }
        return input;
    }

}