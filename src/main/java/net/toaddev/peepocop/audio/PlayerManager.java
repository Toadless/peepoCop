/*
 * MIT License
 *
 * Copyright (c) 2021 Toadless @ toaddev.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package net.toaddev.peepocop.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.toaddev.peepocop.data.Constants;
import net.toaddev.peepocop.entities.database.GuildRegistry;
import net.toaddev.peepocop.entities.exceptions.MusicException;
import net.toaddev.peepocop.main.Launcher;
import net.toaddev.peepocop.utils.DiscordUtil;
import net.toaddev.peepocop.utils.FormatTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager
{
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager()
    {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild)
    {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) ->
        {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    /**
     *
     * @param channel The channel that the message took place in
     * @param trackUrl The track url that has been provided
     * @param event The {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event} to use.
     */
    public void loadAndPlay(TextChannel channel, String trackUrl, MessageReceivedEvent event)
    {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack track)
            {
                musicManager.scheduler.queue(track);
                sendAddedEmbed(track, channel, event);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist)
            {
                final List<AudioTrack> tracks = playlist.getTracks();
                if (playlist.isSearchResult())
                { // Adding a single song from search result
                    sendAddedEmbed(tracks.get(0), channel, event);
                    musicManager.scheduler.queue(tracks.get(0));
                }
                else
                { // Adding a whole playlist
                    channel.sendMessage("Adding to queue: `")
                            .append(String.valueOf(tracks.size()))
                            .append("` tracks from playlist `")
                            .append(playlist.getName())
                            .append("`")
                            .queue();
                    musicManager.scheduler.queue(tracks.get(0));
                    for (final AudioTrack track: tracks)
                    {
                        musicManager.scheduler.queue(track);
                    }
                }
                return;
            }

            @Override
            public void noMatches()
            {
                if (Launcher.DATABASE_ENABLED)
                {
                    channel.sendMessage(":x: No songs found matching `" + event.getMessage().getContentRaw().replace(GuildRegistry.getPrefix(Long.parseLong(event.getGuild().getId())) + "play", "") + "`").queue();
                    return;
                }
                channel.sendMessage(":x: No songs found matching `" + event.getMessage().getContentRaw().replace(Constants.GUILD_PREFIX + "play", "") + "`").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception)
            {
                channel.sendMessage(":x: Failed to load the provided song. Please try again").queue();
                throw new MusicException("Failed to load a song");
            }
        });
    }

    public static PlayerManager getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

    /**
     *
     * @param track The audio track
     * @param channel The text channel to send the embed to
     * @param event The {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event} to use.
     */
    public static void sendAddedEmbed(AudioTrack track, TextChannel channel, MessageReceivedEvent event)
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Added to queue", track.getInfo().uri, event.getAuthor().getAvatarUrl());
        embed.setDescription("[" + track.getInfo().title + "](" + track.getInfo().uri + ")");
        embed.addField("**Channel**", track.getInfo().author, true);
        embed.addField("**Song Duration**", FormatTime.formatTime(track.getDuration()), true);
        embed.setColor(DiscordUtil.getEmbedColor());
        channel.sendMessage(embed.build()).queue();
    }
}