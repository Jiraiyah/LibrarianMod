package jiraiyah.librarian.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//key bind packet client -> server: if enabling, add player to map and send first data packet.
// if disabling, remove player from map. Also in PlayerLogoutEvent remove the player from the map.
//probably best to store them by their UUID, then in the tick event just iterate over the map
// and for each player collect the data in a radius around them, and send the data to them if it has changed
public class VillageDataCollector
{
    public static List<UUID> PLAYERS = new ArrayList<>();

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event)
    {

    }

    public void playerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (PLAYERS.contains(event.player.getUniqueID()))
            PLAYERS.remove(event.player.getUniqueID());
    }
}
