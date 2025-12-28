package xyz.eclipseisoffline.modifyplayerdata;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import xyz.eclipseisoffline.modifyplayerdata.mixin.HungerManagerAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.LivingEntityAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.PlayerEntityAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.ServerPlayerEntityAccessor;

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

    public static void read(ServerPlayer player, ValueInput view) {
        getOptionalBoolean(view, "LeftHanded").ifPresent(leftHanded -> player.setMainArm(leftHanded ? HumanoidArm.LEFT : HumanoidArm.RIGHT));

        // Backwards compatibility, does not apply to commands, only to saved data
        view.read("CustomData", CustomData.CODEC).ifPresent(data -> player.setComponent(DataComponents.CUSTOM_DATA, data));
    }

    public static void apply(ServerPlayer player, ValueInput view) {
        // Entity data
        view.read("Pos", Vec3.CODEC).ifPresent(pos -> player.teleportTo(player.level(), pos.x, pos.y, pos.z, Set.of(),
                player.getYRot(), player.getXRot(), false));

        view.read("Motion", Vec3.CODEC).ifPresent(motion -> {
            player.setDeltaMovement(motion);
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
        });

        view.read("Rotation", Vec2.CODEC).ifPresent(rotation -> player.teleportTo(player.level(), player.getX(), player.getY(), player.getZ(),
                Set.of(), rotation.x, rotation.y, false));

        getOptionalDouble(view, "fall_distance").ifPresent(fallDistance -> player.fallDistance = fallDistance);
        getOptionalShort(view, "Fire").ifPresent(player::setRemainingFireTicks);
        view.getInt("Air").ifPresent(player::setAirSupply);
        getOptionalBoolean(view, "Invulnerable").ifPresent(player::setInvulnerable);
        view.getInt("PortalCooldown").ifPresent(player::setPortalCooldown);
        getOptionalBoolean(view, "Silent").ifPresent(player::setSilent);
        getOptionalBoolean(view, "NoGravity").ifPresent(player::setNoGravity);
        getOptionalBoolean(view, "Glowing").ifPresent(player::setGlowingTag);
        view.getInt("TicksFrozen").ifPresent(player::setTicksFrozen);
        getOptionalBoolean(view, "HasVisualFire").ifPresent(visualFire -> ((VisualFireable) player).modifyPlayerData$setHasVisualFire(visualFire));

        view.list("Tags", Codec.STRING).ifPresent(stream -> {
            List<String> tags = stream.stream().toList();
            Set<String> currentTags = new HashSet<>(player.getTags());
            for (String currentTag : currentTags) {
                if (!tags.contains(currentTag)) {
                    player.removeTag(currentTag);
                }
            }
            for (String newTag : tags) {
                if (!player.getTags().contains(newTag)) {
                    player.addTag(newTag);
                }
            }
        });

        view.read("data", CustomData.CODEC).ifPresent(data -> player.setComponent(DataComponents.CUSTOM_DATA, data));


        // Living entity data
        getOptionalFloat(view, "Health").ifPresent(player::setHealth);
        getOptionalShort(view, "HurtTime").ifPresent(hurtTime -> {
            player.hurtTime = hurtTime;
            player.hurtDuration = hurtTime;
        });
        getOptionalFloat(view, "AbsorptionAmount").ifPresent(player::setAbsorptionAmount);
        view.read("attributes", AttributeInstance.Packed.LIST_CODEC).ifPresent(player.getAttributes()::apply);

        getOptionalBoolean(view, "FallFlying").ifPresent(fallFlying -> {
            if (fallFlying) {
                player.startFallFlying();
            } else {
                player.stopFallFlying();
            }
        });

        EntityReference<Player> attackingPlayer = EntityReference.read(view, "last_hurt_by_player");
        int lastHurtByPlayer = view.getIntOr("last_hurt_by_player_memory_time", player.getLastHurtByPlayerMemoryTime());
        if (attackingPlayer != null) {
            ((LivingEntityAccessor) player).invokeSetAttacking(attackingPlayer, lastHurtByPlayer);
        }

        EntityReference<LivingEntity> attacker = EntityReference.read(view, "last_hurt_by_mob");
        int ticksSinceLastHurt = view.getIntOr("ticks_since_last_hurt_by_mob", player.tickCount - player.getLastHurtByMobTimestamp());
        if (attacker != null) {
            ((LivingEntityAccessor) player).setAttackerReference(attacker);
            ((LivingEntityAccessor) player).setLastAttackedTime(ticksSinceLastHurt + player.tickCount);
        }

        view.read("equipment", EntityEquipment.CODEC).ifPresent(equipment -> ((LivingEntityAccessor) player).getEquipment().setAll(equipment));


        // Technically a mob property, we include it anyway because it's fun
        getOptionalBoolean(view, "LeftHanded").ifPresent(leftHanded -> player.setMainArm(leftHanded ? HumanoidArm.LEFT : HumanoidArm.RIGHT));


        // Player data
        view.list("Inventory", ItemStackWithSlot.CODEC).ifPresent(player.getInventory()::load);

        view.read("SelectedItem", ItemStack.CODEC).ifPresent(stack -> {
            player.getInventory().setItem(player.getInventory().getSelectedSlot(), stack);
            player.connection.send(new ClientboundSetEquipmentPacket(player.getId(),
                    List.of(Pair.of(EquipmentSlot.MAINHAND, stack))));
        });
        view.getInt("SelectedItemSlot").ifPresent(slot -> {
            player.getInventory().setSelectedSlot(slot);
            player.connection.send(new ClientboundSetHeldSlotPacket(slot));
        });

        getOptionalShort(view, "SleepTimer").ifPresent(timer -> ((PlayerEntityAccessor) player).setSleepTimer(timer));
        view.getInt("Score").ifPresent(player::setScore);

        view.getInt("foodLevel").ifPresent(player.getFoodData()::setFoodLevel);
        view.getInt("foodTickTimer").ifPresent(timer -> ((HungerManagerAccessor) player.getFoodData()).setFoodTickTimer(timer));
        getOptionalFloat(view, "foodSaturationLevel").ifPresent(player.getFoodData()::setSaturation);
        getOptionalFloat(view, "foodExhaustionLevel").ifPresent(level -> ((HungerManagerAccessor) player.getFoodData()).setExhaustion(level));

        view.read("abilities", Abilities.Packed.CODEC).ifPresent(packed -> {
            player.getAbilities().apply(packed);
            player.onUpdateAbilities();
        });

        view.list("EnderItems", ItemStackWithSlot.CODEC).ifPresent(player.getEnderChestInventory()::fromSlots);

        view.read("current_explosion_impact_pos", Vec3.CODEC).ifPresent(pos -> player.currentImpulseImpactPos = pos);
        getOptionalBoolean(view, "ignore_fall_damage_from_current_explosion").ifPresent(ignoreDamage -> ((PlayerEntityAccessor) player).setIgnoreFallDamageFromCurrentExplosionRaw(ignoreDamage));
        view.getInt("current_impulse_context_reset_grace_time").ifPresent(time -> ((PlayerEntityAccessor) player).setCurrentExplosionResetGraceTime(time));


        // Server player data
        view.read("warden_spawn_tracker", WardenSpawnTracker.CODEC).ifPresent(manager -> ((ServerPlayerEntityAccessor) player).setSculkShriekerWarningManager(manager));
        view.read("entered_nether_pos", Vec3.CODEC).ifPresent(pos -> ((ServerPlayerEntityAccessor) player).setEnteredNetherPos(pos));
        getOptionalBoolean(view, "seenCredits").ifPresent(seenCredits -> ((ServerPlayerEntityAccessor) player).setSeenCredits(seenCredits));
        view.read("respawn", ServerPlayer.RespawnConfig.CODEC).ifPresent(respawn -> player.setRespawnPosition(respawn, false));
        getOptionalBoolean(view, "spawn_extra_particles_on_fall").ifPresent(player::setSpawnExtraParticlesOnFall);
        view.read("raid_omen_position", BlockPos.CODEC).ifPresent(player::setRaidOmenPosition);
    }

    public static void write(ServerPlayer player, ValueOutput view) {
        if (player.getMainArm() == HumanoidArm.LEFT) {
            view.putBoolean("LeftHanded", true);
        }
    }

    private static OptionalDouble getOptionalDouble(ValueInput view, String key) {
        return view.read(key, Codec.DOUBLE).stream().mapToDouble(d -> d).findFirst();
    }

    private static Optional<Short> getOptionalShort(ValueInput view, String key) {
        return view.read(key, Codec.SHORT);
    }

    private static Optional<Boolean> getOptionalBoolean(ValueInput view, String key) {
        return view.read(key, Codec.BOOL);
    }

    private static Optional<Float> getOptionalFloat(ValueInput view, String key) {
        return view.read(key, Codec.FLOAT);
    }
}
