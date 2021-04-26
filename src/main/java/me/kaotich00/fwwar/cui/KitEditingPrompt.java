package me.kaotich00.fwwar.cui;

import com.github.stefvanschie.inventoryframework.Gui;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.gui.kit.KitEditGui;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.services.SimpleKitService;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class KitEditingPrompt implements ConversationAbandonedListener{

    private final ConversationFactory conversationFactory;

    public KitEditingPrompt(Fwwar plugin) {
        this.conversationFactory = new ConversationFactory(plugin)
                .withModality(false)
                .withFirstPrompt(new KitMenuPrompt())
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
            abandonedEvent.getContext().getForWhom().sendRawMessage(MessageUtils.formatSuccessMessage(""));
        } else {
            abandonedEvent.getContext().getForWhom().sendRawMessage(MessageUtils.formatErrorMessage("Conversation aborted"));
        }
    }

    private class KitMenuPrompt extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            return Message.KIT_MENU.asString();
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            switch (input.intValue()) {
                case 1:
                    return new NewKitPrompt();
                case 2:
                    if(SimpleKitService.getInstance().getKitsForType(SimpleWarService.getInstance().getWar().get().getWarType()).size() == 0) {
                        Message.NO_KIT.send((Player) context.getForWhom());
                    } else {
                        return new KitEditPrompt();
                    }
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

    private class NewKitPrompt extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            return Message.KIT_NAME_SELECTION.asString();
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            SimpleWarService warService = SimpleWarService.getInstance();
            SimpleKitService kitService = SimpleKitService.getInstance();

            Optional<War> optWar = warService.getWar();
            if(!optWar.isPresent()) return Prompt.END_OF_CONVERSATION;

            War war = optWar.get();

            boolean doesKitExists = kitService.getKitForName(war.getWarType(), input).isPresent();

            if(doesKitExists) {
                Message.KIT_ALREADY_EXIST.send((Player) context.getForWhom(), input);
                return Prompt.END_OF_CONVERSATION;
            }

            Kit kit = new Kit(input);
            kitService.addKit(war.getWarType(), kit);

            context.setSessionData("kit", kit);

            return new NewKitRequiredPrompt();
        }

    }

    private class NewKitRequiredPrompt extends FixedSetPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            return Message.KIT_REQUIRED.asString();
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
            Kit kit = (Kit) conversationContext.getSessionData("kit");
            if(s.equals("true"))
                kit.setRequired(true);

            return new NewKitQuantityPrompt();
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            return (input.equals("true") || input.equals("false"));
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return "Input must either be true of false.";
        }
    }

    private class NewKitQuantityPrompt extends NumericPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            return Message.KIT_QUANTITY.asString();
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {

            Kit kit = (Kit) context.getSessionData("kit");
            kit.setQuantity(input.intValue());

            Message.KIT_CREATED.send((Player) context.getForWhom(), kit.getName());
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Fwwar.getPlugin(Fwwar.class), () -> {
                Player player = (Player) context.getForWhom();
                KitEditGui kitEditGui = new KitEditGui(kit.getName(), player);
                Gui kitGUI = kitEditGui.prepareGui();
                kitGUI.show(player);
            }, 40L);

            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        protected boolean isNumberValid(ConversationContext context, Number input) {
            return input.intValue() >= -1 && input.intValue() != 0;
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
            return "Input number must be greater than -1 and not 0";
        }

    }

    private class KitEditPrompt extends FixedSetPrompt  {

        @Override
        public String getPromptText(ConversationContext context) {
            StringBuilder promptMessage = new StringBuilder(Message.KIT_EDITOR.asString());

            SimpleKitService kitService = SimpleKitService.getInstance();
            SimpleWarService warService = SimpleWarService.getInstance();
            Optional<War> optWar = warService.getWar();
            if(!optWar.isPresent()) return "";

            War war = optWar.get();

            Collection<Kit> kitsList = kitService.getKitsForType(war.getWarType());
            for(Kit kit : kitsList) {
                promptMessage.append(ChatColor.YELLOW + " - ").append(kit.getName()).append("\n");
            }

            promptMessage.append(MessageUtils.chatDelimiter());
            return promptMessage.toString();
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String input) {
            Player player = (Player) context.getForWhom();
            KitEditGui kitEditGui = new KitEditGui(input, player);
            Gui kitGUI = kitEditGui.prepareGui();
            kitGUI.show(player);

            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        protected boolean isInputValid(ConversationContext context, String input) {
            SimpleWarService warService = SimpleWarService.getInstance();
            Optional<War> optWar = warService.getWar();
            if(!optWar.isPresent()) return false;

            War war = optWar.get();

            return SimpleKitService.getInstance().getKitForName(war.getWarType(), input).isPresent();
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return "Please input a valid kit name";
        }

    }

}
