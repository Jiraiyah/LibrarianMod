package jiraiyah.librarian.inits.blocks;

import jiraiyah.librarian.references.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class BlockRendererRegisters
{
    public static void register()
    {
        registerBlock(BlockInits.CHUNK_LOADER);
    }

    private static void registerBlock(Block block)
    {
        Item item = Item.getItemFromBlock(block);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
                new ModelResourceLocation(Reference.MOD_ID + ":" + block.getRegistryName(), "inventory"));
    }
}
