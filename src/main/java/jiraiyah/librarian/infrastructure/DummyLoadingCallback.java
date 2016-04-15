package jiraiyah.librarian.infrastructure;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.LinkedList;
import java.util.List;

public class DummyLoadingCallback implements ForgeChunkManager.OrderedLoadingCallback, ForgeChunkManager.PlayerOrderedLoadingCallback
{
    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world) {
    }

    @Override
    public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
        return new LinkedList<Ticket>();
    }

    @Override
    public ListMultimap<String, Ticket> playerTicketsLoaded(ListMultimap<String, Ticket> tickets, World world) {
        return LinkedListMultimap.create();
    }
}
