package me.kaotich00.fwwar.objects.kit;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    String name;
    private final List<ItemStack> itemsList;

    public Kit(String name) {
        this.name = name;
        this.itemsList = new ArrayList<>();
    }

    public List<ItemStack> getItemsList() {
        return this.itemsList;
    }

    public void addItemToKit(ItemStack item) {
        this.itemsList.add(item);
    }

    public void clearKit() {
        this.itemsList.clear();
    }

    public String getName() {
        return this.name;
    }

}
