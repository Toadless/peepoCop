package uk.toadl3ss.peepocop.entities.command.init;

import uk.toadl3ss.peepocop.command.TestCommand;
import uk.toadl3ss.peepocop.command.admin.EvalCommand;
import uk.toadl3ss.peepocop.command.admin.ExitCommand;
import uk.toadl3ss.peepocop.command.music.control.*;
import uk.toadl3ss.peepocop.command.music.info.DurationCommand;
import uk.toadl3ss.peepocop.command.music.info.InfoCommand;
import uk.toadl3ss.peepocop.command.music.info.NowPlayingCommand;
import uk.toadl3ss.peepocop.command.music.info.QueueCommand;
import uk.toadl3ss.peepocop.command.music.seeking.SeekCommand;
import uk.toadl3ss.peepocop.entities.command.CommandManager;

import static uk.toadl3ss.peepocop.entities.command.CommandRegistry.registerAlias;
import static uk.toadl3ss.peepocop.entities.command.CommandRegistry.registerCommand;

public class CommandInitializer extends CommandManager
{
    public static void initCommands()
    {
        registerCommand(new TestCommand());
        registerCommand(new PlayCommand());
        registerCommand(new JoinCommand());
        registerCommand(new LeaveCommand());
        registerCommand(new StopCommand());
        registerCommand(new DestroyCommand());
        registerCommand(new PauseCommand());
        registerCommand(new RepeatCommand());
        registerCommand(new ShuffleCommand());
        registerCommand(new SkipCommand());
        registerCommand(new SoundCloud());
        registerCommand(new VolumeCommand());
        registerCommand(new DurationCommand());
        registerCommand(new InfoCommand());
        registerCommand(new NowPlayingCommand());
        registerCommand(new QueueCommand());
        registerCommand(new RepeatCommand());
        registerCommand(new SeekCommand());

        registerCommand(new EvalCommand());
        registerCommand(new ExitCommand());

        registerAlias("setvolume", "volume");
        registerAlias("skip", "next");
        registerAlias("repeat", "loop");
        registerAlias("nowplaying", "np");
        registerAlias("leave", "disconnect");
        registerAlias("join", "summon");
        registerAlias("join", "connect");
        registerAlias("pause", "resume");
        registerAlias("soundcloud", "sc");
        registerAlias("play", "p");
        registerAlias("duration", "position");
        registerAlias("volume", "vol");
    }
}