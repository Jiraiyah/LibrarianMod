package jiraiyah.librarian.events;

import jiraiyah.librarian.inits.KeyBindings;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class VillageDataHandler
{
    private boolean showVillages;

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt)
    {
        if (!showVillages)
            return;
        //TODO : draw the info you already have in tesr for village indicator
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if(KeyBindings.VILLAGE_DATA.isPressed() && !showVillages)
        {
            showVillages = true;
            //TODO : send packet from client to server, add this player to the list, and request for information
        }
        else if (KeyBindings.VILLAGE_DATA.isPressed() && !showVillages)
        {
            showVillages = false;
            //TODO : send packet from client to server, remove this player from the list
        }
    }
}
