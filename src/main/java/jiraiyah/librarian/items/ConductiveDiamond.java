package jiraiyah.librarian.items;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.references.Names;
import jiraiyah.librarian.references.Reference;
import net.minecraft.item.Item;


public class ConductiveDiamond extends Item
{
	public ConductiveDiamond()
	{
		setUnlocalizedName(Reference.MOD_ID.toLowerCase() + "." + Names.CONDUCTIVE_DIAMOND_NAME);
		setRegistryName(Names.CONDUCTIVE_DIAMOND_NAME);;
		setMaxStackSize(16);
		setCreativeTab(Librarian.CREATIVE_TAB);
	}
}
