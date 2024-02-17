package net.jason13.automessage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jason13.automessage.config.Config;
import net.jason13.automessage.util.PlayerCountersData;
import net.jason13.automessage.util.toml.Toml;
import net.jason13.monolib.methods.BlockMethods;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FabricExampleMod implements ModInitializer {
    
    public static boolean debuggingEnabled = false;
    
    // to be referenced by the mod at any time, during the PlayerTickHandler
    public static List<Object> messages;
    public static List<Object> links;
    public static List<Object> intervals;
    public static List<Object> soft_limits;
    public static List<Object> hard_limits;
    public static boolean am_enabled;
    
    public static final ResourceLocation INITIAL_SYNC = new ResourceLocation(CommonConstants.MOD_ID, "initial_sync");
    public static final ResourceLocation PLAYTIME = new ResourceLocation(CommonConstants.MOD_ID, "playtime");
    public static final ResourceLocation SOFT_COUNTERS = new ResourceLocation(CommonConstants.MOD_ID, "soft_counters");
    public static final ResourceLocation HARD_COUNTERS = new ResourceLocation(CommonConstants.MOD_ID, "hard_counters");
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        CommonConstants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        
        // validate configuration file or initialize a new one
        if (Config.configFound())
        {
            Toml config = Config.readTomlFromFile();
            
            messages = config.getList("messages");
            links = config.getList("links");
            intervals = config.getList("intervals");
            soft_limits = config.getList("soft_limits");
            hard_limits = config.getList("hard_limits");
            am_enabled = config.getBoolean("am_enabled");
        }
        else
        {
            final String CONFIG_DIRECTORY_PATH = "config";
            final File CONFIG_DIRECTORY = new File(CONFIG_DIRECTORY_PATH);
            if (!CONFIG_DIRECTORY.isDirectory()) {
                CONFIG_DIRECTORY.mkdir();
            }
            Config.initialize();
            Toml config = Config.readTomlFromFile();
            
            messages = config.getList("messages");
            links = config.getList("links");
            intervals = config.getList("intervals");
            soft_limits = config.getList("soft_limits");
            hard_limits = config.getList("hard_limits");
            am_enabled = config.getBoolean("am_enabled");
        }
        // config should now be accessible both client-side and server-side, and able to be referenced.
        
        
        // ServerTickEvents.START_SERVER_TICK.register(new PlayerTickHandler());
        
        
        
        
        
        
        
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerData playerState = StateSaverAndLoader.getPlayerState(handler.getPlayer());
            FriendlyByteBuf data = PacketByteBufs.create();
	        
	          Arrays.fill(playerState.soft_counters, 0);
            
            // for (int index=0; index<playerState.soft_counters.length; index++) {
            //   playerState.soft_counters[index] = 0;
            // }
            
            
            data.writeInt(playerState.playtime);
            data.writeVarIntArray(playerState.soft_counters);
            data.writeVarIntArray(playerState.hard_counters);
            
            
            
            server.execute(() -> {
                ServerPlayNetworking.send(handler.getPlayer(), INITIAL_SYNC, data);
            });
        });
        
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            
            
            
            boolean flag_SkipZeroTick = server.getTickCount() != 0; // = event.getServer().getTickCount() != 0;
            boolean flag_OperatingOncePerSecond = server.getTickCount() % 20 == 0; // = event.getServer().getTickCount() % (20) == 0;
            
            
            
            if (flag_SkipZeroTick && flag_OperatingOncePerSecond && FabricExampleMod.am_enabled) {
                // System.out.println("ticking!");
                // while (server.getPlayerManager().getPlayerList().iterator().hasNext()) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    
                    
                    // System.out.println("on player!");
                    
                    //ServerPlayerEntity player = server.getPlayerManager().getPlayerList().iterator().next();
                    
                    
                    PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                    playerState.playtime += 1;
                    
                    
                    
                    // CUSTOM LOGIC
                    // CUSTOM LOGIC
                    // CUSTOM LOGIC
                    // CUSTOM LOGIC
                    
                    
                    
                    
                    
                    // DEBUGGING START
                    boolean leftHandCommand = BlockMethods.compareBlockToItemStack(Blocks.COMMAND_BLOCK, player.getOffhandItem());
                    boolean rightHandCommand = BlockMethods.compareBlockToItemStack(Blocks.COMMAND_BLOCK, player.getMainHandItem());
                    
                    if (!FabricExampleMod.debuggingEnabled && leftHandCommand && rightHandCommand) {
                        FabricExampleMod.debuggingEnabled = true;
                        player.sendSystemMessage(Component.literal("debuggingEnabled" + CommonConstants.MOD_NAME));
                    }
                    // DEBUGGING END
                    
                    
                    // IEntityDataSaver dataPlayer = ((IEntityDataSaver) player);
                    // CompoundTag nbt = dataPlayer.getPersistentData();
                    // int getPlaytime = nbt.getInt("playtime");
                    //
                    // PlayerCountersData.initializeValuesIfAbsent(dataPlayer);
                    //
                    // PlayerCountersData.incrementPlaytime(dataPlayer);
                    
                    for (int i=0; i<FabricExampleMod.messages.size(); i++)
                    {
                        final int messageIndex = i;
                        
                        int getPlaytime = playerState.playtime;
                        int[] softCounts = playerState.soft_counters;
                        int[] hardCounts = playerState.hard_counters;
                        
                        // int[] softCounts = nbt.getIntArray("soft");
                        // int[] hardCounts = nbt.getIntArray("hard");
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
                            
                            
                            playerState.incrementSoftCounterAtIndex(messageIndex);
                            playerState.incrementHardCounterAtIndex(messageIndex);
                        }
                    }
                    
                    
                    
                    
                    
                    
                    
                    
                    // CUSTOM LOGIC
                    // CUSTOM LOGIC
                    // CUSTOM LOGIC
                    // CUSTOM LOGIC
                    
                    
                    
                    
                    
                    
                    FriendlyByteBuf data = PacketByteBufs.create();
                    data.writeInt(playerState.playtime);
                    data.writeVarIntArray(playerState.soft_counters);
                    data.writeVarIntArray(playerState.hard_counters);
                    
                    ServerPlayer playerEntity = server.getPlayerList().getPlayer(player.getUUID());
                    server.execute(() -> {
                        ServerPlayNetworking.send(playerEntity, PLAYTIME, data);
                        ServerPlayNetworking.send(playerEntity, SOFT_COUNTERS, data);
                        ServerPlayNetworking.send(playerEntity, HARD_COUNTERS, data);
                    });
                }
            }
        });
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
}
