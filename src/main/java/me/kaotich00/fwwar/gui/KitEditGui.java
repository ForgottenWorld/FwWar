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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitEditGui {

    private String kitName;
    private Player player;
    private Kit kit;

    public KitEditGui(String kitName, Player player) {
        this.kitName = kitName;
        this.player = player;

        War currentWar = SimpleWarService.getInstance().getCurrentWar().get();
        this.kit = SimpleWarService.getInstance().getKitForName(currentWar.getWarType(), kitName).get();
    }

    public Gui prepareGui() {
        War currentWar = SimpleWarService.getInstance().getCurrentWar().get();
        Gui mainGUI = new Gui(6, "Editing kit: " + ChatColor.RED + kitName);

        OutlinePane background = new OutlinePane(0, 4, 9, 2, Pane.Priority.LOWEST);
        background.addItem(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));
        background.setRepeat(true);
        background.setOnClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        mainGUI.addPane(background);

        OutlinePane items = new OutlinePane(0, 0, 9, 4, Pane.Priority.HIGH);
        for(ItemStack item: SimpleWarService.getInstance().getKitForName(currentWar.getWarType(), kitName).get().getItemsList()) {
            items.addItem(new GuiItem(item));
        }
        mainGUI.addPane(items);

        StaticPane navigationPane = new StaticPane(1, 5, 7, 1);

        ItemStack kitEditInfo = kitEditInfo();
        navigationPane.addItem(new GuiItem(kitEditInfo, inventoryClickEvent -> inventoryClickEvent.setCancelled(true)),1, 0);

        ItemStack kitConfirm = kitConfirm();
        navigationPane.addItem(new GuiItem(kitConfirm, event -> {
            this.kit.clearKit();

            Inventory inventory = this.player.getOpenInventory().getTopInventory();
            for( int i = 0; i < 36; i++ ) {
                ItemStack item = inventory.getItem(i);
                if(item != null) {
                    this.kit.addItemToKit(item);
                }
            }

            SimpleWarService.getInstance().updateKit(currentWar.getWarType(), this.kitName, this.kit);

            Message.KIT_MODIFIED.send(player);

            event.getWhoClicked().closeInventory();
        }), 3, 0);

        ItemStack kitCancel = kitCancel();
        navigationPane.addItem(new GuiItem(kitCancel, event -> {
            event.getWhoClicked().closeInventory();
        }), 5, 0);

        mainGUI.addPane(navigationPane);
        return mainGUI;
    }

    private ItemStack kitEditInfo() {
        ItemStack kitEditInfo = new ItemStack(Material.PAPER);
        ItemMeta kitEditInfoItemMeta = kitEditInfo.getItemMeta();
        kitEditInfoItemMeta.setDisplayName(ChatColor.YELLOW + "INFO");

        List<String> kitEditInfoLore = new ArrayList<>();
        kitEditInfoLore.add(ChatColor.GRAY + "Through this menu you can edit");
        kitEditInfoLore.add(ChatColor.GRAY + "and modify kits by adding the");
        kitEditInfoLore.add(ChatColor.GRAY + "necessary items in the top 36 slots");
        kitEditInfoItemMeta.setLore(kitEditInfoLore);
        kitEditInfo.setItemMeta(kitEditInfoItemMeta);

        return kitEditInfo;
    }

    private ItemStack kitConfirm() {
        ItemStack kitConfirm = new ItemStack(Material.NETHER_STAR);
        ItemMeta kitConfirmItemMeta = kitConfirm.getItemMeta();
        kitConfirmItemMeta.setDisplayName(ChatColor.GREEN + "Confirm kit");

        List<String> kitConfirmLore = new ArrayList<>();
        kitConfirmLore.add(ChatColor.GRAY + "Click to apply kit modifications");

        kitConfirmItemMeta.setLore(kitConfirmLore);
        kitConfirm.setItemMeta(kitConfirmItemMeta);

        return kitConfirm;
    }

    private ItemStack kitCancel() {
        ItemStack kitCancel = new ItemStack(Material.BARRIER);
        ItemMeta kitCancelItemMeta = kitCancel.getItemMeta();
        kitCancelItemMeta.setDisplayName(ChatColor.RED + "Cancel");

        List<String> kitCancelLore = new ArrayList<>();
        kitCancelLore.add(ChatColor.GRAY + "Click to exit kit modification");

        kitCancelItemMeta.setLore(kitCancelLore);
        kitCancel.setItemMeta(kitCancelItemMeta);

        return kitCancel;
    }

}
