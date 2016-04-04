package jiraiyah.librarian.utilities;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCTimer
{
    public static int clientTimer;
    public static float renderTimer;
    public static float renderPartialTickTime;
    public static int serverTimer;

    static
    {
        MinecraftForge.EVENT_BUS.register(new MCTimer());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void serverTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) {
            serverTimer += 1;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void clientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) {
            clientTimer += 1;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderTick(TickEvent.RenderTickEvent event)
    {
        renderPartialTickTime = event.renderTickTime;
        renderTimer = clientTimer + renderPartialTickTime;
    }
}
