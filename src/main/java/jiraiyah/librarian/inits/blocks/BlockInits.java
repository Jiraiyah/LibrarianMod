package jiraiyah.librarian.inits.blocks;

import jiraiyah.librarian.blocks.ChunkLoader;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class BlockInits
{
    public static List<Block> BLOCK_LIST = new ArrayList<>();

    public static Block CHUNK_LOADER;

    public static void initialize()
    {
        CHUNK_LOADER = new ChunkLoader();

        BLOCK_LIST.add(CHUNK_LOADER);
    }
}
