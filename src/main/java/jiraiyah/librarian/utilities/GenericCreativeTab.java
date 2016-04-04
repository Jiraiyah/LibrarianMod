package jiraiyah.librarian.utilities;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GenericCreativeTab extends CreativeTabs
{
    private Block block = null;
    private Item item = null;
    private String label;

    public GenericCreativeTab(String label)
    {
        super(label);
        this.label = label;
    }

    public void setIcon(Block block)
    {
        this.block = block;
    }

    public void setIcon (Item item)
    {
        this.item = item;
    }

    @Override
    @SideOnly( Side.CLIENT )
    public ItemStack getIconItemStack ()
    {
        if (item != null)
        {
            return new ItemStack( item );
        }
        if (block != null)
        {
            return new ItemStack( block );
        }
        return super.getIconItemStack();
    }

    @Override
    public Item getTabIconItem ()
    {
        if (item != null)
        {
            return item;
        }
        if (block != null)
        {
            return Item.getItemFromBlock( block );
        }
        return null;
    }

    @Override
    public String getTranslatedTabLabel ()
    {
        return label;
    }
}
