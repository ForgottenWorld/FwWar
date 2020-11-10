package me.kaotich00.fwwar.war.assault;

public class AssaultWar {

    /**
     * Start war if all conditions are met
     * @param sender
     */
    /*public void startWar(CommandSender sender) {
        War war = this.currentWar;

        TownyAPI townyAPI = TownyAPI.getInstance();

        for(Nation nation: townyAPI.getDataSource().getNations()) {
            if(!nation.isNeutral()) {
                boolean canJoin = war.addParticipant(nation);
                if(canJoin) {
                    Message.NATION_JOIN_WAR.broadcast(nation.getName());
                } else {
                    Message.NATION_CANNOT_JOIN_WAR.broadcast(nation.getName());
                }
            }
        }

        // Check if the required amount of Nations is present
        if(war.getParticipantNations().size() < 2) {
            Message.NOT_ENOUGH_NATIONS.broadcast();
            return;
        }

        // Check if at least 2 Nations are considered enemies between each other
        boolean areThereEnemies = false;
        for(Nation nation: war.getParticipantNations()) {
            for(Nation plausibleEnemy: war.getParticipantNations()) {
                if(nation.hasEnemy(plausibleEnemy)) {
                    areThereEnemies = true;
                }
            }
        }

        if(!areThereEnemies) {
            Message.NO_ENEMY_NATION.broadcast();
            return;
        }

        Message.WAR_STARTED.broadcast();
        Bukkit.getServer().broadcastMessage(oldWar.getPrintableParticipants());

        this.currentWar = oldWar;
        SimpleScoreboardService.getInstance().initWarScoreboard();

        FileConfiguration defaultConfig = Fwwar.getDefaultConfig();
        Long seconds = defaultConfig.getLong("war.plot_check_time") * 20;

        warTaskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Fwwar.getPlugin(Fwwar.class), new WarPlotConquestTask(Fwwar.getPlugin(Fwwar.class), oldWar), seconds, seconds);

    }*/

}
