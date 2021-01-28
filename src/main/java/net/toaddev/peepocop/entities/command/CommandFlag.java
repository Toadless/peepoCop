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

package net.toaddev.peepocop.entities.command;

/**
 * An enum for flags related to {@link CommandManager command} execution.
 */
public enum CommandFlag
{
    /**
     * If the {@link CommandManager command} must be executed in a {@link net.dv8tion.jda.api.entities.Guild guild}.
     *
     */
    GUILD_ONLY,
    /**
     * If the {@link CommandManager command} can only be used by developers.
     *
     * @see CommandEvent#isDeveloper().
     */
    DEVELOPER_ONLY,
    /**
     * If the {@link CommandManager command} can only be used by server admins.
     *
     * @see CommandEvent#isServerAdmin().
     */
    SERVER_ADMIN_ONLY,
    /**
     * If the {@link CommandManager command} is disabled.
     *
     */
    /**
     * If the {@link CommandManager commands} message needs to be deleted.
     */
    AUTO_DELETE_MESSAGE,

    /**
     * If the {@link CommandManager command} is disabled.
     */
    DISABLED;
}