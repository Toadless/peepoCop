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

package net.toaddev.peepocop.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.toaddev.peepocop.data.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordUtil
{
    private static final Logger log = LoggerFactory.getLogger(DiscordUtil.class);
    private static final int embedColor = 0xfc0303;

    private DiscordUtil() {

    }

    public static String getOwnerId()
    {
        return Config.INS.getOwnerID();
    }

    public static Boolean isOwner(User user)
    {
        String userId = user.getId();
        String ownerId = getOwnerId();
        if (userId.equals(ownerId))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static Boolean isServerAdmin(Member member)
    {
        if (member.hasPermission(Permission.MANAGE_SERVER))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static int getEmbedColor()
    {
        return embedColor;
    }
}