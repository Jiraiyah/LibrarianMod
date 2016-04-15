package jiraiyah.librarian.infrastructure;

import net.minecraft.world.ChunkCoordIntPair;

public class DimChunkCoord
{
    public final int dimension;
    public final int chunkX;
    public final int chunkZ;

    public DimChunkCoord(int dim, ChunkCoordIntPair coord)
    {
        this(dim, coord.chunkXPos, coord.chunkZPos);
    }

    public DimChunkCoord(int dim, int x, int z)
    {
        dimension = dim;
        chunkX = x;
        chunkZ = z;
    }

    @Override
    public int hashCode()
    {
        return ((chunkX * 31) + chunkZ) * 31 + dimension;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof DimChunkCoord)
        {
            DimChunkCoord o2 = (DimChunkCoord) o;
            return dimension == o2.dimension && chunkX == o2.chunkX && chunkZ == o2.chunkZ;
        }
        return false;
    }

    public ChunkCoordIntPair getChunkCoord()
    {
        return new ChunkCoordIntPair(chunkX, chunkZ);
    }
}