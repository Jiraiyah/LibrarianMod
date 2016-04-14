package jiraiyah.librarian.items;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.references.Reference;
import net.minecraft.item.Item;


public class DiamondCore extends Item
{
	public DiamondCore()
	{
		setUnlocalizedName(Reference.MOD_ID.toLowerCase() + "." + Names.DIAMOND_CORE_NAME);
		setRegistryName(Names.DIAMOND_CORE_NAME);;
		setMaxStackSize(16);
		setCreativeTab(Librarian.CREATIVE_TAB);
	}
}
