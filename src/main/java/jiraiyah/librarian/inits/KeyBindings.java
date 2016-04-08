package jiraiyah.librarian.inits;

import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

//http://minecraft.gamepedia.com/Key_codes
// for config :
//https://github.com/maruohon/itemscroller/blob/master/src/main/java/fi/dy/masa/itemscroller/config/Configs.java
// onConfigChangedEvent <-- client side
//https://github.com/maruohon/itemscroller/blob/master/src/main/java/fi/dy/masa/itemscroller/ItemScroller.java#L32
public class KeyBindings
{
    public static KeyBinding VILLAGE_DATA;

    public static void register()
    {
        VILLAGE_DATA = new KeyBinding("key.villagedata", Keyboard.KEY_V, "key.categories." + Reference.MOD_ID.toLowerCase());

        ClientRegistry.registerKeyBinding(VILLAGE_DATA);

        Log.info("=========================================================> Registered Key Bindings");
    }
}
