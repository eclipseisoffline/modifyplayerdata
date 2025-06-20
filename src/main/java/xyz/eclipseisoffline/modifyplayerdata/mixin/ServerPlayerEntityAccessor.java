package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor {

    @Accessor
    void setSculkShriekerWarningManager(SculkShriekerWarningManager manager);

    @Accessor
    void setEnteredNetherPos(Vec3d enteredNetherPos);

    @Accessor
    void setSeenCredits(boolean seenCredits);
}
