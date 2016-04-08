package jiraiyah.librarian.inits.events;

import jiraiyah.librarian.events.VillageDataCollector;
import jiraiyah.librarian.utilities.Log;
import net.minecraftforge.common.MinecraftForge;

public class CommonEventRegister
{
    //private static LibrarianEventHandler librarianEventHander = new LibrarianEventHandler();

    public static void register()
    {
        MinecraftForge.EVENT_BUS.register(new VillageDataCollector());
        Log.info("=========================================================> Registered Common Events");
    }
}
