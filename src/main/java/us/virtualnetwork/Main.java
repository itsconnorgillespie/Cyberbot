package us.virtualnetwork;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static JDA jda;
    public static String token = "ODI5ODE2MjYzMzE2MDc4Njcz.YG9ong.anzYYUjpqJFxUSwUYOgOgIG2oPM";
    public static List<String> emails = new ArrayList<>(Collections.singletonList("cdggamer@yahoo.com"));

    public static void main(String[] args) {
        JDABuilder jdaBuilder = JDABuilder.createDefault(token);

        try {
            jdaBuilder.addEventListeners(new Listener());
            jdaBuilder.setActivity(Activity.watching("for commands!"));
            jda = jdaBuilder.build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
