package jiraiyah.librarian.inits.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegisters
{
    public static void register()
    {
        if (ItemInits.ITEM_LIST.size() == 0)
            return;
        for(Item item : ItemInits.ITEM_LIST)
            GameRegistry.register(item);
    }
}
