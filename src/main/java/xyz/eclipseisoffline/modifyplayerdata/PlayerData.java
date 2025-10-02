package xyz.eclipseisoffline.modifyplayerdata;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityEquipment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackWithSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import xyz.eclipseisoffline.modifyplayerdata.mixin.HungerManagerAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.LivingEntityAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.PlayerEntityAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.ServerPlayerEntityAccessor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;

public class PlayerData {

    public static void read(ServerPlayerEntity player, ReadView view) {
        getOptionalBoolean(view, "LeftHanded").ifPresent(leftHanded -> player.setMainArm(leftHanded ? Arm.LEFT : Arm.RIGHT));

        // Backwards compatibility, does not apply to commands, only to saved data
        view.read("CustomData", NbtComponent.CODEC).ifPresent(data -> player.setComponent(DataComponentTypes.CUSTOM_DATA, data));
    }

    public static void apply(ServerPlayerEntity player, ReadView view) {
        // Entity data
        view.read("Pos", Vec3d.CODEC).ifPresent(pos -> player.teleport(player.getEntityWorld(), pos.x, pos.y, pos.z, Set.of(),
                player.getYaw(), player.getPitch(), false));

        view.read("Motion", Vec3d.CODEC).ifPresent(motion -> {
            player.setVelocity(motion);
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        });

        view.read("Rotation", Vec2f.CODEC).ifPresent(rotation -> player.teleport(player.getEntityWorld(), player.getX(), player.getY(), player.getZ(),
                Set.of(), rotation.x, rotation.y, false));

        getOptionalDouble(view, "fall_distance").ifPresent(fallDistance -> player.fallDistance = fallDistance);
        getOptionalShort(view, "Fire").ifPresent(player::setFireTicks);
        view.getOptionalInt("Air").ifPresent(player::setAir);
        getOptionalBoolean(view, "Invulnerable").ifPresent(player::setInvulnerable);
        view.getOptionalInt("PortalCooldown").ifPresent(player::setPortalCooldown);
        getOptionalBoolean(view, "Silent").ifPresent(player::setSilent);
        getOptionalBoolean(view, "NoGravity").ifPresent(player::setNoGravity);
        getOptionalBoolean(view, "Glowing").ifPresent(player::setGlowing);
        view.getOptionalInt("TicksFrozen").ifPresent(player::setFrozenTicks);
        getOptionalBoolean(view, "HasVisualFire").ifPresent(visualFire -> ((VisualFireable) player).modifyPlayerData$setHasVisualFire(visualFire));

        view.getOptionalTypedListView("Tags", Codec.STRING).ifPresent(stream -> {
            List<String> tags = stream.stream().toList();
            Set<String> currentTags = new HashSet<>(player.getCommandTags());
            for (String currentTag : currentTags) {
                if (!tags.contains(currentTag)) {
                    player.removeCommandTag(currentTag);
                }
            }
            for (String newTag : tags) {
                if (!player.getCommandTags().contains(newTag)) {
                    player.addCommandTag(newTag);
                }
            }
        });

        view.read("data", NbtComponent.CODEC).ifPresent(data -> player.setComponent(DataComponentTypes.CUSTOM_DATA, data));


        // Living entity data
        getOptionalFloat(view, "Health").ifPresent(player::setHealth);
        getOptionalShort(view, "HurtTime").ifPresent(hurtTime -> {
            player.hurtTime = hurtTime;
            player.maxHurtTime = hurtTime;
        });
        getOptionalFloat(view, "AbsorptionAmount").ifPresent(player::setAbsorptionAmount);
        view.read("attributes", EntityAttributeInstance.Packed.LIST_CODEC).ifPresent(player.getAttributes()::unpack);

        getOptionalBoolean(view, "FallFlying").ifPresent(fallFlying -> {
            if (fallFlying) {
                player.startGliding();
            } else {
                player.stopGliding();
            }
        });

        LazyEntityReference<PlayerEntity> attackingPlayer = LazyEntityReference.fromData(view, "last_hurt_by_player");
        int lastHurtByPlayer = view.getInt("last_hurt_by_player_memory_time", player.getPlayerHitTimer());
        if (attackingPlayer != null) {
            ((LivingEntityAccessor) player).invokeSetAttacking(attackingPlayer, lastHurtByPlayer);
        }

        LazyEntityReference<LivingEntity> attacker = LazyEntityReference.fromData(view, "last_hurt_by_mob");
        int ticksSinceLastHurt = view.getInt("ticks_since_last_hurt_by_mob", player.age - player.getLastAttackedTime());
        if (attacker != null) {
            ((LivingEntityAccessor) player).setAttackerReference(attacker);
            ((LivingEntityAccessor) player).setLastAttackedTime(ticksSinceLastHurt + player.age);
        }

        view.read("equipment", EntityEquipment.CODEC).ifPresent(equipment -> ((LivingEntityAccessor) player).getEquipment().copyFrom(equipment));


        // Technically a mob property, we include it anyway because it's fun
        getOptionalBoolean(view, "LeftHanded").ifPresent(leftHanded -> player.setMainArm(leftHanded ? Arm.LEFT : Arm.RIGHT));


        // Player data
        view.getOptionalTypedListView("Inventory", StackWithSlot.CODEC).ifPresent(player.getInventory()::readData);

        view.read("SelectedItem", ItemStack.CODEC).ifPresent(stack -> {
            player.getInventory().setStack(player.getInventory().getSelectedSlot(), stack);
            player.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(player.getId(),
                    List.of(Pair.of(EquipmentSlot.MAINHAND, stack))));
        });
        view.getOptionalInt("SelectedItemSlot").ifPresent(slot -> {
            player.getInventory().setSelectedSlot(slot);
            player.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(slot));
        });

        getOptionalShort(view, "SleepTimer").ifPresent(timer -> ((PlayerEntityAccessor) player).setSleepTimer(timer));
        view.getOptionalInt("Score").ifPresent(player::setScore);

        view.getOptionalInt("foodLevel").ifPresent(player.getHungerManager()::setFoodLevel);
        view.getOptionalInt("foodTickTimer").ifPresent(timer -> ((HungerManagerAccessor) player.getHungerManager()).setFoodTickTimer(timer));
        getOptionalFloat(view, "foodSaturationLevel").ifPresent(player.getHungerManager()::setSaturationLevel);
        getOptionalFloat(view, "foodExhaustionLevel").ifPresent(level -> ((HungerManagerAccessor) player.getHungerManager()).setExhaustion(level));

        view.read("abilities", PlayerAbilities.Packed.CODEC).ifPresent(packed -> {
            player.getAbilities().unpack(packed);
            player.sendAbilitiesUpdate();
        });

        view.getOptionalTypedListView("EnderItems", StackWithSlot.CODEC).ifPresent(player.getEnderChestInventory()::readData);

        view.read("current_explosion_impact_pos", Vec3d.CODEC).ifPresent(pos -> player.currentExplosionImpactPos = pos);
        getOptionalBoolean(view, "ignore_fall_damage_from_current_explosion").ifPresent(ignoreDamage -> ((PlayerEntityAccessor) player).setIgnoreFallDamageFromCurrentExplosionRaw(ignoreDamage));
        view.getOptionalInt("current_impulse_context_reset_grace_time").ifPresent(time -> ((PlayerEntityAccessor) player).setCurrentExplosionResetGraceTime(time));


        // Server player data
        view.read("warden_spawn_tracker", SculkShriekerWarningManager.CODEC).ifPresent(manager -> ((ServerPlayerEntityAccessor) player).setSculkShriekerWarningManager(manager));
        view.read("entered_nether_pos", Vec3d.CODEC).ifPresent(pos -> ((ServerPlayerEntityAccessor) player).setEnteredNetherPos(pos));
        getOptionalBoolean(view, "seenCredits").ifPresent(seenCredits -> ((ServerPlayerEntityAccessor) player).setSeenCredits(seenCredits));
        view.read("respawn", ServerPlayerEntity.Respawn.CODEC).ifPresent(respawn -> player.setSpawnPoint(respawn, false));
        getOptionalBoolean(view, "spawn_extra_particles_on_fall").ifPresent(player::setSpawnExtraParticlesOnFall);
        view.read("raid_omen_position", BlockPos.CODEC).ifPresent(player::setStartRaidPos);
    }

    public static void write(ServerPlayerEntity player, WriteView view) {
        if (player.getMainArm() == Arm.LEFT) {
            view.putBoolean("LeftHanded", true);
        }
    }

    private static OptionalDouble getOptionalDouble(ReadView view, String key) {
        return view.read(key, Codec.DOUBLE).stream().mapToDouble(d -> d).findFirst();
    }

    private static Optional<Short> getOptionalShort(ReadView view, String key) {
        return view.read(key, Codec.SHORT);
    }

    private static Optional<Boolean> getOptionalBoolean(ReadView view, String key) {
        return view.read(key, Codec.BOOL);
    }

    private static Optional<Float> getOptionalFloat(ReadView view, String key) {
        return view.read(key, Codec.FLOAT);
    }
}
