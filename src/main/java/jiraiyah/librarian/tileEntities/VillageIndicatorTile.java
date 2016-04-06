package jiraiyah.librarian.tileEntities;

import jiraiyah.librarian.infrastructure.VillageData;
import jiraiyah.librarian.network.VillageIdicatorMessage;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageDoorInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class VillageIndicatorTile extends TileEntity implements ITickable
{
    private int resetTimerTicks = 20;
    private int resetCoolDown;
    private List<VillageData> villageDataList = new ArrayList<>();

    public VillageIndicatorTile()
    {
        super();
        resetCoolDown = resetTimerTicks;
    }

    @Override
    public void update()
    {

        resetCoolDown --;
        if (resetCoolDown <= 0)
        {
            resetVillageDataList();
            resetCoolDown = resetTimerTicks;
        }
        if (!getWorld().isRemote)
            return;
        drawVillageInfo();
    }

    private void resetVillageDataList()
    {
        villageDataList.clear();
        float psx = pos.getX();
        float psz = pos.getZ();
        VillageCollection villageCollection = getWorld().getVillageCollection();
        if (villageCollection == null)
            return;
        List<Village> allVillages = villageCollection.getVillageList();
        if (allVillages == null || allVillages.size() == 0)
            return;
        allVillages.stream()
                .filter(v -> (psx < v.getCenter().getX() + v.getVillageRadius() &&
                        psz < v.getCenter().getZ() + v.getVillageRadius()) &&
                        (psx > v.getCenter().getX() - v.getVillageRadius() &&
                                psz > v.getCenter().getZ() - v.getVillageRadius()))
                .forEach(v -> {
                    int radius = v.getVillageRadius();
                    BlockPos center = v.getCenter();
                    List<VillageDoorInfo> doorInfos = v.getVillageDoorInfoList();
                    List<BlockPos> doorPositions = new ArrayList<>();
                    for (VillageDoorInfo info : doorInfos)
                        doorPositions.add(info.getDoorBlockPos());
                    villageDataList.add(new VillageData(radius, center, doorPositions));
                });
        if (!worldObj.isRemote)
            VillageIdicatorMessage.sendMessage(villageDataList);
    }

    private void drawVillageInfo()
    {
        Log.info("==================================> " + villageDataList.size());
        for(VillageData data : villageDataList)
        {
            //TODO : Draw the village info
            Random random = new Random();
            float rand = random.nextFloat();
            VertexBuffer buffer = Tessellator.getInstance().getBuffer();
            glTranslatef(data.center.getX(), data.center.getY(), data.center.getZ());
            glDisable(GL_LIGHTING);
            buffer.begin(GL_LINE, DefaultVertexFormats.POSITION_TEX);
            for(BlockPos doorinfo : data.doorPositions)
            {
                //buffer.addVertexData(new int[]{data.center.getX(),data.center.getY(),data.center.getZ()});
                buffer.addVertexData(new int[]{0, 0, 0});
                buffer.addVertexData(new int[]{doorinfo.getX(),doorinfo.getY() + 1, doorinfo.getZ()});
            }
            Tessellator.getInstance().draw();
            buffer.putColorRGB_F4(rand, rand, rand);
            glTranslatef(-data.center.getX(), -data.center.getY(), -data.center.getZ());
        }
    }

    public void UpdateDataFromServer(List<VillageData> data)
    {
        villageDataList = data;
    }
}
