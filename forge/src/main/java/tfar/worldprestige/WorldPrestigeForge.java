package tfar.worldprestige;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import tfar.worldprestige.datagen.ModDatagen;
import tfar.worldprestige.init.ModAttributes;
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
        bus.addListener(this::register);
        MinecraftForge.EVENT_BUS.addListener(this::checkAdvancements);
        MinecraftForge.EVENT_BUS.addListener(this::advancementLoad);
        MinecraftForge.EVENT_BUS.addListener(this::onSpawn);
        MinecraftForge.EVENT_BUS.addListener(this::onDeath);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::stopServer);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerClone);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLogin);
        MinecraftForge.EVENT_BUS.addListener(this::breakspeed);
        MinecraftForge.EVENT_BUS.addListener(this::onRightClick);

        if (Services.PLATFORM.isDevelopmentEnvironment()) {
            MinecraftForge.EVENT_BUS.addListener(this::addExampleSummon);
        }

        MinecraftForge.EVENT_BUS.addListener(this::addDefaultSummon);


        // Use Forge to bootstrap the Common mod.
        WorldPrestige.init();
    }

    void register(RegisterEvent event) {
        event.register(Registries.ATTRIBUTE,WorldPrestige.id("dig_speed"),() -> ModAttributes.DIG_SPEED);
    }

    void registerCommands(RegisterCommandsEvent event) {
        PrestigeCommands.register(event.getDispatcher());
    }

    void onRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        if (stack.hasTag() && stack.getTag().contains("summon")) {
            if (!player.level().isClientSide) {
                Compat.summonBoss(player);
                stack.shrink(1);
            }
        }
    }

    //called clientside
    void onDeath(LivingDeathEvent event) {
        LivingEntity target = event.getEntity();
        if (!target.level().isClientSide) {
            PrestigeData prestigeData = PrestigeData.getOrCreateDefaultInstance(target.getServer());
            if (target instanceof Mob targetMob) {
                if (((MobDuck) targetMob).triggersPrestige()) {
                    prestigeData.setReady(true);
                    prestigeData.setFightActive(false);
                    LivingEntity killCredit = targetMob.getKillCredit();
                    if (killCredit instanceof ServerPlayer player) {
                        Services.PLATFORM.sendToClient(new S2CPrestigeScreenPacket(prestigeData.counter), player);
                    }
                }
            } else if (target instanceof ServerPlayer player) {
                if (prestigeData.isFightActive()) {
                    ServerLevel serverLevel = player.serverLevel();
                    serverLevel.setDayTime(skipToMidnight(serverLevel.getDayTime()));
                    serverLevel.getServer().getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, serverLevel.getServer());
                }
            }
        }
    }

    void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        MinecraftServer server = event.getEntity().getServer();
        PrestigeData prestigeData = PrestigeData.getOrCreateDefaultInstance(server);
        prestigeData.applyPowers( (ServerPlayer) event.getEntity());
    }

    //caution, called on both sides
    void breakspeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        double boost = player.getAttributeValue(ModAttributes.DIG_SPEED);
        event.setNewSpeed((float) (event.getNewSpeed() + boost));
    }

    void onPlayerClone(PlayerEvent.Clone event) {
        MinecraftServer server = event.getEntity().getServer();
        PrestigeData prestigeData = PrestigeData.getDefaultInstance(server);
        if (prestigeData != null) {
            prestigeData.applyPowers( (ServerPlayer) event.getEntity());
        }
    }

    public static long skipToMidnight(long time) {
        long timeOfDay = time % ServerLevel.TICKS_PER_DAY;
        long day = time = ServerLevel.TICKS_PER_DAY;
        return day * ServerLevel.TICKS_PER_DAY + 18000;
    }

    void onSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob mob = event.getEntity();
        MinecraftServer server = event.getEntity().getServer();
        PrestigeData prestigeData = PrestigeData.getDefaultInstance(server);
        if (prestigeData != null && prestigeData.isBossReady()) {
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

    void addDefaultSummon(BossPrestigeEvent event) {
        if (Services.PLATFORM.getConfig().useDefaultPrestige()) {
            Compat.spawnBoss(event.getEntity());
        }
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