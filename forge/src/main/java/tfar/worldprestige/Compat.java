package tfar.worldprestige;

import com.cerbon.bosses_of_mass_destruction.entity.BMDEntities;
import com.cerbon.bosses_of_mass_destruction.entity.custom.lich.LichEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Compat {

    public static void spawnBoss(ServerPlayer player) {
        ItemStack stack = Items.NETHER_STAR.getDefaultInstance();
        stack.setHoverName(Component.literal("Summons the final boss"));
        stack.getOrCreateTag().putBoolean("summon", true);
        player.addItem(stack);
    }

    public static void summonBoss(Player player) {
        LichEntity lichEntity = BMDEntities.LICH.get().spawn((ServerLevel) player.level(),player.blockPosition(), MobSpawnType.MOB_SUMMONED);
        ((MobDuck)lichEntity).setTriggersPrestige(true);
    }
}
