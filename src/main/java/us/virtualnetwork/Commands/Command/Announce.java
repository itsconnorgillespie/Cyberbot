package us.virtualnetwork.Commands.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import us.virtualnetwork.Commands.CommandContext;
import us.virtualnetwork.Commands.ICommand;
import us.virtualnetwork.Main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Announce implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        // Options
        String title = ctx.getEvent().getOptionsByName("title").get(0).getAsString();
        String content = ctx.getEvent().getOptionsByName("content").get(0).getAsString();
        MessageChannel channel = ctx.getEvent().getOptionsByName("channel").get(0).getAsMessageChannel();

        // Embed
        EmbedBuilder embed = new EmbedBuilder();
        embed.addField(title, content, false);
        embed.setTimestamp(new Date().toInstant());
        embed.setFooter("Posted by " + ctx.getEvent().getMember().getUser().getName());
        channel.sendMessage(embed.build()).queue();

        // Email
        if (ctx.getEvent().getOptionsByName("email").get(0).getAsBoolean()) {
            Email email = EmailBuilder.startingBlank()
                    .from("Virtualbot", "ltk.watermelon@gmail.com")
                    .to(Main.emails.get(0))
                    .withSubject(title)
                    .withPlainText(content)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer("smtp.gmail.com", 587, "ltk.watermelon@gmail.com", "ysamyjdtlpstidax")
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria()
                    .async()
                    .buildMailer();

            mailer.sendMail(email);
        }

        // Reply
        ctx.getEvent().getHook().sendMessage("Announcement created.").setEphemeral(getEphemeral()).queue();
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
        return options;
    }

}
