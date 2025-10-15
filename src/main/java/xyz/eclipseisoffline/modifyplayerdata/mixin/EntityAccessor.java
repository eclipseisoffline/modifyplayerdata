package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor
    NbtComponent getCustomData();
}
