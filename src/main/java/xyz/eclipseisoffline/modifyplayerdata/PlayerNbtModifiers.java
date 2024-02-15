package xyz.eclipseisoffline.modifyplayerdata;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import xyz.eclipseisoffline.modifyplayerdata.mixin.HungerManagerAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.PlayerEntityAccessor;
import xyz.eclipseisoffline.modifyplayerdata.mixin.ServerPlayerEntityAccessor;

public enum PlayerNbtModifiers {
    AIR("Air", ((player, value) -> player.setAir(((AbstractNbtNumber) value).shortValue()))),
    FALL_DISTANCE("FallDistance", ((player, value) -> player.fallDistance = ((AbstractNbtNumber) value).floatValue())),
    FIRE("Fire", ((player, value) -> player.setFireTicks(((AbstractNbtNumber) value).shortValue()))),
    GLOWING("Glowing", ((player, value) -> player.setGlowing(getBoolean(value)))),
    INVULNERABLE("Invulnerable", ((player, value) -> player.setInvulnerable(getBoolean(value)))),
    MOTION("Motion", ((player, value) -> {
        NbtList velocity = (NbtList) value;
        player.setVelocity(velocity.getDouble(0), velocity.getDouble(1), velocity.getDouble(2));
        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
    })),
    NO_GRAVITY("NoGravity", ((player, value) -> player.setNoGravity(getBoolean(value)))),
    PORTAL_COOLDOWN("PortalCooldown", ((player, value) -> player.setPortalCooldown(((AbstractNbtNumber) value).intValue()))),
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
    TICKS_FROZEN("TicksFrozen", ((player, value) -> player.setFrozenTicks(((AbstractNbtNumber) value).intValue()))),
    ABSORPTION_AMOUNT("AbsorptionAmount", ((player, value) -> player.setAbsorptionAmount(((AbstractNbtNumber) value).floatValue()))),
    HEALTH("Health", ((player, value) -> player.setHealth(((AbstractNbtNumber) value).floatValue()))),
    // TODO add read write hook
    LEFT_HANDED("LeftHanded", ((player, value) -> player.setMainArm(getBoolean(value) ? Arm.LEFT : Arm.RIGHT))),
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
        player.getEnderChestInventory().readNbtList(enderInventory);
    }))),
    FOOD_EXHAUSTION_LEVEL("foodExhaustionLevel", ((player, value) -> player.getHungerManager().setExhaustion(((AbstractNbtNumber) value).floatValue()))),
    FOOD_LEVEL("foodLevel", ((player, value) -> player.getHungerManager().setFoodLevel(((AbstractNbtNumber) value).intValue()))),
    FOOD_SATURATION_LEVEL("foodSaturationLevel", ((player, value) -> player.getHungerManager().setSaturationLevel(((AbstractNbtNumber) value).floatValue()))),
    FOOD_TICK_TIMER("foodTickTimer", ((player, value) -> ((HungerManagerAccessor) player.getHungerManager()).setFoodTickTimer(((AbstractNbtNumber) value).intValue()))),
    INVENTORY("Inventory", ((player, value) -> {
        NbtList inventory = (NbtList) value;
        player.getInventory().readNbt(inventory);
    })),
    SCORE("Score", ((player, value) -> player.setScore(((AbstractNbtNumber) value).intValue()))),
    SEEN_CREDITS("seenCredits", ((player, value) -> ((ServerPlayerEntityAccessor) player).setSeenCredits(getBoolean(value)))),
    SELECTED_ITEM_SLOT("SelectedItemSlot", ((player, value) -> {
        int slot = ((AbstractNbtNumber) value).intValue();
        if (PlayerInventory.isValidHotbarIndex(slot)) {
            player.getInventory().selectedSlot = slot;
            player.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(slot));
        }
    })),
    SLEEP_TIMER("SleepTimer", ((player, value) -> ((PlayerEntityAccessor) player).setSleepTimer(((AbstractNbtNumber) value).shortValue())));

    private final String key;
    private final BiConsumer<ServerPlayerEntity, NbtElement> action;

    PlayerNbtModifiers(String key, BiConsumer<ServerPlayerEntity, NbtElement> action) {
        this.key = key;
        this.action = action;
    }

    public static void modifyNbtKey(String key, NbtElement value, ServerPlayerEntity player) {
        for (PlayerNbtModifiers modifier : values()) {
            if (modifier.key.equals(key)) {
                try {
                    modifier.action.accept(player, value);
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }
    }

    private static boolean getBoolean(NbtElement element) {
        return ((AbstractNbtNumber) element).byteValue() != 0;
    }
}
