package uk.toadl3ss.peepocop.main;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.toadl3ss.peepocop.agents.ShardWatchDogAgent;
import uk.toadl3ss.peepocop.agents.VoiceChannelCleanupAgent;
import uk.toadl3ss.peepocop.data.Config;
import uk.toadl3ss.peepocop.data.Constants;
import uk.toadl3ss.peepocop.entities.command.CommandManager;
import uk.toadl3ss.peepocop.entities.command.CommandRegistry;
import uk.toadl3ss.peepocop.entities.command.init.CommandInitializer;
import uk.toadl3ss.peepocop.entities.database.DatabaseManager;
import uk.toadl3ss.peepocop.entities.database.GuildRegistry;
import uk.toadl3ss.peepocop.event.ShardListener;
import uk.toadl3ss.peepocop.utils.SetActivity;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Launcher
{
    public static final Logger logger = LoggerFactory.getLogger(Launcher.class);
    public static String version = "1.0.0 BETA";
    private static final ArrayList<Launcher> shards = new ArrayList<>();
    private static AtomicInteger numShardsReady = new AtomicInteger(0);
    public static final long START_TIME = System.currentTimeMillis();
    public static final int UNKNOWN_SHUTDOWN_CODE = -991023;
    public static int shutdownCode = UNKNOWN_SHUTDOWN_CODE;
    public static final int SHARD_CREATION_SLEEP_INTERVAL = 5100;
    private static boolean vanity = true;
    private boolean hasReadiedOnce = false;
    public static boolean DATABASE_ENABLED = false;
    static JDA jda;
    ShardListener shardListener = null;
    public static uk.toadl3ss.peepocop.event.EventListener listenerBot;
    private static DatabaseManager databaseManager;
    private static CommandManager commandManager;
    private static String getVersionInfo()
    {
        String indentation = "\t";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z");
        String startTime = format.format(new Date());
        int Cores = Runtime.getRuntime().availableProcessors();
        String versionInfo = new StringBuilder()
                .append(indentation + "Version:        ") .append(Launcher.version + "\n")
                .append(indentation + "Development:    ") .append(Config.INS.getDevelopment() + "\n")
                .append(indentation + "Cores:          ") .append(Cores + "\n")
                .append(indentation + "Author:         ") .append("Toadless" + "\n")
                .append(indentation + "StartTime:      ") .append(startTime + "\n")
                .append(indentation + "JVM:            ") .append(System.getProperty("java.version") + "\n")
                .append(indentation + "Lavaplayer      ") .append(PlayerLibrary.VERSION + "\n")
                .append(indentation + "JDA             ") .append(JDAInfo.VERSION + "\n")
                .append("\n")
                .toString()
                ;
        if (vanity)
        {
            versionInfo =
                    getVanity() +
                            "\n" +
                            "\n" +
                            versionInfo
            ;
        }
        return versionInfo;
    }
    private static String getVanity()
    {
        String red = "[31m";
        String green = "[32m";
        String defaultC = "[0m";

        String vanity = ("\n                         r               ____\n" +
                "       g.  r _ __   ___  ___ _ __   ___  / ___|___  _ __ g__ _ _\n" +
                "      g/\\\\ r| '_ \\ / _ \\/ _ \\ '_ \\ / _ \\| |   / _ \\| '_ \\g\\ \\ \\ \\\n" +
                "     g( ( )r| |_) |  __/  __/ |_) | (_) | |__| (_) | |_) |g\\ \\ \\ \\\n" +
                "      g\\\\/ r| .__/ \\___|\\___| .__/ \\___/ \\____\\___/| .__/  g) ) ) )\n" +
                "       g'  r|_|             |_|                    |_|    g/ / / /\n" +
                "    d====================================================g/_/_/_/d");

        vanity = vanity.replace("r", red);
        vanity = vanity.replace("g", green);
        vanity = vanity.replace("d", defaultC);
        return vanity;
    }
    public static void main(String[] args)
    {
        Config.init("application.yml");
        if (Config.INS.getDevelopment())
        {
            version = version + " " + "DEV";
        }
        if (args.length > 0 &&
                (args[0].equals("-v".toLowerCase()) || args[0].equals("--version".toLowerCase(Locale.ROOT))))
        {
            vanity = false;
            logger.info(getVersionInfo());
            return;
        }
        logger.info(getVersionInfo());
        logger.info("Starting peepoCop v" + version + ".");

        listenerBot = new uk.toadl3ss.peepocop.event.EventListener();

        Constants.Init();

        commandManager = new CommandManager();

        logger.info("Loaded commands, registry size is " + CommandRegistry.getSize());


        DATABASE_ENABLED = Config.INS.getDatabase();

        if (DATABASE_ENABLED)
        {
            if (Config.INS.getMongoUri() == null || Config.INS.getMongoUri().equals(""))
            {
                DATABASE_ENABLED = false;
                return;
            }
            if (Config.INS.getMongoName() == null || Config.INS.getMongoName().equals(""))
            {
                DATABASE_ENABLED = false;
                return;
            }
            databaseManager = new DatabaseManager(Config.INS.getMongoUri());
            databaseManager.setDatabaseName(Config.INS.getMongoName());
        }

        /* Init JDA */
        initBotShards(listenerBot);
        SetActivity.SetActivity(jda);

        VoiceChannelCleanupAgent voiceChannelCleanupAgent = new VoiceChannelCleanupAgent();
        voiceChannelCleanupAgent.setDaemon(true);
        voiceChannelCleanupAgent.start();

        ShardWatchDogAgent shardWatchDogAgent = new ShardWatchDogAgent();
        shardWatchDogAgent.setDaemon(true);
        shardWatchDogAgent.start();

        logger.info("Active Threads:\t" + java.lang.Thread.activeCount());
    }

    private static void initBotShards(EventListener listener) {
        for(int i = Config.INS.getShardStart(); i < Config.INS.getNumShards(); i++)
        {
            try
            {
                shards.add(i, new BotController(i, listener));
            } catch (Exception e)
            {
                logger.error("Caught an exception while starting shard " + i + "!", e);
                numShardsReady.getAndIncrement();
            }
            try
            {
                Thread.sleep(SHARD_CREATION_SLEEP_INTERVAL);
            } catch (InterruptedException e)
            {
                throw new RuntimeException("Got interrupted while setting up bot shards!", e);
            }
        }
        logger.info(shards.size() + " shards have been constructed");
        logger.info("Lavalite ready in {}ms", System.currentTimeMillis() - START_TIME);
    }

    public void onInit(ReadyEvent readyEvent)
    {
        if (!hasReadiedOnce)
        {
            numShardsReady.incrementAndGet();
            hasReadiedOnce = false;
        }

        logger.info("Received ready event for " + Launcher.getInstance(readyEvent.getJDA()).getShardInfo().getShardString());

        int ready = numShardsReady.get();
        if (ready == Config.INS.getNumShards())
        {
            logger.info("All " + ready + " shards are ready.");
        }
    }

    public static void shutdown(int code)
    {
        logger.info("Shutting down with exit code " + code);
        CommandRegistry.registry.clear();
        CommandRegistry.logger.info("Clearing all command registry");
        GuildRegistry.guildRegistry.clear();
        shutdownCode = code;
        for(Launcher lch : shards) {
            lch.getJda().shutdown();
        }
        getJda().shutdown();
        System.exit(code);
    }

    public static JDA getJda()
    {
        return jda;
    }

    public static Launcher getInstance(JDA jda)
    {
        int sId = jda.getShardInfo() == null ? 0 : jda.getShardInfo().getShardId();

        for(Launcher lch : shards)
        {
            if(((BotController) lch).getShardId() == sId) {
                return lch;
            }
        }

        throw new IllegalStateException("Attempted to get instance for JDA shard that is not indexed");
    }

    public static Launcher getInstance(int id)
    {
        return shards.get(id);
    }

    public JDA.ShardInfo getShardInfo()
    {
        int sId = jda.getShardInfo() == null ? 0 : jda.getShardInfo().getShardId();
        return new JDA.ShardInfo(sId, Config.INS.getNumShards());
    }

    public void revive()
    {
        jda.shutdown();
        logger.info("Reviving a shard");
        shards.set(getShardInfo().getShardId(), new BotController(getShardInfo().getShardId(), listenerBot));
    }

    public static List<Launcher> getShards()
    {
        return shards;
    }

    public static List<Guild> getAllGuilds()
    {
        ArrayList<Guild> list = new ArrayList<>();

        for (Launcher lch : shards) {
            list.addAll(lch.getJda().getGuilds());
        }

        return list;
    }

    public static Map<String, User> getAllUsersAsMap()
    {
        HashMap<String, User> map = new HashMap<>();

        for (Launcher lch : shards) {
            for (User usr : lch.getJda().getUsers())
            {
                map.put(usr.getId(), usr);
            }
        }

        return map;
    }

    public static long getAllShards()
    {
        long size = getJda().getShardInfo().getShardTotal();
        return size;
    }

    public ShardListener getShardListener()
    {
        return shardListener;
    }

    public static DatabaseManager getDatabaseManager()
    {
        return databaseManager;
    }

    public static CommandManager getCommandManager()
    {
        return commandManager;
    }
}
