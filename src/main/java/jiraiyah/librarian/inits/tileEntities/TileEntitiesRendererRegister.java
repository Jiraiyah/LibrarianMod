package jiraiyah.librarian.inits.tileEntities;

import jiraiyah.librarian.TESR.VillageIndicatorTESR;
import jiraiyah.librarian.tileEntities.VillageIndicatorTile;
import jiraiyah.librarian.utilities.Log;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class TileEntitiesRendererRegister
{
    public static void register()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(VillageIndicatorTile.class, new VillageIndicatorTESR());
        Log.info("=========================================================> Registered TESR");
    }
}
