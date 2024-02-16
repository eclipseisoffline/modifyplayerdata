package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.block.entity.SculkShriekerWarningManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SculkShriekerWarningManager.class)
public interface SculkShriekerWarningManagerAccessor {

    @Accessor
    void setTicksSinceLastWarning(int ticksSinceLastWarning);
    @Accessor
    void setCooldownTicks(int cooldownTicks);
}
