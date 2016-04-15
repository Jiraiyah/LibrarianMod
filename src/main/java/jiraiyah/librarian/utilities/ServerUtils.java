package jiraiyah.librarian.utilities;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.Locale;

public class ServerUtils
{
    public static MinecraftServer mc()
    {
        return FMLServerHandler.instance().getServer();
    }

    public static boolean isPlayerOP(String username) {
        GameProfile prof = getGameProfile(username);
        return prof != null && mc().getPlayerList().canSendCommands(prof);
    }

    public static GameProfile getGameProfile(String username) {
        EntityPlayer player = getPlayer(username);
        if(player != null)
            return player.getGameProfile();
        username = username.toLowerCase(Locale.ROOT);
        return mc().getPlayerProfileCache().getGameProfileForUsername(username);
    }

    public static EntityPlayerMP getPlayer(String playername) {
        return mc().getPlayerList().getPlayerByUsername(playername);
    }
}
