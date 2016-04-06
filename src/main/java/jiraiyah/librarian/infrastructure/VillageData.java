package jiraiyah.librarian.infrastructure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillageDoorInfo;

import java.util.ArrayList;
import java.util.List;

public class VillageData
{
    public int radiusList;
    public BlockPos center;
    public List<VillageDoorInfo> doorInfos = new ArrayList<>();



    public VillageData(int radiusList, BlockPos centerList, List<VillageDoorInfo> doorInfos)
    {
        this.radiusList = radiusList;
        this.center = centerList;
        this.doorInfos = doorInfos;
    }
}
