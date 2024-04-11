package xyz.eclipseisoffline.modifyplayerdata;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.datafixers.util.Pair;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
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
        NbtList velocity = (NbtList) value;
        player.setVelocity(velocity.getDouble(0), velocity.getDouble(1), velocity.getDouble(2));
        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
    })),
    NO_GRAVITY("NoGravity", ((player, value) -> player.setNoGravity(getBoolean(value)))),
    PORTAL_COOLDOWN("PortalCooldown",
            ((player, value) -> player.setPortalCooldown(((AbstractNbtNumber) value).intValue()))),
    POS("Pos", ((player, value) -> {
        NbtList pos = (NbtList) value;
        player.teleport(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
    })),
    ROTATION("Rotation", ((player, value) -> {
        NbtList rotation = (NbtList) value;
        player.teleport(player.getServerWorld(), player.getX(), player.getY(), player.getZ(),
                PositionFlag.ROT, rotation.getFloat(0), rotation.getFloat(1));
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
            String newTag = tags.getString(i);
            if (!player.getCommandTags().contains(newTag)) {
                player.addCommandTag(newTag);
            }
        }
    })),
    TICKS_FROZEN("TicksFrozen",
            ((player, value) -> player.setFrozenTicks(((AbstractNbtNumber) value).intValue()))),
    ABSORPTION_AMOUNT("AbsorptionAmount", ((player, value) -> player.setAbsorptionAmount(
            ((AbstractNbtNumber) value).floatValue()))),
    FALL_FLYING("FallFlying", ((player, value) -> {
        if (getBoolean(value)) {
            player.startFallFlying();
        } else {
            player.stopFallFlying();
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
                if (nbt.contains("LeftHanded", NbtElement.BYTE_TYPE)) {
                    player.setMainArm(nbt.getBoolean("LeftHanded") ? Arm.LEFT : Arm.RIGHT);
                }
            }),
            ((player, nbt) -> nbt.putBoolean("LeftHanded", player.getMainArm() == Arm.LEFT))),
    ABILITIES("abilities", ((player, value) -> {
        NbtCompound abilities = (NbtCompound) value;
        player.getAbilities().flying = abilities.getBoolean("flying");
        player.getAbilities().setFlySpeed(abilities.getFloat("flySpeed"));

        player.getAbilities().creativeMode = abilities.getBoolean("instabuild");
        player.getAbilities().invulnerable = abilities.getBoolean("invulnerable");
        player.getAbilities().allowModifyWorld = abilities.getBoolean("mayBuild");
        player.getAbilities().allowFlying = abilities.getBoolean("mayfly");

        player.getAbilities().setWalkSpeed(abilities.getFloat("walkSpeed"));

        player.sendAbilitiesUpdate();
    })),
    ENDER_ITEMS("EnderItems", (((player, value) -> {
        NbtList enderInventory = (NbtList) value;
        player.getEnderChestInventory().readNbtList(enderInventory, player.getRegistryManager());
    }))),
    FOOD_EXHAUSTION_LEVEL("foodExhaustionLevel", ((player, value) -> player.getHungerManager()
            .setExhaustion(((AbstractNbtNumber) value).floatValue()))),
    FOOD_LEVEL("foodLevel", ((player, value) -> player.getHungerManager()
            .setFoodLevel(((AbstractNbtNumber) value).intValue()))),
    FOOD_SATURATION_LEVEL("foodSaturationLevel", ((player, value) -> player.getHungerManager()
            .setSaturationLevel(((AbstractNbtNumber) value).floatValue()))),
    FOOD_TICK_TIMER("foodTickTimer",
            ((player, value) -> ((HungerManagerAccessor) player.getHungerManager()).setFoodTickTimer(
                    ((AbstractNbtNumber) value).intValue()))),
    INVENTORY("Inventory", ((player, value) -> {
        NbtList inventory = (NbtList) value;
        player.getInventory().readNbt(inventory);
    })),
    RECIPE_BOOK("recipeBook", ((player, value) -> {
        NbtCompound recipeBook = (NbtCompound) value;
        player.getRecipeBook().readNbt(recipeBook, Objects.requireNonNull(player.getServer()).getRecipeManager());
        player.getRecipeBook().sendInitRecipesPacket(player);
    })),
    SCORE("Score", ((player, value) -> player.setScore(((AbstractNbtNumber) value).intValue()))),
    SEEN_CREDITS("seenCredits",
            ((player, value) -> ((ServerPlayerEntityAccessor) player).setSeenCredits(
                    getBoolean(value)))),
    SELECTED_ITEM("SelectedItem", ((player, value) -> {
        ItemStack item = ItemStack.fromNbt(player.getRegistryManager(), value).orElseThrow(() -> new SimpleCommandExceptionType(Text.of("Error parsing item data")).create());
        player.getInventory().setStack(player.getInventory().selectedSlot, item);
        player.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(player.getId(),
                List.of(Pair.of(EquipmentSlot.MAINHAND, item))));
    })),
    SELECTED_ITEM_SLOT("SelectedItemSlot", ((player, value) -> {
        int slot = ((AbstractNbtNumber) value).intValue();
        if (PlayerInventory.isValidHotbarIndex(slot)) {
            player.getInventory().selectedSlot = slot;
            player.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(slot));
        }
    })),
    SLEEP_TIMER("SleepTimer", ((player, value) -> ((PlayerEntityAccessor) player).setSleepTimer(
            ((AbstractNbtNumber) value).shortValue()))),
    WARDEN_SPAWN_TRACKER("warden_spawn_tracker", ((player, value) -> {
        SculkShriekerWarningManager wardenTracker = player.getSculkShriekerWarningManager().orElseThrow();
        NbtCompound trackerNbt = (NbtCompound) value;
        wardenTracker.setWarningLevel(trackerNbt.getInt("warning_level"));
        ((SculkShriekerWarningManagerAccessor) wardenTracker).setCooldownTicks(trackerNbt.getInt("cooldown_ticks"));
        ((SculkShriekerWarningManagerAccessor) wardenTracker).setTicksSinceLastWarning(trackerNbt.getInt("ticks_since_last_warning"));
    })),
    CUSTOM("CustomData", ((player, value) -> {
        ((CustomDataHolder) player).modifyPlayerData$setCustomNbtData((NbtCompound) value);
    }), ((player, nbt) -> {
        if (nbt.contains("CustomData", NbtElement.COMPOUND_TYPE)) {
            ((CustomDataHolder) player).modifyPlayerData$setCustomNbtData(nbt.getCompound("CustomData"));
        }
    }), ((player, nbt) -> {
        nbt.put("CustomData", ((CustomDataHolder) player).modifyPlayerData$getCustomNbtData());
    }));

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
