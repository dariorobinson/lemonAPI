package com.revature.lemon.discord;

import com.revature.lemon.discord.audio.AudioTrackScheduler;
import com.revature.lemon.discord.audio.GuildAudioManager;
import com.revature.lemon.discord.commands.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.MessageChannel;
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

        //BOT JOINS -> CREATE PLAYER -> CREATE PROVIDER -> ASSIGN PROVIDER TO THAT CHANNEL

        // JOIN
        // Constructs command to get the bot in a voice channel
        commands.put("join", event -> Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                // Go down to the channel level so we can perform the join command with a created audio provider.
                .flatMap(channel -> channel.join(spec -> spec.setProvider(GuildAudioManager.of(channel.getGuildId()).getProvider())))
                .then());

        // PLAY
        // Constructs a command to play a given song

        commands.put("play", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(command -> {
                    try {
                        // Get the url from the second line provided in the command
                        String audioUrl = command.getContent().split(" ")[1];

                        // Get the guild ID for targeting
                        Snowflake snowflake =  command.getGuildId().orElseThrow(RuntimeException::new);

                        // Audio manager for scheduler manipulation
                        GuildAudioManager audioManager = GuildAudioManager.of(snowflake);

                        // Grab the scheduler and then load it with the given URL
                        AudioTrackScheduler scheduler = audioManager.getScheduler();
                        PLAYER_MANAGER.loadItem(audioUrl, scheduler);
                    } catch (Exception E) {
                        // If that doesn't work then we tell the user they put it in wrong
                        command.getChannel().flatMap(message ->
                                message.createMessage("Invalid input! Give a valid youtube, soundcloud, or bandcamp url!"))
                                .subscribe();
                    }

                })
                .then());

        // LEAVE
        // Constructs a command to get the bot to leave the channel
        commands.put("leave", event -> Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState) // Gets the current voice state of the member who calls the command
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> {
                    // Get the guild Id we can point to
                    Snowflake snowflake = channel.getGuildId();
                    // Get the GuildAudioManager so we can clear
                    GuildAudioManager audioManager = GuildAudioManager.of(snowflake);
                    // Clear the schedule, then destroy the player
                    audioManager.getScheduler().clear();
                    audioManager.getPlayer().destroy();
                    // Perform the disconnect.
                    return channel.sendDisconnectVoiceState();
                })
                .then());

        // SKIP
        // Skips the currently playing song and moves on to the next.
        // If there is no next song, stops playing altogether.
        commands.put("skip", event -> Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                // Navigate to the user's current voice channel
                .doOnNext(channel -> {
                    // Get the guild ID for manipulation
                    Snowflake snowflake = channel.getGuildId();

                    // Grab the audio manager from the guild ID
                    GuildAudioManager audioManager = GuildAudioManager.of(snowflake);

                    // Perform a skip which returns a boolean value.
                    // If that value is false, then we need to destroy the player because we're out of songs.
                    if(!audioManager.getScheduler().skip()) {
                        audioManager.getPlayer().destroy();
                    }
                })
                .then());

        // NOW PLAYING
        // Spits out the currently playing song in discord
        commands.put("np", event ->Mono.justOrEmpty(event.getMessage())
                .doOnNext(channel -> {
                        // Get the guild ID
                        Snowflake guildID = channel.getGuildId().orElseThrow(RuntimeException::new);
                        // Setup the message to be made;
                        System.out.println("Inside message!");
                        // Send the message
                        channel.getChannel().flatMap(message -> {
                                System.out.println("Inside Channel!");
                                AudioTrackInfo currTrackInfo = GuildAudioManager.of(guildID).getPlayer().getPlayingTrack().getInfo();
                                return message.createMessage(
                                    "Now playing: " +
                                    currTrackInfo.title + "\nBy: " +
                                    currTrackInfo.author + "\nAt: " +
                                    currTrackInfo.uri
                                );
                            })
                            .subscribe(); // Necessary for nested streams.
                        }

                )
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
