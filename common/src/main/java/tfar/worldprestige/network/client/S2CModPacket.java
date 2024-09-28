package tfar.worldprestige.network.client;


import tfar.worldprestige.network.ModPacket;

public interface S2CModPacket extends ModPacket {

    void handleClient();

}
