package uk.toadl3ss.peepocop.entities.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.toadl3ss.peepocop.entities.command.init.CommandInitializer;

public class CommandManager
{
    public CommandManager()
    {
        CommandInitializer.initCommands();
    }

    public void handleCommand(MessageReceivedEvent event, String prefix, String[] args) {
        String providedCommandName = args[0].replace(prefix, "");
        Command command = CommandRegistry.getCommand(providedCommandName);
        if (command == null) {
            return;
        }
        CommandEvent ctx = new CommandEvent(event, args, prefix);
        command.process(ctx);
    }
}