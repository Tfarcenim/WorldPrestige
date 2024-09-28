package tfar.worldprestige;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class BossPrestigeEvent extends PlayerEvent {
    public BossPrestigeEvent(Player player) {
        super(player);
    }

    @Override
    public ServerPlayer getEntity() {
        return (ServerPlayer) super.getEntity();
    }
}
