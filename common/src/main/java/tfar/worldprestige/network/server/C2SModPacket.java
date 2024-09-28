package tfar.worldprestige.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.worldprestige.network.ModPacket;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
