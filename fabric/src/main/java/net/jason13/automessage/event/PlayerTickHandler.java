package net.jason13.automessage.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.jason13.automessage.CommonConstants;
import net.jason13.automessage.FabricExampleMod;
import net.jason13.automessage.util.IEntityDataSaver;
import net.jason13.automessage.util.PlayerCountersData;
import net.jason13.monolib.methods.BlockMethods;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

import java.util.Iterator;

public class PlayerTickHandler implements ServerTickEvents.StartTick {
  @Override
  public void onStartTick(MinecraftServer server)
  {
    Iterator<ServerPlayer> playerIterator = server.getPlayerList().getPlayers().iterator();
    
    boolean flag_SkipZeroTick = server.getTickCount() != 0; // = event.getServer().getTickCount() != 0;
    boolean flag_OperatingOncePerSecond = server.getTickCount() % 20 == 0; // = event.getServer().getTickCount() % (20) == 0;
    
    if (flag_SkipZeroTick && flag_OperatingOncePerSecond && FabricExampleMod.am_enabled)
    {
      // for (ServerPlayer player : server.getPlayerList().getPlayers())
      while (playerIterator.hasNext())
      {
        
        ServerPlayer player = (ServerPlayer)playerIterator.next();
        
        // DEBUGGING START
        boolean leftHandCommand = BlockMethods.compareBlockToItemStack(Blocks.COMMAND_BLOCK, player.getOffhandItem());
        boolean rightHandCommand = BlockMethods.compareBlockToItemStack(Blocks.COMMAND_BLOCK, player.getMainHandItem());
        
        if (!FabricExampleMod.debuggingEnabled && leftHandCommand && rightHandCommand) {
          FabricExampleMod.debuggingEnabled = true;
          player.sendSystemMessage(Component.literal("debuggingEnabled" + CommonConstants.MOD_NAME));
        }
        // DEBUGGING END
        
        
        IEntityDataSaver dataPlayer = ((IEntityDataSaver) player);
        CompoundTag nbt = dataPlayer.getPersistentData();
        int getPlaytime = nbt.getInt("playtime");
        
        PlayerCountersData.initializeValuesIfAbsent(dataPlayer);
        
        PlayerCountersData.incrementPlaytime(dataPlayer);
        
        for (int i=0; i<FabricExampleMod.messages.size(); i++)
        {
          final int messageIndex = i;
          
          int[] softCounts = nbt.getIntArray("soft");
          int[] hardCounts = nbt.getIntArray("hard");
          int softCountAtMessageIndex = softCounts[messageIndex];
          int hardCountAtMessageIndex = hardCounts[messageIndex];
          
          boolean flag_PlaytimeHittingInterval = getPlaytime % Integer.parseInt(FabricExampleMod.intervals.get(messageIndex).toString()) == 0;
          boolean flag_SoftLessThanLimit = softCountAtMessageIndex < Integer.parseInt(FabricExampleMod.soft_limits.get(messageIndex).toString());
          boolean flag_SoftUnlimited = Integer.parseInt(FabricExampleMod.soft_limits.get(messageIndex).toString()) == 0;
          boolean flag_HardLessThanLimit = hardCountAtMessageIndex < Integer.parseInt(FabricExampleMod.hard_limits.get(messageIndex).toString());
          boolean flag_HardUnlimited = Integer.parseInt(FabricExampleMod.hard_limits.get(messageIndex).toString()) == 0;
          boolean flag_Enabled = FabricExampleMod.am_enabled;
          
          if (flag_PlaytimeHittingInterval && (flag_SoftLessThanLimit || flag_SoftUnlimited) && (flag_HardLessThanLimit || flag_HardUnlimited) && flag_Enabled) {
            player.sendSystemMessage(Component.literal(FabricExampleMod.messages.get(messageIndex).toString()).withStyle(style -> style.withClickEvent(
              new ClickEvent(ClickEvent.Action.OPEN_URL, FabricExampleMod.links.get(messageIndex).toString()))));
            PlayerCountersData.addSoftCounterAtIndex(dataPlayer,messageIndex);
            PlayerCountersData.addHardCounterAtIndex(dataPlayer,messageIndex);
          }
        }
      }
    }
  }
}
