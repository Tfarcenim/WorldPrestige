package tfar.worldprestige.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import tfar.worldprestige.WorldPrestige;
import tfar.worldprestige.world.PrestigeData;
import tfar.worldprestige.world.PrestigePower;
import tfar.worldprestige.world.PrestigePowers;

public class C2SSelectPrestigePowerPacket implements C2SModPacket {

    public final String id;

    public C2SSelectPrestigePowerPacket(FriendlyByteBuf buf) {
        id = buf.readUtf();
    }

    public C2SSelectPrestigePowerPacket(String id) {
        this.id = id;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        MinecraftServer server = player.server;

        PrestigePower power = PrestigePowers.powers.get(id);

        if (power != null) {

            PrestigeData data = PrestigeData.getDefaultInstance(server);
            if (data != null) {
                if (data.isReady()) {
                    data.activate(power);
                } else {
                    WorldPrestige.LOG.warn("Unexpected prestige power packet from {}", player.getGameProfile().getName());
                }
            }
        } else {
            WorldPrestige.LOG.warn("Invalid prestige power packet from {}: {}", player.getGameProfile().getName(),id);
        }
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(id);
    }
}
