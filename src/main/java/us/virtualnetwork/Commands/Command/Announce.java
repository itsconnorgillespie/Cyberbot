package us.virtualnetwork.Commands.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import us.virtualnetwork.Commands.CommandContext;
import us.virtualnetwork.Commands.ICommand;
import us.virtualnetwork.Database;
import us.virtualnetwork.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Announce implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        // Permissions
        if (!ctx.getEvent().getMember().hasPermission(Permission.ADMINISTRATOR)) {
            ctx.getEvent().getHook().sendMessage(":interrobang: You do not have permission to use this command.").setEphemeral(getEphemeral()).queue();
            return;
        }

        // Required Options
        String title = ctx.getEvent().getOptionsByName("title").get(0).getAsString();
        String content = ctx.getEvent().getOptionsByName("content").get(0).getAsString().replaceAll("\\\\n", System.getProperty("line.separator"));
        MessageChannel channel = ctx.getEvent().getOptionsByName("channel").get(0).getAsMessageChannel();

        // Optional Options
        String color = null;
        String image = null;

        if (ctx.getEvent().getOptionsByName("color").size() > 0) {
            color = ctx.getEvent().getOptionsByName("color").get(0).getAsString();
        }

        if (ctx.getEvent().getOptionsByName("image").size() > 0) {
            image = ctx.getEvent().getOptionsByName("image").get(0).getAsString();
        }

        // Embed
        EmbedBuilder embed = new EmbedBuilder();
        embed.addField(title, content, false);
        embed.setTimestamp(new Date().toInstant());
        embed.setFooter("Posted by " + ctx.getEvent().getMember().getUser().getName());

        // Color
        if (color != null && !color.isEmpty()) {
            embed.setColor(Color.decode(color));
        } else {
            embed.setColor(Color.decode(Main.properties.getProperty("bot.color")));
        }

        // Image
        if (image != null && !image.isEmpty()) {
            embed.setImage(image);
        }

        // Ping
        if (ctx.getEvent().getOptionsByName("ping").get(0).getAsBoolean()) {
            channel.sendMessage("@everyone").queue();
        }
        channel.sendMessage(embed.build()).queue();

        // Email
        if (ctx.getEvent().getOptionsByName("email").get(0).getAsBoolean()) {
            List<String> emails = Database.getEmails();

            // Check
            if (emails == null || emails.size() == 0){
                ctx.getEvent().getHook().sendMessage(":white_check_mark: Announcement created in <#" + channel.getId() + ">.\n" +
                        ":interrobang: No emails in database.").setEphemeral(getEphemeral()).queue();
                return;
            }

            Email mail = EmailBuilder.startingBlank()
                    .from(Main.properties.getProperty("email.name"), Main.properties.getProperty("email.email"))
                    .to(Main.properties.getProperty("email.email"))
                    .bccAddresses(emails)
                    .withSubject(title)
                    .withPlainText(content)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer(Main.properties.getProperty("email.host"), Integer.parseInt(Main.properties.getProperty("email.port")),
                            Main.properties.getProperty("email.email"), Main.properties.getProperty("email.password"))
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria()
                    .async()
                    .buildMailer();

            mailer.sendMail(mail);

            // Reply
            ctx.getEvent().getHook().sendMessage(":white_check_mark: Announcement created in <#" + channel.getId() + ">.\n" +
                    ":busts_in_silhouette: Sent to **" + emails.size() + "** linked accounts.").setEphemeral(getEphemeral()).queue();
            return;
        }

        // Reply
        ctx.getEvent().getHook().sendMessage(":white_check_mark: Announcement created in <#" + channel.getId() + ">.\n").setEphemeral(getEphemeral()).queue();
    }

    @Override
    public String getName() {
        return "announce";
    }

    @Override
    public String getHelp() {
        return "A simple, effective announcement command.";
    }

    @Override
    public Boolean getEphemeral() {
        return true;
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.CHANNEL, "channel", "The channel where the announcement should be posted.", true));
        options.add(new OptionData(OptionType.STRING, "title", "The title of the announcement.", true));
        options.add(new OptionData(OptionType.STRING, "content", "The content for the body of the announcement.", true));
        options.add(new OptionData(OptionType.BOOLEAN, "email", "Should the announcement be emailed to linked users?", true));
        options.add(new OptionData(OptionType.BOOLEAN, "ping", "Should we ping everyone for this announcement?", true));
        options.add(new OptionData(OptionType.STRING, "image", "An optional image displayed in the embed.", false));
        options.add(new OptionData(OptionType.STRING, "color", "An optional HEX color code to change the default embed color.", false));
        return options;
    }

}
