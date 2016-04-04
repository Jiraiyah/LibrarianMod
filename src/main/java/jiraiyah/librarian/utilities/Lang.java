package jiraiyah.librarian.utilities;

import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.TreeMap;

public class Lang
{
    private static boolean DEOBF_DEV_ENV = true;
    private static final TreeMap<String, String> lang = DEOBF_DEV_ENV ? new TreeMap() : null;
    private static final HashMap<String, String> textKey = new HashMap();
    private static final int MAX_KEY_LEN = 32;
    private static final TObjectIntHashMap<String> numRandomEntries = new TObjectIntHashMap();

    public static String translate(String text)
    {
        return translatePrefix(text);
    }

    public static String translatePrefix(String text)
    {
        String key = getKey(text);
        return translate(key, text);
    }

    public static String getKey(String text)
    {
        String key = (String)textKey.get(text);
        if (key == null)
        {
            key = makeKey(text);
            textKey.put(text, key);
            if (DEOBF_DEV_ENV) {
                translate(key, text);
            }
        }
        return key;
    }

    private static String makeKey(String text)
    {
        String t = stripText(text);
        String key = "extrautils2.text." + t;
        return key;
    }

    @Nonnull
    public static String stripText(String text)
    {
        String t = text.replaceAll("([^A-Za-z\\s])", "").trim();
        t = t.replaceAll("\\s+", ".").toLowerCase();
        if (t.length() > 32)
        {
            int n = t.indexOf('.', 32);
            if (n != -1) {
                t = t.substring(0, n);
            }
        }
        return t;
    }

    public static String translate(String key, String _default)
    {
        if (I18n.canTranslate(key)) {
            return I18n.translateToLocal(key);
        }
        initKey(key, _default);
        return _default;
    }

    public static String initKey(String key, String _default)
    {
        if ((DEOBF_DEV_ENV) && (FMLLaunchHandler.side() == Side.CLIENT) &&
                (!_default.equals(lang.get(key))))
        {
            lang.put(key, _default);
            createMissedFile();
        }
        return key;
    }

    public static void createMissedFile()
    {
        String t;
        PrintWriter out = null;
        try
        {
            try
            {
                File file = getFile();
                if ((file.getParentFile() != null) &&
                        (file.getParentFile().mkdirs())) {
                    Log.info("Making Translation Directory");
                }
                out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                t = null;
                for (Map.Entry<String, String> entry : lang.entrySet())
                {
                    int i = ((String)entry.getKey()).indexOf('.');
                    if (i < 0) {
                        i = 1;
                    }
                    String s = ((String)entry.getKey()).substring(0, i);
                    if ((t != null) &&
                            (!t.equals(s))) {
                        out.println("");
                    }
                    t = s;

                    out.println((String)entry.getKey() + "=" + (String)entry.getValue());
                }
            }
            finally
            {
                if (out != null) {
                    out.close();
                }
            }
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }
    }

    private static File getFile()
    {
        return new File(new File(new File("."), "debug_text"), "missed_en_US.lang");
    }

    public static String translateArgs(boolean dummy, String key, String _default, Object... args)
    {
        String translate = translate(key, _default);
        try
        {
            return String.format(translate, args);
        }
        catch (IllegalFormatException err)
        {
            throw new RuntimeException("Message: \"" + _default + "\" with key : \"" + key + "\" and translation: \"" + translate + "\"", err);
        }
    }

    public static String translateArgs(String message, Object... args)
    {
        String translate = translate(message);
        try
        {
            return String.format(translate, args);
        }
        catch (IllegalFormatException err)
        {
            throw new RuntimeException("Message: \"" + message + "\" with key : \"" + getKey(message) + "\" and translation: \"" + translate + "\"", err);
        }
    }

    public static String getItemName(Block block)
    {
        return getItemName(new ItemStack(block));
    }

    public static String getItemName(Item item)
    {
        return getItemName(new ItemStack(item));
    }

    public static String getItemName(ItemStack stack)
    {
        return stack.getDisplayName();
    }

    public static String random(String key)
    {
        return random(key, Random.rand);
    }

    public static String random(String key, java.util.Random rand)
    {
        int n = getNumSelections(key);
        if (n == 0) {
            return I18n.translateToLocal(key);
        }
        return I18n.translateToLocal(key + "." + rand.nextInt(n));
    }

    public static String random(String key, int index)
    {
        int n = getNumSelections(key);
        int i = Math.abs(index) % n;
        return I18n.translateToLocal(key + "." + i);
    }

    private static int getNumSelections(String key)
    {
        int i;
        if (numRandomEntries.containsKey(key))
        {
            i = numRandomEntries.get(key);
        }
        else
        {
            i = 0;
            while (I18n.canTranslate(key + "." + i)) {
                i++;
            }
            i++;
            numRandomEntries.put(key, i);
        }
        return i;
    }

    public static void init() {}
}

