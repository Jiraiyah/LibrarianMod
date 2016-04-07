package jiraiyah.librarian.inits.blocks;

import jiraiyah.librarian.utilities.Log;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegisters
{
    public static void register()
    {
        if (BlockInits.BLOCK_LIST.size() > 0)
            for(Block block : BlockInits.BLOCK_LIST)
            {
                if (block != null)
                {
                    GameRegistry.register(block);
                    GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
                }
            }
        Log.info("=========================================================> Registered Blocks");
    }
}
