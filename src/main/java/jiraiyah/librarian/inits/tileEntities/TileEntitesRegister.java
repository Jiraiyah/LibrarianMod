package jiraiyah.librarian.inits.tileEntities;

import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.tileEntities.ChunkLoaderTile;
import jiraiyah.librarian.utilities.Log;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntitesRegister
{
    public static void register()
    {
        //GameRegistry.registerTileEntity(ChunkLoaderTile.class, Names.VILLAGE_INDICATOR_TILE_NAME);
        GameRegistry.registerTileEntity(ChunkLoaderTile.class, Names.CHUNK_LOADER_TILE_NAME);
        Log.info("=========================================================> Registered Tile Entities");
    }
}
