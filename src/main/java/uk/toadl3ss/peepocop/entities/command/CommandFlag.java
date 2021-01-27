package uk.toadl3ss.peepocop.entities.command;

/**
 * An enum for flags related to {@link uk.toadl3ss.peepocop.entities.command.CommandManager command} execution.
 */
public enum CommandFlag
{
    /**
     * If the {@link uk.toadl3ss.peepocop.entities.command.CommandManager command} must be executed in a {@link net.dv8tion.jda.api.entities.Guild guild}.
     *
     */
    GUILD_ONLY,
    /**
     * If the {@link uk.toadl3ss.peepocop.entities.command.CommandManager command} can only be used by developers.
     *
     * @see uk.toadl3ss.peepocop.entities.command.CommandEvent#isDeveloper().
     */
    DEVELOPER_ONLY,
    /**
     * If the {@link uk.toadl3ss.peepocop.entities.command.CommandManager command} can only be used by server admins.
     *
     * @see uk.toadl3ss.peepocop.entities.command.CommandEvent#isServerAdmin().
     */
    SERVER_ADMIN_ONLY,
    /**
     * If the {@link uk.toadl3ss.peepocop.entities.command.CommandManager command} is disabled.
     *
     */
    /**
     * If the {@link uk.toadl3ss.peepocop.entities.command.CommandManager commands} message needs to be deleted.
     */
    AUTO_DELETE_MESSAGE,

    /**
     * If the {@link uk.toadl3ss.peepocop.entities.command.CommandManager command} is disabled.
     */
    DISABLED;
}