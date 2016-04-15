package jiraiyah.librarian.tileEntities;

import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.events.ChunkLoaderManager;
import jiraiyah.librarian.inits.blocks.BlockInits;
import jiraiyah.librarian.interfaces.IChunkLoader;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public abstract class ChunkLoaderBaseTile extends TileEntity implements IChunkLoader, ITickable
{
    public String owner;
    protected boolean loaded = false;
    protected boolean powered = false;
    public boolean active = false;

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean("powered", powered);
        if (owner != null)
            tag.setString("owner", owner);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        if (tag.hasKey("owner"))
            owner = tag.getString("owner");
        if (tag.hasKey("powered"))
            powered = tag.getBoolean("powered");
        loaded = true;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (!worldObj.isRemote)
            deactivate();
    }

    @Override
    public void validate()
    {
        super.validate();
        if (!worldObj.isRemote && loaded && !powered)
            activate();
    }

    @Override
    public String getOwner()
    {
        return owner;
    }

    @Override
    public Object getMod()
    {
        return Librarian.INSTANCE;
    }

    @Override
    public World getWorld()
    {
        return worldObj;
    }

    @Override
    public BlockPos getPosition()
    {
        return getPos();
    }

    @Override
    public void deactivate()
    {
        loaded = true;
        active = false;
        ChunkLoaderManager.remChunkLoader(this);
        worldObj.notifyBlockUpdate(getPos(), worldObj.getBlockState(getPos()), worldObj.getBlockState(getPos()), 3);
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void activate()
    {
        loaded = true;
        active = true;
        ChunkLoaderManager.addChunkLoader(this);
        worldObj.notifyBlockUpdate(getPos(), worldObj.getBlockState(getPos()), worldObj.getBlockState(getPos()), 3);
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void update()
    {
        if (!worldObj.isRemote)
        {
            boolean nowPowered = isPowered();
            if (powered != nowPowered)
            {
                powered = nowPowered;
                if (powered)
                    deactivate();
                else
                    activate();
            }
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    //y+ = up
    //y- = down
    //z+ = south
    //z- = north
    //x+ = east
    //x- = west
    public boolean isPowered() {
        return isPoweringTo(worldObj, getPos().up(), EnumFacing.DOWN) ||
                isPoweringTo(worldObj, getPos().down(), EnumFacing.UP) ||
                isPoweringTo(worldObj, getPos().south(), EnumFacing.NORTH) ||
                isPoweringTo(worldObj, getPos().north(), EnumFacing.SOUTH) ||
                isPoweringTo(worldObj, getPos().east(), EnumFacing.WEST) ||
                isPoweringTo(worldObj, getPos().west(), EnumFacing.EAST);
    }

    public static boolean isPoweringTo(World world, BlockPos pos, EnumFacing facing)
    {//int side) {
        return world.getBlockState(pos).getBlock().getWeakPower(world.getBlockState(pos), world, pos, facing) > 0;
    }

    public void destroyBlock()
    {
        BlockInits.CHUNK_LOADER.dropBlockAsItem(worldObj, getPos(), worldObj.getBlockState(getPos()), 0);
        worldObj.setBlockToAir(getPos());
    }

    public ChunkCoordIntPair getChunkPosition()
    {
        return new ChunkCoordIntPair(getPos().getX() >> 4, getPos().getZ() >> 4);
    }

    public void onBlockPlacedBy(EntityLivingBase entityliving)
    {
        if (entityliving instanceof EntityPlayer)
            owner = entityliving.getName();
        if (owner.equals(""))
            owner = null;
        activate();
    }

}
