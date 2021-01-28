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

package net.toaddev.peepocop.command.music.control;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.toaddev.peepocop.entities.command.Command;
import net.toaddev.peepocop.entities.command.CommandEvent;
import net.toaddev.peepocop.entities.command.CommandFlag;
import net.toaddev.peepocop.utils.IsUrl;
import net.toaddev.peepocop.audio.GuildMusicManager;
import net.toaddev.peepocop.audio.PlayerManager;

public class PlayCommand extends Command
{

    public PlayCommand() {
        super("play", "Plays the provided song");
        addFlags(CommandFlag.GUILD_ONLY);
        addSelfPermissions(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK);
        addMemberPermissions(Permission.VOICE_CONNECT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx) {
        String songName = ctx.getMessage().getContentRaw().replaceFirst("^" + ctx.getPrefix() + "play" + " ", "");
        songName = songName.replaceFirst("^" + ctx.getPrefix() + "p" + " ", "");
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (ctx.getArgs().length < 2)
        {
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
            if (musicManager.audioPlayer.getPlayingTrack() != null)
            {
                boolean paused = musicManager.scheduler.player.isPaused();
                musicManager.scheduler.player.setPaused(!paused);
                String status = paused ? "paused" : "playing";
                String newStatus = !paused ? "paused" : "playing";
                channel.sendMessage("Changed the player from **" + status+ "** to **" + newStatus + "**. \n This event occured because a song is and no arguments were provided!").queue();
                return;
            }
            channel.sendMessage("Please provide a url or search query.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        String song = ctx.getArgs()[1];
        if (!IsUrl.isUrl(song))
        {
            song = "ytsearch:" + songName;
            channel.sendMessage("Searching :mag_right: `" + songName + "`").queue();
        }
        PlayerManager.getInstance()
                .loadAndPlay(channel, song, ctx.getEvent());
    }
}