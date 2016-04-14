package jiraiyah.librarian.inits;

import jiraiyah.librarian.utilities.Log;

//http://minecraft.gamepedia.com/Key_codes
// for config :
//https://github.com/maruohon/itemscroller/blob/master/src/main/java/fi/dy/masa/itemscroller/config/Configs.java
// onConfigChangedEvent <-- client side
//https://github.com/maruohon/itemscroller/blob/master/src/main/java/fi/dy/masa/itemscroller/ItemScroller.java#L32
public class KeyBindings
{

    public static void register()
    {
        //VILLAGE_DATA_DOORS = new KeyBinding(Reference.MOD_ID.toLowerCase() + ".key.villagedatadoors", KeyConflictContext.IN_GAME, KeyModifier.CONTROL, Keyboard.KEY_1, Reference.MOD_ID.toLowerCase() + ".key.categories");
        //ClientRegistry.registerKeyBinding(VILLAGE_DATA_SPHERE);

        Log.info("=========================================================> Registered Key Bindings");
    }
}
