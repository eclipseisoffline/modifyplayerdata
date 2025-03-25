package xyz.eclipseisoffline.modifyplayerdata;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import xyz.eclipseisoffline.modifyplayerdata.mixin.HungerManagerAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.PlayerEntityAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.SculkShriekerWarningManagerAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.ServerPlayerEntityAccessor;

public enum PlayerNbtModifier {
    AIR("Air", ((player, value) -> player.setAir(((AbstractNbtNumber) value).shortValue()))),
    FALL_DISTANCE("FallDistance",
            ((player, value) -> player.fallDistance = ((AbstractNbtNumber) value).floatValue())),
    FIRE("Fire",
            ((player, value) -> player.setFireTicks(((AbstractNbtNumber) value).shortValue()))),
    GLOWING("Glowing", ((player, value) -> player.setGlowing(getBoolean(value)))),
    HAS_VISUAL_FIRE("HasVisualFire",
            ((player, value) -> ((VisualFireable) player).modifyPlayerData$setHasVisualFire(
                    getBoolean(value)))),
    INVULNERABLE("Invulnerable", ((player, value) -> player.setInvulnerable(getBoolean(value)))),
    MOTION("Motion", ((player, value) -> {
        Vec3d velocity = Vec3d.CODEC.parse(NbtOps.INSTANCE, value).result().orElse(Vec3d.ZERO);
        player.setVelocity(velocity.x, velocity.y, velocity.z);
        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
    })),
    NO_GRAVITY("NoGravity", ((player, value) -> player.setNoGravity(getBoolean(value)))),
    PORTAL_COOLDOWN("PortalCooldown",
            ((player, value) -> player.setPortalCooldown(((AbstractNbtNumber) value).intValue()))),
    POS("Pos", ((player, value) -> {
        Vec3d pos = Vec3d.CODEC.parse(NbtOps.INSTANCE, value).result().orElse(Vec3d.ZERO);
        player.teleport(player.getServerWorld(), pos.x, pos.y, pos.z, Set.of(),
                player.getYaw(), player.getPitch(), false);
    })),
    ROTATION("Rotation", ((player, value) -> {
        Vec2f rotation = Vec2f.CODEC.parse(NbtOps.INSTANCE, value).result().orElse(Vec2f.ZERO);
        player.teleport(player.getServerWorld(), player.getX(), player.getY(), player.getZ(),
                Set.of(), rotation.x, rotation.y, false);
    })),
    SILENT("Silent", ((player, value) -> player.setSilent(getBoolean(value)))),
    TAGS("Tags", ((player, value) -> {
        NbtList tags = (NbtList) value;
        Set<String> currentTags = new HashSet<>(player.getCommandTags());
        for (String currentTag : currentTags) {
            if (!tags.contains(currentTag)) {
                player.removeCommandTag(currentTag);
            }
        }
        for (int i = 0; i < tags.size(); i++) {
            String newTag = tags.getString(i, "");
            if (!player.getCommandTags().contains(newTag)) {
                player.addCommandTag(newTag);
            }
        }
    })),
    TICKS_FROZEN("TicksFrozen",
            ((player, value) -> player.setFrozenTicks(((AbstractNbtNumber) value).intValue()))),
    ABSORPTION_AMOUNT("AbsorptionAmount", ((player, value) -> player.setAbsorptionAmount(((AbstractNbtNumber) value).floatValue()))),
    ATTRIBUTES("attributes", (player, element) -> player.getAttributes().readNbt((NbtList) element)),
    FALL_FLYING("FallFlying", ((player, value) -> {
        if (getBoolean(value)) {
            player.startGliding();
        } else {
            player.stopGliding();
        }
    })),
    HEALTH("Health",
            ((player, value) -> player.setHealth(((AbstractNbtNumber) value).floatValue()))),
    HURT_TIME("HurtTime", ((player, value) -> {
        player.hurtTime = ((AbstractNbtNumber) value).shortValue();
        player.maxHurtTime = player.hurtTime;
    })),
    LEFT_HANDED("LeftHanded",
            ((player, value) -> player.setMainArm(getBoolean(value) ? Arm.LEFT : Arm.RIGHT)),
            ((player, nbt) -> {
                if (nbt.contains("LeftHanded")) {
                    player.setMainArm(nbt.getBoolean("LeftHanded", false) ? Arm.LEFT : Arm.RIGHT);
                }
            }),
            ((player, nbt) -> nbt.putBoolean("LeftHanded", player.getMainArm() == Arm.LEFT))),
    ABILITIES("abilities", ((player, value) -> {
        NbtCompound abilities = (NbtCompound) value;
        player.getAbilities().flying = abilities.getBoolean("flying", false);
        player.getAbilities().setFlySpeed(abilities.getFloat("flySpeed", 0.05F));

        player.getAbilities().creativeMode = abilities.getBoolean("instabuild", false);
        player.getAbilities().invulnerable = abilities.getBoolean("invulnerable", false);
        player.getAbilities().allowModifyWorld = abilities.getBoolean("mayBuild", true);
        player.getAbilities().allowFlying = abilities.getBoolean("mayfly", false);

        player.getAbilities().setWalkSpeed(abilities.getFloat("walkSpeed", 0.1F));

        player.sendAbilitiesUpdate();
    })),
    ENDER_ITEMS("EnderItems", (((player, value) -> {
        NbtList enderInventory = (NbtList) value;
        player.getEnderChestInventory().readNbtList(enderInventory, player.getRegistryManager());
    }))),
    FOOD_EXHAUSTION_LEVEL("foodExhaustionLevel",
            ((player, value) -> ((HungerManagerAccessor) player.getHungerManager()).setExhaustion(((AbstractNbtNumber) value).floatValue()))),
    FOOD_LEVEL("foodLevel", ((player, value) -> player.getHungerManager()
            .setFoodLevel(((AbstractNbtNumber) value).intValue()))),
    FOOD_SATURATION_LEVEL("foodSaturationLevel", ((player, value) -> player.getHungerManager()
            .setSaturationLevel(((AbstractNbtNumber) value).floatValue()))),
    FOOD_TICK_TIMER("foodTickTimer",
            ((player, value) -> ((HungerManagerAccessor) player.getHungerManager()).setFoodTickTimer(((AbstractNbtNumber) value).intValue()))),
    INVENTORY("Inventory", ((player, value) -> {
        NbtList inventory = (NbtList) value;
        player.getInventory().readNbt(inventory);
    })),
    RECIPE_BOOK("recipeBook", ((player, value) -> {
        NbtCompound recipeBook = (NbtCompound) value;
        player.getRecipeBook().readNbt(recipeBook, key -> Objects.requireNonNull(player.getServer()).getRecipeManager().get(key).isPresent());
        player.getRecipeBook().sendInitRecipesPacket(player);
    })),
    SCORE("Score", ((player, value) -> player.setScore(((AbstractNbtNumber) value).intValue()))),
    SEEN_CREDITS("seenCredits",
            ((player, value) -> ((ServerPlayerEntityAccessor) player).setSeenCredits(
                    getBoolean(value)))),
    SELECTED_ITEM("SelectedItem", ((player, value) -> {
        ItemStack item = ItemStack.fromNbt(player.getRegistryManager(), value).orElse(ItemStack.EMPTY);
        player.getInventory().setStack(player.getInventory().getSelectedSlot(), item);
        player.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(player.getId(),
                List.of(Pair.of(EquipmentSlot.MAINHAND, item))));
    })),
    SELECTED_ITEM_SLOT("SelectedItemSlot", ((player, value) -> {
        int slot = ((AbstractNbtNumber) value).intValue();
        if (PlayerInventory.isValidHotbarIndex(slot)) {
            player.getInventory().setSelectedSlot(slot);
            player.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(slot));
        }
    })),
    SLEEP_TIMER("SleepTimer", ((player, value) -> ((PlayerEntityAccessor) player).setSleepTimer(
            ((AbstractNbtNumber) value).shortValue()))),
    WARDEN_SPAWN_TRACKER("warden_spawn_tracker", ((player, value) -> {
        SculkShriekerWarningManager wardenTracker = player.getSculkShriekerWarningManager().orElseThrow();
        NbtCompound trackerNbt = (NbtCompound) value;
        wardenTracker.setWarningLevel(trackerNbt.getInt("warning_level", 0));
        ((SculkShriekerWarningManagerAccessor) wardenTracker).setCooldownTicks(trackerNbt.getInt("cooldown_ticks", 0));
        ((SculkShriekerWarningManagerAccessor) wardenTracker).setTicksSinceLastWarning(trackerNbt.getInt("ticks_since_last_warning", 0));
    })),
    CUSTOM("data", ((player, value) -> {
        player.setComponent(DataComponentTypes.CUSTOM_DATA, NbtComponent.of((NbtCompound) value));
    }), ((player, nbt) -> {
        if (nbt.contains("CustomData")) {
            player.setComponent(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt.getCompoundOrEmpty("CustomData"))); // Backwards compatibility, does not apply to commands, only to saved data
        }
    }), ((player, nbt) -> {}));

    private final String key;
    private final NbtApplier action;
    private final BiConsumer<ServerPlayerEntity, NbtCompound> readNbt;
    private final BiConsumer<ServerPlayerEntity, NbtCompound> writeNbt;

    PlayerNbtModifier(String key, NbtApplier action) {
        this(key, action, null, null);
    }

    PlayerNbtModifier(String key, NbtApplier action,
            BiConsumer<ServerPlayerEntity, NbtCompound> readNbt,
            BiConsumer<ServerPlayerEntity, NbtCompound> writeNbt) {
        this.key = key;
        this.action = action;
        this.readNbt = readNbt;
        this.writeNbt = writeNbt;
    }

    public static void modifyNbtKey(String key, NbtElement value, ServerPlayerEntity player)
            throws CommandSyntaxException {
        for (PlayerNbtModifier modifier : values()) {
            if (modifier.key.equals(key)) {
                try {
                    modifier.action.apply(player, value);
                } catch (CommandSyntaxException exception) {
                    throw exception;
                } catch (Exception ignored) {}
            }
        }
    }

    public static void readCustomNbt(ServerPlayerEntity player, NbtCompound nbt) {
        for (PlayerNbtModifier modifier : values()) {
            if (modifier.readNbt != null) {
                modifier.readNbt.accept(player, nbt);
            }
        }
    }

    public static void writeCustomNbt(ServerPlayerEntity player, NbtCompound nbt) {
        for (PlayerNbtModifier modifier : values()) {
            if (modifier.writeNbt != null) {
                modifier.writeNbt.accept(player, nbt);
            }
        }
    }

    private static boolean getBoolean(NbtElement element) {
        return ((AbstractNbtNumber) element).byteValue() != 0;
    }

    private interface NbtApplier {
        void apply(ServerPlayerEntity player, NbtElement element) throws CommandSyntaxException;
    }
}
