package xyz.eclipseisoffline.modifyplayerdata.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.eclipseisoffline.modifyplayerdata.PlayerData;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.advancements.criterion.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.TagValueInput;

@Mixin(EntityDataAccessor.class)
public class EntityDataAccessorMixin {

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "setData", at = @At("HEAD"), cancellable = true)
    public void setPlayerNbt(CompoundTag newNbt, CallbackInfo callbackInfo) {
        if (entity instanceof ServerPlayer player) {
            CompoundTag oldNbt = NbtPredicate.getEntityTagToCompare(entity);

            Set<String> keys = new HashSet<>(newNbt.keySet());
            for (String key : keys) {
                if (newNbt.get(key).equals(oldNbt.get(key))) {
                    newNbt.remove(key);
                }
            }

            try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(this.entity.problemPath(), LOGGER)) {
                PlayerData.apply(player, TagValueInput.create(logging, player.registryAccess(), newNbt));
            }
            callbackInfo.cancel();
        }
    }
}
