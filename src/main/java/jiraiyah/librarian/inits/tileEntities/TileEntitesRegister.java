package jiraiyah.librarian.inits.tileEntities;

import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.tileEntities.VillageIndicatorTile;
import jiraiyah.librarian.utilities.Log;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntitesRegister
{
    public static void register()
    {
        GameRegistry.registerTileEntity(VillageIndicatorTile.class, Names.VILLAGE_INDICATOR_TILE_NAME);
        Log.info("=========================================================> Registered Tile Entities");
    }
}
