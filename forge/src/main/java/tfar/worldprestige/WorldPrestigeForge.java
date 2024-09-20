package tfar.worldprestige;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tfar.worldprestige.datagen.ModDatagen;
import tfar.worldprestige.world.PrestigeData;

@Mod(WorldPrestige.MOD_ID)
public class WorldPrestigeForge {
    
    public WorldPrestigeForge() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER,TomlConfig.SERVER_SPEC);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        bus.addListener(ModDatagen::gather);
        MinecraftForge.EVENT_BUS.addListener(this::checkAdvancements);
        MinecraftForge.EVENT_BUS.addListener(this::advancementLoad);
        MinecraftForge.EVENT_BUS.addListener(this::onSpawn);
        // Use Forge to bootstrap the Common mod.
        WorldPrestige.init();
    }

    void onSpawn(MobSpawnEvent.FinalizeSpawn event) {
        MinecraftServer server = event.getEntity().getServer();
        PrestigeData prestigeData = PrestigeData.getDefaultInstance(server);
        if (prestigeData != null && prestigeData.isBossReady()) {
            if ()
        }
    }

    void checkAdvancements(AdvancementEvent.AdvancementEarnEvent event) {
        WorldPrestige.checkAdvancements((ServerPlayer) event.getEntity());
    }

    void advancementLoad(OnDatapackSyncEvent event) {
        WorldPrestige.loadAdvancements(event.getPlayerList().getServer());
    }

}