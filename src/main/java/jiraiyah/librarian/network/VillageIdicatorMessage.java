package jiraiyah.librarian.network;

/*
public class VillageIdicatorMessage implements IMessageHandler<VillageIdicatorMessage.Packet, IMessage>
{
    @Override
    public IMessage onMessage ( VillageIdicatorMessage.Packet message, MessageContext ctx )
    {
        */
/*Minecraft.getMinecraft().addScheduledTask( () ->
                ((ChunkLoaderTile)Minecraft
                        .getMinecraft()
                        .theWorld
                        .getTileEntity(message.entityPos))
                        .UpdateDataFromServer(message.data));*//*

        VillageDataHandler.setVillageData(message.data);
        return null;
    }

    */
/*public static void sendMessage(MinecraftServer minecraftServer, List<VillageData> data, BlockPos pos)
    {
        Packet packet = new Packet(data, pos);
        for (EntityPlayerMP player : minecraftServer.getPlayerList().getPlayerList())
        {
            BlockPos playerPosition = player.getPosition();
            if (pos.distanceSq(playerPosition) < 100 * 100)
                    NetworkMessages.network.sendTo(packet, player);
        }
    }*//*


    public static void sendMessage(UUID player, List<VillageData> data)
    {
        Packet packet = new Packet(data);
        EntityPlayerMP playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(player);
        NetworkMessages.network.sendTo(packet, playerMP);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Packet implements IMessage
    {
        public List<VillageData> data = new ArrayList<>();

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
            data = new ArrayList<>();
            for (int i = 0; i < dataSize; i++)
            {
                int villagerCount = buf.readInt();
                int reputation = buf.readInt();
                int radius = buf.readInt();
                BlockPos center = BlockPos.fromLong(buf.readLong());
                int doorListSize = buf.readInt();
                List<BlockPos> doorPositions = new ArrayList<>();
                if (doorListSize != 0)
                    for (int j = 0; j < doorListSize; j++)
                        doorPositions.add(BlockPos.fromLong(buf.readLong()));
                VillageData vData = new VillageData(radius, center,doorPositions, villagerCount, reputation);
                data.add(vData);
            }
        }

        @Override
        public void toBytes(ByteBuf buf)
        {
            buf.writeInt( data.size() );
            for (VillageData vData : data)
            {
                buf.writeInt(vData.villagerCount);
                buf.writeInt(vData.reputation);
                buf.writeInt( vData.radius );
                buf.writeLong(vData.center.toLong());
                buf.writeInt( vData.doorPositions.size() );
                for (BlockPos position : vData.doorPositions)
                    buf.writeLong( position.toLong() );
            }
        }
    }
}
*/
