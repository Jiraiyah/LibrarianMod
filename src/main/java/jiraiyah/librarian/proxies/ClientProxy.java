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
        BlockRendererRegisters.register();
        ItemRendererRegisters.register();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);

        TileEntitiesRendererRegister.register();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    public static boolean isCtrlDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) ||
                Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }

    public static boolean isShiftDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ||
                Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    public static boolean isAltDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_LMENU) ||
                Keyboard.isKeyDown(Keyboard.KEY_RMENU);
    }
}
