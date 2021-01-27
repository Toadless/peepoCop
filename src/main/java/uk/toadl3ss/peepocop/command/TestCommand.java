package uk.toadl3ss.peepocop.command;

import net.dv8tion.jda.api.Permission;
import uk.toadl3ss.peepocop.entities.command.Command;
import uk.toadl3ss.peepocop.entities.command.CommandEvent;
import uk.toadl3ss.peepocop.entities.command.CommandFlag;

import javax.annotation.Nonnull;

public class TestCommand extends Command
{
    public TestCommand()
    {
        super("test", "A simple test command");
        addFlags(CommandFlag.AUTO_DELETE_MESSAGE);
        addSelfPermissions(Permission.MANAGE_SERVER);
    }
    @Override
    public void run(@Nonnull CommandEvent ctx)
    {
        ctx.getChannel().sendMessage("Test").queue();
    }
}