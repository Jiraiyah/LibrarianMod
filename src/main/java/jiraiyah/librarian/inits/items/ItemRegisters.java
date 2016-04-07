package jiraiyah.librarian.inits.items;

import jiraiyah.librarian.utilities.Log;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegisters
{
    public static void register()
    {
        if (ItemInits.ITEM_LIST.size() > 0)
            for(Item item : ItemInits.ITEM_LIST)
                if (item != null)
                    GameRegistry.register(item);
        Log.info("=========================================================> Registered Items");
    }
}
