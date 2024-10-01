package tfar.worldprestige.world;

import net.minecraft.world.entity.player.Player;
import tfar.worldprestige.PlayerDuck;

public class MiningSpeedPower extends PrestigePower {
    public MiningSpeedPower(String id) {
        super(id);
    }

    @Override
    public void apply(Player player, int level) {
        ((PlayerDuck)player).setMiningSpeedBoost(level);
    }

    @Override
    public void remove(Player player) {
        ((PlayerDuck)player).setMiningSpeedBoost(0);
    }
}
