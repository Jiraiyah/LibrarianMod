package jiraiyah.librarian.inits.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class BlockRendererRegisters
{
    public static void register()
    {
        registerBlock(BlockInits.CHUNK_LOADER);
    }

    private static void registerBlock(Block block)
    {
        Item item = Item.getItemFromBlock(block);
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation( block.getRegistryName(), "inventory"));

    }
}
