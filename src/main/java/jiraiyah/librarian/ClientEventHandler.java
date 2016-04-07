package jiraiyah.librarian;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ClientEventHandler extends RenderWorldLastEvent
{
    public ClientEventHandler(RenderGlobal context, float partialTicks)
    {
        super(context, partialTicks);
    }


}
