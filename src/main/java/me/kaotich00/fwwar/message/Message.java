package me.kaotich00.fwwar.message;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.locale.LocalizationManager;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public enum Message {

    PREFIX("fwwar_prefix", false),

    HELP_MESSAGE("help_message", false),

    NOT_REGISTERED("not_registered", true),
    NOT_PART_OF_A_TOWN("not_part_of_a_town", true),
    NOT_A_MAJOR("not_a_major", true),

    CORE_PLOT_ALREADY_PRESENT("core_plot_already_present", true),
    CORE_PLOT_SUCCESSFULLY_SET("core_plot_successfully_set", true),
    NOT_A_TOWN_BLOCK("not_a_town_block", true),
    NOT_YOUR_TOWN_BLOCK("not_your_town_block", true),
    NOT_AN_OUTPOST("not_an_outpost", true),

    /* War related */
    WAR_NOT_FOUND("war_not_found", true),
    WAR_ALREADY_PRESENT("war_already_present", true),
    WAR_STARTED("war_started", true),
    NOT_ENOUGH_NATIONS("war_not_enough_nations", true),
    NO_ENEMY_NATION("war_no_enemy_nation", true),
    NATION_CANNOT_JOIN_WAR("war_nation_cannot_join_war", true),
    NATION_JOIN_WAR("war_nation_join_war", true),
    TOWN_CONQUER_STATUS("war_town_conquer_status", true),
    TOWN_DEFEATED("war_town_defeated", true),
    TOWN_DEFEATED_SIEGE_WAR("war_town_defeated_siege_war", true),
    NATION_DEFEATED("war_nation_defeated", true),
    WAR_ENDED("war_ended", true),
    WAR_DOES_NOT_SUPPORT_KIT("war_does_not_support_kit", true),
    WAR_AT_LEAST_TWO_NATION("war_at_least_two_nation", true),
    FACTION_WAR_NOT_ENOUGH_KITS("war_faction_not_enough_kits", true),
    WAR_CONFIRMED("war_confirmed", true),
    WAR_ALREADY_CONFIRMED("war_already_confirmed", true),
    WAR_ALREADY_STARTED("war_already_started", true),
    WAR_MUST_BE_CONFIRMED("war_must_be_confirmed", true),
    WAR_MUST_BE_STARTED("war_must_be_started", true),
    WAR_CANNOT_DROP_ITEMS("war_cannot_drop_items", true),
    WAR_CANNOT_CHOOSE_KIT("war_cannot_choose_kit", true),
    WAR_PLAYER_DEFEATED("war_player_defeated", true),
    WAR_CANNOT_START_KIT_REQUIRED("war_cannot_start_kit_required", true),
    WAR_CANNOT_START_ARENA_REQUIRED("war_cannot_start_arena_required", true),
    WAR_CANNOT_START_NOT_ENOUGH_CORE_PLOTS("war_cannot_start_not_enough_core_plot", true),
    WAR_WILL_BEGAN("war_will_began", true),
    WAR_CREATION_MENU("war_creation_menu", false),
    WAR_CREATION_BOLT_WAR("war_creation_bolt_war", false),
    WAR_CREATION_BOLT_WAR_FACTION("war_creation_bolt_war_faction", false),
    WAR_CREATION_BOLT_WAR_RANDOM("war_creation_bolt_war_random", false),
    WAR_CREATION_ASSAULT_WAR("war_creation_assault_war", false),
    WAR_CREATION_ASSAULT_WAR_CLASSIC("war_creation_assault_war_classic", false),
    WAR_CREATION_ASSAULT_WAR_SIEGE("war_creation_assault_war_siege", false),
    WAR_MUST_SELECT_ARENA("war_must_select_arena", true),

    KIT_ALREADY_EXIST("kit_already_exists", true),
    KIT_CREATED("kit_created", true),
    KIT_MODIFIED("kit_modified", true),
    NO_KIT("no_kit", true),
    KIT_SELECTED("kit_selected", true),
    KIT_MENU("kit_menu", false),
    KIT_NAME_SELECTION("kit_name_selection", false),
    KIT_EDITOR("kit_editor", false),

    ARENA_ALREADY_EXISTS("arena_already_exists", true),
    ARENA_NOT_FOUND("arena_not_found", true),
    ARENA_CREATED("arena_created", false),
    ARENA_CREATION_STEP_COMPLETED("arena_creation_step_completed", true),
    ARENA_CREATION_STEP("arena_creation_step", true),
    ARENA_CREATION_COMPLETED("arena_creation_completed", true),
    ARENA_DELETED("arena_deleted", true),
    ARENA_SELECTED("arena_selected", true),

    NATION_ALREADY_PRESENT("nation_already_present", true),
    NATION_NOT_PRESENT("nation_not_present", true),
    NATION_DOES_NOT_EXISTS("nation_does_not_exists", true),
    NATION_SUCCESSFULLY_ADDED("nation_successfully_added", true),
    NATION_SUCCESSFULLY_REMOVED("nation_successfully_removed", true),
    NATION_NOT_ENOUGH_MONEY("nation_not_enough_money", true),
    NATION_PAYED_ENTRY( "nation_payed_entry", true),
    CANNOT_ADD_MORE_NATIONS("nation_cannot_add_more_nations", true),

    NO_LONGER_PART_OF_WAR("no_longer_part_of_war", true),

    /* Miscellaneous */
    CONFIG_RELOADED("config_reloaded", true),
    ECHELON_MISSING("echelon_missing", true),
    IMPORTER_INFO("importer_info", false);

    private final String message;
    private final boolean showPrefix;
    private final LocalizationManager localizationManager;

    Message(String message, boolean showPrefix) {
        this.message = MessageUtils.rewritePlaceholders(message);
        this.showPrefix = showPrefix;
        this.localizationManager = Fwwar.getLocalizationManager();
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
        String string = localizationManager.localize(this.message);
        if(this.showPrefix) {
            string = localizationManager.localize(PREFIX.message) + " " + string;
        }
        for (int i = 0; i < objects.length; i++) {
            Object o = objects[i];
            string = string.replace("{" + i + "}", String.valueOf(o));
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
