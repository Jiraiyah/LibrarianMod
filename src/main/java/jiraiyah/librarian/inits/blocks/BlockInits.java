package jiraiyah.librarian.inits.blocks;

import jiraiyah.librarian.blocks.ChunkLoader;
import net.minecraft.block.Block;

@SuppressWarnings("WeakerAccess")
public class BlockInits
{
    public static Block CHUNK_LOADER;

    public static void initialize()
    {
        CHUNK_LOADER = new ChunkLoader();
    }
}
