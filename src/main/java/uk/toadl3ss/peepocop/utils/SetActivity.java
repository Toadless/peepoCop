package uk.toadl3ss.peepocop.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import uk.toadl3ss.peepocop.data.Constants;
import uk.toadl3ss.peepocop.main.Launcher;

public class SetActivity
{
    public static Boolean defaultStatus = false;
    public static void SetActivity(JDA jda)
    {
        if (Constants.status.equals(""))
        {
            defaultStatus = true;
        }
        if (Constants.game.equals(""))
        {
            defaultStatus = true;
        }
        if (Constants.status.equals("playing"))
        {
            if (defaultStatus)
            {
                return;
            }
            jda.getPresence().setActivity(Activity.playing(Constants.game));
            return;
        }
        if (Constants.status.equals("watching"))
        {
            if (defaultStatus)
            {
                return;
            }
            jda.getPresence().setActivity(Activity.watching(Constants.game));
            return;
        }
        if (Constants.status.equals("listening"))
        {
            if (defaultStatus)
            {
                return;
            }
            jda.getPresence().setActivity(Activity.listening(Constants.game));
            return;
        }
        if (Constants.status.equals("competing"))
        {
            if (defaultStatus)
            {
                return;
            }
            jda.getPresence().setActivity(Activity.competing(Constants.game));
            return;
        }
        if (defaultStatus)
        {
            jda.getPresence().setActivity(Activity.playing("Lavalite v" + Launcher.version));
        }
    }
}