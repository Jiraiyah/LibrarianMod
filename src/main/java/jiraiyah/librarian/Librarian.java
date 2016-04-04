package jiraiyah.librarian;

import jiraiyah.librarian.infrastructure.ChunkData;
import jiraiyah.librarian.proxies.CommonProxy;
import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.utilities.GenericCreativeTab;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
@Mod(modid = Reference.MOD_ID, version = Reference.VERSION)
public class Librarian
{
    @Instance(Reference.MOD_ID)
    public static Librarian INSTANCE;

    public static List<ChunkData> LOADING_CHUNKS = new ArrayList<>();

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy PROXY;

    public static final GenericCreativeTab CREATIVE_TAB = new GenericCreativeTab(Reference.MOD_ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        PROXY.preInit(event);
        if ( Loader.isModLoaded( "Waila" ))
            FMLInterModComms.sendMessage( "Waila","register","jiraiyah.jpiston.compatibility.WailaCompatibility.register" );
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        PROXY.init(event);
        CREATIVE_TAB.setIcon(Items.enchanted_book);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        PROXY.postInit(event);
    }
}
