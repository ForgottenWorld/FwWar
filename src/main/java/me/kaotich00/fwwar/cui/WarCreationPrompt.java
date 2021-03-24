package me.kaotich00.fwwar.cui;

import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.MessageUtils;
import me.kaotich00.fwwar.utils.WarTypes;
import me.kaotich00.fwwar.war.WarFactory;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class WarCreationPrompt implements ConversationAbandonedListener {

    private final ConversationFactory conversationFactory;

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
            String promptMessage = Message.WAR_CREATION_MENU.asString();
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            switch (input.intValue()) {
                case 1:
                    return new BoltWarGamemode();
                case 2:
                    return new AssaultWarGamemode();
            }
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.intValue() > 0 && input.intValue() <= 2;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
            return "Input number must be equal or lesser than 2";
        }

    }

    private class BoltWarGamemode extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = Message.WAR_CREATION_BOLT_WAR.asString();
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            SimpleWarService warService = SimpleWarService.getInstance();

            Player player = (Player) context.getForWhom();
            String promptMessage = "";

            switch(input.intValue()) {
                case 1:
                    warService.setCurrentWar(WarFactory.getWarForType(WarTypes.BOLT_WAR_FACTION));
                    promptMessage = Message.WAR_CREATION_BOLT_WAR_FACTION.asString();
                    player.sendMessage(promptMessage);
                    break;
                case 2:
                    warService.setCurrentWar(WarFactory.getWarForType(WarTypes.BOLT_WAR_RANDOM));
                    promptMessage = Message.WAR_CREATION_BOLT_WAR_RANDOM.asString();
                    player.sendMessage(promptMessage);
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
            return "Input number must be equal or lesser than 2";
        }

    }

    private class AssaultWarGamemode extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = Message.WAR_CREATION_ASSAULT_WAR.asString();
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            SimpleWarService warService = SimpleWarService.getInstance();

            Player player = (Player) context.getForWhom();
            String promptMessage = "";

            switch(input.intValue()) {
                case 1:
                    warService.setCurrentWar(WarFactory.getWarForType(WarTypes.ASSAULT_WAR_CLASSIC));
                    promptMessage = Message.WAR_CREATION_ASSAULT_WAR_CLASSIC.asString();
                    player.sendMessage(promptMessage);
                    break;
                case 2:
                    warService.setCurrentWar(WarFactory.getWarForType(WarTypes.ASSAULT_WAR_CONQUEST));
                    promptMessage = Message.WAR_CREATION_ASSAULT_WAR_SIEGE.asString();
                    player.sendMessage(promptMessage);
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
            return "Input number must be equal or lesser than 2";
        }

    }

}
