package jiraiyah.librarian.network;

import io.netty.buffer.ByteBuf;
import jiraiyah.librarian.infrastructure.VillageData;
import jiraiyah.librarian.inits.NetworkMessages;
import jiraiyah.librarian.tileEntities.VillageIndicatorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class VillageIdicatorMessage implements IMessageHandler<VillageIdicatorMessage.Packet, IMessage>
{
    @Override
    public IMessage onMessage ( VillageIdicatorMessage.Packet message, MessageContext ctx )
    {
        Minecraft.getMinecraft().addScheduledTask( () ->
                ((VillageIndicatorTile)Minecraft.getMinecraft().theWorld.getTileEntity(message.entityPos)).UpdateDataFromServer(message.data));
        return null;
    }

    @SideOnly( Side.CLIENT)
    public static void sendMessage(BlockPos pos, List<VillageData> data)
    {
        Packet packet = new Packet(data);
        //TODO : See how to find the clients connected to this server to send the message to, alternatively, see if using nbt on te would eliminate this need
        NetworkMessages.network.sendToAllAround(packet, new NetworkRegistry.TargetPoint(0, pos.getX(), pos.getY(),pos.getZ(), 100));
    }

    @SuppressWarnings("WeakerAccess")
    public static class Packet implements IMessage
    {
        public List<VillageData> data = new ArrayList<>();
        public BlockPos entityPos;

        public Packet()
        {

        }

        public Packet(List<VillageData> data)
        {
            this.data = data;
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            int dataSize = buf.readInt();
            entityPos = BlockPos.fromLong(buf.readLong());
            data = new ArrayList<>();
            for (int i = 0; i < dataSize; i++)
            {
                int radius = buf.readInt();
                BlockPos center = BlockPos.fromLong(buf.readInt());
                int doorListSize = buf.readInt();
                List<BlockPos> doorPositions = new ArrayList<>();
                for (int j = 0; j < doorListSize; j++)
                    doorPositions.add(BlockPos.fromLong(buf.readLong()));
                VillageData vData = new VillageData(radius, center,doorPositions);
                data.add(vData);
            }
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            buf.writeInt( data.size() );
            buf.writeLong(entityPos.toLong());
            for (VillageData vData : data)
            {
                buf.writeInt( vData.radius );
                buf.writeLong(vData.center.toLong());
                buf.writeInt( vData.doorPositions.size() );
                for (BlockPos position : vData.doorPositions)
                    buf.writeLong( position.toLong() );
            }
        }
    }
}
