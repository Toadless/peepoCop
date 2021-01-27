package uk.toadl3ss.peepocop.command.music.control;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.peepocop.audio.GuildMusicManager;
import uk.toadl3ss.peepocop.audio.PlayerManager;
import uk.toadl3ss.peepocop.entities.command.Command;
import uk.toadl3ss.peepocop.entities.command.CommandEvent;
import uk.toadl3ss.peepocop.entities.command.CommandFlag;

public class VolumeCommand extends Command
{
    public VolumeCommand()
    {
        super("setvolume", "Changes the players volume");
        addFlags(CommandFlag.GUILD_ONLY);
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel())
        {
            channel.sendMessage("You need to be in a voice channel for this command to work.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in a voice channel for this to work.").queue();
            return;
        }
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel()))
        {
            channel.sendMessage("You need to be in the same voice channel as me for this to work!").queue();
            return;
        }
        if (ctx.getArgs().length < 2)
        {
            channel.sendMessage("You need to provide a volume to set").queue();
        }
        try
        {
            int volume = Integer.parseInt(ctx.getArgs()[1]);
            if (volume <= -1){
                channel.sendMessage("Please provide a valid volume to set").queue();
                return;
            }
            if (volume > 200){
                channel.sendMessage("Please provide a valid volume to set").queue();
                return;
            }
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;
            audioPlayer.setVolume(volume);
            channel.sendMessageFormat("Set the volume to: %s", volume).queue();
        } catch (NumberFormatException e)
        {
            ctx.getChannel().sendMessage("Please provide a valid number.").queue();
        }
    }
}