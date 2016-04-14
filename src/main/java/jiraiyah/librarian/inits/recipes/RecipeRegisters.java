package jiraiyah.librarian.inits.recipes;

import jiraiyah.librarian.inits.blocks.BlockInits;
import jiraiyah.librarian.inits.items.ItemInits;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeRegisters
{
    public static void register()
    {
        registerShaped();
        registerShapeless();
        registerSmelting();
        Log.info("=========================================================> Registered Recipes");
    }

    private static void registerShaped()
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemInits.CONDUCTIVE_EMERALD, 1),
                "aba",
                "bcb",
                "aba",
                'a', "dustRedstone",
                'b', "gemLapis",
                'c', "gemEmerald"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemInits.CONDUCTIVE_DIAMOND, 1),
                "aba",
                "bcb",
                "aba",
                'a', "dustRedstone",
                'b', "gemLapis",
                'c', "gemDiamond"));



        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockInits.CHUNK_LOADER, 1),
                "aba",
                "bcb",
                "aba",
                'a', "ingotIron",
                'b', "blockGlassColorless",
                'c', new ItemStack(ItemInits.DIAMOND_CORE)));
        Log.info("=========================================================> Registered Shaped Recipes");
    }

    private static void registerShapeless()
    {
        Log.info("=========================================================> Registered Shapeless Recipes");
    }

    private static void registerSmelting()
    {
        GameRegistry.addSmelting(ItemInits.CONDUCTIVE_EMERALD, new ItemStack(ItemInits.LIVING_CAGE, 1), 2f);
        GameRegistry.addSmelting(ItemInits.CONDUCTIVE_DIAMOND, new ItemStack(ItemInits.DIAMOND_CORE, 1), 2f);
        Log.info("=========================================================> Registered Smelting Recipes");
    }
}
