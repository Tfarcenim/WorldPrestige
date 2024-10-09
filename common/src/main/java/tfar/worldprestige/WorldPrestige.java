package tfar.worldprestige;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import tfar.worldprestige.network.PacketHandler;
import tfar.worldprestige.platform.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.worldprestige.world.PrestigeData;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class WorldPrestige {

    public static final String MOD_ID = "worldprestige";
    public static final String MOD_NAME = "WorldPrestige";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final String TRIGGERS_PRESTIGE = id("trigger_prestige").toString();

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        PacketHandler.registerPackets();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

    public static Set<Advancement> cachedAdvancements;

    public static void loadAdvancements(MinecraftServer server) {
        cachedAdvancements = new HashSet<>();
        for (String s : Services.PLATFORM.getConfig().getRequiredAdvancements()) {
            Advancement advancement = server.getAdvancements().getAdvancement(new ResourceLocation(s));
            if (advancement != null) {
                cachedAdvancements.add(advancement);
            } else {
                LOG.warn("advancement {} not found", s);
            }
        }
    }

    public static void checkAdvancements(ServerPlayer player) {
        MinecraftServer server = player.server;
        PrestigeData prestigeData = PrestigeData.getDefaultInstance(server);
        if (prestigeData == null || !prestigeData.isBossReady()) {
            boolean allComplete = true;
            for (Advancement advancement : cachedAdvancements) {
                if (!player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    allComplete = false;
                    break;
                }
            }
            if (allComplete) {
                PrestigeData.getOrCreateDefaultInstance(server).setBossReady();
                player.sendSystemMessage(Component.literal("The final boss is ready to summon...").withStyle(ChatFormatting.ITALIC));
                Services.PLATFORM.postEvent(player);
            }
        }
    }

    //        nbt.putInt("SpawnX", this.xSpawn);
    //        nbt.putInt("SpawnY", this.ySpawn);
    //        nbt.putInt("SpawnZ", this.zSpawn);

    public static void modifyData(CompoundTag tag) {
        if (tag != null) {
            MinecraftServer server = Services.PLATFORM.getServer();
            if (server != null) {
                PrestigeData prestigeData = PrestigeData.getDefaultInstance(server);
                if (prestigeData != null && prestigeData.deleteEverything) {
                    Set<String> toRemove = remove();
                    for (String s : toRemove) {
                        tag.remove(s);
                    }
                    BlockPos newSpawn =createNewSpawn(tag.getInt("SpawnX"),tag.getInt("SpawnY"),tag.getInt("SpawnZ"));
                    tag.putInt("SpawnX",newSpawn.getX());
                    tag.putInt("SpawnY",newSpawn.getY());
                    tag.putInt("SpawnZ",newSpawn.getZ());
                }
            }
        }
    }

    public static Set<String> remove() {
        Set<String> strings = new HashSet<>();
        strings.add("Player");
        return strings;
    }

    public static BlockPos createNewSpawn(int x,int y,int z) {
        Random random = new Random();
        int r = 100000;
        int newX = random.nextInt(2 * r) - r + x;
        int newZ = random.nextInt(2 * r) - r + z;
        return new BlockPos(newX,y,newZ);
    }

}