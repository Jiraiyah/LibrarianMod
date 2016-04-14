package jiraiyah.librarian.infrastructure;

import jiraiyah.librarian.events.ChunkLoaderLoginTimes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class SaveManager extends WorldSavedData
{
	public static final String SAVE_DATA_NAME = "LibrarianSaveData";
	public static SaveManager manager;
	public static SaveModule[] saveModules = { ChunkLoaderLoginTimes.INSTANCE };

	public SaveManager(String name)
	{
		super(name);
	}

	public static void init()
	{
		WorldServer worldServer = DimensionManager.getWorld(0);
		manager = (SaveManager)worldServer.loadItemData(SaveManager.class, "LibrarianSaveData");
		if (manager == null)
		{
			manager = new SaveManager("LibrarianSaveData");
			worldServer.setItemData("LibrarianSaveData", manager);
			manager.markDirty();
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		for (SaveModule saveModule : saveModules)
		{
			saveModule.reset();
			if (nbt.hasKey(saveModule.name, 10))
			{
				try
				{
					saveModule.readFromNBT(nbt.getCompoundTag(saveModule.name));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					saveModule.reset();
				}
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		for (SaveModule saveModule : saveModules)
		{
			try
			{
				NBTTagCompound tag = new NBTTagCompound();
				saveModule.writeToNBT(tag);
				nbt.setTag(saveModule.name, tag);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
