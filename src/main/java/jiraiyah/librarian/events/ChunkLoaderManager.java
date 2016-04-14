package jiraiyah.librarian.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import gnu.trove.list.array.TIntArrayList;
import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.tileEntities.ChunkLoaderTile;
import jiraiyah.librarian.utilities.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.PlayerOrderedLoadingCallback;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ChunkLoaderManager implements LoadingCallback, PlayerOrderedLoadingCallback
{
	public static ChunkLoaderManager INSTANCE = new ChunkLoaderManager();
	public static boolean dirty = false;
	public static final HashMultimap<World, ChunkLoaderTile> chunkLoaders = HashMultimap.create();

	private static HashMap<World, HashMap<GameProfile, ForgeChunkManager.Ticket>> playerTickets = new HashMap<>();

	public static void init()
	{
		ForgeChunkManager.setForcedChunkLoadingCallback(Librarian.INSTANCE, INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	public static void register(ChunkLoaderTile loader)
	{
		synchronized (chunkLoaders)
		{
			GameProfile profile = loader.getProfile();
			if (profile != null)
			{
				ForgeChunkManager.Ticket playerTicket = INSTANCE.getPlayerTickets(profile, loader.getWorld());
				if (playerTicket != null)
					ForgeChunkManager.forceChunk(playerTicket, new ChunkCoordIntPair(loader.getPos().getX() >> 4, loader.getPos().getZ() >> 4));
			}
			chunkLoaders.put(loader.getWorld(), loader);
			dirty = true;
		}
	}

	public static void unregister(ChunkLoaderTile loader)
	{
		synchronized (chunkLoaders)
		{
			chunkLoaders.remove(loader.getWorld(), loader);
			dirty = true;
		}
	}

	public static void clear()
	{
		chunkLoaders.clear();
		playerTickets.clear();
		ChunkLoaderLoginTimes.INSTANCE.loaded = false;
	}

	public void reloadChunkLoaders()
	{
		Multimap<ForgeChunkManager.Ticket, ChunkCoordIntPair> toUnload;
		Multimap<ForgeChunkManager.Ticket, ChunkCoordIntPair> toAdd;
		Iterator<ForgeChunkManager.Ticket> iterator;
		synchronized (chunkLoaders)
		{
			dirty = false;
			HashMultimap<World, ChunkCoordIntPair> worldChunks = HashMultimap.create();
			toUnload = HashMultimap.create();
			Multimap<ForgeChunkManager.Ticket, ChunkCoordIntPair> loaded = HashMultimap.create();
			toAdd = HashMultimap.create();
			for (HashMap<GameProfile, ForgeChunkManager.Ticket> map : playerTickets.values())
			{
				for (ForgeChunkManager.Ticket ticket : map.values())
				{
					ImmutableSet<ChunkCoordIntPair> chunkList = ticket.getChunkList();
					for (ChunkCoordIntPair pair : chunkList)
						ticket.world.getBlockState(pair.getCenterBlock(20));
					worldChunks.putAll(ticket.world, chunkList);
					toUnload.putAll(ticket, chunkList);
					loaded.putAll(ticket, chunkList);
				}
			}
			ChunkLoaderTile chunkLoader;
			ForgeChunkManager.Ticket ticket;
			for (Iterator<ChunkLoaderTile> iter = chunkLoaders.values().iterator(); iter.hasNext();)
			{
				chunkLoader = (ChunkLoaderTile)iter.next();
				if (chunkLoader.isInvalid())
				{
					dirty = true;
					iter.remove();
				}
				if (!chunkLoader.isLoaded())
				{
					dirty = true;
				}
				GameProfile profile = chunkLoader.getProfile();
				if ((chunkLoader.active) && (profile != null) && (ChunkLoaderLoginTimes.INSTANCE.isValid(profile)))
				{
					ticket = getPlayerTickets(profile, chunkLoader.getWorld());
					if (ticket != null)
					{
						for (ChunkCoordIntPair coordIntPair : chunkLoader.getChunkCoords())
						{
							worldChunks.remove(chunkLoader.getWorld(), coordIntPair);
							toUnload.remove(ticket, coordIntPair);
							if (!loaded.containsEntry(ticket, coordIntPair))
								toAdd.put(ticket, coordIntPair);
						}
					}
				}
			}
			for (HashMap<GameProfile, ForgeChunkManager.Ticket> map : playerTickets.values())
			{
				for (iterator = map.values().iterator(); iterator.hasNext();)
				{
					ticket = iterator.next();
					for (ChunkCoordIntPair pair : toUnload.get(ticket))
						ForgeChunkManager.unforceChunk(ticket, pair);
					for (ChunkCoordIntPair pair : toAdd.get(ticket))
						ForgeChunkManager.forceChunk(ticket, pair);
					if (ticket.getChunkList().isEmpty())
					{
						ForgeChunkManager.releaseTicket(ticket);
						iterator.remove();
					}
					else
					{
						TIntArrayList x = new TIntArrayList();
						TIntArrayList z = new TIntArrayList();
						for (ChunkCoordIntPair chunkCoordIntPair : ticket.getChunkList())
						{
							x.add(chunkCoordIntPair.chunkXPos);
							z.add(chunkCoordIntPair.chunkZPos);
						}
						ticket.getModData().setIntArray("x", x.toArray());
						ticket.getModData().setIntArray("z", z.toArray());
					}
				}
			}

		}
	}

	public ForgeChunkManager.Ticket getPlayerTickets(GameProfile profile, World world)
	{
		HashMap<GameProfile, ForgeChunkManager.Ticket> gameProfileTicketHashMap = (HashMap)playerTickets.get(world);
		if (gameProfileTicketHashMap == null)
			playerTickets.put(world, gameProfileTicketHashMap = new HashMap());
		ForgeChunkManager.Ticket ticket = (ForgeChunkManager.Ticket)gameProfileTicketHashMap.get(profile);
		if (ticket == null)
		{
			ticket = ForgeChunkManager.requestPlayerTicket(Librarian.INSTANCE, profile.getName(), world, net.minecraftforge.common.ForgeChunkManager.Type.NORMAL);
			NBTTagCompound tag = ticket.getModData();
			tag.setString("Name", profile.getName());
			UUID id = profile.getId();
			if (id != null)
			{
				tag.setLong("UUIDL", id.getLeastSignificantBits());
				tag.setLong("UUIDU", id.getMostSignificantBits());
			}
			gameProfileTicketHashMap.put(profile, ticket);
		}
		return ticket;
	}

	@Override
	public ListMultimap<String, ForgeChunkManager.Ticket> playerTicketsLoaded(ListMultimap<String, ForgeChunkManager.Ticket> tickets, World world)
	{
		return tickets;
	}

	@Override
	public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world)
	{
		dirty = true;
		HashMap<GameProfile, ForgeChunkManager.Ticket> cache = new HashMap();
		playerTickets.put(world, cache);
		for (ForgeChunkManager.Ticket ticket : tickets)
		{
			NBTTagCompound modData = ticket.getModData();
			GameProfile profile = NBTUtils.profileFromNBT(modData);
			cache.put(profile, ticket);
			int[] x = modData.getIntArray("x");
			int[] z = modData.getIntArray("z");
			if (x.length == z.length)
			{
				net.minecraft.world.chunk.Chunk chunk;
				for (int i = 0; i < x.length; i++)
				{
					ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(x[i], z[i]));
					chunk = world.getChunkFromChunkCoords(x[i], z[i]);
				}
			}
		}
	}

	@SubscribeEvent
	public void serverTick(TickEvent.ServerTickEvent event)
	{
		if (dirty)
			reloadChunkLoaders();
	}

	@SubscribeEvent
	public void onWorldUnloaded(WorldEvent.Unload event)
	{
		playerTickets.remove(event.getWorld());
		chunkLoaders.removeAll(event.getWorld());
	}
}
