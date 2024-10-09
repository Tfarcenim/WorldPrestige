package tfar.worldprestige;

import com.cerbon.bosses_of_mass_destruction.entity.BMDEntities;
import com.cerbon.bosses_of_mass_destruction.entity.custom.lich.LichEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class Compat {

    public static void spawnBoss(ServerPlayer player) {
        ItemStack stack = Items.NETHER_STAR.getDefaultInstance();
        stack.setHoverName(Component.literal("Summons the final boss"));
        stack.getOrCreateTag().putBoolean("summon", true);
        player.addItem(stack);
    }

    static final List<EntityType<?>> possible_bosses = new ArrayList<>();

    static {
        possible_bosses.add(BMDEntities.LICH.get());
        possible_bosses.add(BMDEntities.VOID_BLOSSOM.get());
    }

    public static void summonBoss(Player player) {
        int random = player.getRandom().nextInt(possible_bosses.size());
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(WorldPrestige.TRIGGERS_PRESTIGE,true);
        Entity lichEntity = possible_bosses.get(random).spawn((ServerLevel) player.level(),tag,null,player.blockPosition(), MobSpawnType.MOB_SUMMONED,false,false);
    }
}
