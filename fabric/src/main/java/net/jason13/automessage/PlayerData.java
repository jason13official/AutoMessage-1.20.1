package net.jason13.automessage;

public class PlayerData {
	public int playtime = 0;
	public int[] soft_counters = new int[FabricExampleMod.messages.size()];
	public int[] hard_counters = new int[FabricExampleMod.messages.size()];
	
	public void incrementPlaytime() {
		this.playtime++;
	}
	
	public void incrementSoftCounterAtIndex(int index) {
		this.soft_counters[index]++;
	}
	public void incrementHardCounterAtIndex(int index) {
		this.hard_counters[index]++;
	}
}
