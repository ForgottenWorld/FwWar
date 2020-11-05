package me.kaotich00.fwwar.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.services.SimpleWarService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KitSelectionGui {

    private Player player;

    public KitSelectionGui(Player player) {
        this.player = player;
    }

    public Gui prepareGui() {
        War currentWar = SimpleWarService.getInstance().getCurrentWar().get();
        Gui mainGUI = new Gui(6, ChatColor.YELLOW + "Click on a kit to choose it");

        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        mainGUI.addPane(background);

        OutlinePane kits = new OutlinePane(1, 1, 7, 4, Pane.Priority.HIGH);
        for(Kit kit: currentWar.getKits()) {
            ItemStack kitItem = kitBuilder(kit);
            kits.addItem(new GuiItem(kitItem, event -> {
                event.setCancelled(true);

                Inventory inventory = event.getClickedInventory();
                int rawSlot = event.getRawSlot();

                ItemStack itemStack = inventory.getItem(rawSlot);
                String kitName = itemStack.getItemMeta().getDisplayName();

                Optional<Kit> optSelectedKit = currentWar.getKitForName(kitName);
                optSelectedKit.ifPresent(selectedKit -> {
                    currentWar.setPlayerKit(player, selectedKit);
                    Message.KIT_SELECTED.send(player, kitName);
                });

                player.getOpenInventory().close();
            }));
        }
        mainGUI.addPane(kits);
        return mainGUI;
    }

    private ItemStack kitBuilder(Kit kit) {
        ItemStack kitBuilder = kit.getItemsList().size() > 0 ? kit.getItemsList().get(0) : new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta kitBuilderItemMeta = kitBuilder.getItemMeta();
        kitBuilderItemMeta.setDisplayName(kit.getName());

        List<String> kitBuilderLore = new ArrayList<>();
        for(ItemStack item: kit.getItemsList()) {
            kitBuilderLore.add(ChatColor.GREEN + item.getI18NDisplayName() + " x " + item.getAmount());
        }
        kitBuilderItemMeta.setLore(kitBuilderLore);
        kitBuilderItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        kitBuilder.setItemMeta(kitBuilderItemMeta);

        return kitBuilder;
    }

}
