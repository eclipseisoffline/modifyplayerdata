package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.eclipseisoffline.modifyplayerdata.VisualFireable;

@Mixin(Entity.class)
public abstract class EntityMixin implements VisualFireable {

    @Shadow
    @Final
    protected static int FLAG_ONFIRE;
    @Shadow
    private boolean hasVisualFire;

    @Shadow
    protected abstract void setSharedFlag(int index, boolean value);

    @Override
    public void modifyPlayerData$setHasVisualFire(boolean hasVisualFire) {
        this.hasVisualFire = hasVisualFire;
        setSharedFlag(FLAG_ONFIRE, hasVisualFire);
    }
}
