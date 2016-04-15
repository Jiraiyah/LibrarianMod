package jiraiyah.librarian.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public interface IDataInput
{
    long readLong();
    int readInt();
    short readShort();
    int readUShort();
    byte readByte();
    short readUByte();
    double readDouble();
    float readFloat();
    boolean readBoolean();
    char readChar();
    int readVarShort();
    int readVarInt();
    byte[] readByteArray(int length);
    String readString();
    BlockPos readCoord();
    NBTTagCompound readNBTTagCompound();
    ItemStack readItemStack();
}
