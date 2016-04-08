package jiraiyah.librarian.tileEntities;

import jiraiyah.librarian.infrastructure.VillageData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class VillageIndicatorTile extends TileEntity implements ITickable
{
    private static final int RESET_TIMER_TICKS = 20;
    private int resetCoolDown;

    public List<VillageData> villageDataList = new ArrayList<>();

    public VillageIndicatorTile()
    {
        super();
        resetCoolDown = 0;
    }

    @Override
    public void update()
    {

        resetCoolDown --;
        if (resetCoolDown <= 0)
        {
            resetVillageDataList();
            resetCoolDown = RESET_TIMER_TICKS;
        }
        /*if (!getWorld().isRemote)
            return;*/

        //drawVillageInfo();
    }

    private void resetVillageDataList()
    {

    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared()
    {
        return Double.POSITIVE_INFINITY;
    }

    public void UpdateDataFromServer(List<VillageData> data)
    {
        villageDataList = data;
    }
}
