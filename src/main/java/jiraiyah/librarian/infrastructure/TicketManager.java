package jiraiyah.librarian.infrastructure;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.HashMap;
import java.util.Stack;

public abstract class TicketManager
{
    public HashMap<Integer, Stack<Ticket>> ticketsWithSpace = new HashMap<>();
    public HashMap<DimChunkCoord, Ticket> heldChunks = new HashMap<>();

    protected void addChunk(DimChunkCoord coord)
    {
        if (heldChunks.containsKey(coord))
            return;

        Stack<Ticket> freeTickets = ticketsWithSpace.get(coord.dimension);
        if (freeTickets == null)
            ticketsWithSpace.put(coord.dimension, freeTickets = new Stack<>());

        Ticket ticket;
        if (freeTickets.isEmpty())
            freeTickets.push(ticket = createTicket(coord.dimension));
        else
            ticket = freeTickets.peek();

        ForgeChunkManager.forceChunk(ticket, coord.getChunkCoord());
        heldChunks.put(coord, ticket);
        if (ticket.getChunkList().size() == ticket.getChunkListDepth() && !freeTickets.isEmpty())
            freeTickets.pop();
    }

    protected abstract Ticket createTicket(int dimension);

    protected void remChunk(DimChunkCoord coord)
    {
        Ticket ticket = heldChunks.remove(coord);
        if (ticket == null)
            return;

        ForgeChunkManager.unforceChunk(ticket, coord.getChunkCoord());

        if (ticket.getChunkList().size() == ticket.getChunkListDepth() - 1)
        {
            Stack<Ticket> freeTickets = ticketsWithSpace.get(coord.dimension);
            if (freeTickets == null)
                ticketsWithSpace.put(coord.dimension, freeTickets = new Stack<>());
            freeTickets.push(ticket);
        }
    }

    public void unloadDimension(int dimension)
    {
        ticketsWithSpace.remove(dimension);
    }
}
