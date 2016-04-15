package jiraiyah.librarian.network;

import jiraiyah.librarian.infrastructure.PacketCustom;
import jiraiyah.librarian.tileEntities.ChunkLoaderTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChunkLoaderSPH implements PacketCustom.IServerPacketHandler
{
    public static String channel = "ChickenChunks";

    @Override
    public void handlePacket(PacketCustom packet, EntityPlayerMP sender, INetHandlerPlayServer handler) {
        switch(packet.getType())
        {
            case 1:
                PlayerChunkViewerManager.instance().closeViewer(sender.getName());
                break;
            case 2:
                handleChunkLoaderChangePacket(sender.worldObj, packet);
                break;

        }
    }

    private void handleChunkLoaderChangePacket(World world, PacketCustom packet)
    {
        TileEntity tile = world.getTileEntity(new BlockPos(packet.readInt(), packet.readInt(), packet.readInt()));
        if(tile instanceof ChunkLoaderTile)
        {
            ChunkLoaderTile ctile = (ChunkLoaderTile)tile;
            ctile.setShapeAndRadius(ChunkLoaderShape.values()[packet.readUByte()], packet.readUByte());
        }
    }
}
