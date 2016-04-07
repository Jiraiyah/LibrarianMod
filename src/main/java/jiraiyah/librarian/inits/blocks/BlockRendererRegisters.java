package jiraiyah.librarian.inits.blocks;

import jiraiyah.librarian.utilities.Log;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockRendererRegisters
{
    public static void register()
    {
        if (BlockInits.BLOCK_LIST.size() > 0)
            for(Block block : BlockInits.BLOCK_LIST)
                if (block != null)
                    registerBlock(block);
        Log.info("=========================================================> Registered Block Renderers");
    }

    private static void registerBlock(Block block)
    {
        Item item = Item.getItemFromBlock(block);
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation( block.getRegistryName(), "inventory"));

    }
}
