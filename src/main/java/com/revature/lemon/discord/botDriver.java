package com.revature.lemon.discord;


import com.revature.lemon.discord.audio.GuildAudioManager;
import com.revature.lemon.discord.audio.TrackScheduler;
import com.revature.lemon.discord.commands.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.event.domain.channel.TypingStartEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.net.URI;
import java.net.URISyntaxException;

public class botDriver {
    // ##################### FUNCTIONS ########################
    // Boolean statement to learn if somethingi s aurl or not
    private static boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    // Formatting for any timestamps we need
    private static String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }



    // #################### VARIABLES ########################
    // Create our global player manager to keep track of our audio players
    public static final AudioPlayerManager PLAYER_MANAGER;
    // Map for our commands
    private static final Map<String , Command> commands = new HashMap<>();

    static {
        PLAYER_MANAGER = new DefaultAudioPlayerManager();
        // This is an optimization strategy that Discord4J can utilize to minimize allocations
        PLAYER_MANAGER.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        // Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER);
        AudioSourceManagers.registerLocalSource(PLAYER_MANAGER);
    }


    // ################## MAIN #########################
    public static void main(String[] args) {
        // ################# VARIABLES #######################
        // Write down our bot token so we can create a session.
        // Make sure the bot token is set in the arguments for this file!
        String bToken = new String(args[0]);



        // ################### COMMANDS #########################
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
                                        "Connecting at `" +
                                        channel.getName() + "`!"
                                );
                            })
                            .subscribe();
                            return channel.join(spec -> spec.setProvider(GuildAudioManager.of(channel.getGuildId()).getProvider()))
                                    // Leave automatically if the bot is alone.
                                    .flatMap(connection -> {
                                            // The bot itself has a VoiceState; 1 VoiceState signals bot is alone
                                            final Publisher<Boolean> voiceStateCounter = channel.getVoiceStates()
                                                    .count()
                                                    .map(count -> 1L == count);

                                            // Get the Audio Manager
                                            Snowflake playerID = channel.getGuildId();
                                            GuildAudioManager audioManager = GuildAudioManager.of(playerID);

                                            // Boolean publisher to tell when there's nothing playing
                                            final Publisher<Boolean> isPlaying = channel.getVoiceStates()
                                                    .map(currPlaying -> audioManager.getPlayer().getPlayingTrack() == null);

                                            // After 10 seconds, check that music is playing. If not, then we leave the channel.
                                            final Mono<Void> onDelay = Mono.delay(Duration.ofSeconds(10L))
                                                    .filterWhen(filler -> isPlaying)
                                                    .switchIfEmpty(Mono.never())
                                                    .then();

                                            // As people join and leave `channel`, check if the bot is alone.
                                            // Note the first filter is not strictly necessary, but it does prevent many unnecessary cache calls
                                            final Mono<Void> onEvent = channel.getClient().getEventDispatcher().on(VoiceStateUpdateEvent.class)
                                                    .filter(agEvent -> agEvent.getOld().flatMap(VoiceState::getChannelId).map(channel.getId()::equals).orElse(false))
                                                    .filterWhen(ignored -> voiceStateCounter)
                                                    .next()
                                                    .then();

                                            // Disconnect the bot if either onDelay or onEvent are completed!
                                            return Mono.first(onDelay, onEvent).then(connection.disconnect());
                                    });
                        })
                        .subscribe();


                    })
                .then());


        // PLAY
        // Constructs a command to play a given song
        commands.put("play", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(command -> {
                    try {
                        // Get the url from the second line provided in the command
                        String audioUrl = command.getContent().substring(command.getContent().indexOf(" ") + 1);

                        // Get the guild ID for targeting
                        Snowflake snowflake =  command.getGuildId().orElseThrow(RuntimeException::new);

                        // Audio manager for scheduler manipulation
                        GuildAudioManager audioManager = GuildAudioManager.of(snowflake);

                        // Check that our URL is a URL afterall, and if not we search for it
                        if (!isUrl(audioUrl)) {
                            audioUrl = "ytsearch:" + audioUrl;
                        }

                        // Grab the scheduler and then load it with the given URL
                        TrackScheduler scheduler = audioManager.getScheduler();

                        // Create a new load requeuest using a new handler that overrides the usual so we can access
                        // both the channel and the track for parsing.
                        String finalAudioUrl = audioUrl;
                        PLAYER_MANAGER.loadItemOrdered(audioManager, audioUrl, new AudioLoadResultHandler() {
                            @Override
                            public void trackLoaded(AudioTrack track) {
                                scheduler.queue(track);

                                command.getChannel().flatMap(channel -> {
                                    String message = command.getUserData().username() +
                                            " added `" +
                                            track.getInfo().title +
                                            "` by `" +
                                            track.getInfo().author +
                                            "` to the queue!";
                                    return channel.createMessage(message);
                                }).subscribe();
                            }
                            // Override for playlists
                            @Override
                            public void playlistLoaded(AudioPlaylist playlist) {
                                if (playlist.isSearchResult()){
                                    trackLoaded(playlist.getTracks().get(0));
                                }
                                else {
                                    final List<AudioTrack> tracks = playlist.getTracks();

                                    command.getChannel().flatMap(channel -> {
                                        String message = command.getUserData().username() +
                                                " added `" +
                                                String.valueOf(tracks.size()) +
                                                "` tracks from the playlist `" +
                                                playlist.getName() +
                                                "` to the queue!";
                                        return channel.createMessage(message);
                                    }).subscribe();

                                    for (final AudioTrack track : tracks) {
                                        audioManager.getScheduler().queue(track);
                                    }
                                }

                            }

                            @Override
                            public void noMatches() {
                                command.getChannel().flatMap(channel -> {
                                    String message = "Unable to find a match for " + finalAudioUrl;
                                    return channel.createMessage(message);
                                }).subscribe();
                            }

                            @Override
                            public void loadFailed(FriendlyException exception) {
                                command.getChannel().flatMap(channel -> {
                                    String message = "Unable to load " + finalAudioUrl + " if it is age restricted, " +
                                            "this cannot be helped. Sorry.";
                                    return channel.createMessage(message);
                                }).subscribe();
                            }
                        });
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
                                                    "Skipping `" +
                                                    audioManager.getPlayer().getPlayingTrack().getInfo().title + "`"
                                            );
                                        })
                                        .subscribe();

                                // Perform a skip which returns a boolean value.
                                // If that value is false, then we need to destroy the player because we're out of songs.
                                if(!audioManager.getScheduler().nextTrack()) {
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
                            String nowPlaying = "Placeholder";
                            try {
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

                                nowPlaying = "Now playing: `" +
                                        currTrack.getInfo().title + "`\nBy: `" +
                                        currTrack.getInfo().author + "`\nAt: `" +
                                        currSTime + "`\nFound At: " +
                                        currTrack.getInfo().uri + "\n";
                            } catch (Exception e) {
                                return channel.createMessage(
                                        nowPlaying = "There's nothing playing right now..."
                                );
                            }
                            return channel.createMessage(nowPlaying);
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
                                // Add the seconds as miliseconds to the seekTime
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
                                            "Seeking to `" + givenPosition + "`"
                                    );
                                })
                                .subscribe();
                    }
                    // Else we tell them to do it right
                    else{
                        message.getChannel()
                                .flatMap(channel -> {
                                    return channel.createMessage(
                                            "Couldn't seek to that position. Try `Hours:Minutes:Seconds` or `Minutes:Seconds` " +
                                            "with numeric values in the appropriate places."
                                    );
                                })
                                .subscribe();
                    }

                })
                .then());

        // QUEUE
        // Show the queue of songs that are coming next
        commands.put("queue", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(message -> {
                    // Get the guild ID
                    Snowflake guildID = message.getGuildId().orElseThrow(RuntimeException::new);

                    // Grab the audio manager
                    GuildAudioManager guildAudio = GuildAudioManager.of(guildID);

                    // Grab the queue
                    BlockingQueue<AudioTrack> queue = guildAudio.getScheduler().queue;

                    message.getChannel()
                            .flatMap(channel -> {
                                // Create our initial return message
                                String returnMessage = "";

                                // If the queue is empty return this message
                                if (queue.isEmpty()) {
                                    returnMessage = "The queue is currently empty";
                                }
                                // Else create our queue message with a maximum of 20 items to prevent overflow in discord.
                                else {
                                    final List<AudioTrack> trackList = new ArrayList<>(queue);
                                    final int trackCount = Math.min(queue.size(), 20);

                                    for (int i = 0; i < trackCount; i++){
                                        final AudioTrack track = trackList.get(i);
                                        final AudioTrackInfo info = track.getInfo();

                                        returnMessage = returnMessage + "#" +
                                                (i + 1) +
                                                " `" + info.title + "` " +
                                                " by `" +
                                                info.author + "` [`" +
                                                formatTime(track.getDuration()) + "`]\n";
                                    }

                                    if (trackList.size() > trackCount) {
                                        returnMessage = returnMessage + " and `" +
                                                String.valueOf(trackList.size() - trackCount) + "` more...";
                                    }
                                }
                                return channel.createMessage(returnMessage);

                            }).subscribe();
                })
                .then());

        // REMOVE
        // Removes an item from the queue at a given address
        commands.put("remove", event -> Mono.justOrEmpty(event.getMessage())
                .doOnNext(message -> {
                    // Create our initial return string
                    String returnMessage = "";

                    // Get the guild ID
                    Snowflake guildID = message.getGuildId().orElseThrow(RuntimeException::new);

                    // Get the player
                    GuildAudioManager audioManager = GuildAudioManager.of(guildID);

                    //Get the scheduler
                    TrackScheduler scheduler = audioManager.getScheduler();

                    // Make a list we can easily get to the value of
                    List<AudioTrack> trackList = new ArrayList<>(scheduler.queue);

                    try {
                        // Get our track
                        AudioTrack target = trackList.get(Integer.valueOf(message.getContent().split(" ")[1]) - 1);

                        if (scheduler.queue.remove(target)) {
                            returnMessage = "Removed `" + target.getInfo().title +
                                    "` from the queue!";
                        } else {
                            returnMessage = "Unable to remove `" + target.getInfo().title + "` from the queue...";
                        }

                    } catch (Exception e) {
                        returnMessage = "Invalid Command. Try remove followed by the integer position of the song in the queue";
                    }

                    // Send our result over discord
                    String finalMessage = returnMessage;
                    message.getChannel().flatMap(channel -> {
                        return channel.createMessage(finalMessage);
                    }).subscribe();

                })
                .then());


        // ########################### CONNECTION LOGIC ##################################
        // Creates our connection and stops the code from running until the bot is logged in.
        // Builds the client
        final GatewayDiscordClient client =DiscordClientBuilder.create(bToken).build().login().block();



        // ########################## COMMAND LISTENER $##################################
        client.getEventDispatcher().on(MessageCreateEvent.class)
                // 3.1 Message.getContent() is a String
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(commands.entrySet())
                                // We will be using ! as our "prefix" to any command in the system.
                                .filter(entry -> content.startsWith('!' + entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();

        // After a certain delay, disconnect from a channel you're connected to.



    /* Old Code
        client.getEventDispatcher().on(TypingStartEvent.class)
                .flatMap(event -> Mono.just(event.get())
                        .doOnNext(author -> {
                            Snowflake guildID = author.get();
                            GuildAudioManager audioManager = GuildAudioManager.of(guildID);
                            author.getVoiceState()
                                    .flatMap(VoiceState::getChannel)
                                    .doOnNext(vc -> {
                                        System.out.println("Inside of the do on next for auto disconnect!");
                                        if (audioManager.getPlayer().getPlayingTrack() == null){
                                            if (System.currentTimeMillis() - audioManager.getIdleTime() >= 180000){
                                                vc.sendDisconnectVoiceState();
                                            }
                                        }
                                        else {
                                            audioManager.setIdleTime(System.currentTimeMillis());
                                        }

                                    });
                        })

                ).subscribe(); */



        // ########################### DISCONNECT ########################################
        // This closes the bot when disconnected.
        client.onDisconnect().block();
    }
}
