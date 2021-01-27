package uk.toadl3ss.peepocop.command.music.control;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import uk.toadl3ss.peepocop.audio.PlayerManager;
import uk.toadl3ss.peepocop.entities.command.Command;
import uk.toadl3ss.peepocop.entities.command.CommandEvent;
import uk.toadl3ss.peepocop.entities.command.CommandFlag;

public class SoundCloud extends Command
{
    public SoundCloud() {
        super("soundcloud", "Plays music from soundcloud");
        addFlags(CommandFlag.GUILD_ONLY);
        addMemberPermissions(Permission.VOICE_CONNECT);
        addSelfPermissions(Permission.VOICE_CONNECT);
    }

    @Override
    public void run(@NotNull CommandEvent ctx)
    {
        String songName = ctx.getMessage().getContentRaw().replaceFirst("^" + ctx.getPrefix() + "soundcloud" + " ", "");
        songName = songName.replaceFirst("^" + ctx.getPrefix() + "sc" + " ", "");
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
            channel.sendMessage("Please provide a search query.").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel())
        {
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        String song = "scsearch:" + songName;
        channel.sendMessage("Searching :mag_right: `" + songName + "`").queue();
        PlayerManager.getInstance()
                .loadAndPlay(channel, song, ctx.getEvent());
        return;
    }
}