package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {

    @Accessor
    void setSleepTimer(int sleepTimer);

    @Accessor("ignoreFallDamageFromCurrentExplosion")
    void setIgnoreFallDamageFromCurrentExplosionRaw(boolean ignoreFallDamageFromCurrentExplosion);

    @Accessor
    void setCurrentExplosionResetGraceTime(int currentExplosionResetGraceTime);
}
