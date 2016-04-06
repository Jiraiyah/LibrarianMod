package jiraiyah.librarian.infrastructure;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class VillageData
{
    public int radius;
    public BlockPos center;
    public List<BlockPos> doorPositions = new ArrayList<>();



    public VillageData(int radiusList, BlockPos centerList, List<BlockPos> doorPositions)
    {
        this.radius = radiusList;
        this.center = centerList;
        this.doorPositions = doorPositions;
    }
}
