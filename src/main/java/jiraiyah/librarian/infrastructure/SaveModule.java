package jiraiyah.librarian.infrastructure;

import net.minecraft.nbt.NBTTagCompound;

public abstract class SaveModule
{
	String name;

	public SaveModule(String name)
	{
		this.name = name;
	}

	public abstract void readFromNBT(NBTTagCompound paramNBTTagCompound);

	public abstract void writeToNBT(NBTTagCompound paramNBTTagCompound);

	public void markDirty()
	{
		if (SaveManager.manager != null)
		{
			SaveManager.manager.markDirty();
		}
	}

	public abstract void reset();
}
