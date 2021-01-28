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

package net.toaddev.peepocop.entities.command.init;

import net.toaddev.peepocop.command.admin.*;
import net.toaddev.peepocop.command.maintanance.ShardsCommand;
import net.toaddev.peepocop.command.music.control.*;
import net.toaddev.peepocop.command.util.*;
import net.toaddev.peepocop.command.fun.AvatarCommand;
import net.toaddev.peepocop.command.fun.CoinflipCommand;
import net.toaddev.peepocop.command.fun.JokeCommand;
import net.toaddev.peepocop.command.maintanance.StatsCommand;
import net.toaddev.peepocop.command.moderation.BanCommand;
import net.toaddev.peepocop.command.moderation.ClearCommand;
import net.toaddev.peepocop.command.moderation.KickCommand;
import net.toaddev.peepocop.command.music.info.DurationCommand;
import net.toaddev.peepocop.command.music.info.InfoCommand;
import net.toaddev.peepocop.command.music.info.NowPlayingCommand;
import net.toaddev.peepocop.command.music.info.QueueCommand;
import net.toaddev.peepocop.command.music.seeking.RestartCommand;
import net.toaddev.peepocop.command.music.seeking.SeekCommand;
import net.toaddev.peepocop.entities.command.CommandManager;

import static net.toaddev.peepocop.entities.command.CommandRegistry.registerAlias;
import static net.toaddev.peepocop.entities.command.CommandRegistry.registerCommand;

public class CommandInitializer extends CommandManager
{
    public static void initCommands()
    {
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
        registerCommand(new RestartCommand());

        registerCommand(new EvalCommand());
        registerCommand(new ExitCommand());
        registerCommand(new RegistryCommand());
        registerCommand(new ReviveCommand());
        registerCommand(new TestCommand());
        registerCommand(new GuildRebuildCommand());

        registerCommand(new StatsCommand());
        registerCommand(new ShardsCommand());

        registerCommand(new JokeCommand());
        registerCommand(new AvatarCommand());
        registerCommand(new CoinflipCommand());

        registerCommand(new HelpCommand());
        registerCommand(new CommandsCommand());
        registerCommand(new PingCommand());
        registerCommand(new InviteCommand());
        registerCommand(new PrefixCommand());

        registerCommand(new BanCommand());
        registerCommand(new KickCommand());
        registerCommand(new ClearCommand());

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