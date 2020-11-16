package me.kaotich00.fwwar.cui;

import com.github.stefvanschie.inventoryframework.Gui;
import me.kaotich00.fwwar.Fwwar;
import me.kaotich00.fwwar.api.war.War;
import me.kaotich00.fwwar.gui.kit.KitEditGui;
import me.kaotich00.fwwar.message.Message;
import me.kaotich00.fwwar.objects.kit.Kit;
import me.kaotich00.fwwar.services.SimpleWarService;
import me.kaotich00.fwwar.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.util.Collections;

public class KitEditingPrompt implements ConversationAbandonedListener{

    private ConversationFactory conversationFactory;

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
            String promptMessage = Message.KIT_MENU.asString();
            return promptMessage;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            switch (input.intValue()) {
                case 1:
                    return new NewKitPrompt();
                case 2:
                    if(SimpleWarService.getInstance().getKitsForType(SimpleWarService.getInstance().getCurrentWar().get().getWarType()).size() == 0) {
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
            String promptMessage = Message.KIT_NAME_SELECTION.asString();
            return promptMessage;
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            SimpleWarService warService = SimpleWarService.getInstance();
            War currentWar = warService.getCurrentWar().get();

            boolean doesKitExists = warService.getKitForName(currentWar.getWarType(), input).isPresent();

            if(doesKitExists) {
                Message.KIT_ALREADY_EXIST.send((Player) context.getForWhom(), input);
                return Prompt.END_OF_CONVERSATION;
            }

            Kit kit = new Kit(input);
            warService.addKit(currentWar.getWarType(), kit);

            Message.KIT_CREATED.send((Player) context.getForWhom(), input);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Fwwar.getPlugin(Fwwar.class), () -> {
                Player player = (Player) context.getForWhom();
                KitEditGui kitEditGui = new KitEditGui(input, player);
                Gui kitGUI = kitEditGui.prepareGui();
                kitGUI.show(player);
            }, 40L);

            return Prompt.END_OF_CONVERSATION;
        }

    }

    private class KitEditPrompt extends FixedSetPrompt  {

        @Override
        public String getPromptText(ConversationContext context) {
            String promptMessage = Message.KIT_EDITOR.asString();

            SimpleWarService warService = SimpleWarService.getInstance();
            War war = warService.getCurrentWar().get();

            for(Kit kit : warService.getKitsForType(war.getWarType())) {
                promptMessage += ChatColor.YELLOW + "" + ChatColor.BOLD + "\n ["+kit.getName()+"] \n";
            }

            promptMessage += ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "\n" + String.join("", Collections.nCopies(45, "-"));
            return promptMessage;
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
            return SimpleWarService.getInstance().getKitForName(SimpleWarService.getInstance().getCurrentWar().get().getWarType(), input).isPresent();
        }

        @Override
        protected String getFailedValidationText(ConversationContext context, String invalidInput) {
            return "Please input a valid kit name";
        }

    }

}
