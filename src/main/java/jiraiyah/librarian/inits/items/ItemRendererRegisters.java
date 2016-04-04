package jiraiyah.librarian.inits.items;

import jiraiyah.librarian.references.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class ItemRendererRegisters
{
    public static void register()
    {

    }

    private static void registerBlock(Item item)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
                new ModelResourceLocation(Reference.MOD_ID + ":" + item.getRegistryName(), "inventory"));
    }
}
