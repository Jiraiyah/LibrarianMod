package jiraiyah.librarian.events;

import com.mojang.authlib.GameProfile;
import gnu.trove.map.hash.TObjectLongHashMap;
import gnu.trove.procedure.TObjectLongProcedure;
import jiraiyah.librarian.infrastructure.SaveModule;
import jiraiyah.librarian.utilities.NBTUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

public class ChunkLoaderLoginTimes extends SaveModule
{
	private static final long MAX_WAIT_TIME = 604800000L;
	public static ChunkLoaderLoginTimes INSTANCE;
	static TObjectLongHashMap<GameProfile> loginTimes = new TObjectLongHashMap();

	static
	{
		INSTANCE = new ChunkLoaderLoginTimes();
		MinecraftForge.EVENT_BUS.register(INSTANCE);
	}

	boolean loaded = false;

	public ChunkLoaderLoginTimes()
	{
		super("ChunkLoaderData");
	}

	public boolean isValid(GameProfile profile)
	{
		if (!this.loaded)
			return true;
		for (EntityPlayerMP playerMP : FMLServerHandler.instance().getServer().getPlayerList().getPlayerList())
		{
			if (playerMP.getGameProfile().equals(profile))
			{
				loginTimes.put(profile, System.currentTimeMillis());
				return true;
			}
		}
		long l = System.currentTimeMillis() - loginTimes.get(profile);
		return l < 604800000L;
	}

	@SubscribeEvent
	public void playerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		loginTimes.put(event.player.getGameProfile(), System.currentTimeMillis());
		ChunkLoaderManager.dirty = true;
		markDirty();
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		loginTimes.clear();
		NBTTagList loginTimes = nbt.getTagList("LoginTimes", 10);
		for (int i = 0; i < loginTimes.tagCount(); i++)
		{
			NBTTagCompound loginTime = loginTimes.getCompoundTagAt(i);
			GameProfile profile = NBTUtils.profileFromNBT(loginTime);
			if (profile != null)
				loginTimes.put(profile, loginTime.getLong("LoginTime"));
		}
		this.loaded = true;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		final NBTTagList tagList = new NBTTagList();
		loginTimes.forEachEntry(new TObjectLongProcedure()
		{
			public boolean execute(GameProfile a, long b)
			{
				NBTTagCompound t = NBTUtils.proifleToNBT(a);
				t.setLong("LoginTime", b);
				tagList.appendTag(t);
				return true;
			}
		});
		nbt.setTag("LoginTimes", tagList);
	}

	public void reset()
	{
		loginTimes.clear();
	}
}
