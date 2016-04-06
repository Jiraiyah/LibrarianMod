package jiraiyah.librarian;

import jiraiyah.librarian.infrastructure.ChunkData;
import jiraiyah.librarian.proxies.CommonProxy;
import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.utilities.GenericCreativeTab;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/*

Set your command block to repeat, always active
entitydata @e[type=Enderman,tag=!a] {carried:minecraft:piston_extension,Tags­:["a"]}
set to clock / no redstone

 */
@SuppressWarnings({"unused", "WeakerAccess"})
@Mod(modid = Reference.MOD_ID, version = Reference.VERSION)
public class Librarian
{
    public static final boolean deobf_folder;
    public static final boolean deobf;

    static
    {
        boolean d;
        try
        {
            World.class.getMethod("getBlockState", BlockPos.class);
            d = true;
            Log.info("Dev Enviroment detected. Releasing hounds...");
        }
        catch (NoSuchMethodException | SecurityException e)
        {
            d = false;
        }
        deobf = d;
        if (deobf)
        {
            URL resource = Librarian.class.getClassLoader().getResource(Librarian.class.getName().replace('.', '/').concat(".class"));
            deobf_folder = (resource != null) && ("file".equals(resource.getProtocol()));
        }
        else
        {
            deobf_folder = false;
        }
    }

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
