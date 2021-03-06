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

package net.toaddev.peepocop.data;

import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Config
{
    // ################################################################################
    // ##                     Login / credentials
    // ################################################################################
    private File config;
    public static Config INS;
    private String Prefix;
    private String Token;
    private Boolean Development;
    private String OwnerID;
    private String Game;
    private String Status;
    private Boolean Invite;
    private String Join;
    private int numShards;
    private int shardStart;
    private int maxShards;
    private Boolean database;
    private String mongoUri;
    private String mongoName;
    private final Logger logger = LoggerFactory.getLogger(Config.class);

    /**
     *
     * @param file The config files name
     */
    private Config(String file)
    {
        config = new File(file);
        initConfig();
    }

    /**
     *
     * @param file The config files name
     */
    public static void init(String file)
    {
        INS = new Config(file);
    }

    private void initConfig()
    {
        FileConfiguration config = new YamlConfiguration();
        try
        {
            config.load(this.config);
        } catch (InvalidConfigurationException | IOException e)
        {
            logger.error("Invalid application.yml");
            return;
        }
        this.Token = config.getString("credentials.discordBotToken");
        this.Prefix = config.getString("config.prefix");
        this.Development = config.getBoolean("config.development");
        this.OwnerID = config.getString("config.ownerid");
        this.Game = config.getString("config.game");
        this.Status = config.getString("config.status");
        this.Invite = config.getBoolean("config.invite");
        this.Join = config.getString("config.join");
        this.numShards = config.getInt("config.shardCount");
        this.database = config.getBoolean("config.database");
        this.mongoUri = config.getString("credentials.mongoDBUri");
        this.mongoName = config.getString("credentials.mongoDBName");
        this.shardStart = config.getInt("config.shardStart");
        this.maxShards = config.getInt("config.maxShards");
    }

    /**
     *
     * @return The token to the discord api login with
     */
    public String getToken()
    {
        return Token;
    }

    /**
     *
     * @return The bots default prefix
     */
    public String getPrefix()
    {
        return Prefix;
    }

    /**
     *
     * @return If the bot is in development mode
     */
    public Boolean getDevelopment()
    {
        return Development;
    }

    /**
     *
     * @return The discord bots owner id
     */
    public String getOwnerID()
    {
        return OwnerID;
    }

    /**
     *
     * @return The discord bots status
     */
    public String getGame()
    {
        return Game;
    }

    /**
     *
     * @return The discord bots status type
     */
    public String getStatus()
    {
        return Status;
    }

    /**
     *
     * @return The guild invite message
     */
    public Boolean getInvite()
    {
        return Invite;
    }

    /**
     *
     * @return The guild join message
     */
    public String getJoin()
    {
        return Join;
    }

    /**
     *
     * @return The number of shards that will be built
     */
    public int getNumShards()
    {
        return numShards;
    }

    /**
     *
     * @return The database name
     */
    public Boolean getDatabase()
    {
        return database;
    }

    /**
     *
     * @return The mongodb connection uri
     */
    public String getMongoUri()
    {
        return mongoUri;
    }

    /**
     *
     * @return The mongodb database name
     */
    public String getMongoName()
    {
        return mongoName;
    }

    /**
     *
     * @return The shard builder staring point
     */
    public int getShardStart()
    {
        return shardStart;
    }

    /**
     *
     * @return The max amount of shards
     */
    public int getMaxShards()
    {
        return maxShards;
    }
}
