package jiraiyah.librarian.items;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.references.Reference;
import net.minecraft.item.Item;


public class ConductiveEmerald extends Item
{
	public ConductiveEmerald()
	{
		setUnlocalizedName(Reference.MOD_ID.toLowerCase() + "." + Names.CONDUCTIVE_EMERALD_NAME);
		setRegistryName(Names.CONDUCTIVE_EMERALD_NAME);;
		setMaxStackSize(16);
		setCreativeTab(Librarian.CREATIVE_TAB);
	}
}
