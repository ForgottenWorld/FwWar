package me.kaotich00.fwwar.integration;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import it.forgottenworld.fwechelonapi.FWEchelonApi;
import it.forgottenworld.fwechelonapi.discourse.DiscoursePost;
import it.forgottenworld.fwechelonapi.services.DiscourseService;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.plot.CorePlot;
import me.kaotich00.fwwar.services.SimplePlotService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.WarTypes;
import me.kaotich00.fwwar.war.WarFactory;
import me.kaotich00.fwwar.war.assault.SiegeWar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DiscourseImporter {

    String topicId;
    CommandSender sender;

    public DiscourseImporter(String topicId, CommandSender sender) {
        this.sender = sender;
        this.topicId = topicId;
    }

    public void importData() {
        FWEchelonApi api = (FWEchelonApi) Bukkit.getPluginManager().getPlugin("FWEchelon");
        DiscourseService discourseIntegrationService = api.getDiscourseService();

        Message.IMPORTER_INFO.send(sender, "Collecting data from Topic ID: " + topicId);
        discourseIntegrationService.getPostsWithCustomNoticeTypeInTopic(Integer.parseInt(topicId), (postList) -> {
            SimpleWarService warService = SimpleWarService.getInstance();
            boolean firstNation = true;
            War importedWar = null;

            for(DiscoursePost post: postList) {
                DiscourseParser discourseParser = new DiscourseParser(post);
                Map<String,String> parsedValues = discourseParser.getParsedData();

                Message.IMPORTER_INFO.send(sender, "Found the following data: ");
                for(Map.Entry<String,String> entry: parsedValues.entrySet()) {
                    Message.IMPORTER_INFO.send(sender, ChatColor.YELLOW + entry.getKey() + ": " + ChatColor.WHITE + entry.getValue());
                }

                // Create a new war
                if(firstNation) {
                    Message.IMPORTER_INFO.send(sender, ChatColor.GREEN + "Creating a new war of type " + ChatColor.BOLD + parsedValues.get("tipologia"));

                    Message.IMPORTER_INFO.send(sender, ChatColor.GREEN + "Successfully created war of type " + ChatColor.BOLD + parsedValues.get("tipologia"));

                    // Add the first participant nation
                    importedWar = WarFactory.getWarForType(WarTypes.valueOf(parsedValues.get("tipologia")));

                    firstNation = false;
                }

                Message.IMPORTER_INFO.send(sender, "Adding nations");
                String nationName = parsedValues.get("nazione");

                try{
                    TownyAPI townyAPI = TownyAPI.getInstance();
                    Nation nation = townyAPI.getDataSource().getNation(nationName);

                    Message.IMPORTER_INFO.send(sender, "Adding nation " + ChatColor.GREEN + nation.getName() + ChatColor.GRAY + " to participants");

                    if(SimpleWarService.getInstance().getCurrentWar().get() instanceof SiegeWar) {
                        Set<Town> missingTowns = new HashSet<>();

                        SimplePlotService plotService = SimplePlotService.getInstance();
                        for (Town town : nation.getTowns()) {
                            Optional<CorePlot> townCorePlot = plotService.getCorePlotOfTown(town.getUuid());
                            if (!townCorePlot.isPresent()) {
                                missingTowns.add(town);
                            }
                        }

                        if (missingTowns.size() > 0) {
                            Message.WAR_CANNOT_START_NOT_ENOUGH_CORE_PLOTS.send(sender);
                            for (Town town : missingTowns) {
                                sender.sendMessage(net.md_5.bungee.api.ChatColor.DARK_AQUA + "" + net.md_5.bungee.api.ChatColor.BOLD + "  >> " + net.md_5.bungee.api.ChatColor.AQUA + "" + net.md_5.bungee.api.ChatColor.BOLD + town.getName());
                            }
                            return;
                        }

                        for(Town town: nation.getTowns()) {
                            plotService.getCorePlotOfTown(town.getUuid()).ifPresent(corePlot -> {
                                corePlot.setConquestPercentage(0);
                            });
                        }
                    }

                    Double price = Double.parseDouble(parsedValues.get("prezzo").replace("z", ""));
                    if(!nation.getAccount().canPayFromHoldings(price)) {
                        Message.NATION_NOT_ENOUGH_MONEY.send(sender, nation.getName());
                        Message.IMPORTER_INFO.send(sender, ChatColor.RED + "Exiting, please fix the above errors");
                        return;
                    }
                    nation.getAccount().withdraw(price, "Joining the war");

                    Message.IMPORTER_INFO.send(sender, ChatColor.GREEN + "Successfully added nation " + ChatColor.GOLD + nationName);
                    importedWar.addNation(nation);

                    String[] participants = parsedValues.get("partecipanti").replace("[", "").replace("]", "").split(",");
                    for(String playerName: participants) {
                        Message.IMPORTER_INFO.send(sender, "Adding resident " + ChatColor.GREEN + playerName + ChatColor.GRAY + " to participants");
                        Resident resident = townyAPI.getDataSource().getResident(playerName);

                        importedWar.addPlayerToWar(resident.getTown(), resident.getUUID());
                        Message.IMPORTER_INFO.send(sender, ChatColor.GREEN + "Successfully added " + ChatColor.GREEN + playerName + ChatColor.GREEN + " to participants");
                    }
                } catch (NotRegisteredException e) {
                    Message.IMPORTER_INFO.send(sender, ChatColor.RED + "Nation or player not registered on Towny, please fix the name and run the importer again");
                    return;
                } catch (EconomyException e) {
                    warService.deleteWar();
                }

                if(postList.indexOf(post) == postList.size() - 1) {
                    Message.IMPORTER_INFO.send(sender, ChatColor.GREEN + "War successfully imported, you may now proceed with default commands for the war type");
                    warService.setCurrentWar(importedWar);
                }
            }
        }, (error) -> {
            sender.sendMessage(error);
        });
    }

}
