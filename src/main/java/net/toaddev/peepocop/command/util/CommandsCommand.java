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

package net.toaddev.peepocop.command.util;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.RestAction;
import net.toaddev.peepocop.entities.command.Command;
import net.toaddev.peepocop.entities.command.CommandEvent;
import net.toaddev.peepocop.entities.command.CommandFlag;
import net.toaddev.peepocop.entities.command.CommandRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandsCommand extends Command
{
    public CommandsCommand()
    {
        super("commands", "Displays all of the bots commands");
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(@Nonnull CommandEvent ctx)
    {
        StringBuilder helpString = new StringBuilder();
        helpString.append("```md\n");
        String title = "< {name} Commands >\n";
        title = title.replace("{name}", ctx.getJDA().getSelfUser().getName());
        helpString.append(title);

        List<String> helpList = new ArrayList<>();
        CommandRegistry.registry.forEach(((s, command) ->
        {
            String help = command.getDescription();
            if (helpList.contains(help)) {
                return;
            }
            if (help == null) {
                return;
            }
            helpString.append(ctx.getPrefix() + s + "\n");
            helpString.append("#" + help + "\n");
            helpList.add(help);
        }));

        helpString.append("\n```");

        RestAction<PrivateChannel> privateChannel = ctx.getMember().getUser().openPrivateChannel();
        privateChannel
                .flatMap(channel -> channel.sendMessage(helpString))
                .flatMap(channel -> ctx.getChannel().sendMessage(ctx.getMember().getUser().getName() + ": Documentation has been sent to your DMs!"))
                .queue(null, new ErrorHandler().ignore(ErrorResponse.UNKNOWN_MESSAGE).handle(ErrorResponse.CANNOT_SEND_TO_USER, (e) ->
                {
                    ctx.getChannel().sendMessage(helpString.toString()).queue();
                }));
    }
}