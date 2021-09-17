package us.virtualnetwork.Commands;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface ICommand {

    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    Boolean getEphemeral();

    default List<OptionData> getOptions(){
        return List.of();
    }

}