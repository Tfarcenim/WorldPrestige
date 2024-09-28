package tfar.worldprestige.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import tfar.worldprestige.PrestigeScreen;
import tfar.worldprestige.network.client.S2CPrestigeScreenPacket;

public class ClientPacketHandler {
    public static void handlePrestigeScreenPacket(S2CPrestigeScreenPacket s2CPrestigeScreenPacket) {
        Minecraft.getInstance().setScreen(new PrestigeScreen(Component.literal("Choose Prestige Power")));
    }
}
