package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {

    @Accessor
    void setWardenSpawnTracker(WardenSpawnTracker manager);

    @Accessor
    void setEnteredNetherPosition(Vec3 enteredNetherPos);

    @Accessor
    void setSeenCredits(boolean seenCredits);
}
