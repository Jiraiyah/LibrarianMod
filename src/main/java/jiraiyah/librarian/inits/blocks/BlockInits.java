package jiraiyah.librarian.inits.blocks;

import jiraiyah.librarian.blocks.ChunkLoader;
import jiraiyah.librarian.blocks.VillageIndicator;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class BlockInits
{
    public static List<Block> BLOCK_LIST = new ArrayList<>();

    public static Block CHUNK_LOADER;
    public static Block VILLAGE_INDICATOR;

    public static void initialize()
    {
        CHUNK_LOADER = new ChunkLoader();
        VILLAGE_INDICATOR = new VillageIndicator();

        BLOCK_LIST.add(CHUNK_LOADER);
        BLOCK_LIST.add(VILLAGE_INDICATOR);
        Log.info("=========================================================> Initialized Blocks");
    }
}
