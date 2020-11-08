package me.kaotich00.fwwar.message;

import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.Bukkit;
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

    CORE_PLOT_ALREADY_PRESENT(MessageUtils.formatErrorMessage("Your town already has a Core Plot!"), true),
    CORE_PLOT_SUCCESSFULLY_SET(MessageUtils.formatSuccessMessage("Successfully set town core Plot!"), true),
    NOT_A_TOWN_BLOCK(MessageUtils.formatErrorMessage("You are not in a town block"), true),
    NOT_YOUR_TOWN_BLOCK(MessageUtils.formatErrorMessage("This is not your town"), true),
    NOT_AN_OUTPOST(MessageUtils.formatErrorMessage("The core plot must be in an outpost"), true),

    /* War related */
    WAR_NOT_FOUND(MessageUtils.formatErrorMessage("There is no war open at the moment"), true),
    WAR_ALREADY_PRESENT(MessageUtils.formatErrorMessage("A war is already open at the moment"), true),
    WAR_STARTED(MessageUtils.formatSuccessMessage("The war has been started!"), true),
    NOT_ENOUGH_NATIONS(MessageUtils.formatErrorMessage("Not enough nations to start the war. Can't start"), true),
    NO_ENEMY_NATION(MessageUtils.formatErrorMessage("There are no enemy nations in this war. Can't start."), true),
    NATION_CANNOT_JOIN_WAR(ChatColor.GOLD + "{}" + MessageUtils.formatErrorMessage(" can't join the war. No towns found with core block!"), true),
    NATION_JOIN_WAR("Nation " + ChatColor.GOLD + "{}" + MessageUtils.formatSuccessMessage(" joined the war!"), true),
    TOWN_CONQUER_STATUS(MessageUtils.formatSuccessMessage("{} is under attack! {}% reamining before defeat"), true),
    NATION_DEFEATED(MessageUtils.formatErrorMessage("The Nation {} has been defeated"), true),
    WAR_ENDED(MessageUtils.formatSuccessMessage("The war has ended!"), true),
    WAR_DOES_NOT_SUPPORT_KIT(MessageUtils.formatErrorMessage("The current war does not support kits."), true),
    WAR_AT_LEAST_TWO_NATION(MessageUtils.formatErrorMessage("There must be at least two nations to confirm the war. To add one use the command ") + ChatColor.YELLOW + "/war add <nation>" , true),
    FACTION_WAR_NOT_ENOUGH_KITS(MessageUtils.formatErrorMessage("There must be at least one kit to confirm the war. You can modify them via the command ") + ChatColor.YELLOW + "/war kit", true),
    WAR_CONFIRMED(MessageUtils.formatSuccessMessage("War successfully confirmed, from now on, you will not be able to modify anything. Type ") + ChatColor.YELLOW + "/war start" + MessageUtils.formatSuccessMessage(" when you will need to start the war."), true),
    WAR_ALREADY_CONFIRMED(MessageUtils.formatErrorMessage("The war is confirmed, you can no longer edit anything."), true),
    WAR_ALREADY_STARTED(MessageUtils.formatErrorMessage("The war is started, you can no longer edit anything."), true),
    WAR_MUST_BE_CONFIRMED(MessageUtils.formatErrorMessage("The war must be confirmed."), true),
    WAR_MUST_BE_STARTED(MessageUtils.formatErrorMessage("The war must be started."), true),
    WAR_CANNOT_DROP_ITEMS(MessageUtils.formatErrorMessage("You cannot drop items during the war."), true),
    WAR_CANNOT_CHOOSE_KIT(MessageUtils.formatErrorMessage("Cannot choose kit at the moment."), true),
    WAR_PLAYER_DEFEATED(MessageUtils.formatErrorMessage("You have been killed in the war. Teleporting to town spawn..."), true),
    WAR_CANNOT_START_KIT_REQUIRED(MessageUtils.formatErrorMessage("Cannot start war, the following player have not chosen a kit. Please type ") + ChatColor.YELLOW + " /war chooseKit" + MessageUtils.formatErrorMessage(" to select one"), true),

    KIT_ALREADY_EXIST(MessageUtils.formatErrorMessage("A kit for the name ") + ChatColor.GOLD + "{}" + MessageUtils.formatErrorMessage(" already exists"), true),
    KIT_CREATED(MessageUtils.formatSuccessMessage("Successfully created kit ") + ChatColor.GOLD + "{}" + MessageUtils.formatSuccessMessage(". You will be able to edit it in a few seconds."), true),
    KIT_MODIFIED(MessageUtils.formatSuccessMessage("Successfully modified kit "), true),
    NO_KIT(MessageUtils.formatErrorMessage("No kits to be found."), true),

    NATION_ALREADY_PRESENT(MessageUtils.formatErrorMessage("The nation you specified is already part of the war"), true),
    NATION_NOT_PRESENT(MessageUtils.formatErrorMessage("The nation you specified is not part of the war"), true),
    NATION_DOES_NOT_EXISTS(MessageUtils.formatErrorMessage("The nation you specified is not registered on Towny"), true),
    NATION_SUCCESSFULLY_ADDED(MessageUtils.formatSuccessMessage("Successfully added Nation {} to participants"), true),
    NATION_SUCCESSFULLY_REMOVED(MessageUtils.formatSuccessMessage("Successfully removed Nation {} from participants"), true),
    CANNOT_ADD_MORE_NATIONS(MessageUtils.formatErrorMessage("The maximum number of allowed nations has been reached. Consider deleting one."), true),

    NO_LONGER_PART_OF_WAR(MessageUtils.formatErrorMessage("Sorry, it seems like you are no longer part of the war."), true),
    KIT_SELECTED(MessageUtils.formatSuccessMessage("Successfully selected kit ") + ChatColor.GOLD + "{}", true),

    /* Miscellaneous */
    CONFIG_RELOADED(MessageUtils.formatSuccessMessage("Successfully reloaded config.yml"), true);

    private final String message;
    private final boolean showPrefix;

    Message(String message, boolean showPrefix) {
        this.message = MessageUtils.rewritePlaceholders(message);
        this.showPrefix = showPrefix;
    }

    public void send(CommandSender sender, Object... objects) {
        sender.sendMessage(asString(objects));
    }

    public void broadcast(Object... objects) {
        Bukkit.getServer().broadcastMessage(asString(objects));
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
