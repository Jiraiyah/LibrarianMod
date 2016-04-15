package jiraiyah.librarian.infrastructure;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.events.ChunkLoaderManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class PlayerOrganiser extends ChunkLoaderOrganiser
{
    public static boolean dirty;
    public final String username;

    public PlayerOrganiser(String username)
    {
        this.username = username;
    }

    @Override
    public boolean canForceNewChunks(int required, int dim)
    {
        return required + numLoadedChunks() < ChunkLoaderManager.getPlayerChunkLimit(username) && required < ForgeChunkManager.ticketCountAvailableFor(username) * ForgeChunkManager.getMaxChunkDepthFor("ChickenChunks");
    }

    @Override
    public Ticket createTicket(int dimension)
    {
        return ForgeChunkManager.requestPlayerTicket(Librarian.INSTANCE, username, DimensionManager.getWorld(dimension), ForgeChunkManager.Type.NORMAL);
    }

    @Override
    public void setDirty()
    {
        dirty = true;
    }
}
