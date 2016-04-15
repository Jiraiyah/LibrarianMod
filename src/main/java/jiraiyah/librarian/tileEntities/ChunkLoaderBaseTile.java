package jiraiyah.librarian.tileEntities;

import jiraiyah.librarian.interfaces.IChunkLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.Collection;

public class ChunkLoaderBaseTile extends TileEntity implements IChunkLoader
{
    public String owner;
    protected boolean loaded = false;
    protected boolean powered = false;
    public boolean active = false;

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

    public static boolean isPoweringTo(World world, BlockPos pos, EnumFacing facing){//int side) {
        return world.getBlockState(pos).getBlock().shouldCheckWeakPower(world.getBlockState(pos), world, pos, facing);//.isProvidingWeakPower(world, pos, side) > 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("powered", powered);
        if (owner != null)
            tag.setString("owner", owner);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("owner"))
            owner = tag.getString("owner");
        if (tag.hasKey("powered"))
            powered = tag.getBoolean("powered");
        loaded = true;
    }

    public void validate() {
        super.validate();
        if (!worldObj.isRemote && loaded && !powered)
            activate();
    }

    @Override
    public String getOwner()
    {
        return null;
    }

    @Override
    public Object getMod()
    {
        return null;
    }

    @Override
    public World getWorld()
    {
        return worldObj;
    }

    @Override
    public BlockPos getPosition()
    {
        return null;
    }

    @Override
    public void deactivate()
    {

    }

    @Override
    public void activate()
    {

    }

    @Override
    public Collection<ChunkCoordIntPair> getChunks()
    {
        return null;
    }
}
