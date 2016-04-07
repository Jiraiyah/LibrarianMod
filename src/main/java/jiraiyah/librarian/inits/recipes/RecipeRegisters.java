package jiraiyah.librarian.inits.recipes;

import jiraiyah.librarian.utilities.Log;

public class RecipeRegisters
{
    public static void register()
    {
        registerShaped();
        registerShapeless();
        Log.info("=========================================================> Registered Recipes");
    }

    private static void registerShaped()
    {
        Log.info("=========================================================> Registered Shaped Recipes");
    }

    private static void registerShapeless()
    {
        Log.info("=========================================================> Registered Shapeless Recipes");
    }
}
