package net.jason13.automessage;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class FabricExampleModClient implements ClientModInitializer {
	
	public static PlayerData playerData = new PlayerData();
	
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(FabricExampleMod.PLAYTIME, (client, handler, buf, responseSender) -> {
			playerData.playtime = buf.readInt();
			
			// client.execute(() -> {
			// 	client.player.sendSystemMessage(Component.literal("playtime: " + playerData.playtime));
			// });
		});
		ClientPlayNetworking.registerGlobalReceiver(FabricExampleMod.SOFT_COUNTERS, (client, handler, buf, responseSender) -> {
			playerData.soft_counters = buf.readVarIntArray();
			
			// client.execute(() -> {
			// 	client.player.sendSystemMessage(Component.literal("soft_counters: " + playerData.soft_counters));
			// });
		});
		ClientPlayNetworking.registerGlobalReceiver(FabricExampleMod.HARD_COUNTERS, (client, handler, buf, responseSender) -> {
			playerData.hard_counters = buf.readVarIntArray();
			
			// client.execute(() -> {
			// 	client.player.sendSystemMessage(Component.literal("hard_counters: " + playerData.hard_counters));
			// });
		});
		
		ClientPlayNetworking.registerGlobalReceiver(FabricExampleMod.INITIAL_SYNC, (client, handler, buf, responseSender) -> {
			playerData.playtime = buf.readInt();
			playerData.soft_counters = buf.readVarIntArray();
			playerData.hard_counters = buf.readVarIntArray();
			
			// client.execute(() -> {
			// 	client.player.sendSystemMessage(Component.literal("Initial playtime: " + playerData.playtime));
			// 	client.player.sendSystemMessage(Component.literal("Initial soft_counters: " + Arrays.toString(playerData.soft_counters)));
			// 	client.player.sendSystemMessage(Component.literal("Initial hard_counters: " + Arrays.toString(playerData.hard_counters)));
			// });
		});
		
		
	}
}
