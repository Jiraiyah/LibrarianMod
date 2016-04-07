package jiraiyah.librarian.inits;

import jiraiyah.librarian.references.Reference;
import jiraiyah.librarian.utilities.Log;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings
{
    public static KeyBinding VILLAGE_DATA;

    public static void register()
    {
        VILLAGE_DATA = new KeyBinding("key.villagedata", Keyboard.KEY_O, "key.categories." + Reference.MOD_ID.toLowerCase());

        ClientRegistry.registerKeyBinding(VILLAGE_DATA);

        Log.info("=========================================================> Registered Key Bindings");
    }
}
