package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.modifyplayerdata.CustomDataHolder;
import xyz.eclipseisoffline.modifyplayerdata.PlayerNbtModifier;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readExtraCustomNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
        PlayerNbtModifier.readCustomNbt((ServerPlayerEntity) (Object) this, nbt);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeExtraCustomNbt(NbtCompound nbt, CallbackInfo callbackInfo) {
        PlayerNbtModifier.writeCustomNbt((ServerPlayerEntity) (Object) this, nbt);
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyCustomData(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
        ((CustomDataHolder) this).modifyPlayerData$setCustomNbtData(((CustomDataHolder) oldPlayer).modifyPlayerData$getCustomNbtData());
    }
}
