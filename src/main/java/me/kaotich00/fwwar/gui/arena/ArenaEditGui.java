package me.kaotich00.fwwar.gui.arena;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.arena.Arena;
import me.kaotich00.fwwar.services.SimpleArenaService;
import me.kaotich00.fwwar.utils.LocationType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ArenaEditGui {

    private final Arena arena;
    private final Player player;

    public ArenaEditGui(Arena arena, Player player) {
        this.arena = arena;
        this.player = player;
    }

    public Gui prepareGui() {
        Gui mainGUI = new Gui(3, "Arena editing");

        mainGUI.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        mainGUI.addPane(background);

        /* Prepare Spawn Points Setters */
        StaticPane navigationPane = new StaticPane(1, 1, 7, 1);

        ItemStack firstNationSpawnPoint = firstNationSpawnPoint();
        navigationPane.addItem(new GuiItem(firstNationSpawnPoint, event -> {
            SimpleArenaService.getInstance().setPlayerArenaSetupStep(player, arena, SimpleArenaService.CreationStep.FIRST_NATION_SPAWN_POINT);
            Message.ARENA_CREATION_STEP.send(player,"First Nation Spawn");
            event.getWhoClicked().closeInventory();
        }), 0, 0);

        ItemStack firstNationBattlePoint = firstNationBattlePoint();
        navigationPane.addItem(new GuiItem(firstNationBattlePoint, event -> {
            SimpleArenaService.getInstance().setPlayerArenaSetupStep(player, arena, SimpleArenaService.CreationStep.FIRST_NATION_BATTLE_POINT);
            Message.ARENA_CREATION_STEP.send(player,"First Nation Battle");
            event.getWhoClicked().closeInventory();
        }), 2, 0);

        ItemStack secondNationSpawnPoint = secondNationSpawnPoint();
        navigationPane.addItem(new GuiItem(secondNationSpawnPoint, event -> {
            SimpleArenaService.getInstance().setPlayerArenaSetupStep(player, arena, SimpleArenaService.CreationStep.SECOND_NATION_SPAWN_POINT);
            Message.ARENA_CREATION_STEP.send(player,"Second Nation Spawn");
            event.getWhoClicked().closeInventory();
        }), 4, 0);

        ItemStack secondNationBattlePoint = secondNationBattlePoint();
        navigationPane.addItem(new GuiItem(secondNationBattlePoint, event -> {
            SimpleArenaService.getInstance().setPlayerArenaSetupStep(player, arena, SimpleArenaService.CreationStep.SECOND_NATION_BATTLE_POINT);
            Message.ARENA_CREATION_STEP.send(player,"Second Nation Battle");
            event.getWhoClicked().closeInventory();
        }), 6, 0);

        mainGUI.addPane(navigationPane);
        return mainGUI;
    }

    private ItemStack firstNationSpawnPoint() {
        ItemStack setFirstNationSpawnPoint = new ItemStack(Material.RED_WOOL);
        ItemMeta setFirstNationSpawnPointMeta = setFirstNationSpawnPoint.getItemMeta();
        setFirstNationSpawnPointMeta.setDisplayName(ChatColor.YELLOW + "First nation spawn Location");

        List<String> setFirstNationSpawnPointLore = new ArrayList<>();
        setFirstNationSpawnPointLore.add(ChatColor.GRAY + "Click to set\n");

        if(arena.getLocation(LocationType.FIRST_NATION_SPAWN_POINT) == null) {
            setFirstNationSpawnPointLore.add(ChatColor.RED + "✘ Spawn point not yet set");
        } else {
            setFirstNationSpawnPointLore.add(ChatColor.GREEN + "✔ Spawn point set");
        }

        setFirstNationSpawnPointMeta.setLore(setFirstNationSpawnPointLore);
        setFirstNationSpawnPoint.setItemMeta(setFirstNationSpawnPointMeta);

        return setFirstNationSpawnPoint;
    }

    private ItemStack firstNationBattlePoint() {
        ItemStack setFirstNationBattlePoint = new ItemStack(Material.RED_CONCRETE);
        ItemMeta setFirstNationBattlePointItemMeta = setFirstNationBattlePoint.getItemMeta();
        setFirstNationBattlePointItemMeta.setDisplayName(ChatColor.YELLOW + "First nation battle Location\n");

        List<String> setFirstNationSpawnPointLore = new ArrayList<>();
        setFirstNationSpawnPointLore.add(ChatColor.GRAY + "Click to set\n");

        if(arena.getLocation(LocationType.FIRST_NATION_BATTLE_POINT) == null) {
            setFirstNationSpawnPointLore.add(ChatColor.RED + "✘ Spawn point not yet set");
        } else {
            setFirstNationSpawnPointLore.add(ChatColor.GREEN + "✔ Spawn point set");
        }

        setFirstNationBattlePointItemMeta.setLore(setFirstNationSpawnPointLore);
        setFirstNationBattlePoint.setItemMeta(setFirstNationBattlePointItemMeta);

        return setFirstNationBattlePoint;
    }

    private ItemStack secondNationSpawnPoint() {
        ItemStack setSecondNationSpawnPoint = new ItemStack(Material.BLUE_WOOL);
        ItemMeta setSecondNationSpawnPointItemMeta = setSecondNationSpawnPoint.getItemMeta();
        setSecondNationSpawnPointItemMeta.setDisplayName(ChatColor.YELLOW + "Second nation spawn Location");

        List<String> setSecondNationSpawnPointLore = new ArrayList<>();
        setSecondNationSpawnPointLore.add(ChatColor.GRAY + "Click to set\n");

        if(arena.getLocation(LocationType.SECOND_NATION_SPAWN_POINT) == null) {
            setSecondNationSpawnPointLore.add(ChatColor.RED + "✘ Spawn point not yet set");
        } else {
            setSecondNationSpawnPointLore.add(ChatColor.GREEN + "✔ Spawn point set");
        }

        setSecondNationSpawnPointItemMeta.setLore(setSecondNationSpawnPointLore);
        setSecondNationSpawnPoint.setItemMeta(setSecondNationSpawnPointItemMeta);

        return setSecondNationSpawnPoint;
    }

    private ItemStack secondNationBattlePoint() {
        ItemStack setSecondNationBattlePoint = new ItemStack(Material.BLUE_CONCRETE);
        ItemMeta setSecondNationBattlePointItemMeta = setSecondNationBattlePoint.getItemMeta();
        setSecondNationBattlePointItemMeta.setDisplayName(ChatColor.YELLOW + "Second nation battle Location");

        List<String> setSecondNationBattleLore = new ArrayList<>();
        setSecondNationBattleLore.add(ChatColor.GRAY + "Click to set\n");

        if(arena.getLocation(LocationType.SECOND_NATION_BATTLE_POINT) == null) {
            setSecondNationBattleLore.add(ChatColor.RED + "✘ Spawn point not yet set");
        } else {
            setSecondNationBattleLore.add(ChatColor.GREEN + "✔ Spawn point set");
        }

        setSecondNationBattlePointItemMeta.setLore(setSecondNationBattleLore);
        setSecondNationBattlePoint.setItemMeta(setSecondNationBattlePointItemMeta);

        return setSecondNationBattlePoint;
    }

}
