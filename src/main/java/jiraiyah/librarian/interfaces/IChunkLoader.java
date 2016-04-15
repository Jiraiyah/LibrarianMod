package jiraiyah.librarian.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.util.Collection;

public interface IChunkLoader
{
    String getOwner();
    Object getMod();
    World getWorld();
    BlockPos getPosition();
    void deactivate();
    void activate();
    Collection<ChunkCoordIntPair> getChunks();
}
