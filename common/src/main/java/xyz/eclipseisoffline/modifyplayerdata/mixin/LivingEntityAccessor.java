package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor
    EntityEquipment getEquipment();

    @Accessor
    void setEffectsDirty(boolean effectsDirty);

    @Invoker
    void invokeSetLastHurtByPlayer(EntityReference<Player> attackingPlayer, int playerHitTimer);

    @Accessor
    void setLastHurtByMob(EntityReference<LivingEntity> attacker);

    @Accessor
    void setLastHurtByMobTimestamp(int lastAttackedTime);

    @Accessor
    void setCurrentImpulseContextResetGraceTime(int currentImpulseContextResetGraceTime);
}
