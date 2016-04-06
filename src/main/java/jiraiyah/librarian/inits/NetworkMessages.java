package jiraiyah.librarian.inits;

import jiraiyah.librarian.references.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@SuppressWarnings("WeakerAccess")
public class NetworkMessages
{
    public static SimpleNetworkWrapper network;

    public static void register()
    {
        network = new SimpleNetworkWrapper( Reference.MOD_ID );
		//network.registerMessage( FactoryTabChanged.class, FactoryTabChanged.Packet.class, nextId(), Side.SERVER );
    }

    private static int ID = 0;

    public static int nextId ()
    {
        return ID++;
    }
}
