package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor
    CustomData getCustomData();
}
