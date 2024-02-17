package net.jason13.automessage.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.jason13.automessage.util.IEntityDataSaver;
import net.jason13.automessage.util.PlayerCountersData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class PlayerJoinLevelEvent implements ServerPlayConnectionEvents.Join{
  @Override
  public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
    ServerPlayer player =  handler.getPlayer();
    IEntityDataSaver dataPlayer = ((IEntityDataSaver) player);
    
    PlayerCountersData.initializeValuesIfAbsent(dataPlayer);
    PlayerCountersData.resetSoftCounters(dataPlayer);
  }
}
