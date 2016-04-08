package jiraiyah.librarian.inits;

import jiraiyah.librarian.network.VillageIdicatorMessage;
import jiraiyah.librarian.network.VillageInfoPlayerMessage;
import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.utilities.Log;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

//https://github.com/maruohon/enderutilities/blob/master/src/main/java/fi/dy/masa/enderutilities/network/message/MessageKeyPressed.java
@SuppressWarnings("WeakerAccess")
public class NetworkMessages
{
    public static SimpleNetworkWrapper network;

    public static void register()
    {
        network = new SimpleNetworkWrapper( Reference.MOD_ID );
		network.registerMessage( VillageIdicatorMessage.class, VillageIdicatorMessage.Packet.class, nextId(), Side.CLIENT );
        network.registerMessage( VillageInfoPlayerMessage.class, VillageInfoPlayerMessage.Packet.class, nextId(), Side.SERVER );
        Log.info("=========================================================> Registered Network Messages");
    }

    private static int ID = 0;

    public static int nextId ()
    {
        return ID++;
    }
}
