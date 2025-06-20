package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.modifyplayerdata.PlayerData;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "readCustomData", at = @At("TAIL"))
    public void readExtraCustomData(ReadView view, CallbackInfo callbackInfo) {
        PlayerData.read((ServerPlayerEntity) (Object) this, view);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    public void writeExtraCustomData(WriteView view, CallbackInfo callbackInfo) {
        PlayerData.write((ServerPlayerEntity) (Object) this, view);
    }
}
