package jiraiyah.librarian.tileEntities;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntity;

public class ChunkLoaderTile extends TileEntity implements ITickable
{
    public ChunkLoaderTile()
    {
        super();
    }

    @Override
    public void tick()
    {

    }

    public String getOwner()
    {
        return "";
    }
}
