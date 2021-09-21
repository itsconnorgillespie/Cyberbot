package us.virtualnetwork.Commands.Command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import us.virtualnetwork.Commands.CommandContext;
import us.virtualnetwork.Commands.ICommand;
import us.virtualnetwork.Main;

import java.util.ArrayList;
import java.util.List;

public class Lock implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        // Permissions
        if (!ctx.getEvent().getMember().hasPermission(Permission.ADMINISTRATOR)) {
            ctx.getEvent().getHook().sendMessage(":interrobang: You do not have permission to use this command.").setEphemeral(getEphemeral()).queue();
            return;
        }

        // Role Check
        Role role = ctx.getEvent().getGuild().getRoleById(Main.properties.getProperty("bot.lock"));

        if (role == null) {
            ctx.getEvent().getHook().sendMessage(":interrobang: The lock role is currently not set in `config.properties` or does not exist.").setEphemeral(getEphemeral()).queue();
            return;
        }

        // Lock
        int count = 0;

        if (ctx.getEvent().getOptionsByName("lock").get(0).getAsBoolean()) {
            // Add
            for (Member member : ctx.getEvent().getGuild().getMembers()) {
                if (!member.getRoles().contains(role)) {
                    ctx.getEvent().getGuild().addRoleToMember(member.getIdLong(), role).queue();
                    count++;
                }
            }

            ctx.getEvent().getHook().sendMessage(":lock: Locked " + count + " users.").setEphemeral(getEphemeral()).queue();
        } else {
            // Remove
            for (Member member : ctx.getEvent().getGuild().getMembers()) {
                if (member.getRoles().contains(role)) {
                    ctx.getEvent().getGuild().removeRoleFromMember(member.getIdLong(), role).queue();
                    count++;
                }
            }

            ctx.getEvent().getHook().sendMessage(":lock: Unlocked " + count + " users.").setEphemeral(getEphemeral()).queue();
        }
    }

    @Override
    public String getName() {
        return "lock";
    }

    @Override
    public String getHelp() {
        return "Locks all user communication during competitions.";
    }

    @Override
    public Boolean getEphemeral() {
        return true;
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.BOOLEAN, "lock", "Should we lock all user communication?.", true));
        return options;
    }

}
