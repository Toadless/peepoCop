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

package net.toaddev.peepocop.command.fun;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.toaddev.peepocop.entities.command.Command;
import net.toaddev.peepocop.entities.command.CommandEvent;
import net.toaddev.peepocop.entities.command.CommandFlag;
import net.toaddev.peepocop.utils.DiscordUtil;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class AvatarCommand extends Command
{
    public AvatarCommand()
    {
        super("avatar", "Displays a users avatar.");
        addFlags(CommandFlag.GUILD_ONLY, CommandFlag.AUTO_DELETE_MESSAGE);
        addSelfPermissions(Permission.MESSAGE_EMBED_LINKS);
    }
    @Override
    public void run(@Nonnull CommandEvent ctx)
    {
        if(ctx.getMessage().getMentionedMembers().isEmpty())
        {
            ctx.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle(ctx.getMember().getUser().getAsTag() + "'s Avatar")
                    .setColor(DiscordUtil.getEmbedColor())
                    .setImage(ctx.getMember().getUser().getAvatarUrl() + "?size=4096").build())
                    .delay(5, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
        }
        else
        {
            ctx.getMessage().getMentionedMembers().forEach(member ->
                    ctx.getChannel().sendMessage(new EmbedBuilder()
                            .setTitle(member.getUser().getAsTag() + "'s Avatar")
                            .setColor(DiscordUtil.getEmbedColor())
                            .setImage(member.getUser().getEffectiveAvatarUrl() + "?size=4096").build())
                            .delay(5, TimeUnit.SECONDS)
                            .flatMap(Message::delete)
                            .queue()
                    );
        }

    }
}