package jiraiyah.librarian.events;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class VillageDataHandler
{
    private boolean showVillages;

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt)
    {
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {

    }
}
