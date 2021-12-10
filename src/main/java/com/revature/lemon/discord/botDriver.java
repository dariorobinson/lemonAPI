package com.revature.lemon.discord;

import com.revature.lemon.discord.audio.AudioTrackScheduler;
import com.revature.lemon.discord.audio.GuildAudioManager;
import com.revature.lemon.discord.commands.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

public class botDriver {
    private static final Map<String , Command> commands = new HashMap<>();


    static {
        commands.put("ping", event -> event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage("Pong!"))
                .then());
    }

    public static final AudioPlayerManager PLAYER_MANAGER;

    static {
        PLAYER_MANAGER = new DefaultAudioPlayerManager();
        // This is an optimization strategy that Discord4J can utilize to minimize allocations
        PLAYER_MANAGER.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        // Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER);
        AudioSourceManagers.registerLocalSource(PLAYER_MANAGER);
    }

    public static void main(String[] args) {
        // Write down our bot token so we can create a session.
        // Make sure the bot token is set in the arguments for this file!
        String bToken = new String(args[0]);

        // TODO: Setup the bot to run on individual threads for each server it's connected to.
        //BOT JOINS -> CREATE PLAYER -> CREATE PROVIDER -> ASSIGN PROVIDER TO THAT CHANNEL

        // JOIN
        // Constructs command to get the bot in a voice channel
        commands.put("join", event -> Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                // join returns a VoiceConnection which would be required if we were
                // adding disconnection features, but for now we are just ignoring it.
                .flatMap(channel -> channel.join(spec -> spec.setProvider(GuildAudioManager.of(channel.getGuildId()).getProvider())))
                .then());

        // PLAY
        // Constructs a command to play a given song

        commands.put("play", event -> Mono.justOrEmpty(event.getMessage())
               // .map(content -> Arrays.asList(content.getContent().split(" ")))
                .doOnNext(command -> {
                    String audioUrl = command.getContent().split(" ")[1];
                    Snowflake snowflake =  command.getGuildId().orElseThrow(RuntimeException::new);
                    GuildAudioManager audioManager = GuildAudioManager.of(snowflake);
                    AudioTrackScheduler scheduler = audioManager.getScheduler();
                    PLAYER_MANAGER.loadItem(audioUrl, scheduler);
                })
                .then());

        // LEAVE
        // Constructs a command to get the bot to leave the channel
        commands.put("leave", event -> Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState) // Gets the current voice state of the member who calls the command
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> {
                    Snowflake snowflake = channel.getGuildId();
                    GuildAudioManager audioManager = GuildAudioManager.of(snowflake);
                    audioManager.getScheduler().clear();
                    audioManager.getPlayer().destroy();
                    return channel.sendDisconnectVoiceState();
                }) // Uses that voice channel to then disconnect the bot.
                .then());

        commands.put("skip", event -> Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .doOnNext(channel -> {
                    Snowflake snowflake = channel.getGuildId();
                    GuildAudioManager audioManager = GuildAudioManager.of(snowflake);
                    if(!audioManager.getScheduler().skip()) {
                        audioManager.getPlayer().destroy();
                    }
                })
                .then());

        // Creates our connection and stops the code from running until the bot is logged in.
        // Builds the client
        final GatewayDiscordClient client =DiscordClientBuilder.create(bToken).build().login().block();

        // Constructs simple ping pong command to test that the bot is working properly
        client.getEventDispatcher().on(MessageCreateEvent.class)
                // 3.1 Message.getContent() is a String
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(commands.entrySet())
                                // We will be using ! as our "prefix" to any command in the system.
                                .filter(entry -> content.startsWith('!' + entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();



        // This closes the bot when disconnected.
        client.onDisconnect().block();
    }
}
