package us.virtualnetwork.Commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import us.virtualnetwork.Commands.Command.Announce;
import us.virtualnetwork.Main;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();
    private final String pattern = "([^\"]\\S*|\".+?\")\\s*";

    public CommandManager() {
        addCommand(new Announce());
    }

    private void addCommand(ICommand command) {
        if (commands.contains(command)) {
            throw new IllegalArgumentException("A command with this name is already registered!");
        } else {
            if (command.getName() == null || command.getHelp() == null || command.getEphemeral() == null) {
                throw new IllegalArgumentException("The command does not contain required interfaces!");
            }

            commands.add(command);

            CommandListUpdateAction commands = Main.jda.getGuildById(Main.properties.getProperty("bot.guild")).updateCommands();
            commands.addCommands(new CommandData(command.getName(), command.getHelp()).addOptions(command.getOptions()));
            commands.queue();
        }
    }

    @Nullable
    public ICommand getCommand(String search) {
        for (ICommand command : commands) {
            if (command.getName().equalsIgnoreCase(search.toLowerCase())) {
                return command;
            }
        }
        return null;
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public void handle(SlashCommandEvent event) {
        ICommand command = getCommand(event.getName());

        if (command != null && event.getGuild().getIdLong() == Long.parseLong(Main.properties.getProperty("bot.guild"))) {
            event.deferReply(command.getEphemeral()).queue();
            CommandContext ctx = new CommandContext(event);
            command.handle(ctx);
        }
    }
}
