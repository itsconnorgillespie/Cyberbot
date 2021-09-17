package us.virtualnetwork.Commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class CommandContext {

    private final SlashCommandEvent event;

    public CommandContext(SlashCommandEvent event) {
        this.event = event;
    }

    public SlashCommandEvent getEvent() {
        return this.event;
    }

}
