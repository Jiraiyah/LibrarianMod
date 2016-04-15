package jiraiyah.librarian.infrastructure;

import jiraiyah.librarian.events.ChunkLoaderManager;
import jiraiyah.librarian.interfaces.IChunkLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

public abstract class ChunkLoaderOrganiser extends TicketManager
{
    private HashMap<Integer, HashSet<BlockPos>> dormantLoaders = new HashMap<>();
    private HashMap<DimChunkCoord, LinkedList<IChunkLoader>> forcedChunksByChunk = new HashMap<>();
    private HashMap<IChunkLoader, HashSet<ChunkCoordIntPair>> forcedChunksByLoader = new HashMap<>();
    private HashMap<DimChunkCoord, Integer> timedUnloadQueue = new HashMap<>();

    private boolean reviving;
    private boolean dormant = false;

    public abstract boolean canForceNewChunks(int newChunks, int dim);

    public boolean canForceNewChunks(int dimension, Collection<ChunkCoordIntPair> chunks)
    {
        if (dormant)
            return true;

        int required = 0;
        for (ChunkCoordIntPair coord : chunks) {
            LinkedList<IChunkLoader> loaders = forcedChunksByChunk.get(new DimChunkCoord(dimension, coord));
            if (loaders == null || loaders.isEmpty())
                required++;
        }
        return canForceNewChunks(required, dimension);
    }

    public final int numLoadedChunks()
    {
        return forcedChunksByChunk.size();
    }

    public void addChunkLoader(IChunkLoader loader)
    {
        if (reviving)
            return;

        int dim = CommonUtils.getDimension(loader.getWorld());
        if (dormant) {
            HashSet<BlockPos> coords = dormantLoaders.get(dim);
            if (coords == null)
                dormantLoaders.put(dim, coords = new HashSet<BlockPos>());
            coords.add(loader.getPosition());
        } else {
            forcedChunksByLoader.put(loader, new HashSet<ChunkCoordIntPair>());
            forceChunks(loader, dim, loader.getChunks());
        }
        setDirty();
    }

    public void remChunkLoader(IChunkLoader loader)
    {
        int dim = CommonUtils.getDimension(loader.getWorld());
        if (dormant) {
            HashSet<BlockPos> coords = dormantLoaders.get(dim);
            if(coords != null)
                coords.remove(loader.getPosition());
        } else {
            HashSet<ChunkCoordIntPair> chunks = forcedChunksByLoader.remove(loader);
            if (chunks == null)
                return;
            unforceChunks(loader, dim, chunks, true);
        }
        setDirty();
    }

    private void unforceChunks(IChunkLoader loader, int dim, Collection<ChunkCoordIntPair> chunks, boolean remLoader)
    {
        for (ChunkCoordIntPair coord : chunks)
        {
            DimChunkCoord dimCoord = new DimChunkCoord(dim, coord);
            LinkedList<IChunkLoader> loaders = forcedChunksByChunk.get(dimCoord);
            if (loaders == null || !loaders.remove(loader))
                continue;

            if (loaders.isEmpty()) {
                forcedChunksByChunk.remove(dimCoord);
                timedUnloadQueue.put(dimCoord, 100);
            }
        }

        if (!remLoader)
            forcedChunksByLoader.get(loader).removeAll(chunks);
        setDirty();
    }

    private void forceChunks(IChunkLoader loader, int dim, Collection<ChunkCoordIntPair> chunks)
    {
        for (ChunkCoordIntPair coord : chunks) {
            DimChunkCoord dimCoord = new DimChunkCoord(dim, coord);
            LinkedList<IChunkLoader> loaders = forcedChunksByChunk.get(dimCoord);
            if (loaders == null)
                forcedChunksByChunk.put(dimCoord, loaders = new LinkedList<IChunkLoader>());
            if (loaders.isEmpty()) {
                timedUnloadQueue.remove(dimCoord);
                addChunk(dimCoord);
            }

            if (!loaders.contains(loader))
                loaders.add(loader);
        }

        forcedChunksByLoader.get(loader).addAll(chunks);
        setDirty();
    }

    public abstract void setDirty();

