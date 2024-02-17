package net.jason13.automessage.event;

import net.jason13.automessage.CommonConstants;
import net.jason13.automessage.capability.MessagingProvider;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommonConstants.MOD_ID)
public class ModEvents {
	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		
		System.out.println("onPlayerCloned");
		
		if(event.isWasDeath()) {
			
			System.out.println("isWasDeath");
			
			// event.getEntity().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(messagingCapability -> {
			// 	System.out.println("ACCESSING MAIN ENTITY CAPABILITY");
			// });
			// event.getOriginal().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(messagingCapability -> {
			// 	System.out.println("ACCESSING ORIGINAL ENTITY CAPABILITY");
			// });
			
			event.getOriginal().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(oldStore -> {
				
				System.out.println("ifPresent1");
				
				event.getEntity().getCapability(MessagingProvider.PLAYER_MESSAGING_CAPABILITY).ifPresent(newStore -> {
					
					System.out.println("ifPresent2");
					
					newStore.copyFrom(oldStore);
				});
			});
		}
	}
}
