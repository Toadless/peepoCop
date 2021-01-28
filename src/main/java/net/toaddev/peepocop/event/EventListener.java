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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.toaddev.peepocop.entities.command.CommandManager;
import net.toaddev.peepocop.entities.database.GuildRegistry;
import net.toaddev.peepocop.data.Constants;
import net.toaddev.peepocop.main.Launcher;

public class EventListener extends AbstractEventListener
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getMessage().isWebhookMessage())
        {
            return;
        }
        if (event.getAuthor().equals(event.getJDA().getSelfUser()))
        {
            return;
        }
        if (event.getMember().getUser().isBot())
        {
            return;
        }
        CommandManager commandManager = Launcher.getCommandManager();
        if (Launcher.DATABASE_ENABLED)
        {
            String[] args = event.getMessage().getContentRaw().split(" ");
            Long guildId = Long.parseLong(event.getGuild().getId());
            String guildPrefix = GuildRegistry.getPrefix(guildId);
            if (event.getMessage().getContentRaw().startsWith(guildPrefix))
            {
                commandManager.handleCommand(event, guildPrefix, args);
                return;
            }
            // If the prefix is @<bot> <command>.
            String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
            if (event.getMessage().getContentRaw().startsWith(mention))
            {
                commandManager.handleCommand(event, guildPrefix, args);
                return;
            }
            return;
        }
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (event.getMessage().getContentRaw().startsWith(Constants.GUILD_PREFIX))
        {
            commandManager.handleCommand(event, Constants.GUILD_PREFIX, args);
            return;
        }
        // If the prefix is @<bot> <command>.
        String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
        if (event.getMessage().getContentRaw().startsWith(mention))
        {
            commandManager.handleCommand(event, Constants.GUILD_PREFIX, args);
            return;
        }
        return;
    }
}