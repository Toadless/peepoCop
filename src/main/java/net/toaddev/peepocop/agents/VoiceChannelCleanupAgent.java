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

package net.toaddev.peepocop.agents;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.toaddev.peepocop.main.Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.toaddev.peepocop.audio.GuildMusicManager;
import net.toaddev.peepocop.audio.PlayerManager;

import java.util.concurrent.atomic.AtomicInteger;

public class VoiceChannelCleanupAgent extends Thread
{
    private static final Logger log = LoggerFactory.getLogger(VoiceChannelCleanupAgent.class);
    private static final int INTERVAL_MILLIS = 60000;
    @Override
    public void run()
    {
        log.info("Started VoiceChannelCleanupAgent");
        while (true)
        {
            try
            {
                cleanup();
                sleep(INTERVAL_MILLIS);
            } catch (Exception e)
            {
                try
                {
                    sleep(1000);
                } catch (InterruptedException e1)
                {
                    log.error("Interrupted while sleeping after an exception in voice channel watchdog", e);
                }
            }
        }
    }
    private static void cleanup()
    {
        JDA jda = Launcher.getJda();
        jda.getGuilds().forEach((guild -> {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
            VoiceChannel vc = guild.getAudioManager().getConnectedChannel();
            if (vc == null)
            {
                return; // If there is no voice channel we dont want to check how many members is in the "voice channel".
            }
            AtomicInteger humansInVC = getHumanMembersInVC(vc);
            if (Integer.parseInt(String.valueOf(humansInVC)) == 0)
            {
                musicManager.audioPlayer.destroy();
                AudioManager audioManager = guild.getAudioManager();
                audioManager.closeAudioConnection();
                log.info("Disconnect from one of " + guild.getName() + "'s voice channels because of inactivity.");
            }
        }));
    }

    private static AtomicInteger getHumanMembersInVC(VoiceChannel vc)
    {
        AtomicInteger members = new AtomicInteger();
        vc.getMembers().forEach(member ->
        {
            if (member.getUser().isBot())
            {
                return;
            }
            members.getAndIncrement();
        });
        return members;
    }
}