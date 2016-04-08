package jiraiyah.librarian.events;

import jiraiyah.librarian.infrastructure.VillageData;
import jiraiyah.librarian.inits.KeyBindings;
import jiraiyah.librarian.network.VillageInfoPlayerMessage;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;

public class VillageDataHandler
{
    private boolean showVillages;
    public static List<VillageData> villageDataList = new ArrayList<>();

    private static final float PI = (float) Math.PI;
    private static final float DEG2RAD = PI/180;

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event)
    {
        if (!showVillages || villageDataList == null || villageDataList.size() == 0)
            return;
        VertexBuffer buffer = Tessellator.getInstance().getBuffer();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        float ticks = event.getPartialTicks();
        double plX = player.lastTickPosX + ((player.posX - player.lastTickPosX) * ticks);
        double plY = player.lastTickPosY + ((player.posY - player.lastTickPosY) * ticks);
        double plZ = player.lastTickPosZ + ((player.posZ - player.lastTickPosZ) * ticks);
        for (VillageData data : villageDataList)
        {

            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(-0.5f + data.center.getX() - plX,//x - 0.5f + data.center.getX() - pos.getX(),
                        -0.5f + data.center.getY() - plY,//y - 0.5f + data.center.getY() - pos.getY(),
                        -0.5f + data.center.getZ() - plZ);//z - 0.5f + data.center.getZ() - pos.getZ());
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                {
                    // region DRAW BORDER SPHERE
                    drawBorderSpehere(data.radius, buffer, new Vector4f(1f, 0f, 1f, 1f));
                    // endregion
                    // region DRAW DOORS
                    drawDoors(data.center, data.doorPositions, buffer, new Vector4f(1f, 1f, 1f, 1f));
                    // endregion
                    // region DRAW BORDER SQUARE
                    drawBorderSquare(data.radius, buffer, new Vector4f(1f, 1f, 0f, 0.5f));
                    // endregion
                    // region DRAW GOLEM SPAWN
                    drawGolemSpawn(buffer, new Vector4f(0f, 1f, 0f, 1f));
                    // endregion
                    // region DRAW CENTER
                    drawCenter(buffer, new Vector4f(1f, 0f, 0f, 1f));
                    //endregion
                }
                GlStateManager.glLineWidth(1);
                GlStateManager.disableBlend();
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(-0.5f + data.center.getX() - plX,//x - 0.5f + data.center.getX() - pos.getX(),
                        -0.5f + data.center.getY() - plY + 2,//y - 0.5f + data.center.getY() - pos.getY(),
                        -0.5f + data.center.getZ() - plZ);//z - 0.5f + data.center.getZ() - pos.getZ());
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                {
                    GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
                    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
                    GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate((float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-0.025F, -0.025F, 0.025F);
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    FontRenderer fontrenderer = renderManager.getFontRenderer();
                    String str = "Villagers : 5";
                    int j = fontrenderer.getStringWidth(str) / 2;
                    buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    int i = str.equals("deadmau5") ? -10 : 0;
                    buffer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    Tessellator.getInstance().draw();
                    GlStateManager.enableTexture2D();
                    fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
                    GlStateManager.disableBlend();
                }
                GlStateManager.glLineWidth(1);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(-0.5f + data.center.getX() - plX,//x - 0.5f + data.center.getX() - pos.getX(),
                        -0.5f + data.center.getY() - plY + 1.5,//y - 0.5f + data.center.getY() - pos.getY(),
                        -0.5f + data.center.getZ() - plZ);//z - 0.5f + data.center.getZ() - pos.getZ());
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                {
                    GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
                    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
                    GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate((float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-0.025F, -0.025F, 0.025F);
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    FontRenderer fontrenderer = renderManager.getFontRenderer();
                    String str = "Doors : 19";
                    int j = fontrenderer.getStringWidth(str) / 2;
                    buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    int i = str.equals("deadmau5") ? -10 : 0;
                    buffer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    Tessellator.getInstance().draw();
                    GlStateManager.enableTexture2D();
                    fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
                    GlStateManager.disableBlend();
                }
                GlStateManager.glLineWidth(1);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(-0.5f + data.center.getX() - plX,//x - 0.5f + data.center.getX() - pos.getX(),
                        -0.5f + data.center.getY() - plY + 1,//y - 0.5f + data.center.getY() - pos.getY(),
                        -0.5f + data.center.getZ() - plZ);//z - 0.5f + data.center.getZ() - pos.getZ());
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                {
                    GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
                    RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
                    GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate((float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-0.025F, -0.025F, 0.025F);
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    FontRenderer fontrenderer = renderManager.getFontRenderer();
                    String str = "Golems : Yes";
                    int j = fontrenderer.getStringWidth(str) / 2;
                    buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    buffer.pos((double) (-j - 1), (double) (-1), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (-j - 1), (double) (8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (j + 1), (double) (8), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    buffer.pos((double) (j + 1), (double) (-1), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    Tessellator.getInstance().draw();
                    GlStateManager.enableTexture2D();
                    fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, 0, -1);
                    GlStateManager.disableBlend();
                }
                GlStateManager.glLineWidth(1);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
            }
            GlStateManager.popMatrix();
        }

    }

    private void drawCenter(VertexBuffer buffer, Vector4f color)
    {
        GlStateManager.glLineWidth(1);
        buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-0.5, -0.5, -0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-0.5, -0.5, 0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(0.5, -0.5, 0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(0.5, -0.5, -0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-0.5, -0.5, -0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-0.5, 0.5, -0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-0.5, 0.5, 0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(0.5, 0.5, 0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(0.5, 0.5, -0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-0.5, 0.5, -0.5).color(color.x, color.y, color.z, color.w).endVertex();
        Tessellator.getInstance().draw();
        buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-0.5, -0.5, 0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-0.5, 0.5, 0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(0.5, -0.5, 0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(0.5, 0.5, 0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(0.5, -0.5, -0.5).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(0.5, 0.5, -0.5).color(color.x, color.y, color.z, color.w).endVertex();
        Tessellator.getInstance().draw();
    }

    private void drawGolemSpawn(VertexBuffer buffer, Vector4f color)
    {
        GlStateManager.glLineWidth(1);
        buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-8.5f, -3, -8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-8.5f, -3, 8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(8.5f, -3, 8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(8.5f, -3, -8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-8.5f, -3, -8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-8.5f, 3, -8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-8.5f, 3, 8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(8.5f, 3, 8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(8.5f, 3, -8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-8.5f, 3, -8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        Tessellator.getInstance().draw();
        buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-8.5f, -3, 8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-8.5f, 3, 8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(8.5f, -3, 8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(8.5f, 3, 8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(8.5f, -3, -8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(8.5f, 3, -8.5f).color(color.x, color.y, color.z, color.w).endVertex();
        Tessellator.getInstance().draw();
    }

    private void drawBorderSquare(int radius, VertexBuffer buffer, Vector4f color)
    {
        GlStateManager.glLineWidth(10);
        buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(-radius - 0.5f, 0, -radius - 0.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-radius - 0.5f, 0, radius + 0.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(radius + 0.5f, 0, radius + 0.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(radius + 0.5f, 0, -radius - 0.5f).color(color.x, color.y, color.z, color.w).endVertex();
        buffer.pos(-radius - 0.5f, 0, -radius - 0.5f).color(color.x, color.y, color.z, color.w).endVertex();
        Tessellator.getInstance().draw();
    }

    private void drawDoors(BlockPos center, List<BlockPos> doorPositions, VertexBuffer buffer, Vector4f color)
    {
        GlStateManager.glLineWidth(1);
        buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        for (BlockPos doorPos : doorPositions)
        {
            buffer.pos(0, 0, 0)
                    .color(color.x, color.y, color.z, color.w)
                    .endVertex();
            buffer.pos(doorPos.getX() - center.getX() + 1,
                    doorPos.getY() - center.getY() + 1,
                    doorPos.getZ() - center.getZ() + 1)
                    .color(color.x, color.y, color.z, color.w)
                    .endVertex();
        }
        Tessellator.getInstance().draw();
    }

    private void drawBorderSpehere(int radius, VertexBuffer buffer, Vector4f color)
    {
        GlStateManager.glLineWidth(1);
        int space = 10;
        int upper = 90;
        int upper2 = 360 - space;
        double x, y, z;
        int R = radius;
        for (int b = -space; b <= upper; b += space)
        {
            buffer.begin(GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
            for (int a = 0; a <= upper2; a++)
            {
                x = R * Math.sin((a) * DEG2RAD) * Math.sin((b) * DEG2RAD);
                z = R * Math.cos((a) * DEG2RAD) * Math.sin((b) * DEG2RAD);
                y = R * Math.cos((b) * DEG2RAD);
                buffer.pos(x, y, z).color(color.x, color.y, color.z, color.w).endVertex();
            }
            Tessellator.getInstance().draw();
        }
        upper = 180 - space;
        upper2 = 90;
        for (int b = 0; b <= upper; b += space)
        {
            buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (int a = -90; a <= upper2; a++)//=space)
            {
                x = R * Math.sin((a) * DEG2RAD) * Math.sin((b) * DEG2RAD);
                z = R * Math.cos((a) * DEG2RAD) * Math.sin((b) * DEG2RAD);
                y = R * Math.cos((b) * DEG2RAD);
                buffer.pos(x, z, y).color(color.x, color.y, color.z, color.w).endVertex();
            }
            Tessellator.getInstance().draw();
        }
        upper = 180 - space;
        upper2 = 90;
        for (int b = 0; b <= upper; b += space)
        {
            buffer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
            for (int a = -90; a <= upper2; a++)//=space)
            {
                x = R * Math.sin((a) * DEG2RAD) * Math.sin((b) * DEG2RAD);
                z = R * Math.cos((a) * DEG2RAD) * Math.sin((b) * DEG2RAD);
                y = R * Math.cos((b) * DEG2RAD);
                buffer.pos(y, z, x).color(color.x, color.y, color.z, color.w).endVertex();
            }
            Tessellator.getInstance().draw();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if(KeyBindings.VILLAGE_DATA.isPressed())
        {
            showVillages = !showVillages;
            VillageInfoPlayerMessage.sendMessage(Minecraft.getMinecraft().thePlayer.getUniqueID(), showVillages);
        }
    }

    public static void setVillageData(List<VillageData> data)
    {
        villageDataList = data;
        Log.info("------------Client received village data-----------------> " + villageDataList.size());
    }

}
