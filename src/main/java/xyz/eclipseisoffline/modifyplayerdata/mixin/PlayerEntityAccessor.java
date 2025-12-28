package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerEntityAccessor {

    @Accessor
    void setSleepCounter(int sleepTimer);

    @Accessor("ignoreFallDamageFromCurrentImpulse")
    void setIgnoreFallDamageFromCurrentExplosionRaw(boolean ignoreFallDamageFromCurrentExplosion);

    @Accessor
    void setCurrentImpulseContextResetGraceTime(int currentExplosionResetGraceTime);
}
