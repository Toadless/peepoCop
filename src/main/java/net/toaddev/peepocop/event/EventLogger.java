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

package net.toaddev.peepocop.event;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.toaddev.peepocop.data.Config;
import net.toaddev.peepocop.data.Constants;

public class EventLogger extends ListenerAdapter
{
    public static JDA jda;
    public static final Logger log = LoggerFactory.getLogger(EventLogger.class);

    private void send(String msg) {
        log.info(msg);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        send
                (
                        "Joined guild `" + event.getGuild() + "`. Users: `" + event.getGuild().getMembers().size() + "`."
                )
        ;
        try
        {
            String defaultMessage = Config.INS.getJoin();
            defaultMessage = defaultMessage.replace("{prefix}", Constants.GUILD_PREFIX);
            defaultMessage = defaultMessage.replace("{guildName}", event.getGuild().getName());
            TextChannel systemChannel = event.getGuild().getDefaultChannel();
            if (systemChannel == null) {
                event.getGuild().getDefaultChannel().sendMessage(defaultMessage).queue();
                return;
            }
            systemChannel.sendMessage(defaultMessage).queue();
        } catch (PermissionException e)
        {
            log.info("Unable to send welcome message. Insufficient permissions.");
        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event)
    {
        send
                (
                        "Left guild `" + event.getGuild() + "`. Users: `" + event.getGuild().getMembers().size() + "`."
                )
        ;
    }
}