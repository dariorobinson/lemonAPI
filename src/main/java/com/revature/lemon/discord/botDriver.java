package com.revature.lemon.discord;

import com.revature.lemon.discord.audio.AudioTrackScheduler;
import com.revature.lemon.discord.audio.GuildAudioManager;
import com.revature.lemon.discord.commands.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import org.hibernate.id.GUIDGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
        commands.put("join", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(message -> {
                    // Find the current user's voice channel and connect to it
                    message.getAuthorAsMember()
                        .flatMap(Member::getVoiceState)
                        .flatMap(VoiceState::getChannel)
                        // Perform the connection
                        .flatMap(channel ->  {
                            // Send the message that we're connectiong
                            message.getChannel().flatMap(textChannel -> {
                                return textChannel.createMessage(
                                        "Connecting at " +
                                        channel.getName()
                                );
                            })
                            .subscribe();
                            return channel.join(spec -> spec.setProvider(GuildAudioManager.of(channel.getGuildId()).getProvider()));
                        })
                        .subscribe();


                    })
                .then());

        /*
        commands.put("join", event -> Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                // Go down to the channel level so we can perform the join command with a created audio provider.
                .flatMap(channel -> channel.join(spec -> spec.setProvider(GuildAudioManager.of(channel.getGuildId()).getProvider())))
                .then());

         */

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

                        // TODO: Fix the issue where only the second item in the queue existing triggers the print

                        // If it works, then tell them it's playing
                        command.getChannel().flatMap(message -> {
                             return message.createMessage(
                                     command.getUserData().username() +
                                             " added " +
                                             scheduler.getQueue().get(scheduler.getQueue().size() - 1).getInfo().title +
                                             " to the queue"
                             );
                        })
                        .subscribe();
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
        commands.put("leave", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(message -> {
                    // Get the guild ID
                    Snowflake guildID = message.getGuildId().orElseThrow(RuntimeException::new);
                    // Get the audio manager
                    GuildAudioManager audioManager = GuildAudioManager.of(guildID);
                    // Clear schedule + destroy player
                    audioManager.getScheduler().clear();
                    audioManager.getPlayer().destroy();

                    // Find the current user's voice channel and disconnect from it
                    message.getAuthorAsMember()
                            .flatMap(Member::getVoiceState)
                            .flatMap(VoiceState::getChannel)
                            .flatMap(channel ->  channel.sendDisconnectVoiceState())
                                    .subscribe();

                    // Send the message that we're leaving
                    message.getChannel().flatMap(textChannel -> {
                        return textChannel.createMessage(
                                "Goodbye!"
                        );
                    })
                    .subscribe();
                })
                .then());

        // SKIP
        // Skips the currently playing song and moves on to the next.
        // If there is no next song, stops playing altogether.
        commands.put("skip", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(message -> {
                    // Find the current user's voice channel and skip the currently playing song.
                    message.getAuthorAsMember()
                            .flatMap(Member::getVoiceState)
                            .flatMap(VoiceState::getChannel)
                            .doOnNext(channel -> {
                                // Get the guild ID for manipulation
                                Snowflake snowflake = channel.getGuildId();

                                // Grab the audio manager from the guild ID
                                GuildAudioManager audioManager = GuildAudioManager.of(snowflake);

                                // Tell them the skip is happening
                                message.getChannel().flatMap(textChannel -> {
                                            return textChannel.createMessage(
                                                    "Skipping " +
                                                    audioManager.getPlayer().getPlayingTrack().getInfo().title
                                            );
                                        })
                                        .subscribe();

                                // Perform a skip which returns a boolean value.
                                // If that value is false, then we need to destroy the player because we're out of songs.
                                if(!audioManager.getScheduler().skip()) {
                                    audioManager.getPlayer().destroy();
                                }
                            })
                            .subscribe();

                })
                .then());

        // NOW PLAYING
        // Spits out the currently playing song in discord
        commands.put("np", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(message -> {
                        // Get the guild ID
                        Snowflake guildID = message.getGuildId().orElseThrow(RuntimeException::new);

                        // Enter the message's channel
                        message.getChannel().flatMap(channel -> {
                            // Get the currently playing track
                            AudioTrack currTrack = GuildAudioManager.of(guildID).getPlayer().getPlayingTrack();

                            // Get the current position in the track as a string
                            // Create a duration to convert to seconds properly for string formatting
                            Duration currDurr = Duration.ofMillis(currTrack.getPosition());
                            long currSTimeSeconds = currDurr.getSeconds();
                            String currSTime = String.format("%02d:%02d:%02d",
                                    currSTimeSeconds / 3600, // Hours
                                    (currSTimeSeconds % 3600) / 60 , // Minutes
                                    (currSTimeSeconds % 60)); // Seconds

                            return channel.createMessage(
                                "Now playing: " +
                                currTrack.getInfo().title + "\nBy: " +
                                currTrack.getInfo().author + "\nAt: " +
                                currSTime + "\nFound At: " +
                                currTrack.getInfo().uri + "\n"
                            );
                            })
                            .subscribe(); // Necessary for nested streams.
                        }

                )
                .then());

        // PAUSE
        // Halts the currently playing audio
        commands.put("pause", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(channel -> {
                    // Get the server ID
                    Snowflake guildID = channel.getGuildId().orElseThrow(RuntimeException::new);
                    // Point to the correct audioManager for the channel
                    GuildAudioManager audioManager = GuildAudioManager.of(guildID);
                    // Set the pause status to true
                    audioManager.getPlayer().setPaused(true);
                    // Send a message telling them the song has been skipped
                    channel.getChannel()
                            .flatMap(message -> {

                                return message.createMessage(
                                        "Track paused!"
                                );
                            })
                            .subscribe();
                })
                .then());



        // RESUME
        // Resumes the audio if it was previously paused
        commands.put("resume", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(message -> {
                    // Get the server ID
                    Snowflake guildID = message.getGuildId().orElseThrow(RuntimeException::new);
                    // Point to the correct audioManager for the channel
                    GuildAudioManager audioManager = GuildAudioManager.of(guildID);
                    // Set the pause status to true
                    audioManager.getPlayer().setPaused(false);
                    // Send a message telling them the song has been skipped
                    message.getChannel()
                            .flatMap(channel -> {

                                return channel.createMessage(
                                        "Track resumed!"
                                );
                            })
                            .subscribe();
                })
                .then());

        // SEEK
        // Sets the currently playing song's time to a user given value
        commands.put("seek", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(message -> {
                    // Get the guild ID
                    Snowflake guildID = message.getGuildId().orElseThrow(RuntimeException::new);

                    // Get the audioPlayer
                    GuildAudioManager guildAudio = GuildAudioManager.of(guildID);

                    // Get the new position
                    String givenPosition = message.getContent().split(" ")[1];
                    // Create a Duration for this time
                    Long seekTime = Long.valueOf(0);

                    // Checks that they gave us a valid input
                    boolean isLong = false;
                    try {
                        seekTime = Long.valueOf(givenPosition.split(":")[0]);
                        isLong = true;
                    } catch (Exception e) {
                        isLong = false;
                    }

                    // Counter to see if it's hours:minutes:seconds or minutes:seconds
                    int minSec = 0;

                    // Check that we have the right amount of inputs first
                    if ( ( givenPosition.split(":").length == 2 || givenPosition.split(":").length == 3 ) && isLong)
                    {
                        // While loop that gives us our seek time in milliseconds for the command
                        while (minSec < givenPosition.split(":").length  ) {

                            // Case for hours
                            System.out.println(givenPosition.split(":")[minSec]);
                            if (minSec - givenPosition.split(":").length == -3) {
                                // Add the hours as miliseconds to the seekTime
                                seekTime = seekTime + Long.valueOf(givenPosition.split(":")[minSec]) * 3600000L;

                            }

                            // Case for minutes
                            if (minSec - givenPosition.split(":").length == -2) {
                                // Add the minutes as miliseconds to the seekTime
                                seekTime = seekTime + Long.valueOf(givenPosition.split(":")[minSec]) * 60000L;
                            }

                            // Case for seconds
                            if (minSec - givenPosition.split(":").length == -1) {
                                // Add the minutes as miliseconds to the seekTime
                                seekTime = seekTime + Long.valueOf(givenPosition.split(":")[minSec]) * 1000L;
                            }

                            minSec++;
                        }

                        // Perform the seek
                        guildAudio.getPlayer().getPlayingTrack().setPosition(seekTime);

                        // Report in
                        message.getChannel()
                                .flatMap(channel -> {
                                    return channel.createMessage(
                                            "Seeking to " + givenPosition
                                    );
                                })
                                .subscribe();
                    }
                    // Else we tell them to do it right
                    else{
                        message.getChannel()
                                .flatMap(channel -> {
                                    return channel.createMessage(
                                            "Couldn't seek to that position. Try Hours:Minutes:Seconds or Minutes:Seconds " +
                                            "with numeric values in the appropriate places."
                                    );
                                })
                                .subscribe();
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
