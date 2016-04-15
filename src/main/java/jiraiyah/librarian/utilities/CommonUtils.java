package jiraiyah.librarian.utilities;

import net.minecraft.world.World;

public class CommonUtils
{
    public static int getDimension(World world)
    {
        return world.provider.getDimension();
    }
}
