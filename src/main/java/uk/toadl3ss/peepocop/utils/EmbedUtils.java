package uk.toadl3ss.peepocop.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.exceptions.PermissionException;
import uk.toadl3ss.peepocop.entities.command.CommandEvent;
import uk.toadl3ss.peepocop.main.Launcher;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class EmbedUtils
{
    private EmbedUtils()
    {
        //Overrides the default, public, constructor
    }

    public static void sendError(MessageChannel channel, String errorText)
    {
        sendDeletingEmbed(channel, new EmbedBuilder()
                .setDescription(errorText)
                .setColor(Color.RED)
                .setTimestamp(Instant.now()));
    }

    public static void sendSuccess(MessageChannel channel, String successText)
    {
        sendDeletingEmbed(channel, new EmbedBuilder()
                .setDescription(successText)
                .setColor(Color.GREEN)
                .setTimestamp(Instant.now()));
    }

    public static void sendDisabledError(CommandEvent ctx)
    {
        sendDeletingEmbed(ctx.getChannel(), new EmbedBuilder()
                .setDescription("This command is currently disabled!")
                .setColor(Color.RED)
                .setTimestamp(Instant.now()));
    }

    public static void sendDeletingEmbed(MessageChannel channel, EmbedBuilder embed, long delay)
    {
        try {
            channel.sendMessage(embed.build()).queue(message -> message.delete().queueAfter(delay, TimeUnit.MILLISECONDS, null, error ->
            {
            }));
        } catch (PermissionException e) {
            try {
                channel.sendMessage("I do not have any permissions. My lowest required permissions is `MESSAGE_EMBED_LINKS`.").queue();
            } catch (PermissionException exception) {
                Guild guild = Launcher.getJda().getGuildChannelById(channel.getId()).getGuild();
                if (guild == null) {
                    return;
                }
                guild.leave().queue();
            }
        }
    }

    public static void sendDeletingEmbed(MessageChannel channel, EmbedBuilder embed)
    {
        sendDeletingEmbed(channel, embed, 10000);
    }
}