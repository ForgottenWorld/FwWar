package me.kaotich00.fwwar.war.bolt;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.api.war.KitWar;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.objects.war.ParticipantNation;
import me.kaotich00.fwwar.objects.war.ParticipantTown;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarStatus;
import me.kaotich00.fwwar.war.AbstractWar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public abstract class BoltWar extends AbstractWar implements KitWar {

    @Override
    public int getMaxAllowedParticipants() {
        return 2;
    }

    @Override
    public boolean hasParticipantsLimit() {
        return true;
    }

    @Override
    public void stopWar() {
        for(ParticipantNation participantNation: this.getNations()) {
            for(ParticipantTown participantTown: participantNation.getTowns()) {
                Set<UUID> residents = participantTown.getPlayers();
                Town town = participantTown.getTown();

                for(UUID uuid: residents) {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player != null) {
                        player.getInventory().clear();
                        player.getInventory().setArmorContents(null);

                        try {
                            player.teleport(town.getSpawn());
                        } catch (TownyException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        setWarStatus(WarStatus.ENDED);
    }

    @Override
    public void handlePlayerDeath(Player player) {
        getDeathQueue().addPlayer(player);

        try {
            TownyAPI townyAPI = TownyAPI.getInstance();

            Resident resident = townyAPI.getDataSource().getResident(player.getName());
            Town town = resident.getTown();

            if(!hasResident(resident)) return;

            getNation(town.getNation().getUuid())
                    .getTown(town.getUuid())
                    .removePlayer(player.getUniqueId());

            Message.WAR_PLAYER_DEFEATED.send(player);
            Message.WAR_PLAYER_DEATH.broadcast(player.getName(), town.getName());

            boolean shouldRemoveNation = getNation(town.getNation().getUuid()).getTowns().size() == 0;

            if(shouldRemoveNation) {
                removeNation(town.getNation());
                Message.NATION_DEFEATED.broadcast(town.getNation().getName());
            }

            if(!hasEnoughParticipants())
                SimpleWarService.getInstance().stopWar();

        } catch (
                TownyException ignored) {
        }
    }

}
