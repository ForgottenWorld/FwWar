package me.kaotich00.fwwar.objects.war;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class War {

    Map<Nation, Set<Town>> participants;

    public enum Status {
        WAR_IDLE,
        WAR_STARTED,
        WAR_ENDED
    }
    Status warStatus;

    public War() {
        this.participants = new HashMap<>();
        this.warStatus = Status.WAR_IDLE;
    }

    /**
     * Add a Nation as participant
     * @param nation
     * @return true if at least one town is allowed per nation, false otherwise
     */
    public boolean addParticipant(Nation nation) {
        this.participants.put(nation, new HashSet<>());

        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();
        int conquestPercentage = 0;

        Set<Town> allowedTowns = new HashSet<>();

        SimplePlotService plotService = SimplePlotService.getInstance();
        for(Town town: nation.getTowns()) {
            plotService.getCorePlotOfTown(town.getUuid()).ifPresent(corePlot -> {
                corePlot.setConquestPercentage(conquestPercentage);
                allowedTowns.add(town);
            });
        }

        if(allowedTowns.size() > 0) {
            this.participants.put(nation, allowedTowns);
            return true;
        }

        return false;
    }

    /**
     * Remove a nation from participants list
     * @param nation
     */
    public void removeParticipant(Nation nation) {
        this.participants.remove(nation);
    }

    /**
     * Remove a town from participants. The town is defeated.
     * @param town
     */
    public void setTownDefeated(Nation nation, Town town) {
        this.participants.get(nation).remove(town);

        if(this.participants.get(nation).size() == 0) {
            this.removeParticipant(nation);
        }
    }

    /**
     * Get all participant nations
     * @return Set<Nation>
     */
    public Set<Nation> getParticipantNations() {
        return this.participants.keySet();
    }

    /**
     * Get the list of allowed participant towns for a given Nation
     * @param nation
     * @return Set<Town>
     */
    public Set<Town> getParticipantTownsForNation(Nation nation) {
        return this.participants.get(nation);
    }

    /**
     * Set the current status for the war
     * @param status
     */
    public void setStatus(Status status) {
        this.warStatus = status;
    }

    /**
     * Return the current status of the war
     * @return
     */
    public Status getWarStatus() {
        return this.warStatus;
    }

    /**
     * Return a well formatted message to recap pre-init war
     * @return
     */
    public String getPrintableParticipants() {
        String printableMessage = MessageUtils.chatHeader();
        for(Nation participant: this.getParticipantNations()) {
            printableMessage = printableMessage.concat(ChatColor.YELLOW + ">> " + ChatColor.GOLD + participant.getName() + MessageUtils.EOL);
            for(Town town: getParticipantTownsForNation(participant)) {
                printableMessage = printableMessage.concat("    " + ChatColor.DARK_AQUA + ">> " + ChatColor.AQUA + town.getName() + MessageUtils.EOL);
            }
        }
        return printableMessage;
    }

}
