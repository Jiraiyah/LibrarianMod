package jiraiyah.librarian.network;

import io.netty.buffer.ByteBuf;
import jiraiyah.librarian.events.VillageDataCollector;
import jiraiyah.librarian.inits.NetworkMessages;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

//clinet -> server : adds the player to the server list
public class VillageInfoPlayerMessage implements IMessageHandler<VillageInfoPlayerMessage.Packet, IMessage>
{
    @Override
    public IMessage onMessage (VillageInfoPlayerMessage.Packet message, MessageContext ctx )
    {
        if (message.adding)
            VillageDataCollector.addPlayerToList(message.playerId);
        else
            VillageDataCollector.removePlayerFromList(message.playerId);
        return null;
    }

    public static void sendMessage(UUID playerId, boolean adding)
    {
        Packet packet = new Packet(playerId, adding);
        NetworkMessages.network.sendToServer(packet);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Packet implements IMessage
    {
        public UUID playerId;
        public boolean adding;

        public Packet()
        {

        }

        public Packet(UUID playerId, boolean adding)
        {
            this.playerId = playerId;
            this.adding = adding;
        }

        @Override
        public void fromBytes(ByteBuf buf)
        {
            adding = buf.readBoolean();
            int size = buf.readInt();
            byte[] bytes = buf.readBytes(size).array();
            String id = new String(bytes);
            playerId = UUID.fromString(id);
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            buf.writeBoolean(adding);
            String id = playerId.toString();
            byte[] bytes = id.getBytes();
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        }
    }
}
