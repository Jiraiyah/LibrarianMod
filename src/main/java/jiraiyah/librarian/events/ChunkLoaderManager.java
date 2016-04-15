package jiraiyah.librarian.events;

import com.google.common.collect.ImmutableSetMultimap;
import jiraiyah.librarian.Librarian;
import jiraiyah.librarian.infrastructure.*;
import jiraiyah.librarian.interfaces.IChunkLoader;
import jiraiyah.librarian.utilities.CommonUtils;
import jiraiyah.librarian.utilities.ServerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.server.FMLServerHandler;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ChunkLoaderManager
{
    public static boolean reloadDimensions = false;
    private static boolean opInteract = false;
    private static int cleanupTicks;
    private static int maxChunks;
    private static int awayTimeout;
    private static HashMap<Object, ModContainer> mods = new HashMap<>();

    private static boolean loaded = false;
    private static HashMap<String, PlayerOrganiser> playerOrganisers;
    private static HashMap<Object, ModOrganiser> modOrganisers;
    private static HashMap<String, Long> loginTimes;
    private static File saveDir;

    public static void registerMod(Object mod)
    {
        ModContainer container = Loader.instance().getModObjectList().inverse().get(mod);
        if (container == null)
            throw new NullPointerException("Mod container not found for: " + mod);
        mods.put(mod, container);
        ForgeChunkManager.setForcedChunkLoadingCallback(mod, new DummyLoadingCallback());
    }

    public static void loadWorld(WorldServer world)
    {
        ReviveChange.DimensionRevive.list.add(world);
    }

    public static World getWorld(int dim, boolean create) {
        if (create)
            return FMLServerHandler.instance().getServer().worldServerForDimension(dim);
        return DimensionManager.getWorld(dim);
    }

    public static void load(WorldServer world)
    {
        if (loaded)
            return;

        loaded = true;

        playerOrganisers = new HashMap<>();
        modOrganisers = new HashMap<>();
        loginTimes = new HashMap<>();
        ReviveChange.load();

        try {
            saveDir = new File(DimensionManager.getCurrentSaveRootDirectory(), "chickenchunks");
            if (!saveDir.exists())
                saveDir.mkdirs();
            loadPlayerChunks();
            loadModChunks();
            loadLoginTimes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadLoginTimes() throws IOException
    {
        File saveFile = new File(saveDir, "loginTimes.dat");
        if (!saveFile.exists())
            return;

        DataInputStream datain = new DataInputStream(new FileInputStream(saveFile));
        try {
            int entries = datain.readInt();
            for (int i = 0; i < entries; i++)
                loginTimes.put(datain.readUTF(), datain.readLong());
        } catch (IOException e) {
            LogManager.getLogger("ChickenChunks").error("Error reading loginTimes.dat", e);
        }
        datain.close();

    }

    private static void loadModChunks() throws IOException
    {
        for (Map.Entry<Object, ModContainer> entry : mods.entrySet()) {
            File saveFile = new File(saveDir, entry.getValue().getModId() + ".dat");
            if (!saveFile.exists())
                return;

            DataInputStream datain = new DataInputStream(new FileInputStream(saveFile));
            ModOrganiser organiser = getModOrganiser(entry.getKey());
            ReviveChange.ModRevive.list.add(organiser);

            organiser.load(datain);
            datain.close();
        }
    }

    private static void loadPlayerChunks() throws IOException
    {
        File saveFile = new File(saveDir, "players.dat");
        if (!saveFile.exists())
            return;

        DataInputStream datain = new DataInputStream(new FileInputStream(saveFile));
        int organisers = datain.readInt();
        for (int i = 0; i < organisers; i++)
        {
            String username = datain.readUTF();
            PlayerOrganiser organiser = getPlayerOrganiser(username);
            organiser.setDormant();
            if (allowOffline(username) && loggedInRecently(username))
                ReviveChange.PlayerRevive.list.add(organiser);

            organiser.load(datain);
        }
        datain.close();
    }

    private static boolean loggedInRecently(String username)
    {
        if (awayTimeout == 0)
            return true;

        Long lastLogin = loginTimes.get(username);
        return lastLogin != null && (System.currentTimeMillis() - lastLogin) / 60000L < awayTimeout;

    }

    public static int getPlayerChunkLimit(String username)
    {
        ConfigTag config = Librarian.config.getTag("players");
        if (config.containsTag(username)) {
            int ret = config.getTag(username).getIntValue(0);
            if (ret != 0)
                return ret;
        }

        if (ServerUtils.isPlayerOP(username)) {
            int ret = config.getTag("OP").getIntValue(0);
            if (ret != 0)
                return ret;
        }

        return config.getTag("DEFAULT").getIntValue(5000);
    }

    public static boolean allowOffline(String username)
    {
        ConfigTag config = Librarian.config.getTag("allowoffline");
        if (config.containsTag(username))
            return config.getTag(username).getBooleanValue(true);

        if (ServerUtils.isPlayerOP(username))
            return config.getTag("OP").getBooleanValue(true);

        return config.getTag("DEFAULT").getBooleanValue(true);
    }

    public static boolean allowChunkViewer(String username)
    {
        ConfigTag config = Librarian.config.getTag("allowchunkviewer");
        if (config.containsTag(username))
            return config.getTag(username).getBooleanValue(true);

        if (ServerUtils.isPlayerOP(username))
            return config.getTag("OP").getBooleanValue(true);

        return config.getTag("DEFAULT").getBooleanValue(true);
    }

    public static void initConfig(ConfigFile config)
    {
        config.getTag("players").setPosition(0).useBraces().setComment("Per player chunk limiting. Values ignored if 0.:Simply add <username>=<value>");
        config.getTag("players.DEFAULT").setComment("Forge gives everyone 12500 by default").getIntValue(5000);
        config.getTag("players.OP").setComment("For server op's only.").getIntValue(5000);
        config.getTag("allowoffline").setPosition(1).useBraces().setComment("If set to false, players will have to be logged in for their chunkloaders to work.:Simply add <username>=<true|false>");
        config.getTag("allowoffline.DEFAULT").getBooleanValue(true);
        config.getTag("allowoffline.OP").getBooleanValue(true);
        config.getTag("allowchunkviewer").setPosition(2).useBraces().setComment("Set to false to deny a player access to the chunk viewer");
        config.getTag("allowchunkviewer.DEFAULT").getBooleanValue(true);
        config.getTag("allowchunkviewer.OP").getBooleanValue(true);
        if (!FMLCommonHandler.instance().getModName().contains("mcpc"))
            cleanupTicks = config.getTag("cleanuptime")
                    .setComment("The number of ticks to wait between attempting to unload orphaned chunks")
                    .getIntValue(1200);
        reloadDimensions = config.getTag("reload-dimensions")
                .setComment("Set to false to disable the automatic reloading of mystcraft dimensions on server restart")
                .getBooleanValue(true);
        opInteract = config.getTag("op-interact")
                .setComment("Enabling this lets OPs alter other player's chunkloaders. WARNING: If you change a chunkloader, you have no idea what may break/explode by not being chunkloaded.")
                .getBooleanValue(false);
        maxChunks = config.getTag("maxchunks")
                .setComment("The maximum number of chunks per chunkloader")
                .getIntValue(400);
        awayTimeout = config.getTag("awayTimeout")
                .setComment("The number of minutes since last login within which chunks from a player will remain active, 0 for infinite.")
                .getIntValue(0);
    }

    public static void addChunkLoader(IChunkLoader loader)
    {
        int dim = CommonUtils.getDimension(loader.getWorld());
        ChunkLoaderOrganiser organiser = getOrganiser(loader);
        if (organiser.canForceNewChunks(dim, loader.getChunks()))
            organiser.addChunkLoader(loader);
        else
            loader.deactivate();
    }

    private static ChunkLoaderOrganiser getOrganiser(IChunkLoader loader)
    {
        String owner = loader.getOwner();
        return owner == null ? getModOrganiser(loader.getMod()) : getPlayerOrganiser(owner);
    }

    public static void remChunkLoader(IChunkLoader loader)
    {
        getOrganiser(loader).remChunkLoader(loader);
    }

    public static void updateLoader(IChunkLoader loader)
    {
        getOrganiser(loader).updateChunkLoader(loader);
    }

    public static boolean canLoaderAdd(IChunkLoader loader, Collection<ChunkCoordIntPair> chunks)
    {
        String owner = loader.getOwner();
        int dim = CommonUtils.getDimension(loader.getWorld());
        if (owner != null)
            return getPlayerOrganiser(owner).canForceNewChunks(dim, chunks);

        return false;
    }

    private static PlayerOrganiser getPlayerOrganiser(String username)
    {
        PlayerOrganiser organiser = playerOrganisers.get(username);
        if (organiser == null)
            playerOrganisers.put(username, organiser = new PlayerOrganiser(username));
        return organiser;
    }

    private static ModOrganiser getModOrganiser(Object mod)
    {
        ModOrganiser organiser = modOrganisers.get(mod);
        if (organiser == null) {
            ModContainer container = mods.get(mod);
            if (container == null)
                throw new NullPointerException("Mod not registered with chickenchunks: " + mod);
            modOrganisers.put(mod, organiser = new ModOrganiser(mod, container));
        }
        return organiser;
    }

    public static void serverShutdown()
    {
        loaded = false;
    }

    public static void save(WorldServer world)
    {
        try {
            if (PlayerOrganiser.dirty) {
                File saveFile = new File(saveDir, "players.dat");
                if (!saveFile.exists())
                    saveFile.createNewFile();
                DataOutputStream dataout = new DataOutputStream(new FileOutputStream(saveFile));
                dataout.writeInt(playerOrganisers.size());
                for (PlayerOrganiser organiser : playerOrganisers.values()) {
                    dataout.writeUTF(organiser.username);
                    organiser.save(dataout);
                }
                dataout.close();
                PlayerOrganiser.dirty = false;
            }


            for (ModOrganiser organiser : modOrganisers.values()) {
                if (organiser.dirty) {
                    File saveFile = new File(saveDir, organiser.container.getModId() + ".dat");
                    if (!saveFile.exists())
                        saveFile.createNewFile();

                    DataOutputStream dataout = new DataOutputStream(new FileOutputStream(saveFile));
                    organiser.save(dataout);
                    dataout.close();
                    organiser.dirty = false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void cleanChunks(WorldServer world) {
        int dim = CommonUtils.getDimension(world);
        int viewdist = ServerUtils.mc().getPlayerList().getViewDistance();

        HashSet<ChunkCoordIntPair> loadedChunks = new HashSet<>();
        for (EntityPlayer player : ServerUtils.getPlayersInDimension(dim)) {
            int playerChunkX = (int) player.posX >> 4;
            int playerChunkZ = (int) player.posZ >> 4;

            for (int cx = playerChunkX - viewdist; cx <= playerChunkX + viewdist; cx++)
                for (int cz = playerChunkZ - viewdist; cz <= playerChunkZ + viewdist; cz++)
                    loadedChunks.add(new ChunkCoordIntPair(cx, cz));
        }

        ImmutableSetMultimap<ChunkCoordIntPair, Ticket> persistantChunks = world.getPersistentChunks();
        PlayerManager manager = world.getPlayerChunkMap();//.getPlayerManager();

        for (Chunk chunk : world.getChunkProvider().loadedChunks)
        {
            ChunkCoordIntPair coord = chunk.getChunkCoordIntPair();
            if (!loadedChunks.contains(coord) && !persistantChunks.containsKey(coord) && world.getChunkProvider().chunkExists(coord.chunkXPos, coord.chunkZPos))
            {
                PlayerManager.PlayerInstance instance = manager.getEntry(coord.chunkXPos, coord.chunkZPos);//.getOrCreateChunkWatcher(coord.chunkXPos, coord.chunkZPos, false);
                if (instance == null)
                {
                    world.getChunkProvider().dropChunk(coord.chunkXPos, coord.chunkZPos);
                }
                else
                {
                    while (instance.players.size() > 0)
                        instance.removePlayer(instance.players.get(0));
                }
            }
        }

        if (ServerUtils.getPlayersInDimension(dim).isEmpty() && world.getPersistentChunks().isEmpty() && !world.provider.getDimensionType().shouldLoadSpawn())
        {
            DimensionManager.unloadWorld(dim);
        }
    }

    public static void tickEnd(WorldServer world) {
        if (world.getWorldTime() % 1200 == 0)
            updateLoginTimes();

        if (cleanupTicks > 0 && world.getWorldTime() % cleanupTicks == 0)
            cleanChunks(world);

        tickDownUnloads();
        revivePlayerLoaders();
    }

    private static void updateLoginTimes()
    {
        long time = System.currentTimeMillis();
        for (EntityPlayer player : ServerUtils.getPlayers())
            loginTimes.put(player.getName(), time);

        try {
            File saveFile = new File(saveDir, "loginTimes.dat");
            if (!saveFile.exists())
                saveFile.createNewFile();

            DataOutputStream dataout = new DataOutputStream(new FileOutputStream(saveFile));
            dataout.writeInt(loginTimes.size());
            for (Map.Entry<String, Long> entry : loginTimes.entrySet()) {
                dataout.writeUTF(entry.getKey());
                dataout.writeLong(entry.getValue());
            }
            dataout.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (PlayerOrganiser organiser : playerOrganisers.values())
            if (!organiser.isDormant() && !loggedInRecently(organiser.username))
                ReviveChange.PlayerDevive.list.add(organiser);
    }

    private static void tickDownUnloads()
    {
        for (Map.Entry<String, PlayerOrganiser> entry : playerOrganisers.entrySet())
            entry.getValue().tickDownUnloads();

        for (Map.Entry<Object, ModOrganiser> entry : modOrganisers.entrySet())
            entry.getValue().tickDownUnloads();
    }

    private static void revivePlayerLoaders()
    {
        for (Object organiser : ReviveChange.PlayerRevive.list)
            ((PlayerOrganiser) organiser).revive();
        ReviveChange.PlayerRevive.list.clear();

        for (Object organiser : ReviveChange.ModRevive.list)
            ((ModOrganiser) organiser).revive();
        ReviveChange.ModRevive.list.clear();

        for (Object world : ReviveChange.DimensionRevive.list)
            for (PlayerOrganiser organiser : playerOrganisers.values())
                organiser.revive((World) world);
        ReviveChange.DimensionRevive.list.clear();

        for (Object organiser : ReviveChange.PlayerDevive.list)
            ((PlayerOrganiser) organiser).devive();
        ReviveChange.PlayerDevive.list.clear();
    }

    public static void playerLogin(String username)
    {
        loginTimes.put(username, System.currentTimeMillis());
        ReviveChange.PlayerRevive.list.add(getPlayerOrganiser(username));
    }

    public static void playerLogout(String username)
    {
        if (!allowOffline(username))
            ReviveChange.PlayerDevive.list.add(getPlayerOrganiser(username));
    }

    public static int maxChunksPerLoader()
    {
        return maxChunks;
    }

    public static boolean opInteract()
    {
        return opInteract;
    }

    public static void unloadWorld(World world) {
        int dim = CommonUtils.getDimension(world);
        for (TicketManager mgr : playerOrganisers.values())
            mgr.unloadDimension(dim);
        for (TicketManager mgr : modOrganisers.values())
            mgr.unloadDimension(dim);
    }
}