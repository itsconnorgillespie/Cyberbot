package us.virtualnetwork;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import us.virtualnetwork.Commands.CommandManager;

public class Listener extends ListenerAdapter {

    private CommandManager commandManager;

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getMember().getUser().isBot()) {
            commandManager.handle(event);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        commandManager = new CommandManager();
    }

}
