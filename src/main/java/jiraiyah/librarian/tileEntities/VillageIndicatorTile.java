package jiraiyah.librarian.tileEntities;

import jiraiyah.librarian.infrastructure.VillageData;
import jiraiyah.librarian.network.VillageIdicatorMessage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageDoorInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        float psx = pos.getX();
        float psz = pos.getZ();
        VillageCollection villageCollection = getWorld().getVillageCollection();
        if (villageCollection == null)
            return;
        List<Village> allVillages = villageCollection.getVillageList();
        if (allVillages == null || allVillages.size() == 0)
            return;
        villageDataList.clear();
        allVillages.stream()
                .filter(v -> (psx < v.getCenter().getX() + v.getVillageRadius() &&
                        psz < v.getCenter().getZ() + v.getVillageRadius()) &&
                        (psx > v.getCenter().getX() - v.getVillageRadius() &&
                                psz > v.getCenter().getZ() - v.getVillageRadius()))
                .forEach(v -> {
                    int radius = v.getVillageRadius();
                    BlockPos center = v.getCenter();
                    List<VillageDoorInfo> doorInfos = v.getVillageDoorInfoList();
                    List<BlockPos> doorPositions = doorInfos.stream().map(VillageDoorInfo::getDoorBlockPos).collect(Collectors.toList());
                    villageDataList.add(new VillageData(radius, center, doorPositions));
                });
        if (!worldObj.isRemote)
            VillageIdicatorMessage.sendMessage(worldObj.getMinecraftServer(), villageDataList, getPos());
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
