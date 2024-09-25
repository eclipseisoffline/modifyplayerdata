package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HungerManager.class)
public interface HungerManagerAccessor {

    @Accessor
    void setFoodTickTimer(int foodTickTimer);

    @Accessor
    void setExhaustion(float exhaustion);
}
