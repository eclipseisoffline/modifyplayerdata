package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    public void cancelSoundIfSilent(@Nullable PlayerEntity source, double x, double y, double z,
            RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch,
            long seed, CallbackInfo callbackInfo) {
        if (source != null && source.isSilent()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "playSoundFromEntity", at = @At("HEAD"), cancellable = true)
    public void cancelEntitySoundIfSilent(@Nullable PlayerEntity source, Entity entity,
            RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch,
            long seed, CallbackInfo callbackInfo) {
        if (source != null && source.isSilent()) {
            callbackInfo.cancel();
        }
    }
}
