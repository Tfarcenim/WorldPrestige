package tfar.worldprestige.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;
import tfar.worldprestige.WorldPrestige;

public class PrestigeData extends SavedData {

    private boolean bossReady;
    final ServerLevel level;

    public PrestigeData(ServerLevel level) {
        this.level = level;
    }

    @Nullable
    public static PrestigeData getInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(compoundTag -> loadStatic(compoundTag, serverLevel), name(serverLevel));
    }

    public void setBossReady() {
        bossReady = true;
        setDirty();
    }

    public boolean isBossReady() {
        return bossReady;
    }

    public static PrestigeData getOrCreateInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag,serverLevel),
                        () -> new PrestigeData(serverLevel),name(serverLevel));
    }

    private static String name(ServerLevel level) {
        return  WorldPrestige.MOD_ID+"_"+level.dimension().location().toString().replace(':','.');
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
        return compoundTag;
    }

    public void load(CompoundTag tag,ServerLevel level) {
        bossReady = tag.getBoolean("boss_ready");
    }
}
