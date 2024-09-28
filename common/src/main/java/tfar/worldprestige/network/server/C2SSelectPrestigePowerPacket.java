package tfar.worldprestige.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import tfar.worldprestige.WorldPrestige;
import tfar.worldprestige.world.PrestigeData;

public class C2SSelectPrestigePowerPacket implements C2SModPacket {

    public final int id;

    public C2SSelectPrestigePowerPacket(FriendlyByteBuf buf) {
        id = buf.readInt();
    }

    public C2SSelectPrestigePowerPacket(int id) {
        this.id = id;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        MinecraftServer server = player.server;
        PrestigeData data = PrestigeData.getDefaultInstance(server);
        if (data != null) {
            if (data.isReady()) {
                data.activate();
            } else {
                WorldPrestige.LOG.warn("Unexpected prestige power packet from {}",player.getGameProfile().getName());
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(id);
    }
}
