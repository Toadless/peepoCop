package uk.toadl3ss.peepocop.event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.toadl3ss.peepocop.data.Constants;
import uk.toadl3ss.peepocop.entities.command.CommandManager;
import uk.toadl3ss.peepocop.entities.database.GuildRegistry;
import uk.toadl3ss.peepocop.main.Launcher;

public class EventListener extends AbstractEventListener
{
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getMessage().isWebhookMessage())
        {
            return;
        }
        if (event.getAuthor().equals(event.getJDA().getSelfUser()))
        {
            return;
        }
        if (event.getMember().getUser().isBot())
        {
            return;
        }
        CommandManager commandManager = Launcher.getCommandManager();
        if (Launcher.DATABASE_ENABLED)
        {
            String[] args = event.getMessage().getContentRaw().split(" ");
            Long guildId = Long.parseLong(event.getGuild().getId());
            String guildPrefix = GuildRegistry.getPrefix(guildId);
            if (event.getMessage().getContentRaw().startsWith(guildPrefix))
            {
                commandManager.handleCommand(event, guildPrefix, args);
                return;
            }
            // If the prefix is @<bot> <command>.
            String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
            if (event.getMessage().getContentRaw().startsWith(mention))
            {
                commandManager.handleCommand(event, guildPrefix, args);
                return;
            }
            return;
        }
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (event.getMessage().getContentRaw().startsWith(Constants.GUILD_PREFIX))
        {
            commandManager.handleCommand(event, Constants.GUILD_PREFIX, args);
            return;
        }
        // If the prefix is @<bot> <command>.
        String mention = "<@!" + event.getJDA().getSelfUser().getId() + ">";
        if (event.getMessage().getContentRaw().startsWith(mention))
        {
            commandManager.handleCommand(event, Constants.GUILD_PREFIX, args);
            return;
        }
        return;
    }
}