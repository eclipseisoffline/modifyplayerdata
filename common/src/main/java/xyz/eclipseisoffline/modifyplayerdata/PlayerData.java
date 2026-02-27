package xyz.eclipseisoffline.modifyplayerdata;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import xyz.eclipseisoffline.modifyplayerdata.mixin.FoodDataAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.LivingEntityAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.PlayerAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.ServerPlayerAccessor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class PlayerData {

    public static void read(ServerPlayer player, ValueInput input) {
        getOptionalBoolean(input, "LeftHanded").ifPresent(leftHanded -> player.setMainArm(leftHanded ? HumanoidArm.LEFT : HumanoidArm.RIGHT));

        // Backwards compatibility, does not apply to commands, only to saved data
        input.read("CustomData", CustomData.CODEC).ifPresent(data -> player.setComponent(DataComponents.CUSTOM_DATA, data));
    }

    public static void apply(ServerPlayer player, ValueInput input) {
        // Entity data
        input.read("Pos", Vec3.CODEC).ifPresent(pos -> player.teleportTo(player.level(), pos.x, pos.y, pos.z, Set.of(),
                player.getYRot(), player.getXRot(), false));

        input.read("Motion", Vec3.CODEC).ifPresent(motion -> {
            player.setDeltaMovement(motion);
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        });

        input.read("Rotation", Vec2.CODEC).ifPresent(rotation -> player.teleportTo(player.level(), player.getX(), player.getY(), player.getZ(),
                Set.of(), rotation.x, rotation.y, false));

        getOptionalDouble(input, "fall_distance").ifPresent(fallDistance -> player.fallDistance = fallDistance);
        getOptionalShort(input, "Fire").ifPresent(player::setRemainingFireTicks);
        input.getInt("Air").ifPresent(player::setAirSupply);
        getOptionalBoolean(input, "Invulnerable").ifPresent(player::setInvulnerable);
        input.getInt("PortalCooldown").ifPresent(player::setPortalCooldown);
        getOptionalBoolean(input, "Silent").ifPresent(player::setSilent);
        getOptionalBoolean(input, "NoGravity").ifPresent(player::setNoGravity);
        getOptionalBoolean(input, "Glowing").ifPresent(player::setGlowingTag);
        input.getInt("TicksFrozen").ifPresent(player::setTicksFrozen);
        getOptionalBoolean(input, "HasVisualFire").ifPresent(visualFire -> ((VisualFireable) player).modifyPlayerData$setHasVisualFire(visualFire));

        input.list("Tags", Codec.STRING).ifPresent(stream -> {
            List<String> tags = stream.stream().toList();
            Set<String> currentTags = new HashSet<>(player.entityTags());
            for (String currentTag : currentTags) {
                if (!tags.contains(currentTag)) {
                    player.removeTag(currentTag);
                }
            }
            for (String newTag : tags) {
                player.addTag(newTag);
            }
        });

        input.read("data", CustomData.CODEC).ifPresent(data -> player.setComponent(DataComponents.CUSTOM_DATA, data));


        // Living entity data
        getOptionalFloat(input, "Health").ifPresent(player::setHealth);
        getOptionalShort(input, "HurtTime").ifPresent(hurtTime -> {
            player.hurtTime = hurtTime;
            player.hurtDuration = hurtTime;
        });
        getOptionalFloat(input, "AbsorptionAmount").ifPresent(player::setAbsorptionAmount);
        input.read("attributes", AttributeInstance.Packed.LIST_CODEC).ifPresent(player.getAttributes()::apply);

        input.read("active_effects", MobEffectInstance.CODEC.listOf()).ifPresent(effects -> {
            for (MobEffectInstance current : player.getActiveEffects()) {
                player.connection.send(new ClientboundRemoveMobEffectPacket(player.getId(), current.getEffect()));
            }
            player.getActiveEffectsMap().clear();
            for (MobEffectInstance effect : effects) {
                player.getActiveEffectsMap().put(effect.getEffect(), effect);
                player.connection.send(new ClientboundUpdateMobEffectPacket(player.getId(), effect, false));
            }
            ((LivingEntityAccessor) player).setEffectsDirty(true);
        });

        getOptionalBoolean(input, "FallFlying").ifPresent(fallFlying -> {
            if (fallFlying) {
                player.startFallFlying();
            } else {
                player.stopFallFlying();
            }
        });

        EntityReference<Player> attackingPlayer = EntityReference.read(input, "last_hurt_by_player");
        int lastHurtByPlayer = input.getIntOr("last_hurt_by_player_memory_time", player.getLastHurtByPlayerMemoryTime());
        if (attackingPlayer != null) {
            ((LivingEntityAccessor) player).invokeSetLastHurtByPlayer(attackingPlayer, lastHurtByPlayer);
        }

        EntityReference<LivingEntity> attacker = EntityReference.read(input, "last_hurt_by_mob");
        int ticksSinceLastHurt = input.getIntOr("ticks_since_last_hurt_by_mob", player.tickCount - player.getLastHurtByMobTimestamp());
        if (attacker != null) {
            ((LivingEntityAccessor) player).setLastHurtByMob(attacker);
            ((LivingEntityAccessor) player).setLastHurtByMobTimestamp(ticksSinceLastHurt + player.tickCount);
        }

        input.read("equipment", EntityEquipment.CODEC).ifPresent(equipment -> ((LivingEntityAccessor) player).getEquipment().setAll(equipment));


        // Technically a mob property, we include it anyway because it's fun
        getOptionalBoolean(input, "LeftHanded").ifPresent(leftHanded -> player.setMainArm(leftHanded ? HumanoidArm.LEFT : HumanoidArm.RIGHT));


        // Player data
        input.list("Inventory", ItemStackWithSlot.CODEC).ifPresent(player.getInventory()::load);

        input.read("SelectedItem", ItemStack.CODEC).ifPresent(stack -> {
            player.getInventory().setItem(player.getInventory().getSelectedSlot(), stack);
            player.connection.send(new ClientboundSetEquipmentPacket(player.getId(),
                    List.of(Pair.of(EquipmentSlot.MAINHAND, stack))));
        });
        input.getInt("SelectedItemSlot").ifPresent(slot -> {
            player.getInventory().setSelectedSlot(slot);
            player.connection.send(new ClientboundSetHeldSlotPacket(slot));
        });

        getOptionalShort(input, "SleepTimer").ifPresent(timer -> ((PlayerAccessor) player).setSleepCounter(timer));
        input.getInt("Score").ifPresent(player::setScore);

        input.getInt("foodLevel").ifPresent(player.getFoodData()::setFoodLevel);
        input.getInt("foodTickTimer").ifPresent(timer -> ((FoodDataAccessor) player.getFoodData()).setTickTimer(timer));
        getOptionalFloat(input, "foodSaturationLevel").ifPresent(player.getFoodData()::setSaturation);
        getOptionalFloat(input, "foodExhaustionLevel").ifPresent(level -> ((FoodDataAccessor) player.getFoodData()).setExhaustionLevel(level));

        input.read("abilities", Abilities.Packed.CODEC).ifPresent(packed -> {
            player.getAbilities().apply(packed);
            player.onUpdateAbilities();
        });

        input.list("EnderItems", ItemStackWithSlot.CODEC).ifPresent(player.getEnderChestInventory()::fromSlots);

        input.read("current_explosion_impact_pos", Vec3.CODEC).ifPresent(pos -> player.currentImpulseImpactPos = pos);
        input.getInt("current_impulse_context_reset_grace_time").ifPresent(time -> ((LivingEntityAccessor) player).setCurrentImpulseContextResetGraceTime(time));


        // Server player data
        input.read("warden_spawn_tracker", WardenSpawnTracker.CODEC).ifPresent(manager -> ((ServerPlayerAccessor) player).setWardenSpawnTracker(manager));
        input.read("entered_nether_pos", Vec3.CODEC).ifPresent(pos -> ((ServerPlayerAccessor) player).setEnteredNetherPosition(pos));
        getOptionalBoolean(input, "seenCredits").ifPresent(seenCredits -> ((ServerPlayerAccessor) player).setSeenCredits(seenCredits));
        input.read("respawn", ServerPlayer.RespawnConfig.CODEC).ifPresent(respawn -> player.setRespawnPosition(respawn, false));
        getOptionalBoolean(input, "spawn_extra_particles_on_fall").ifPresent(player::setSpawnExtraParticlesOnFall);
        input.read("raid_omen_position", BlockPos.CODEC).ifPresent(player::setRaidOmenPosition);
    }

    public static void write(ServerPlayer player, ValueOutput output) {
        if (player.getMainArm() == HumanoidArm.LEFT) {
            output.putBoolean("LeftHanded", true);
        }
    }

    private static OptionalDouble getOptionalDouble(ValueInput input, String key) {
        return input.read(key, Codec.DOUBLE).stream().mapToDouble(d -> d).findFirst();
    }

    private static Optional<Short> getOptionalShort(ValueInput input, String key) {
        return input.read(key, Codec.SHORT);
    }

    private static Optional<Boolean> getOptionalBoolean(ValueInput input, String key) {
        return input.read(key, Codec.BOOL);
    }

    private static Optional<Float> getOptionalFloat(ValueInput input, String key) {
        return input.read(key, Codec.FLOAT);
    }
}
