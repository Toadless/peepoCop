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

package net.toaddev.peepocop.entities.command;

import com.mongodb.lang.NonNull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.toaddev.peepocop.entities.database.GuildRegistry;
import net.toaddev.peepocop.event.EventListener;
import net.toaddev.peepocop.utils.DiscordUtil;

import javax.annotation.Nonnull;
import java.util.List;

/**
 *  A class representing an event for a {@link CommandManager command}.
 */
public class CommandEvent
{
    private final MessageReceivedEvent event;
    private final JDA jda;
    private final String[] args;
    private final MessageChannel channel;
    private final Member member;
    private final String prefix;
    private final Message message;
    private final Guild guild;

    /**
     *  Constructs a new {@link CommandEvent event}.
     *
     * @param event The {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event} to use.
     * @param args The {@link EventListener args} to use.
     * @param prefix The {@link GuildRegistry prefix} to use.
     */
    public CommandEvent(MessageReceivedEvent event, String[] args, String prefix)
    {
        this.event = event;
        this.jda = event.getJDA();
        this.args = args;
        this.channel = event.getChannel();
        this.member = event.getMember();
        this.prefix = prefix;
        this.message = event.getMessage();
        this.guild = event.getGuild();
    }

    /**
     *
     * @return The args/
     */
    @NonNull
    public String[] getArgs()
    {
        return args;
    }

    /**
     *
     * @return The prefix. This varies per {@link net.dv8tion.jda.api.entities.Guild guild}.
     */
    @NonNull
    public String getPrefix()
    {
        return prefix;
    }

    /**
     *
     * @return The {@link net.dv8tion.jda.api.events.message.MessageReceivedEvent event} to use.
     */
    public MessageReceivedEvent getEvent()
    {
        return event;
    }

    /**
     *
     * @return The {@link net.dv8tion.jda.api.JDA jda instance}.
     */
    public JDA getJDA()
    {
        return jda;
    }

    /**
     *
     * @return The {@link net.dv8tion.jda.api.entities.Guild guild} the event took place in.
     */
    public Guild getGuild()
    {
        return guild;
    }

    /**
     *
     * @return The {@link net.dv8tion.jda.api.entities.Member member} for this {@link CommandEvent event}.
     *
     * @throws java.lang.NullPointerException if the member is null.
     */
    public Member getMember()
    {
        return member;
    }

    /**
     *
     * @return The message sent by the user.
     */
    public Message getMessage()
    {
        return message;
    }

    /**
     *
     * @return The channel that the event took place in
     */
    public MessageChannel getChannel()
    {
        return channel;
    }

    /**
     *
     * @return If the {@link #getMember()} is a developer.
     */
    public Boolean isDeveloper()
    {
        return DiscordUtil.isOwner(getMember().getUser());
    }

    /**
     *
     * @return If the {@link #getMember()} is a server admin.
     */
    public Boolean isServerAdmin()
    {
        return DiscordUtil.isServerAdmin(getMember());
    }

    /**
     * Checks whether the {@link net.dv8tion.jda.api.entities.Member self member} has all the needed permissions for execution.
     *
     * @param permissions The {@link net.dv8tion.jda.api.Permission permissions} to check.
     * @return Whether the {@link net.dv8tion.jda.api.entities.Member author} has the requred permissions.
     */
    @Nonnull
    public Boolean selfPermissionCheck(List<Permission> permissions)
    {
        return event.getGuild().getSelfMember().hasPermission(permissions);
    }

    /**
     * Checks whether the author has all the needed permissions for execution.
     *
     * @param permissions The {@link net.dv8tion.jda.api.Permission permissions} to check.
     * @return Whether the {@link net.dv8tion.jda.api.entities.Member author} has the {@link Command#getMemberRequiredPermissions() permissions}.
     */
    @Nonnull
    public Boolean memberPermissionCheck(List<Permission> permissions)
    {
        return (event.getMember() != null && event.getMember().hasPermission((GuildChannel) event.getChannel(), permissions));
    }
}