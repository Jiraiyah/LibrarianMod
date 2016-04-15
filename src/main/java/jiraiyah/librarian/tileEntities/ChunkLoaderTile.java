package jiraiyah.librarian.tileEntities;

import jiraiyah.librarian.events.ChunkLoaderManager;
import jiraiyah.librarian.infrastructure.PacketCustom;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class ChunkLoaderTile extends ChunkLoaderBaseTile
{
    public int radius;
    public ChunkLoaderShape shape = ChunkLoaderShape.Square;

    public static void handleDescriptionPacket(PacketCustom packet, World world)
    {
        TileEntity tile = world.getTileEntity(new BlockPos(packet.readInt(), packet.readInt(), packet.readInt()));
        if(tile instanceof ChunkLoaderTile)
        {
            ChunkLoaderTile ctile = (ChunkLoaderTile)tile;
            ctile.setShapeAndRadius(ChunkLoaderShape.values()[packet.readUByte()], packet.readUByte());
            ctile.active = packet.readBoolean();
            if(packet.readBoolean())
                ctile.owner = packet.readString();
        }
    }

    public boolean setShapeAndRadius(ChunkLoaderShape newShape, int newRadius)
    {
        if(worldObj.isRemote)
        {
            radius = newRadius;
            shape = newShape;
            return true;
        }
        Collection<ChunkCoordIntPair> chunks = getContainedChunks(newShape, getPos().getX(), getPos().getZ(), newRadius);
        if(chunks.size() > ChunkLoaderManager.maxChunksPerLoader())
        {
            return false;
        }
        else if(powered)
        {
            radius = newRadius;
            shape = newShape;
            worldObj.notifyBlockUpdate(getPos(), worldObj.getBlockState(getPos()), worldObj.getBlockState(getPos()), 3);
            //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return true;
        }
        else if(ChunkLoaderManager.canLoaderAdd(this, chunks))
        {
            radius = newRadius;
            shape = newShape;
            ChunkLoaderManager.updateLoader(this);
            worldObj.notifyBlockUpdate(getPos(), worldObj.getBlockState(getPos()), worldObj.getBlockState(getPos()), 3);
            //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return true;
        }
        return false;
    }

    public Packet getDescriptionPacket()
    {
        PacketCustom packet = new PacketCustom(ChunkLoaderSPH.channel, 10);
        packet.writeCoord(getPos());
        packet.writeByte(shape.ordinal());
        packet.writeByte(radius);
        packet.writeBoolean(active);
        packet.writeBoolean(owner != null);
        if(owner != null)
            packet.writeString(owner);
        return packet.toPacket();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setByte("radius", (byte) radius);
        tag.setByte("shape", (byte) shape.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        radius = tag.getByte("radius");
        shape = ChunkLoaderShape.values()[tag.getByte("shape")];
    }

    @Override
    public HashSet<ChunkCoordIntPair> getChunks()
    {
        return getContainedChunks(shape, getPos().getX(), getPos().getZ(), radius);
    }

    public static HashSet<ChunkCoordIntPair> getContainedChunks(ChunkLoaderShape shape, int xCoord, int zCoord, int radius)
    {
        return shape.getLoadedChunks(xCoord >> 4, zCoord >> 4, radius - 1);
    }

    public int countLoadedChunks()
    {
        return getChunks().size();
    }

    @Override
    public void activate()
    {
        if(radius == 0)
        {
            //create a small one and try and increment it to 2
            radius = 1;
            shape = ChunkLoaderShape.Square;
            setShapeAndRadius(ChunkLoaderShape.Square, 2);
        }

        super.activate();
    }
}
