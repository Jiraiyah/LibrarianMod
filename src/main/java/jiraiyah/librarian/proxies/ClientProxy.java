package jiraiyah.librarian.proxies;

import jiraiyah.librarian.inits.blocks.BlockRendererRegisters;
import jiraiyah.librarian.inits.events.ClientEventRegister;
import jiraiyah.librarian.inits.items.ItemRendererRegisters;
import jiraiyah.librarian.inits.tileEntities.TileEntitiesRendererRegister;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        ClientEventRegister.register();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        BlockRendererRegisters.register();
        ItemRendererRegisters.register();
        TileEntitiesRendererRegister.register();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    public boolean isCtrlDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ||
                Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public boolean isShiftDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ||
                Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public boolean isAltDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) ||
                Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }
}
