package net.jason13.automessage.mixin;

import net.jason13.automessage.util.IEntityDataSaver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
  private CompoundTag persistentData;
  
  @Override
  public CompoundTag getPersistentData() {
    if(this.persistentData == null) {
      this.persistentData = new CompoundTag();
    }
    return persistentData;
  }
  
  @Inject(method = "save", at = @At("HEAD"))
  protected void injectWriteMethod(CompoundTag nbt, CallbackInfoReturnable info) {
    if(persistentData != null) {
      nbt.put("automessage.counter_data", persistentData);
    }
  }
  
  @Inject(method = "load", at = @At("HEAD"))
  protected void injectReadMethod(CompoundTag nbt, CallbackInfo info) {
    if (nbt.contains("automessage.counter_data", 10)) {
      persistentData = nbt.getCompound("automessage.counter_data");
    }
  }
}
