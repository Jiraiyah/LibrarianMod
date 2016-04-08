package jiraiyah.librarian.inits.events;

import jiraiyah.librarian.events.VillageDataHandler;
import jiraiyah.librarian.utilities.Log;
import net.minecraftforge.common.MinecraftForge;

public class ClientEventRegister
{
    public static void register()
    {
        MinecraftForge.EVENT_BUS.register(new VillageDataHandler());
        Log.info("=========================================================> Registered Client Events");
    }
}
