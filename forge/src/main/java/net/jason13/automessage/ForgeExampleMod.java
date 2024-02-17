package net.jason13.automessage;

import net.jason13.automessage.capability.MessagingCapability;
import net.jason13.automessage.capability.MessagingProvider;
import net.jason13.automessage.config.Config;
import net.jason13.monolib.methods.BlockMethods;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CommonConstants.MOD_ID)
public class ForgeExampleMod {
    
    public boolean debuggingEnabled = false;
    
    public ForgeExampleMod() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        CommonConstants.LOG.info("Hello Forge world!");
        CommonClass.init();
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        
        bus.addListener(this::onRegisterCapabilitiesEvent);
        
        MinecraftForge.EVENT_BUS.register(this);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "automessage-common.toml");
    }
    
    public void onRegisterCapabilitiesEvent(RegisterCapabilitiesEvent event)
    {
        event.register(MessagingCapability.class);
    }
    
    // @SubscribeEvent
    // public void onPlayerCloneEvent(PlayerEvent.Clone event)
    // {
    //     System.out.println("CHECKPOINT 1 REACHED");
    //     if (event.isWasDeath()) {
    //
    //         System.out.println("CHECKPOINT 2 REACHED");
    //
    //         event.getOriginal().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(oldStore ->
    //         {
    //             System.out.println("CHECKPOINT 3 REACHED");
    //             event.getOriginal().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(newStore ->
    //             {
    //                 System.out.println("CHECKPOINT 4 REACHED");
    //                 newStore.copyOnDeath(oldStore);
    //             });
    //         });
    //     }
    // }
    
    @SubscribeEvent
    public void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
        {
            if (!event.getObject().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).isPresent())
            {
                event.addCapability(new ResourceLocation(CommonConstants.MOD_ID, "properties"), new MessagingProvider());
            }
        }
    }
    @SubscribeEvent
    public void onPlayerJoinLevelEvent(EntityJoinLevelEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer)
        {
            event.getEntity().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(MessagingCapability::resetSoftCounts);
        }
    }
    
    @SubscribeEvent
    public void onStartTick(TickEvent.ServerTickEvent event) {
        // debugging logic
        if (event.phase == TickEvent.Phase.START) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                
                boolean leftHandCommand = BlockMethods.compareBlockToItemStack(Blocks.COMMAND_BLOCK, player.getOffhandItem());
                boolean rightHandCommand = BlockMethods.compareBlockToItemStack(Blocks.COMMAND_BLOCK, player.getMainHandItem());
                
                if (!debuggingEnabled && leftHandCommand && rightHandCommand) {
                    debuggingEnabled = true;
                    player.sendSystemMessage(Component.literal("debuggingEnabled" + CommonConstants.MOD_NAME));
                }
            }
        }
        // debugging logic
        
        boolean flag_StartOfTick = event.phase == TickEvent.Phase.START;
        boolean flag_SkipZeroTick = event.getServer().getTickCount() != 0;
        boolean flag_OperatingOncePerSecond = event.getServer().getTickCount() % (20) == 0;
        
        if (flag_StartOfTick && flag_SkipZeroTick && flag_OperatingOncePerSecond)
        {
            
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers())
            {
                player.getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(messaging ->
                {
                    messaging.incrementPlaytime();
                    
                    for (int i = 0; i < Config.MESSAGES.get().size(); i++) {
                        int messageIndex = i;
                        
                        boolean flag_PlaytimeHittingInterval = messaging.getPlaytime() % Config.INTERVALS.get().get(messageIndex) == 0;
                        boolean flag_SoftLessThanLimit = messaging.getSoftCounts()[messageIndex] < Config.SOFT_LIMITS.get().get(messageIndex);
                        boolean flag_SoftUnlimited = Config.SOFT_LIMITS.get().get(messageIndex) == 0;
                        boolean flag_HardLessThanLimit = messaging.getHardCounts()[messageIndex] < Config.HARD_LIMITS.get().get(messageIndex);
                        boolean flag_HardUnlimited = Config.HARD_LIMITS.get().get(messageIndex) == 0;
                        boolean flag_Enabled = Config.AM_ENABLED.get();
                        
                        if (flag_PlaytimeHittingInterval && (flag_SoftLessThanLimit || flag_SoftUnlimited) && (flag_HardLessThanLimit || flag_HardUnlimited) && flag_Enabled) {
                            
                            player.sendSystemMessage
                              (
                                Component.literal(Config.MESSAGES.get().get(messageIndex)).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.valueOf(Config.LINKS.get().get(messageIndex)))))
                              );
                            
                            messaging.incrementSoftCountAtIndex(messageIndex);
                            messaging.incrementHardCountAtIndex(messageIndex);
                        }
                    }
                });
            }
        }
        
    }
}