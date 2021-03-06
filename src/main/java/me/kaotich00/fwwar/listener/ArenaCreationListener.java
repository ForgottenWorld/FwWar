package me.kaotich00.fwwar.listener;

import me.kaotich00.fwwar.services.SimpleArenaService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ArenaCreationListener  implements Listener {

    @EventHandler
    public void onRightClickSelection(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if(event.getItem() == null || event.getItem().getType() != Material.WOODEN_SHOVEL) {
            return;
        }

        if(!event.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        Player player = event.getPlayer();
        if(!SimpleArenaService.getInstance().isPlayerInCreationMode(player)) {
            return;
        }

        event.setCancelled(true);

        SimpleArenaService simpleArenaService = SimpleArenaService.getInstance();
        simpleArenaService.arenaCreationHandler(player, event.getClickedBlock().getLocation().add(0,1,0));
    }

}
