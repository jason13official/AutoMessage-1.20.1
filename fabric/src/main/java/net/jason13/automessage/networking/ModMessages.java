package net.jason13.automessage.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.jason13.automessage.CommonConstants;
import net.jason13.automessage.networking.packet.CountersSyncDataS2CPacket;
import net.minecraft.resources.ResourceLocation;

public class ModMessages {
  public static final ResourceLocation COUNTERS_SYNC_ID = new ResourceLocation(CommonConstants.MOD_ID, "counters_sync_data");
  
  public static void registerS2CPackets() {
    ClientPlayNetworking.registerGlobalReceiver(COUNTERS_SYNC_ID, CountersSyncDataS2CPacket::receive);
  }
}
