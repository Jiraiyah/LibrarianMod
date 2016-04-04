package jiraiyah.librarian.inits;

import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.utilities.Log;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@SuppressWarnings("WeakerAccess")
public class NetworkMessages
{
    public static SimpleNetworkWrapper network;

    public static void register()
    {
        network = new SimpleNetworkWrapper( Reference.MOD_ID );
		//network.registerMessage( FactoryTabChanged.class, FactoryTabChanged.Packet.class, nextId(), Side.SERVER );
		Log.info( "******************************************** J PISTON : Registered Network Messages" );
    }

    private static int ID = 0;

    public static int nextId ()
    {
        return ID++;
    }
}
