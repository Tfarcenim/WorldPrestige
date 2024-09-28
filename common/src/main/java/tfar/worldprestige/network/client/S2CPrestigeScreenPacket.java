package tfar.worldprestige.network.client;

import net.minecraft.network.FriendlyByteBuf;
import tfar.worldprestige.client.ClientPacketHandler;

public class S2CPrestigeScreenPacket implements S2CModPacket {

    public S2CPrestigeScreenPacket() {
    }

    public S2CPrestigeScreenPacket(FriendlyByteBuf buf) {
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.handlePrestigeScreenPacket(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
    }
}
