package jiraiyah.librarian.tileEntities;

import com.mojang.authlib.GameProfile;
import jiraiyah.librarian.events.ChunkLoaderManager;
import jiraiyah.librarian.utilities.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ChunkLoaderTile extends TileEntity
{
    private GameProfile profile;

    public boolean active;

    public ChunkLoaderTile()
    {
        super();
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setBoolean("Active", this.active);
        if (this.profile != null)
        {
            compound.setTag("profile", NBTUtils.proifleToNBT(this.profile));
        }
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.active = compound.getBoolean("Active");
        this.profile = NBTUtils.profileFromNBT(compound.getCompoundTag("profile"));
    }

    /*public float getPower()
    {
        return 8.0F;
    }

    public void powerChanged(boolean powered)
    {
        if (this.active != powered)
        {
            this.active = powered;
            markDirty();
            onPowerChanged();
        }
    }

    public void onPowerChanged()
    {
        ChunkLoaderManager.dirty = true;
    }*/

    public GameProfile getProfile()
    {
        return this.profile;
    }

    public java.util.Collection<ChunkCoordIntPair> getChunkCoords()
    {
        ArrayList list = new ArrayList(10);
        int x = getPos().getX() >> 4;
        int z = getPos().getZ() >> 4;
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dz = -1; dz <= 1; dz++)
            {
                list.add(new ChunkCoordIntPair(x + dx, z + dz));
            }
        }
        return list;
    }

    public boolean isLoaded()
    {
        return (!this.isInvalid()) &&
                (this.getWorld() != null) &&
                (this.getPos() != null) &&
                (this.getWorld().isBlockLoaded(getPos())) &&
                (this.getWorld().getTileEntity(getPos()) == this);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (!this.worldObj.isRemote)
            ChunkLoaderManager.unregister(this);
    }

    @Override
    public void validate()
    {
        super.validate();
        if (!this.worldObj.isRemote)
            ChunkLoaderManager.register(this);
    }

    @Override
    public void onChunkUnload()
    {
        super.onChunkUnload();
        if (!this.worldObj.isRemote)
            ChunkLoaderManager.unregister(this);
    }
}
