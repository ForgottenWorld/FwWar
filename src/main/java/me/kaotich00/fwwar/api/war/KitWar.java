package me.kaotich00.fwwar.api.war;

import me.kaotich00.fwwar.objects.kit.Kit;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface KitWar extends War {

    /**
     * Set a specific Kit for the given Player
     * @param player the Player to give the kit
     * @param kit the kit to be given
     */
    void setPlayerKit(Player player, Kit kit);

    /**
     * Get a Kit for the given player
     * @param player the Player to be searching for the Kit
     * @return an Optional of Kit. If no kit is found, Optional.Empty() is returned
     */
    Optional<Kit> getPlayerKit(Player player);

}
