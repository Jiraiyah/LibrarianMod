package jiraiyah.librarian.TESR;

import jiraiyah.librarian.infrastructure.VillageData;
import jiraiyah.librarian.tileEntities.VillageIndicatorTile;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;

public class VillageIndicatorTESR extends TileEntitySpecialRenderer<VillageIndicatorTile>
{

    @Override
    public void renderTileEntityAt(VillageIndicatorTile entity, double x, double y, double z, float partialTicks, int destroyStage)
    {
        List<VillageData> villages = entity.villageDataList;
        if (villages == null || villages.size() == 0)
            return;
        Random random = entity.getWorld().rand;
        VertexBuffer buffer = Tessellator.getInstance().getBuffer();
        GlStateManager.pushMatrix();
        {
            GlStateManager.disableLighting();
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            GlStateManager.translate(x, y, z);
            buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0.125f, 0.125f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.125f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.125f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.125f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.125f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.875f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.875f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.875f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.875f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.875f, 0.125f).color(1f,0f,0f,1f).endVertex();
            Tessellator.getInstance().draw();
            /*buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0.125f, 0.875f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.875f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.875f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.875f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.875f, 0.125f).color(1f,0f,0f,1f).endVertex();
            Tessellator.getInstance().draw();*/
            buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            /*buffer.pos(0.125f, 0.125f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.875f, 0.125f).color(1f,0f,0f,1f).endVertex();*/
            buffer.pos(0.875f, 0.125f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.875f, 0.125f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.125f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.875f, 0.875f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.125f, 0.875f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0.125f, 0.875f, 0.875f).color(1f,0f,0f,1f).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
        for (VillageData data : villages)
        {
            //Log.info("==============================> Village Center : " + data.center.getX() + ", " + data.center.getY() + ", " + data.center.getZ());
            float rand = random.nextFloat();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x - 0.5f + data.center.getX() - entity.getPos().getX(),
                                     y - 0.5f + data.center.getY() - entity.getPos().getY(),
                                     z - 0.5f + data.center.getZ() - entity.getPos().getZ());
            GlStateManager.disableLighting();
            GlStateManager.disableTexture2D();
            GlStateManager.disableDepth();
            buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            for (BlockPos doorPos : data.doorPositions)
            {
                buffer.pos(0,0,0).color(1f,1f,1f,1f).endVertex();
                buffer.pos(doorPos.getX() - data.center.getX() + 1, doorPos.getY() - data.center.getY() + 1, doorPos.getZ() - data.center.getZ() + 1).color(1f,1f,1f,1f).endVertex();
            }
            Tessellator.getInstance().draw();
            GlStateManager.glLineWidth(15);
            buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0 - data.radius - 0.5f, 1 ,0 - data.radius - 0.5f).color(1f,0f,1f,1f).endVertex();
            buffer.pos(0 - data.radius - 0.5f, 1 ,0 + data.radius + 0.5f).color(1f,0f,1f,1f).endVertex();
            buffer.pos(0 + data.radius + 0.5f, 1 ,0 + data.radius + 0.5f).color(1f,0f,1f,1f).endVertex();
            buffer.pos(0 + data.radius + 0.5f, 1 ,0 - data.radius - 0.5f).color(1f,0f,1f,1f).endVertex();
            buffer.pos(0 - data.radius - 0.5f, 1 ,0 - data.radius - 0.5f).color(1f,0f,1f,1f).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.glLineWidth(5);
            buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0 - 8 - 0.5f, 3 ,0 - 8 - 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 - 8 - 0.5f, 3 ,0 + 8 + 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 + 8 + 0.5f, 3 ,0 + 8 + 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 + 8 + 0.5f, 3 ,0 - 8 - 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 - 8 - 0.5f, 3 ,0 - 8 - 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 - 8 - 0.5f, 6 ,0 - 8 - 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 - 8 - 0.5f, 6 ,0 + 8 + 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 + 8 + 0.5f, 6 ,0 + 8 + 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 + 8 + 0.5f, 6 ,0 - 8 - 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 - 8 - 0.5f, 6 ,0 - 8 - 0.5f).color(1f,0f,0f,1f).endVertex();
            Tessellator.getInstance().draw();
            buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0 - 8 - 0.5f, 3 ,0 + 8 + 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 - 8 - 0.5f, 6 ,0 + 8 + 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 + 8 + 0.5f, 3 ,0 + 8 + 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 + 8 + 0.5f, 6 ,0 + 8 + 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 + 8 + 0.5f, 3 ,0 - 8 - 0.5f).color(1f,0f,0f,1f).endVertex();
            buffer.pos(0 + 8 + 0.5f, 6 ,0 - 8 - 0.5f).color(1f,0f,0f,1f).endVertex();
            Tessellator.getInstance().draw();
            buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0 - 16 - 0.5f, 3 ,0 - 16 - 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 - 16 - 0.5f, 3 ,0 + 16 + 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 + 16 + 0.5f, 3 ,0 + 16 + 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 + 16 + 0.5f, 3 ,0 - 16 - 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 - 16 - 0.5f, 3 ,0 - 16 - 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 - 16 - 0.5f, 6 ,0 - 16 - 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 - 16 - 0.5f, 6 ,0 + 16 + 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 + 16 + 0.5f, 6 ,0 + 16 + 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 + 16 + 0.5f, 6 ,0 - 16 - 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 - 16 - 0.5f, 6 ,0 - 16 - 0.5f).color(0f,1f,0f,1f).endVertex();
            Tessellator.getInstance().draw();
            buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(0 - 16 - 0.5f, 3 ,0 + 16 + 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 - 16 - 0.5f, 6 ,0 + 16 + 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 + 16 + 0.5f, 3 ,0 + 16 + 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 + 16 + 0.5f, 6 ,0 + 16 + 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 + 16 + 0.5f, 3 ,0 - 16 - 0.5f).color(0f,1f,0f,1f).endVertex();
            buffer.pos(0 + 16 + 0.5f, 6 ,0 - 16 - 0.5f).color(0f,1f,0f,1f).endVertex();
            Tessellator.getInstance().draw();
            GlStateManager.glLineWidth(1);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean isGlobalRenderer(VillageIndicatorTile te)
    {
        return true;
    }
}
