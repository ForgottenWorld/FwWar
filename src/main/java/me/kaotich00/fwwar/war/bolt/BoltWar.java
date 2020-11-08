package me.kaotich00.fwwar.war.bolt;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.utils.WarStatus;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BoltWar implements War {

    protected WarStatus warStatus;
    protected Map<UUID, Integer> killCount;
    protected List<Nation> nations;
    protected Map<Town, List<UUID>> players;
    protected List<UUID> deathQueue;

    @Override
    public int getMaxAllowedParticipants() {
        return 2;
    }

    @Override
    public boolean hasParticipantsLimit() {
        return true;
    }

    @Override
    public void setWarStatus(WarStatus warStatus) {
        this.warStatus = warStatus;
    }

    @Override
    public void addNation(Nation nation) {
        this.nations.add(nation);
    }

    @Override
    public void removeNation(Nation nation) {
        this.nations.remove(nation);
    }

    @Override
    public List<Nation> getParticipantsNations() {
        return nations;
    }

    @Override
    public Set<Town> getParticipantsTowns() {
        return this.players.keySet();
    }

    @Override
    public WarStatus getWarStatus() {
        return warStatus;
    }

    @Override
    public void incrementPlayerKillCount(Player player, int incrementBy) {
        if(!this.killCount.containsKey(player.getUniqueId())) {
            this.killCount.put(player.getUniqueId(), 0);
        }
        this.killCount.put(player.getUniqueId(), this.killCount.get(player.getUniqueId()) + incrementBy);
    }

    @Override
    public int getPlayerKillCount(Player player){
        return this.killCount.get(player.getUniqueId());
    }

    @Override
    public LinkedHashMap<UUID, Integer> getKillCountsLeaderboard(){
        LinkedHashMap<UUID, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : killCount.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, Comparator.comparingInt(o -> (int) o).reversed());
        for (Integer kills : list) {
            for (Map.Entry<UUID, Integer> entry : killCount.entrySet()) {
                if (entry.getValue().equals(kills)) {
                    sortedMap.put(entry.getKey(), kills);
                }
            }
        }
        return sortedMap;
    }

    @Override
    public void addPlayerToWar(Town town, UUID playerUUID) {
        if(!this.players.containsKey(town)) {
            this.players.put(town, new ArrayList<>());
        }
        this.players.get(town).add(playerUUID);
    }

    @Override
    public void removePlayerFromWar(Town town, UUID playerUUID) {
        if(!this.players.containsKey(town)) {
            this.players.put(town, new ArrayList<>());
        }
        this.players.get(town).remove(playerUUID);

        if(this.players.get(town).size() == 0) {
            this.players.remove(town);
        }
    }

    @Override
    public List<UUID> getParticipantsForTown(Town town) {
        return this.players.get(town);
    }

    @Override
    public void addPlayerToDeathQueue(Player player) {
        this.deathQueue.add(player.getUniqueId());
    }

    @Override
    public void removePlayerFromDeathQueue(Player player) {
        this.deathQueue.remove(player.getUniqueId());
    }

    @Override
    public boolean isPlayerInDeathQueue(Player player) {
        return this.deathQueue.contains(player.getUniqueId());
    }

    @Override
    public List<UUID> getParticipantPlayers() {
        List<UUID> playerList = new ArrayList<>();

        for(Map.Entry<Town, List<UUID>> entry : this.players.entrySet()) {
            List<UUID> currentList = entry.getValue();
            playerList.addAll(currentList);
        }

        return playerList;
    }

    @Override
    public Kit generateRandomKit() {

        String kitName = "Random Kit";
        Kit randomKit = new Kit(kitName);

        Material[] allowedHelmets = {Material.DIAMOND_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.LEATHER_HELMET, Material.NETHERITE_HELMET, Material.TURTLE_HELMET};
        Material[] allowedChestplates = {Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.IRON_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.NETHERITE_CHESTPLATE};
        Material[] allowedLeggings= {Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.IRON_LEGGINGS, Material.LEATHER_LEGGINGS, Material.NETHERITE_LEGGINGS};
        Material[] allowedBoots = {Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS, Material.GOLDEN_BOOTS, Material.IRON_BOOTS, Material.LEATHER_BOOTS, Material.NETHERITE_BOOTS};

        Material[] allowedSwords = {Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.NETHERITE_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD};
        Material[] allowedAxes = {Material.DIAMOND_AXE, Material.GOLDEN_AXE, Material.IRON_AXE, Material.NETHERITE_AXE, Material.STONE_AXE, Material.WOODEN_AXE};
        Enchantment[] allowedCrossbowEnchants = {Enchantment.MULTISHOT, Enchantment.QUICK_CHARGE};

        Material[] allowedFood = {Material.COOKED_PORKCHOP, Material.COOKED_CHICKEN, Material.COOKED_BEEF, Material.COOKED_COD, Material.COOKED_MUTTON, Material.COOKED_RABBIT, Material.COOKED_SALMON};

        PotionType[] allowedBuffPotions = {PotionType.REGEN, PotionType.SPEED, PotionType.INVISIBILITY};
        Material[] allowedPotionType = {Material.POTION, Material.SPLASH_POTION};

        PotionType[] allowedDebuffPotions = {PotionType.WEAKNESS, PotionType.SLOWNESS, PotionType.POISON};

        /* Armor enchants */
        ItemStack helmet = new ItemStack(allowedHelmets[ThreadLocalRandom.current().nextInt(allowedHelmets.length - 1)]);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, ThreadLocalRandom.current().nextInt(1, 4));
        randomKit.addItemToKit(helmet);

        ItemStack chestplate = new ItemStack(allowedChestplates[ThreadLocalRandom.current().nextInt(allowedChestplates.length - 1)]);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, ThreadLocalRandom.current().nextInt(1, 4));
        randomKit.addItemToKit(chestplate);

        ItemStack leggings = new ItemStack(allowedLeggings[ThreadLocalRandom.current().nextInt(allowedLeggings.length - 1)]);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, ThreadLocalRandom.current().nextInt(1, 4));
        randomKit.addItemToKit(leggings);

        ItemStack boots = new ItemStack(allowedBoots[ThreadLocalRandom.current().nextInt(allowedBoots.length - 1)]);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, ThreadLocalRandom.current().nextInt(1, 4));
        randomKit.addItemToKit(boots);

        ItemStack sword = new ItemStack(allowedSwords[ThreadLocalRandom.current().nextInt(allowedSwords.length - 1)]);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, ThreadLocalRandom.current().nextInt(1, 4));
        randomKit.addItemToKit(sword);

        ItemStack axe = new ItemStack(allowedAxes[ThreadLocalRandom.current().nextInt(allowedAxes.length - 1)]);
        axe.addEnchantment(Enchantment.DAMAGE_ALL, ThreadLocalRandom.current().nextInt(1, 4));
        randomKit.addItemToKit(axe);

        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        crossbow.addEnchantment(allowedCrossbowEnchants[ThreadLocalRandom.current().nextInt(0, allowedCrossbowEnchants.length - 1)], 1);
        randomKit.addItemToKit(crossbow);

        ItemStack arrows = new ItemStack(Material.ARROW, ThreadLocalRandom.current().nextInt(64));
        randomKit.addItemToKit(arrows);

        ItemStack food = new ItemStack(allowedFood[ThreadLocalRandom.current().nextInt(allowedFood.length - 1)], 64);
        randomKit.addItemToKit(food);

        ItemStack instantHealth = new ItemStack(Material.SPLASH_POTION, ThreadLocalRandom.current().nextInt(3, 8));
        PotionMeta meta = (PotionMeta) instantHealth.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL, false, true));
        instantHealth.setItemMeta(meta);
        randomKit.addItemToKit(instantHealth);

        for(int i = 0; i < 3; i ++) {
            ItemStack randomPotion = new ItemStack(allowedPotionType[ThreadLocalRandom.current().nextInt(allowedPotionType.length - 1)], 1);
            PotionMeta randomMeta = (PotionMeta) randomPotion.getItemMeta();
            randomMeta.setBasePotionData(new PotionData(allowedBuffPotions[ThreadLocalRandom.current().nextInt(allowedBuffPotions.length - 1)], ThreadLocalRandom.current().nextInt(0, 1) == 1 ? true : false, ThreadLocalRandom.current().nextInt(0, 1) == 1 ? true : false));
            randomPotion.setItemMeta(randomMeta);
            randomKit.addItemToKit(randomPotion);
        }

        for(int i = 0; i < 3; i ++) {
            ItemStack randomPotion = new ItemStack(Material.SPLASH_POTION, 1);
            PotionMeta randomMeta = (PotionMeta) randomPotion.getItemMeta();
            randomMeta.setBasePotionData(new PotionData(allowedDebuffPotions[ThreadLocalRandom.current().nextInt(allowedDebuffPotions.length - 1)], ThreadLocalRandom.current().nextInt(0, 1) == 1 ? true : false, ThreadLocalRandom.current().nextInt(0, 1) == 1 ? true : false));
            randomPotion.setItemMeta(randomMeta);
            randomKit.addItemToKit(randomPotion);
        }

        return randomKit;
    }

}
