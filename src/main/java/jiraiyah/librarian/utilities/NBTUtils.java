package jiraiyah.librarian.utilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils
{
    public static NBTTagCompound getOrInitTagCompound(NBTTagCompound parent, String key)
    {
        return getOrInitTagCompound(parent, key, null);
    }

    public static NBTTagCompound getOrInitTagCompound(NBTTagCompound parent, String key, NBTTagCompound defaultTag)
    {
        if (parent.hasKey(key, 10)) {
            return parent.getCompoundTag(key);
        }
        if (defaultTag == null) {
            defaultTag = new NBTTagCompound();
        } else {
            defaultTag = (NBTTagCompound)defaultTag.copy();
        }
        parent.setTag(key, defaultTag);
        return defaultTag;
    }

    public static NBTTagCompound getOrInitTagCompound(ItemStack stack)
    {
        NBTTagCompound tags = stack.getTagCompound();
        if (tags != null) {
            return tags;
        }
        tags = new NBTTagCompound();
        stack.setTagCompound(tags);
        return tags;
    }
}
