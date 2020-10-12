package me.kaotich00.fwwar.objects.war;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class War {

    Map<Nation, Set<Town>> participantTowns;
    Set<Nation> participantNations;

    public enum Status {
        WAR_IDLE,
        WAR_STARTED,
        WAR_ENDED
    }
    Status warStatus;

    public War() {
        this.participantTowns = new HashMap<>();
        this.participantNations = new HashSet<>();
        this.warStatus = Status.WAR_IDLE;
    }

    /**
     * Add a Nation as participant
     * @param nation
     * @return true if at least one town is allowed per nation, false otherwise
     */
    public boolean addParticipant(Nation nation) {
        this.participantNations.add(nation);

        Set<Town> allowedTowns = new HashSet<>();

        SimplePlotService plotService = SimplePlotService.getInstance();
        for(Town town: nation.getTowns()) {
            plotService.getCorePlotOfTown(town.getUuid()).ifPresent(corePlot -> {
                allowedTowns.add(town);
            });
        }

        if(allowedTowns.size() > 0) {
            this.participantTowns.put(nation, allowedTowns);
            return true;
        }

        return false;
    }

    /**
     * Get all participant nations
     * @return Set<Nation>
     */
    public Set<Nation> getParticipantNations() {
        return this.participantNations;
    }

    /**
     * Get the list of allowed participant towns for a given Nation
     * @param nation
     * @return Set<Town>
     */
    public Set<Town> getParticipantTownsForNation(Nation nation) {
        return this.participantTowns.get(nation);
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
