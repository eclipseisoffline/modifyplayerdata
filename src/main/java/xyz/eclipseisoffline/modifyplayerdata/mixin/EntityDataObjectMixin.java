package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.command.EntityDataObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.modifyplayerdata.PlayerNbtModifier;

@Mixin(EntityDataObject.class)
public class EntityDataObjectMixin {

    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "setNbt", at = @At("HEAD"), cancellable = true)
    public void setPlayerNbt(NbtCompound newNbt, CallbackInfo callbackInfo) {
        if (entity instanceof PlayerEntity) {
            if (entity.getWorld().isClient) {
                throw new AssertionError();
            }

            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            for (String key : newNbt.getKeys()) {
                PlayerNbtModifier.modifyNbtKey(key, newNbt.get(key), player);
            }

            callbackInfo.cancel();
        }
    }
}
