package jiraiyah.librarian.inits.events;

import jiraiyah.librarian.utilities.Log;

public class CommonEventRegister
{
    //private static LibrarianEventHandler librarianEventHander = new LibrarianEventHandler();

    public static void register()
    {
        //MinecraftForge.EVENT_BUS.register(librarianEventHander);
        Log.info("=========================================================> Registered Common Events");
    }
}
