package uk.toadl3ss.peepocop.command.admin;

import uk.toadl3ss.peepocop.entities.command.Command;
import uk.toadl3ss.peepocop.entities.command.CommandEvent;
import uk.toadl3ss.peepocop.entities.command.CommandFlag;
import uk.toadl3ss.peepocop.main.Launcher;
import uk.toadl3ss.peepocop.utils.ExitCodes;

import javax.annotation.Nonnull;

public class ExitCommand extends Command
{
    public ExitCommand()
    {
        super("exit", null);
        addFlags(CommandFlag.DEVELOPER_ONLY);
    }

    @Override
    public void run(@Nonnull CommandEvent ctx)
    {
        ctx.getChannel().sendMessage("This will **shut down the whole bot**.").complete();
        Launcher.shutdown(ExitCodes.EXIT_CODE_NORMAL);
    }
}