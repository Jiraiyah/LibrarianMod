package jiraiyah.librarian.infrastructure;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.common.ModContainer;

public class ModOrganiser extends ChunkLoaderOrganiser
{
    public final Object mod;
    public final ModContainer container;
    public boolean dirty;

    public ModOrganiser(Object mod, ModContainer container)
    {
        this.mod = mod;
        this.container = container;
    }

    @Override
    public boolean canForceNewChunks(int required, int dim)
    {
        return required < ForgeChunkManager.ticketCountAvailableFor(mod, DimensionManager.getWorld(dim)) * ForgeChunkManager.getMaxChunkDepthFor(container.getModId());
    }

    @Override
    public void setDirty()
    {
        dirty = false;
    }

    @Override
    protected Ticket createTicket(int dimension)
    {
        return ForgeChunkManager.requestTicket(mod, DimensionManager.getWorld(dimension), ForgeChunkManager.Type.NORMAL);
    }
}
