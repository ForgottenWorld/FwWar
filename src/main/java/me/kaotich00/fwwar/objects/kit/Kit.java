package me.kaotich00.fwwar.objects.kit;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Kit {

    private final String name;
    private final List<ItemStack> itemsList;
    private boolean required;
    private int quantity;

    public Kit(String name) {
        this.name = name;
        this.itemsList = new ArrayList<>();
        this.required = false;
        this.quantity = -1;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}
