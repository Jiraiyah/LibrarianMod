package jiraiyah.librarian.utilities;

import jiraiyah.librarian.Librarian;
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

import static org.apache.commons.io.FileUtils.getFile;

public class Lang
{

    private static final TreeMap<String, String> lang = Librarian.deobf_folder ? new TreeMap() : null;
    private static final HashMap<String, String> textKey = new HashMap();

    public static String translate(String text)
    {
        return translatePrefix(text);
    }

    public static String translatePrefix(String text)
    {
        String key = getKey(text);
        return translate(key, text);
    }

    public static String translate(String key, String _default)
    {
        if (I18n.canTranslate(key)) {
            return I18n.translateToLocal(key);
        }
        initKey(key, _default);
        return _default;
    }

    public static String getKey(String text)
    {
        String key = (String)textKey.get(text);
        if (key == null)
        {
            key = makeKey(text);
            textKey.put(text, key);
            if (Librarian.deobf_folder) {
                translate(key, text);
            }
        }
        return key;
    }

    public static String initKey(String key, String _default)
    {
        if ((Librarian.deobf_folder) && (FMLLaunchHandler.side() == Side.CLIENT) &&
                (!_default.equals(lang.get(key))))
        {
            lang.put(key, _default);
            createMissedFile();
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

    public static void createMissedFile()
    {
        PrintWriter out = null;
        String t;
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
                    int i = (entry.getKey()).indexOf('.');
                    if (i < 0) {
                        i = 1;
                    }
                    String s = (entry.getKey()).substring(0, i);
                    if ((t != null) &&
                            (!t.equals(s))) {
                        out.println("");
                    }
                    t = s;

                    out.println(entry.getKey() + "=" + entry.getValue());
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
}
