package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.command.EntityDataObject;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.NbtReadView;
import net.minecraft.util.ErrorReporter;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.modifyplayerdata.PlayerData;

import java.util.HashSet;
import java.util.Set;

@Mixin(EntityDataObject.class)
public class EntityDataObjectMixin {

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "setNbt", at = @At("HEAD"), cancellable = true)
    public void setPlayerNbt(NbtCompound newNbt, CallbackInfo callbackInfo) {
        if (entity instanceof ServerPlayerEntity player) {
            NbtCompound oldNbt = NbtPredicate.entityToNbt(entity);

            Set<String> keys = new HashSet<>(newNbt.getKeys());
            for (String key : keys) {
                if (newNbt.get(key).equals(oldNbt.get(key))) {
                    newNbt.remove(key);
                }
            }

            try (ErrorReporter.Logging logging = new ErrorReporter.Logging(this.entity.getErrorReporterContext(), LOGGER)) {
                PlayerData.apply(player, NbtReadView.create(logging, player.getRegistryManager(), newNbt));
            }
            callbackInfo.cancel();
        }
    }
}
