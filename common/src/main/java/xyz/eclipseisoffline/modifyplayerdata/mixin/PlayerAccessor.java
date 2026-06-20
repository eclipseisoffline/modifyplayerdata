package xyz.eclipseisoffline.modifyplayerdata.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerAccessor {

    @Accessor
    void setEnchantmentSeed(int enchantmentSeed);

    @Accessor
    void setSleepCounter(int sleepCounter);
}
