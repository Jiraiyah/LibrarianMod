package jiraiyah.librarian.inits.items;

import jiraiyah.librarian.items.ConductiveDiamond;
import jiraiyah.librarian.items.ConductiveEmerald;
import jiraiyah.librarian.items.DiamondCore;
import jiraiyah.librarian.items.LivingCell;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ItemInits
{
    public static List<Item> ITEM_LIST = new ArrayList<>();

    public static Item LIVING_CAGE;
    public static Item DIAMOND_CORE;
    public static Item CONDUCTIVE_EMERALD;
    public static Item CONDUCTIVE_DIAMOND;

    public static void initialize()
    {
        LIVING_CAGE = new LivingCell();
        DIAMOND_CORE = new DiamondCore();
        CONDUCTIVE_EMERALD = new ConductiveEmerald();
        CONDUCTIVE_DIAMOND = new ConductiveDiamond();

        ITEM_LIST.add(LIVING_CAGE);
        ITEM_LIST.add(DIAMOND_CORE);
        ITEM_LIST.add(CONDUCTIVE_EMERALD);
        ITEM_LIST.add(CONDUCTIVE_DIAMOND);
        Log.info("=========================================================> Initialized Items");
    }
}
