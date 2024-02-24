package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.eclipseisoffline.modifyplayerdata.CustomDataHolder;
import xyz.eclipseisoffline.modifyplayerdata.VisualFireable;

@Mixin(Entity.class)
public abstract class EntityMixin implements VisualFireable, CustomDataHolder {

    @Shadow
    @Final
    protected static int ON_FIRE_FLAG_INDEX;
    @Shadow
    private boolean hasVisualFire;
    @Unique
    private NbtCompound customNbtData = new NbtCompound();

    @Shadow
    protected abstract void setFlag(int index, boolean value);

    @Override
    public void modifyPlayerData$setHasVisualFire(boolean hasVisualFire) {
        this.hasVisualFire = hasVisualFire;
        setFlag(ON_FIRE_FLAG_INDEX, hasVisualFire);
    }

    @Override
    public NbtCompound modifyPlayerData$getCustomNbtData() {
        return customNbtData;
    }

    @Override
    public void modifyPlayerData$setCustomNbtData(NbtCompound customNbtData) {
        this.customNbtData = customNbtData;
    }
}
