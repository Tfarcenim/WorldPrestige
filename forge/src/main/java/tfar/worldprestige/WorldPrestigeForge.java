package tfar.worldprestige;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tfar.worldprestige.datagen.ModDatagen;
import tfar.worldprestige.network.client.S2CPrestigeScreenPacket;
import tfar.worldprestige.platform.Services;
import tfar.worldprestige.world.PrestigeData;

import java.io.IOException;

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
        MinecraftForge.EVENT_BUS.addListener(this::onDeath);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::stopServer);

        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            MinecraftForge.EVENT_BUS.addListener(this::addExampleSummon);
        }

        // Use Forge to bootstrap the Common mod.
        WorldPrestige.init();
    }

    void registerCommands(RegisterCommandsEvent event) {
        PrestigeCommands.register(event.getDispatcher());
    }

    void onDeath(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        if (target instanceof Mob targetMob) {
            if (((MobDuck)targetMob).triggersPrestige()) {
                PrestigeData prestigeData = PrestigeData.getOrCreateDefaultInstance(target.getServer());
                prestigeData.setReady(true);
                prestigeData.setFightActive(false);
                LivingEntity killCredit = targetMob.getKillCredit();
                if (killCredit instanceof ServerPlayer player) {
                    Services.PLATFORM.sendToClient(new S2CPrestigeScreenPacket(),player);
                }
            }
        }
    }

    void onSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        MinecraftServer server = event.getEntity().getServer();
        PrestigeData prestigeData = PrestigeData.getDefaultInstance(server);
        if (prestigeData != null && prestigeData.isBossReady()) {
            ResourceLocation id = new ResourceLocation(Services.PLATFORM.getConfig().getBossEntityType());
            EntityType<?> type = event.getEntity().getType();
            if (((MobDuck) mob).triggersPrestige() && prestigeData.isBossReady()) {
                server.getPlayerList().broadcastSystemMessage(Component.literal("The final fight has begun"),true);
                prestigeData.setFightActive(true);
            }
        }
    }

    void addExampleSummon(BossPrestigeEvent event) {
        ItemStack stack = Items.EVOKER_SPAWN_EGG.getDefaultInstance();
        CompoundTag entityTag = new CompoundTag();
        entityTag.putBoolean(WorldPrestige.TRIGGERS_PRESTIGE,true);
        stack.getOrCreateTag().put(EntityType.ENTITY_TAG,entityTag);
        event.getEntity().addItem(stack);
    }

    void stopServer(ServerStoppedEvent event) {
        MinecraftServer server = event.getServer();
        PrestigeData prestigeData = PrestigeData.getOrCreateDefaultInstance(server);
        if (prestigeData.prepare) {
            try {
                prestigeData.deleteAlmostEverything(server, server.storageSource);
            } catch (IOException e) {
               // throw new RuntimeException(e);
            }
        }
    }

    void checkAdvancements(AdvancementEvent.AdvancementEarnEvent event) {
        WorldPrestige.checkAdvancements((ServerPlayer) event.getEntity());
    }

    void advancementLoad(OnDatapackSyncEvent event) {
        WorldPrestige.loadAdvancements(event.getPlayerList().getServer());
    }

}