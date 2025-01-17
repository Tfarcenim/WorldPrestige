package tfar.worldprestige.world;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public abstract class PrestigePower {

    private final String id;

    public PrestigePower(String id) {
        this.id = id;
    }

    public abstract void apply(Player player,int level);
    public abstract void remove(Player player);

    public boolean canStack() {
        return true;
    }

    public int getMaxLevel() {
        return 10000000;
    }

    public String getId() {
        return id;
    }

    public MutableComponent translationKey() {
        return Component.translatable("worldprestige.prestige_power."+id);
    }
}
