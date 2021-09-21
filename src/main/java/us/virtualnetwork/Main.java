package us.virtualnetwork;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.commons.io.FileUtils;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Main {

    public static JDA jda;
    public static Properties properties;
    public static List<String> emails = new ArrayList<>(Collections.singletonList("cdggamer@yahoo.com"));

    public static void main(String[] args) {
        try {
            File file = new File(System.getProperty("user.dir") + "/config.properties");

            if (!file.exists()) {
                InputStream resource = Main.class.getClassLoader().getResourceAsStream("config.properties");
                assert resource != null;
                FileUtils.copyInputStreamToFile(resource, file);
            }

            properties = new Properties();
            properties.load(new FileInputStream(file));
            JDABuilder jdaBuilder = JDABuilder.createDefault(properties.getProperty("bot.token"));
            jdaBuilder.addEventListeners(new Listener());
            jdaBuilder.setActivity(Activity.watching("for commands!"));
            jdaBuilder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES);
            jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
            jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
            jda = jdaBuilder.build();
            jda.awaitReady();
        } catch (IOException | InterruptedException | LoginException e) {
            e.printStackTrace();
        }

        Database.checkSchema();
    }

}
