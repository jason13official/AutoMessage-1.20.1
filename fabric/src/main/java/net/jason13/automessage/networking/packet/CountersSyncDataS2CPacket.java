package net.jason13.automessage.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.jason13.automessage.util.IEntityDataSaver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class CountersSyncDataS2CPacket {
  public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
    ((IEntityDataSaver) client.player).getPersistentData().putIntArray("soft", buf.readVarIntArray());
  }
}
