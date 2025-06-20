package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.entity.EntityEquipment;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor
    EntityEquipment getEquipment();

    @Invoker
    void invokeSetAttacking(LazyEntityReference<PlayerEntity> attackingPlayer, int playerHitTimer);

    @Accessor
    void setAttackerReference(LazyEntityReference<LivingEntity> attacker);

    @Accessor
    void setLastAttackedTime(int lastAttackedTime);
}
