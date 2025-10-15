package xyz.eclipseisoffline.modifyplayerdata.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.modifyplayerdata.PlayerData;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    public void readExtraCustomData(ReadView view, CallbackInfo callbackInfo) {
        PlayerData.read((ServerPlayerEntity) (Object) this, view);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    public void writeExtraCustomData(WriteView view, CallbackInfo callbackInfo) {
        PlayerData.write((ServerPlayerEntity) (Object) this, view);
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyCustomData(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
        setComponent(DataComponentTypes.CUSTOM_DATA, ((EntityAccessor) oldPlayer).getCustomData());
    }
}
