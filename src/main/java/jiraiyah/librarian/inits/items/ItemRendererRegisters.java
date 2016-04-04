package jiraiyah.librarian.inits.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemRendererRegisters
{
    public static void register()
    {

    }

    private static void registerBlock(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation( item.getRegistryName(), "inventory"));
    }
}
