package me.kaotich00.fwwar.cui;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.MessageUtils;
import me.kaotich00.fwwar.utils.WarTypes;
import me.kaotich00.fwwar.war.WarFactory;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.Collections;

public class WarCreationPrompt implements ConversationAbandonedListener {

    private ConversationFactory conversationFactory;

    public WarCreationPrompt(Fwwar plugin) {
        this.conversationFactory = new ConversationFactory(plugin)
                .withModality(false)
                .withFirstPrompt(new WarMenuPrompt())
                .withEscapeSequence("cancel")
                .withTimeout(60)
                .thatExcludesNonPlayersWithMessage("Only players can run this conversation")
                .addConversationAbandonedListener(this);
    }

    public void startConversationForPlayer(Player player) {
        conversationFactory.buildConversation(player).begin();
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
        if (abandonedEvent.gracefulExit()) {
            abandonedEvent.getContext().getForWhom().sendRawMessage(MessageUtils.formatSuccessMessage("Setup completed!"));
        } else {
            abandonedEvent.getContext().getForWhom().sendRawMessage(MessageUtils.formatErrorMessage("Setup canceled!"));
        }
    }

    private class WarMenuPrompt extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatColor.YELLOW + String.join("", Collections.nCopies(53, "-")) +
                    ChatColor.GREEN + " Welcome to War Creation manager!\n" +
                    ChatColor.GRAY + " Start by selecting one of the following war types: \n" +
                    ChatColor.YELLOW + "\n [1] " + ChatColor.GOLD + "Bolt war\n" +
                    ChatColor.YELLOW + " [2] " + ChatColor.GOLD + "Assault war " + ChatColor.RED + "[Under development, currently unavailable] \n" +
                    ChatColor.YELLOW + "\n" + String.join("", Collections.nCopies(53, "-"));
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            switch (input.intValue()) {
                case 1:
                    return new BoltWarGamemode();
                case 2:
                    return Prompt.END_OF_CONVERSATION;
            }
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.intValue() > 0 && input.intValue() <= 2;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
            return "Input number must be equal or greater than 2";
        }

    }

    private class BoltWarGamemode extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = ChatColor.DARK_AQUA + String.join("", Collections.nCopies(53, "-")) +
                    ChatColor.GREEN + " You selected " + ChatColor.GOLD + "Bolt War!\n" +
                    ChatColor.GRAY + " Please choose a gamemode for the war: \n" +
                    ChatColor.AQUA + "\n [1] " + ChatColor.DARK_AQUA + "Faction kit\n" +
                    ChatColor.AQUA + " [2] " + ChatColor.DARK_AQUA + "Random kits\n" + ChatColor.RED + "[Under development, currently unavailable] \n" +
                    ChatColor.DARK_AQUA + "\n" + String.join("", Collections.nCopies(53, "-"));
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            SimpleWarService warService = SimpleWarService.getInstance();
            switch(input.intValue()) {
                case 1:
                    warService.setCurrentWar(WarFactory.getWarForType(WarTypes.BOLT_WAR_FACTION));

                    String promptMessage = ChatColor.GREEN + String.join("", Collections.nCopies(53, "-")) +
                            ChatColor.GREEN + " \nSuccessfully created war of type " + ChatColor.GOLD + "Faction Kit\n" +
                            ChatColor.GRAY + " Now that you have created the war, you may proceed as follows: \n" +
                            ChatColor.AQUA + "\n 1) " + ChatColor.GRAY + "Add participant nations with command " + ChatColor.YELLOW + "/war add <nation>\n" +
                            ChatColor.AQUA + "\n 2) " + ChatColor.GRAY + "Create or modify kit by typing " + ChatColor.YELLOW + "/war kit\n" +
                            ChatColor.GREEN + String.join("", Collections.nCopies(53, "-"));
                    Player player = (Player) context.getForWhom();
                    player.sendMessage(promptMessage);
                    break;
                case 2:
                    break;
            }
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.intValue() > 0 && input.intValue() <= 2;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
            return "Input number must be equal or greater than 2";
        }

    }

}
