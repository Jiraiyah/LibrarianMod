package jiraiyah.librarian.inits.items;

import jiraiyah.librarian.items.LivingCage;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ItemInits
{
    public static List<Item> ITEM_LIST = new ArrayList<>();

    public static Item LIVING_CAGE;

    public static void initialize()
    {
        LIVING_CAGE = new LivingCage();

        ITEM_LIST.add(LIVING_CAGE);
    }
}
