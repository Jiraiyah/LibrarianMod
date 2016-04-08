package jiraiyah.librarian.events;

import jiraiyah.librarian.infrastructure.VillageData;
import jiraiyah.librarian.inits.KeyBindings;
import jiraiyah.librarian.network.VillageInfoPlayerMessage;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.ArrayList;
import java.util.List;

public class VillageDataHandler
{
    private boolean showVillages;
    public static List<VillageData> villageDataList = new ArrayList<>();

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event)
    {
        Log.info("---------------Village render call-------------->");
        if (!showVillages)
            return;

        Log.info("----------------------------->" + villageDataList.size());

        //TODO : draw the info you already have in tesr for village indicator
    }

    //TODO : how to get the player that is using the input key event?
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        Log.info("---------------Player Hit Key-------------->");
        if(KeyBindings.VILLAGE_DATA.isPressed() && !showVillages)
        {
            showVillages = true;
            Log.info("---------------Start Showing Villages-------------->");
            VillageInfoPlayerMessage.sendMessage(Minecraft.getMinecraft().thePlayer.getUniqueID(), true);
        }
        else if (KeyBindings.VILLAGE_DATA.isPressed() && showVillages)
        {
            Log.info("---------------Stop Showing Villages-------------->");
            showVillages = false;
            VillageInfoPlayerMessage.sendMessage(Minecraft.getMinecraft().thePlayer.getUniqueID(), false);
        }
    }

    public static void setVillageData(List<VillageData> data)
    {
        villageDataList = data;
    }
}
