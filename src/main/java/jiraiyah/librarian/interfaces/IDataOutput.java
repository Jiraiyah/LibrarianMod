package jiraiyah.librarian.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface IDataOutput
{
    IDataOutput writeLong(long l);
    IDataOutput writeInt(int i);
    IDataOutput writeShort(int s);
    IDataOutput writeByte(int b);
    IDataOutput writeDouble(double d);
    IDataOutput writeFloat(float f);
    IDataOutput writeBoolean(boolean b);
    IDataOutput writeChar(char c);
    IDataOutput writeVarInt(int i);
    IDataOutput writeVarShort(int s);
    IDataOutput writeByteArray(byte[] array);
    IDataOutput writeString(String s);
    IDataOutput writeCoord(int x, int y, int z);
    IDataOutput writeCoord(BlockPos coord);
    IDataOutput writeNBTTagCompound(NBTTagCompound tag);
    IDataOutput writeItemStack(ItemStack stack);
}
