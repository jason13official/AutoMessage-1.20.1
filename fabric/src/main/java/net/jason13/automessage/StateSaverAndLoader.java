package net.jason13.automessage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class StateSaverAndLoader extends SavedData {
	public HashMap<UUID, PlayerData> players = new HashMap<>();
	@Override
	public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
		CompoundTag playerGroupNBT = new CompoundTag();
		players.forEach(((uuid, playerData) -> {
			CompoundTag playerIndividualNBT = new CompoundTag();
			
			playerIndividualNBT.putInt("playtime", playerData.playtime);
			playerIndividualNBT.putIntArray("soft_counters", playerData.soft_counters);
			playerIndividualNBT.putIntArray("hard_counters", playerData.hard_counters);
			
			playerGroupNBT.put(uuid.toString(), playerIndividualNBT);
		}));
		nbt.put("players", playerGroupNBT);
		return nbt;
	}
	public static StateSaverAndLoader createFromNbt(CompoundTag tag) {
		StateSaverAndLoader state = new StateSaverAndLoader();
		CompoundTag playerGroupNBT = tag.getCompound("players");
		playerGroupNBT.getAllKeys().forEach(key -> {
			PlayerData playerData = new PlayerData();
			
			playerData.playtime = playerGroupNBT.getCompound(key).getInt("playtime");
			playerData.soft_counters = playerGroupNBT.getCompound(key).getIntArray("soft_counters");
			playerData.hard_counters = playerGroupNBT.getCompound(key).getIntArray("hard_counters");
			
			UUID uuid = UUID.fromString(key);
			state.players.put(uuid, playerData);
		});
		return state;
	}
	public static StateSaverAndLoader getServerState(MinecraftServer server) {
		DimensionDataStorage persistentStateManager = server.getLevel(Level.OVERWORLD).getDataStorage();
		StateSaverAndLoader state = persistentStateManager.computeIfAbsent(StateSaverAndLoader::createFromNbt, StateSaverAndLoader::new, CommonConstants.MOD_ID);
		state.setDirty();
		return state;
	}
	public static PlayerData getPlayerState(LivingEntity player) {
		StateSaverAndLoader serverState = getServerState(player.level().getServer());
		PlayerData playerState = serverState.players.computeIfAbsent(player.getUUID(), uuid -> new PlayerData());
		return playerState;
	}
}
