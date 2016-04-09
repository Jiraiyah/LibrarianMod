package jiraiyah.librarian.events;

import jiraiyah.librarian.infrastructure.VillageData;
import jiraiyah.librarian.inits.KeyBindings;
import jiraiyah.librarian.network.VillageInfoPlayerMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class VillageDataHandler
{
    private boolean showVillages;
    private boolean showVillagesDoors = true;
    private boolean showVillagesBorder;
    private boolean showVillagesSpehere;
    private boolean showVillagesCenter;
    private boolean showVillagesInfo;
    private boolean showVillagesGolem = true;


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
        Vec3d playerPos = new Vec3d(plX, plY, plZ);
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        FontRenderer fontrenderer = renderManager.getFontRenderer();
        for (VillageData data : villageDataList)
        {
            double distance = Math.sqrt(playerPos.squareDistanceTo(data.center.getX(), data.center.getY(), data.center.getZ()));
            double scaleFactor = -0.015f * distance / 4.209f;
            //Log.info("=================== Distance = " + distance); //17.723549165908132
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(-0.5f + data.center.getX() - plX,//x - 0.5f + data.center.getX() - pos.getX(),
                        -0.5f + data.center.getY() - plY,//y - 0.5f + data.center.getY() - pos.getY(),
                        -0.5f + data.center.getZ() - plZ);//z - 0.5f + data.center.getZ() - pos.getZ());
                GlStateManager.glLineWidth(1);
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                {
                    if (showVillagesSpehere)
                        drawBorderSpehere(data.radius, buffer, new Vector4f(1f, 0f, 1f, 1f));
                    if (showVillagesDoors)
                        drawDoors(data.center, data.doorPositions, buffer, new Vector4f(1f, 1f, 1f, 1f));
                    if (showVillagesBorder)
                        drawBorderSquare(data.radius, buffer, new Vector4f(1f, 1f, 0f, 0.5f));
                    if (showVillagesGolem)
                        drawGolemSpawn(buffer, new Vector4f(0f, 1f, 0f, 1f));
                    if (showVillagesCenter)
                        drawCenter(buffer, new Vector4f(1f, 0f, 0f, 1f));
                    if (showVillagesInfo)
                    {
                        GlStateManager.translate(0f, 1.5f, 0f);
                        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                        GlStateManager.rotate((float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                        GlStateManager.scale(scaleFactor, scaleFactor, -scaleFactor);
                        GlStateManager.enableBlend();
                        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                        GlStateManager.enableTexture2D();
                        drawTextInfo("Villagers : " + data.villagerCount, 1, scaleFactor, fontrenderer);
                        drawTextInfo("Doors : " + data.doorPositions.size(), 2, scaleFactor, fontrenderer);
                        boolean canSpaw = data.doorPositions.size() > 20;
                        drawTextInfo("Max Golem : " + (canSpaw ? (TextFormatting.GREEN + Integer.toString(data.villagerCount / 10)) : TextFormatting.DARK_RED + "" + TextFormatting.BOLD + "0"), 3, scaleFactor, fontrenderer);
                        drawTextInfo("Reputation : " + data.reputation, 4, scaleFactor, fontrenderer);
                        GlStateManager.disableBlend();
                    }
                }
                GlStateManager.glLineWidth(1);
                GlStateManager.disableBlend();
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
            }
            GlStateManager.popMatrix();
        }
    }

    private void drawTextInfo(String text, int lineNumber, double scaleFactor, FontRenderer fontrenderer)
    {
        fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, 10 * lineNumber, -1);
    }

    private void drawCenter(VertexBuffer buffer, Vector4f color)
    {
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
        GlStateManager.glLineWidth(1);
    }

    private void drawDoors(BlockPos center, List<BlockPos> doorPositions, VertexBuffer buffer, Vector4f color)
    {
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
        int space = 5;
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
            //Log.info("=================> Village toggle data key pressed");
        }
        if (KeyBindings.VILLAGE_DATA_BORDER.isPressed())
            showVillagesBorder = !showVillagesBorder;
        if (KeyBindings.VILLAGE_DATA_DOORS.isPressed())
            showVillagesDoors = !showVillagesDoors;
        if (KeyBindings.VILLAGE_DATA_SPHERE.isPressed())
            showVillagesSpehere = !showVillagesSpehere;
        if (KeyBindings.VILLAGE_DATA_GOLEM.isPressed())
            showVillagesGolem = !showVillagesGolem;
        if (KeyBindings.VILLAGE_DATA_INFO.isPressed())
            showVillagesInfo = !showVillagesInfo;
        if (KeyBindings.VILLAGE_DATA_CENTER.isPressed())
            showVillagesCenter = !showVillagesCenter;

    }

    public static void setVillageData(List<VillageData> data)
    {
        villageDataList = data;
        //Log.info("------------Client received village data-----------------> " + villageDataList.size());
    }

}
