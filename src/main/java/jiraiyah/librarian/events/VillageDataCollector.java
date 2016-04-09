package jiraiyah.librarian.events;

import jiraiyah.librarian.infrastructure.VillageData;
import jiraiyah.librarian.network.VillageIdicatorMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.stream.Collectors;

//key bind packet client -> server: if enabling, add player to map and send first data packet.
// if disabling, remove player from map. Also in PlayerLogoutEvent remove the player from the map.
//probably best to store them by their UUID, then in the tick event just iterate over the map
// and for each player collect the data in a radius around them, and send the data to them if it has changed
public class VillageDataCollector
{
    public static List<UUID> PLAYERS = new ArrayList<>();
    private static final int RESET_TIMER_TICKS = 120;
    private int resetCoolDown;
    //public List<VillageData> villageDataList = new ArrayList<>();
    public static Map<UUID, List<VillageData>> villageDataList = new HashMap<>();

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event)
    {
        resetCoolDown --;
        if (resetCoolDown > 0)
            return;
        resetCoolDown = RESET_TIMER_TICKS;
        if (PLAYERS == null || PLAYERS.size() == 0)
            return;
        resetVillageDataList();
        //TODO : for each player, collect the data, if they are changed, send it to their client
    }

    public void playerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event)
    {
        removePlayerFromList(event.player.getUniqueID());
    }

    public static void addPlayerToList(UUID player)
    {
        if (!PLAYERS.contains(player))
        {
            PLAYERS.add(player);
            //Log.info("=================> Added a player to the list");
        }
    }

    public static void removePlayerFromList(UUID player)
    {
        if (PLAYERS.contains(player))
        {
            //Log.info("=================> Removed a player from the list");
            PLAYERS.remove(player);
            if (villageDataList.containsKey(player))
                villageDataList.remove(player);
        }
    }

    private void resetVillageDataList()
    {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld(); //Minecraft.getMinecraft().theWorld;
        VillageCollection villageCollection = world.getVillageCollection();
        if (villageCollection == null)
            return;
        List<Village> allVillages = villageCollection.getVillageList();
        if (allVillages == null || allVillages.size() == 0)
            return;
        villageDataList.clear();
        for (UUID player : PLAYERS)
        {
            List<VillageData> tempList = new ArrayList<>();
            EntityPlayer entityPlayer = world.getPlayerEntityByUUID(player);
            float psx = entityPlayer.getPosition().getX();
            float psz = entityPlayer.getPosition().getZ();
            allVillages.stream()
                    .filter(v -> psx < v.getCenter().getX() + v.getVillageRadius() + 200 &&
                                 psz < v.getCenter().getZ() + v.getVillageRadius() + 200 &&
                                 psx > v.getCenter().getX() - v.getVillageRadius() - 200 &&
                                 psz > v.getCenter().getZ() - v.getVillageRadius() - 200)
                    .forEach(v -> {
                        int radius = v.getVillageRadius();
                        int villagerCount = v.getNumVillagers();
                        int reputation = v.getReputationForPlayer(entityPlayer.getName());
                        BlockPos center = v.getCenter();
                        List<VillageDoorInfo> doorInfos = v.getVillageDoorInfoList();
                        List<BlockPos> doorPositions = doorInfos.stream().map(VillageDoorInfo::getDoorBlockPos).collect(Collectors.toList());
                        tempList.add(new VillageData(radius, center, doorPositions, villagerCount, reputation));
                    });
            if (!villageDataList.containsKey(player))
                villageDataList.put(player, tempList);
            else
                villageDataList.replace(player, tempList);
            VillageIdicatorMessage.sendMessage(player, tempList);
            //Log.info("=================> Sent village data to client");
        }

    }
}
