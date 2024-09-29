package tfar.worldprestige.network.client;

import net.minecraft.network.FriendlyByteBuf;
import tfar.worldprestige.client.ClientPacketHandler;

public class S2CPrestigeScreenPacket implements S2CModPacket {

    public final int counter;

    public S2CPrestigeScreenPacket(int counter) {
        this.counter = counter;
    }

    public S2CPrestigeScreenPacket(FriendlyByteBuf buf) {
        counter = buf.readInt();
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.handlePrestigeScreenPacket(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(counter);
    }
}
