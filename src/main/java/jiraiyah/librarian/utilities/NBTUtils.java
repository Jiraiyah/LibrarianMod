package jiraiyah.librarian.utilities;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

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

    public static NBTTagCompound proifleToNBT(GameProfile profile)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Name", profile.getName());
        UUID id = profile.getId();
        if (id != null)
        {
            tag.setLong("UUIDL", id.getLeastSignificantBits());
            tag.setLong("UUIDU", id.getMostSignificantBits());
        }
        return tag;
    }

    public static GameProfile profileFromNBT(NBTTagCompound tag)
    {
        String name = tag.getString("Name");
        UUID uuid = null;
        if (tag.hasKey("UUIDL"))
            uuid = new UUID(tag.getLong("UUIDU"), tag.getLong("UUIDL"));
        else if (org.apache.commons.lang3.StringUtils.isBlank(name))
            return null;
        return new GameProfile(uuid, name);
    }
}
