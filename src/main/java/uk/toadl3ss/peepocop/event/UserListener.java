package uk.toadl3ss.peepocop.event;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class UserListener
{

    public abstract void onGuildMessageReceived(GuildMessageReceivedEvent event);

}

