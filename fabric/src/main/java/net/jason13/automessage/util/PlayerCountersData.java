package net.jason13.automessage.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jason13.automessage.FabricExampleMod;
import net.jason13.automessage.networking.ModMessages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class PlayerCountersData {
  
  public static void initializeValuesIfAbsent(IEntityDataSaver player) {
    CompoundTag nbt = player.getPersistentData();
    if (!nbt.contains("playtime")) {
      nbt.putInt("playtime", 0);
    }
    if (!nbt.contains("soft")) {
      nbt.putIntArray("soft", new int[FabricExampleMod.messages.size()]);
    }
    if (!nbt.contains("hard")) {
      nbt.putIntArray("hard", new int[FabricExampleMod.messages.size()]);
    }
  }
  
  public static void incrementPlaytime(IEntityDataSaver player) {
    CompoundTag nbt = player.getPersistentData();
    
    if (!nbt.contains("playtime")) {
      nbt.putInt("playtime", 0);
    }
    
    int time = nbt.getInt("playtime");
    time++;
    nbt.putInt("playtime", time);
    
  }
  
  public static int[] addSoftCounterAtIndex(IEntityDataSaver player, int index) {
    CompoundTag nbt = player.getPersistentData();
    
    if (!nbt.contains("soft")) {
      nbt.putIntArray("soft", new int[FabricExampleMod.messages.size()]);
    }
    
    int[] values = nbt.getIntArray("soft");
    values[index] += 1;
    return values;
  }
  public static int[] addHardCounterAtIndex(IEntityDataSaver player, int index) {
    CompoundTag nbt = player.getPersistentData();
    
    if (!nbt.contains("hard")) {
      nbt.putIntArray("hard", new int[FabricExampleMod.messages.size()]);
    }
    
    int[] values = nbt.getIntArray("hard");
    values[index] += 1;
    return values;
  }
  public static int[] resetSoftCounters(IEntityDataSaver player) {
    CompoundTag nbt = player.getPersistentData();
    
    if (!nbt.contains("soft")) {
      nbt.putIntArray("soft", new int[FabricExampleMod.messages.size()]);
    }
    
    int[] values = nbt.getIntArray("soft");
    int[] resetSoft = new int[values.length];
    return resetSoft;
  }
  
  public static int getSoftCounterAtIndex(IEntityDataSaver player, int index) {
    CompoundTag nbt = player.getPersistentData();
    
    if (!nbt.contains("soft")) {
      nbt.putIntArray("soft", new int[FabricExampleMod.messages.size()]);
    }
    
    int[] values = nbt.getIntArray("soft");
    return values[index];
  }
  public static int getHardCounterAtIndex(IEntityDataSaver player, int index) {
    CompoundTag nbt = player.getPersistentData();
    
    if (!nbt.contains("hard")) {
      nbt.putIntArray("hard", new int[FabricExampleMod.messages.size()]);
    }
    
    int[] values = nbt.getIntArray("hard");
    return values[index];
  }
  
  
  
  
  public static void syncSoftCounters(int[] softCounts, ServerPlayer player)
  {
    
    FriendlyByteBuf buffer = PacketByteBufs.create();
    buffer.writeVarIntArray(softCounts);
    ServerPlayNetworking.send(player, ModMessages.COUNTERS_SYNC_ID, buffer);
  }
  public static void syncHardCounters(int[] hardCounts, ServerPlayer player)
  {
    
    FriendlyByteBuf buffer = PacketByteBufs.create();
    buffer.writeVarIntArray(hardCounts);
    ServerPlayNetworking.send(player, ModMessages.COUNTERS_SYNC_ID, buffer);
  }
  
  public static void syncValue(int value, ServerPlayer player) {
    FriendlyByteBuf buffer = PacketByteBufs.create();
    buffer.writeInt(value);
    ServerPlayNetworking.send(player, ModMessages.COUNTERS_SYNC_ID, buffer);
  }
}
