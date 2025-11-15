package org.dirtbot;

import Events.MessageEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Properties config = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            config.load(input);
        } catch (IOException ex) {
            System.err.println("FATAL: Could not load config.properties file.");
            ex.printStackTrace();
            return;
        }

        String botToken = config.getProperty("token");

        if (botToken == null) {
            System.err.println("FATAL: 'token' property not found in config.properties.");
            return;
        }

        JDABuilder jdaBuilder = (JDABuilder) JDABuilder.createDefault(botToken);
        JDA jda = jdaBuilder
                .setActivity(Activity.playing("dirtbot: meow"))
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(new MessageEventListener())
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .build();
    }
}