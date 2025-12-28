package xyz.eclipseisoffline.modifyplayerdata.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.modifyplayerdata.PlayerData;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player {

    public ServerPlayerEntityMixin(Level world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readExtraCustomData(ValueInput view, CallbackInfo callbackInfo) {
        PlayerData.read((ServerPlayer) (Object) this, view);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void writeExtraCustomData(ValueOutput view, CallbackInfo callbackInfo) {
        PlayerData.write((ServerPlayer) (Object) this, view);
    }

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    public void copyCustomData(ServerPlayer oldPlayer, boolean alive, CallbackInfo callbackInfo) {
        setComponent(DataComponents.CUSTOM_DATA, ((EntityAccessor) oldPlayer).getCustomData());
    }
}