    public void updateChunkLoader(IChunkLoader loader) {
        HashSet<ChunkCoordIntPair> loaderChunks = forcedChunksByLoader.get(loader);
        if (loaderChunks == null) {
            addChunkLoader(loader);
            return;
        }
        HashSet<ChunkCoordIntPair> oldChunks = new HashSet<>(loaderChunks);
        HashSet<ChunkCoordIntPair> newChunks = new HashSet<>();
        for (ChunkCoordIntPair chunk : loader.getChunks())
            if (!oldChunks.remove(chunk))
                newChunks.add(chunk);

        int dim = CommonUtils.getDimension(loader.getWorld());
        if (!oldChunks.isEmpty())
            unforceChunks(loader, dim, oldChunks, false);
        if (!newChunks.isEmpty())
            forceChunks(loader, dim, newChunks);
    }

    public void save(DataOutput dataout) throws IOException
    {
        dataout.writeInt(dormantLoaders.size());
        for (Map.Entry<Integer, HashSet<BlockPos>> entry : dormantLoaders.entrySet())
        {
            dataout.writeInt(entry.getKey());
            HashSet<BlockPos> coords = entry.getValue();
            dataout.writeInt(coords.size());
            for (BlockPos coord : coords) {
                //TODO : use the long version
                dataout.writeInt(coord.getX());
                dataout.writeInt(coord.getY());
                dataout.writeInt(coord.getZ());
            }
        }
        dataout.writeInt(forcedChunksByLoader.size());
        for (IChunkLoader loader : forcedChunksByLoader.keySet())
        {
            BlockPos coord = loader.getPosition();
            dataout.writeInt(CommonUtils.getDimension(loader.getWorld()));
            //TODO : use the long version
            dataout.writeInt(coord.getX());
            dataout.writeInt(coord.getY());
            dataout.writeInt(coord.getZ());
        }
    }

    public void load(DataInputStream datain) throws IOException {
        int dimensions = datain.readInt();
        for (int i = 0; i < dimensions; i++) {
            int dim = datain.readInt();
            HashSet<BlockPos> coords = new HashSet<>();
            dormantLoaders.put(dim, coords);
            int numCoords = datain.readInt();
            for (int j = 0; j < numCoords; j++) {
                //TODO : use the long version
                coords.add(new BlockPos(datain.readInt(), datain.readInt(), datain.readInt()));
            }
        }
        int numLoaders = datain.readInt();
        for (int i = 0; i < numLoaders; i++) {
            int dim = datain.readInt();
            HashSet<BlockPos> coords = dormantLoaders.get(dim);
            if (coords == null)
                dormantLoaders.put(dim, coords = new HashSet<BlockPos>());
            //TODO : use the long version
            coords.add(new BlockPos(datain.readInt(), datain.readInt(), datain.readInt()));
        }
    }

    public void revive()
    {
        if (!dormant)
            return;
        dormant = false;
        for (int dim : dormantLoaders.keySet()) {
            World world = ChunkLoaderManager.getWorld(dim, ChunkLoaderManager.reloadDimensions);
            if (world != null)
                revive(world);
        }
    }

    public void revive(World world)
    {
        HashSet<BlockPos> coords = dormantLoaders.get(CommonUtils.getDimension(world));
        if (coords == null)
            return;

        //addChunkLoader will add to the coord set if we are dormant
        ArrayList<BlockPos> verifyCoords = new ArrayList<>(coords);
        coords.clear();

        for (BlockPos coord : verifyCoords) {
            reviving = true;
            TileEntity tile = world.getTileEntity(coord);
            reviving = false;
            if (tile instanceof IChunkLoader) {
                ChunkLoaderManager.addChunkLoader((IChunkLoader) tile);
            }
        }
    }

    public void devive()
    {
        if (dormant)
            return;

        for (IChunkLoader loader : new ArrayList<IChunkLoader>(forcedChunksByLoader.keySet())) {
            int dim = CommonUtils.getDimension(loader.getWorld());
            HashSet<BlockPos> coords = dormantLoaders.get(dim);
            if (coords == null)
                dormantLoaders.put(dim, coords = new HashSet<BlockPos>());
            coords.add(loader.getPosition());
            remChunkLoader(loader);
        }

        dormant = true;
    }

    public void setDormant()
    {
        dormant = true;
    }

    public boolean isDormant()
    {
        return dormant;
    }

    public void tickDownUnloads()
    {
        for (Iterator<Map.Entry<DimChunkCoord, Integer>> iterator = timedUnloadQueue.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<DimChunkCoord, Integer> entry = iterator.next();
            int ticks = entry.getValue();
            if (ticks <= 1) {
                remChunk(entry.getKey());
                iterator.remove();
            } else {
                entry.setValue(ticks - 1);
            }
        }
    }
}
