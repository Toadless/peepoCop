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

package net.toaddev.peepocop.command.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.toaddev.peepocop.entities.command.Command;
import net.toaddev.peepocop.entities.command.CommandEvent;
import net.toaddev.peepocop.entities.command.CommandFlag;
import net.toaddev.peepocop.utils.EmbedUtils;

import javax.annotation.Nonnull;

public class BanCommand extends Command
{
    public BanCommand()
    {
        super("ban", "Bans a user from the guild");
        addFlags(CommandFlag.SERVER_ADMIN_ONLY);
        addSelfPermissions(Permission.BAN_MEMBERS);
    }
    @Override
    public void run(@Nonnull CommandEvent ctx)
    {
        if (ctx.getArgs().length < 2) {
            ctx.getChannel().sendMessage("Please provide a member to ban!").queue();
            return;
        }
        if (ctx.getMessage().getMentionedUsers().isEmpty()) {
            ctx.getChannel().sendMessage("Please provide a valid member to ban!").queue();
            return;
        }
        final Member target = ctx.getMessage().getMentionedMembers().get(0);
        final Member selfMember = ctx.getGuild().getSelfMember();
        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedUtils.sendError(ctx.getChannel(), "I cannot interact with this user.");
            return;
        }
        ctx.getGuild()
                .ban(target, 1)
                .queue();
        EmbedUtils.sendSuccess(ctx.getChannel(), "Successfully banned: " + target.getUser().getName() + ".");
    }
}