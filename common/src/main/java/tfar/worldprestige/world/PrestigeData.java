package tfar.worldprestige.world;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jetbrains.annotations.Nullable;
import tfar.worldprestige.WorldPrestige;
import tfar.worldprestige.platform.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class PrestigeData extends SavedData {

    private boolean bossReady;
    private boolean fightActive;
    private boolean ready;
    public int counter;
    final ServerLevel level;
    public boolean deleteEverything;

    public long startTime;

    private final Object2IntMap<PrestigePower> powers = new Object2IntOpenHashMap<>();

    public PrestigeData(ServerLevel level) {
        this.level = level;
    }

    @Nullable
    public static PrestigeData getInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(compoundTag -> loadStatic(compoundTag, serverLevel), name(serverLevel));
    }

    public void applyPowers(ServerPlayer newPlayer) {
        for (Object2IntMap.Entry<PrestigePower> entry : powers.object2IntEntrySet()) {
            entry.getKey().apply(newPlayer,entry.getIntValue());
        }
    }

    public void setBossReady() {
        setBossReady(true);
    }

    public void setBossReady(boolean bossReady) {
        this.bossReady = bossReady;
        setDirty();
    }

    public boolean isBossReady() {
        return bossReady;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        setDirty();
    }

    public boolean isFightActive() {
        return fightActive;
    }

    public void setFightActive(boolean active) {
        this.fightActive = active;
        if (active) {
            startTime = level.getGameTime();
        }
        setDirty();
    }

    public void activate(PrestigePower power) {
        MinecraftServer server = level.getServer();

        addOrIncrement(power);

        server.getPlayerList().getPlayers().forEach(player -> player.connection.disconnect(Component.literal("Starting Prestige "+counter)));
        if (server.isDedicatedServer()) {
            server.halt(false);
        }
        deleteEverything = true;
        counter++;
    }


    public void tick() {
        long currentTime = level.getGameTime();
        if (isFightActive() && currentTime - startTime > 20 * 60 * 30) {
            onFailure();
        }
    }

    public void onFailure() {
        if (Services.PLATFORM.getConfig().deleteWorldOnFailure()) {
            MinecraftServer server = level.getServer();

            server.getPlayerList().getPlayers().forEach(player -> player.connection.disconnect(Component.literal("Resetting World due to failure")));
            if (server.isDedicatedServer()) {
                server.halt(false);
            }
            deleteEverything = true;
        }
    }

    public void addOrIncrement(PrestigePower power) {
        if (powers.containsKey(power)) {
            powers.put(power,1);
        } else {
            powers.put(power,powers.getInt(power) +1);
        }
    }

    public void deleteAlmostEverything(MinecraftServer server,LevelStorageSource.LevelStorageAccess access) throws IOException {
      //  access.checkLock();
        DimensionDataStorage dimensionDataStorage = server.overworld().getDataStorage();
        String name = null;
        for (Map.Entry<String,SavedData> dataEntry : dimensionDataStorage.cache.entrySet()) {
            String string = dataEntry.getKey();
            if (dataEntry.getValue() == this) {
                dimensionDataStorage.getDataFile(string);
                name = string;
            }
        }

        dimensionDataStorage.cache.clear();
        dimensionDataStorage.set(name,this);

        final Path lockFile = access.levelDirectory.lockFile();
        WorldPrestige.LOG.info("Deleting level {}", access.levelId);
        int $$1 = 1;

        while($$1 <= 5) {
           // LevelStorageSourceMixin.LOGGER.info("Attempt {}...", $$1);

            try {
                Files.walkFileTree(access.levelDirectory.path(), new SimpleFileVisitor<>() {
                    public FileVisitResult visitFile(Path path, BasicFileAttributes $$1) throws IOException {
                        if (!path.equals(lockFile) && !path.equals(access.levelDirectory.dataFile())) {
                            File f = path.toFile();
                            if (!path.toString().contains(WorldPrestige.MOD_ID)) {
                                WorldPrestige.LOG.info("Deleting {}", path);
                                Files.delete(path);
                            } else {
                                WorldPrestige.LOG.info("Skipping prestige data {}", path);
                            }
                        }

                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        if (Objects.equals(dir,getServerConfigPath(server))) {
                            return FileVisitResult.SKIP_SIBLINGS;
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    public FileVisitResult postVisitDirectory(Path path, @javax.annotation.Nullable IOException $$1) throws IOException {
                        if ($$1 != null) {
                            throw $$1;
                        } else {
                            if (path.equals(access.levelDirectory.path())) {
                                access.lock.close();
                                Files.deleteIfExists(lockFile);
                            }

                            if (!Objects.equals(access.levelDirectory.path(),path) && !path.equals(access.levelDirectory.resourcePath(new LevelResource("data")))) {
                                Files.delete(path);
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    }
                });
                break;
            } catch (IOException var6) {
                if ($$1 == 5) {
                    throw var6;
                }

               // LevelStorageSourceMixin.LOGGER.warn("Failed to delete {}", this.levelDirectory.path(), var6);

                try {
                    Thread.sleep(500L);
                } catch (InterruptedException var5) {
                }

                ++$$1;
            }
        }
    }

    private static Path getServerConfigPath(MinecraftServer server) {
        Path serverConfig = server.getWorldPath(new LevelResource("serverconfig"));
        return serverConfig;
    }

    public static PrestigeData getOrCreateInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag,serverLevel),
                        () -> new PrestigeData(serverLevel),name(serverLevel));
    }

    private static String name(ServerLevel level) {
        return  WorldPrestige.MOD_ID+"_"+level.dimension().location().toString().replace(':','_');
    }

    public static PrestigeData getDefaultInstance(MinecraftServer server) {
        return getInstance(server.overworld());
    }

    public static PrestigeData getOrCreateDefaultInstance(MinecraftServer server) {
        return getOrCreateInstance(server.overworld());
    }

    public static PrestigeData loadStatic(CompoundTag compoundTag,ServerLevel level) {
        PrestigeData id = new PrestigeData(level);
        id.load(compoundTag,level);
        return id;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putBoolean("boss_ready",bossReady);
        compoundTag.putBoolean("fight_active",fightActive);
        compoundTag.putBoolean("ready",ready);
        compoundTag.putInt("counter",counter);

        CompoundTag listTag = new CompoundTag();
        for (Object2IntMap.Entry<PrestigePower> entry : powers.object2IntEntrySet()) {
            listTag.putInt(entry.getKey().getId(),entry.getIntValue());
        }
        compoundTag.put("powers",listTag);

        return compoundTag;
    }

    public void load(CompoundTag tag,ServerLevel level) {
        bossReady = tag.getBoolean("boss_ready");
        fightActive = tag.getBoolean("fight_active");
        ready = tag.getBoolean("ready");
        counter = tag.getInt("counter");
        CompoundTag compoundTag = tag.getCompound("powers");
        for (String s :compoundTag.getAllKeys()) {
            powers.put(PrestigePowers.powers.get(s),compoundTag.getInt(s));
        }
    }

    public void resetPowers() {
        powers.forEach((power, integer) -> {
            for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                power.remove(player);
            }
        });
        powers.clear();
        setDirty();
    }
}
