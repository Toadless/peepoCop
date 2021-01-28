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

package net.toaddev.peepocop.command.admin;

import net.toaddev.peepocop.data.Config;
import net.toaddev.peepocop.entities.command.Command;
import net.toaddev.peepocop.entities.command.CommandEvent;
import net.toaddev.peepocop.entities.command.CommandFlag;
import net.toaddev.peepocop.entities.command.CommandRegistry;
import net.toaddev.peepocop.entities.command.init.CommandInitializer;

import javax.annotation.Nonnull;

public class RegistryCommand extends Command
{
    public RegistryCommand()
    {
        super("registry", null);
        addFlags(CommandFlag.DEVELOPER_ONLY);
    }
    @Override
    public void run(@Nonnull CommandEvent ctx)
    {
        if (!Config.INS.getDevelopment())
        {
            ctx.getChannel().sendMessage("This command can only be ran in development.").queue();
            return;
        }
        else if (ctx.getArgs().length < 2)
        {
            ctx.getChannel().sendMessage("Please provide an option. `clear` | `rebuild` | `log`").queue();
            return;
        }
        else if (ctx.getArgs()[1].equals("clear"))
        {
            CommandRegistry.registry.clear();
            CommandRegistry.logger.info("Emptying registry.");
            ctx.getChannel().sendMessage("**Completely emptying the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        }
        else if (ctx.getArgs()[1].equals("rebuild"))
        {
            CommandRegistry.logger.info("Clearing registry.");
            CommandRegistry.registry.clear();
            CommandRegistry.logger.info("Rebuilding registry.");
            CommandInitializer.initCommands();
            CommandRegistry.logger.info("Rebuilt registry.");
            ctx.getChannel().sendMessage("**Completely rebuilding the whole command registry**! I Hope you know what you are doing.").queue();
            return;
        }
        else
        {
            ctx.getChannel().sendMessage("Please provide a valid method. `clear` | `rebuild` | `log`").queue();
            CommandRegistry.logger.info("Failed to apply any actions to the registry.");
            System.out.println(ctx.getArgs()[1]); System.out.println(ctx.getArgs()[1]);
        }
    }
}