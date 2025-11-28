package org.dirtbot.Events;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageEventListener extends ListenerAdapter {

    private static MessageChannel monitoredChannel;
    private final String TARGET_USER_ID = "645969351282917376";
    private final String EMOJI_ID = "1440016188615823512";
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Random random = new Random();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getAuthor().getId().equals(TARGET_USER_ID)) {
            Emoji emoji = event.getJDA().getEmojiById(EMOJI_ID);
            if (emoji != null) {
                event.getMessage().addReaction(emoji).queue();
            }
        }
    }

    private void startSendingMessages() {
        Runnable task = () -> {
            if (monitoredChannel != null) {
                monitoredChannel.sendMessage("dirtbot: meow").queue();
            }
        };

        long delay = 12 * 60 * 60 + random.nextInt(12 * 60 * 60);
        scheduler.scheduleAtFixedRate(task, delay, delay, TimeUnit.SECONDS);
/*        long delay = 60 * 2 + random.nextInt(60 * 4); 2-6 mins for testing
        scheduler.scheduleAtFixedRate(task, delay, delay, TimeUnit.SECONDS);*/
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("init")) {
            monitoredChannel = event.getChannel();
            startSendingMessages();
            event.reply("This channel is now being monitored.").setEphemeral(true).queue();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);
        System.out.println("bot is online");
    }

}
