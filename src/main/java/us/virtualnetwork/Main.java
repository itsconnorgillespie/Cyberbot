package us.virtualnetwork;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Main {

    public static JDA jda;
    public static Properties properties;
    public static List<String> emails = new ArrayList<>(Collections.singletonList("cdggamer@yahoo.com"));

    public static void main(String[] args) {
        properties = new Properties();

        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
            JDABuilder jdaBuilder = JDABuilder.createDefault(properties.getProperty("bot.token"));
            jdaBuilder.addEventListeners(new Listener());
            jdaBuilder.setActivity(Activity.watching("for commands!"));
            jda = jdaBuilder.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        Database.checkSchema();
    }

}
