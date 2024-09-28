package tfar.worldprestige.network;

import net.minecraft.resources.ResourceLocation;
import tfar.worldprestige.WorldPrestige;
import tfar.worldprestige.network.client.S2CPrestigeScreenPacket;
import tfar.worldprestige.network.server.C2SSelectPrestigePowerPacket;
import tfar.worldprestige.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerServerPacket(C2SSelectPrestigePowerPacket.class, C2SSelectPrestigePowerPacket::new);
        Services.PLATFORM.registerClientPacket(S2CPrestigeScreenPacket.class, S2CPrestigeScreenPacket::new);

    }

    public static ResourceLocation packet(Class<?> clazz) {
        return WorldPrestige.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
