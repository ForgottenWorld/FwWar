package me.kaotich00.fwwar.services;

import me.kaotich00.fwwar.api.services.KitService;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarTypes;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SimpleKitService implements KitService {

    private static SimpleKitService instance;
    private final Map<WarTypes, Map<String, Kit>> kits;

    private SimpleKitService() {
        if(instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.kits = new HashMap<>();
    }

    public static SimpleKitService getInstance() {
        if(instance == null) {
            instance = new SimpleKitService();
        }
        return instance;
    }

    public void addKit(WarTypes warType, Kit kit){
        if(!this.kits.containsKey(warType)) {
            this.kits.put(warType, new HashMap<>());
        }
        this.kits.get(warType).put(kit.getName(), kit);
    }

    public void removeKit(WarTypes warType, String kitName) {
        this.kits.get(warType).remove(kitName);
    }

    public void updateKit(WarTypes warType, String kitName, Kit kit) {
        this.kits.get(warType).put(kitName, kit);
    }

    public List<Kit> getKitsForType(WarTypes warType) {
        if(this.kits.get(warType) == null) return Collections.EMPTY_LIST;
        return new ArrayList<>(this.kits.get(warType).values());
    }

    public Optional<Kit> getKitForName(WarTypes warType, String name) {
        if(!this.kits.containsKey(warType)) {
            this.kits.put(warType, new HashMap<>());
        }
        return Optional.ofNullable(this.kits.get(warType).get(name));
    }

    @Override
    public Map<Kit, WarTypes> getAllKits() {
        Map<Kit, WarTypes> kits = new HashMap<>();
        for(Map.Entry<WarTypes, Map<String,Kit>> entry : this.kits.entrySet()) {
            for(Map.Entry<String,Kit> entry2 : entry.getValue().entrySet()) {
                kits.put(entry2.getValue(), entry.getKey());
            }
        }
        return kits;
    }

    public Kit generateRandomKit() {

        String kitName = "Random Kit";
        Kit randomKit = new Kit(kitName);

        Material[] allowedHelmets = {Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET};
        Material[] allowedChestplates = {Material.CHAINMAIL_CHESTPLATE,  Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE};
        Material[] allowedLeggings= {Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS};
        Material[] allowedBoots = {Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS};
        Material[] allowedSwords = {Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD};
        Material[] allowedAxes = {Material.STONE_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE};
        Material[] allowedFood = {Material.CARROT, Material.BAKED_POTATO, Material.COOKED_BEEF, Material.GOLDEN_CARROT};
        int[] allowedHealPotionAmount = {4, 8, 12, 16};

        int armorType = ThreadLocalRandom.current().nextInt(allowedHelmets.length - 1);
        int armorEnchantLevel = ThreadLocalRandom.current().nextInt(1, 4);
        ItemStack helmet = new ItemStack(allowedHelmets[armorType]);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, armorEnchantLevel);
        randomKit.addItemToKit(helmet);

        ItemStack chestplate = new ItemStack(allowedChestplates[armorType]);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, armorEnchantLevel);
        randomKit.addItemToKit(chestplate);

        ItemStack leggings = new ItemStack(allowedLeggings[armorType]);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, armorEnchantLevel);
        randomKit.addItemToKit(leggings);

        ItemStack boots = new ItemStack(allowedBoots[armorType]);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, armorEnchantLevel);
        randomKit.addItemToKit(boots);

        // Select a random sword from the allowed ones
        int swordSlot = ThreadLocalRandom.current().nextInt(allowedSwords.length - 1);
        // Random sharpness enchantment between 1 and 4
        int swordSharpness = ThreadLocalRandom.current().nextInt(1, 4);
        // Apply material and sharpness to the sword
        ItemStack sword = new ItemStack(allowedSwords[swordSlot]);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, swordSharpness);
        randomKit.addItemToKit(sword);

        // Random 64 x food between the allowed ones
        ItemStack food = new ItemStack(allowedFood[ThreadLocalRandom.current().nextInt(allowedFood.length - 1)], 64);
        randomKit.addItemToKit(food);

        // Instant Health potions of random amount between 4, 8, 12, 16
        ItemStack healPotion = new ItemStack(Material.SPLASH_POTION, allowedHealPotionAmount[ThreadLocalRandom.current().nextInt(allowedHealPotionAmount.length - 1)]);
        PotionMeta potionmeta = (PotionMeta) healPotion.getItemMeta();
        potionmeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
        healPotion.setItemMeta(potionmeta);
        randomKit.addItemToKit(healPotion);

        // Bonus items between possible ones
        int bonusItemRandom = ThreadLocalRandom.current().nextInt(1, 4);
        switch(bonusItemRandom) {
            case 1:
                ItemStack bow = new ItemStack(Material.BOW, 1);
                bow.addEnchantment(Enchantment.ARROW_DAMAGE, swordSlot + 1);
                randomKit.addItemToKit(bow);

                ItemStack arrows = new ItemStack(Material.ARROW, 45);
                randomKit.addItemToKit(arrows);
                break;
            case 2:
                ItemStack shield = new ItemStack(Material.SHIELD, 1);
                randomKit.addItemToKit(shield);
                break;
            case 3:
                ItemStack axe = new ItemStack(allowedAxes[swordSlot]);
                axe.addEnchantment(Enchantment.DAMAGE_ALL, swordSharpness);
                randomKit.addItemToKit(axe);
                break;
            case 4:
                ItemStack speedPotions = new ItemStack(Material.POTION, 3);
                PotionMeta speedPotionMeta = (PotionMeta) speedPotions.getItemMeta();
                speedPotionMeta.setBasePotionData(new PotionData(PotionType.SPEED, false, false));
                speedPotions.setItemMeta(speedPotionMeta);
                randomKit.addItemToKit(speedPotions);
                break;
        }

        return randomKit;
    }

}
