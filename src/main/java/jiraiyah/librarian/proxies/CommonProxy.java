package jiraiyah.librarian.proxies;

import jiraiyah.librarian.inits.NetworkMessages;
import jiraiyah.librarian.inits.blocks.BlockInits;
import jiraiyah.librarian.inits.blocks.BlockRegisters;
import jiraiyah.librarian.inits.events.CommonEventRegister;
import jiraiyah.librarian.inits.items.ItemInits;
import jiraiyah.librarian.inits.items.ItemRegisters;
import jiraiyah.librarian.inits.recipes.RecipeRegisters;
import jiraiyah.librarian.inits.tileEntities.TileEntitesRegister;
import jiraiyah.librarian.interfaces.IProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy implements IProxy
{

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        BlockInits.initialize();
        BlockRegisters.register();
        ItemInits.initialize();
        ItemRegisters.register();
        CommonEventRegister.register();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        RecipeRegisters.register();
        TileEntitesRegister.register();
        NetworkMessages.register();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
